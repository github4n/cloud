package com.iot.boss.manager.refund;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iot.boss.dto.OrderPriceDto;
import com.iot.boss.dto.RefundListDto;
import com.iot.boss.dto.RefundListSearch;
import com.iot.boss.dto.refund.VideoRefundRecordDto;
import com.iot.boss.entity.SystemUser;
import com.iot.boss.entity.refund.VideoRefundLog;
import com.iot.boss.entity.refund.VideoRefundRecord;
import com.iot.boss.enums.AdminTypeEnum;
import com.iot.boss.enums.refund.PlanStatusEnum;
import com.iot.boss.enums.refund.RefundAuditStatusEnum;
import com.iot.boss.enums.refund.RefundLogStatusEnum;
import com.iot.boss.enums.refund.RefundStatusEnum;
import com.iot.boss.exception.BusinessExceptionEnum;
import com.iot.boss.service.malf.SystemUserService;
import com.iot.boss.service.refund.VideoRefundLogService;
import com.iot.boss.service.refund.VideoRefundRecordService;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.StringUtil;
import com.iot.payment.api.PaymentApi;
import com.iot.payment.dto.RefundDto;
import com.iot.payment.entity.PayRes;
import com.iot.video.api.VideoManageApi;
import com.iot.video.dto.VideoPayRecordDto;

/**
 * 项目名称：cloud
 * 功能描述：视频退款业务出路
 * 创建人： yeshiyuan
 * 创建时间：2018/5/21 11:06
 * 修改人： yeshiyuan
 * 修改时间：2018/5/21 11:06
 * 修改描述：
 */
@Component
public class VideoRefundManager {

    private static Logger logger = LoggerFactory.getLogger(VideoRefundManager.class);

    private static ExecutorService executorService = Executors.newFixedThreadPool(20);

    private static int DeleteNum = 900;

    @Autowired
    private VideoRefundLogService videoRefundLogService;

    @Autowired
    private VideoRefundRecordService videoRefundRecordService;


    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private VideoManageApi videoManageApi;

    @Autowired
    private PaymentApi paymentApi;



    /**
      * @despriction：记录退款操作日志
      * @author  yeshiyuan
      * @created 2018/5/21 11:39
      * @param null
      * @return
      */
    private void addRefundLog(Long refundId, Long operatorId, BigDecimal refundPrice, RefundLogStatusEnum refundLogStatusEnum, String refundRemark){
        if (!RefundLogStatusEnum.checkRefundStatus(refundLogStatusEnum.getValue())){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR,"refund status is error");
        }
        VideoRefundLog videoRefundLog = new VideoRefundLog(refundId,operatorId,refundPrice,refundLogStatusEnum.getValue(),refundRemark);
//        videoRefundLog.setId(RedisCacheUtil.incr(ModuleConstants.DB_TABLE_VIDEO_REFUND_LOG,0L));
        videoRefundLog.setCreateTime(new Date());
        videoRefundLogService.add(videoRefundLog);
    }
    /**
     * 描述： 退款申请
     * @author 490485964@qq.com
     * @date 2018/5/21 17:34
     * @param
     * @return
     */
    public void applyRefund(VideoRefundRecordDto videoRefundRecordDto){
        if(null == videoRefundRecordDto){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        checkPara(videoRefundRecordDto);
        int userCount = systemUserService.checkUserExist(videoRefundRecordDto.getRefundApplyId());
        if(userCount<1){
            throw new BusinessException(BusinessExceptionEnum.ADMIN_NOT_EXISTS);
        }

        boolean isEdit = false;
        Long id = videoRefundRecordDto.getId();
        VideoRefundRecord videoRefundRecordOld = null;
        if(null != id){
            videoRefundRecordOld = videoRefundRecordService.getVideoRefundRecordById(id);
            if(null != videoRefundRecordOld && null != videoRefundRecordOld.getOrderId()
                    && videoRefundRecordOld.getOrderId().equals(videoRefundRecordDto.getOrderId())
                    && null != videoRefundRecordOld.getAuditStatus()
                    && RefundAuditStatusEnum.NO_PASS.getValue() == videoRefundRecordOld.getAuditStatus()){
                isEdit = true;
            }
        }
        if(!isEdit){
            //检查订单是否重复提交申请退款
            int count = videoRefundRecordService.checkRefundRecordExist(videoRefundRecordDto.getOrderId(),videoRefundRecordDto.getTenantId());
            if(count>0){
                throw new BusinessException(BusinessExceptionEnum.VIDEO_REFUND_ORDER_REPEAT);
            }
        }

        //获取最新的支付成功的订单
        VideoPayRecordDto videoPayRecordDto = videoManageApi.getLatestOrderByPlanIdAndStatus(videoRefundRecordDto.getTenantId(),
                videoRefundRecordDto.getPlanId(), PlanStatusEnum.REFUND.getValue());
        //只能对最新的支付成功的订单进行退款
        if(null == videoPayRecordDto || null == videoPayRecordDto.getOrderId()
                || !videoPayRecordDto.getOrderId().equals(videoRefundRecordDto.getOrderId())){
            throw new BusinessException(BusinessExceptionEnum.VIDEO_REFUND_NOT_LATEST_ORDER);
        }
        //videoPayRecordDto.getPackagePrice()套餐价格  videoPayRecordDto.getCounts()购买月数
        BigDecimal packagePrice = new BigDecimal(Float.toString(videoPayRecordDto.getPackagePrice()*videoPayRecordDto.getCounts()));
        BigDecimal refundPrice = videoRefundRecordDto.getRefundPrice();
        int result = refundPrice.compareTo(packagePrice);
        if(result == 1 ){
            //result == 1  即refundPrice>packagePrice，提交的退款额度不能高于原订单价格
            throw new BusinessException(BusinessExceptionEnum.VIDEO_REFUND_PRICE_BEYOND);
        }

        if(isEdit){
            videoRefundRecordOld.setRefundPrice(videoRefundRecordDto.getRefundPrice());
            videoRefundRecordOld.setRefundReason(videoRefundRecordDto.getRefundReason());
            videoRefundRecordOld.setAuditStatus(RefundAuditStatusEnum.WAIT_AUDIT.getValue());
            videoRefundRecordService.editRecordPrice(videoRefundRecordOld);
            addRefundLog(videoRefundRecordOld.getId(),videoRefundRecordOld.getRefundApplyId(),videoRefundRecordOld.getRefundPrice(),RefundLogStatusEnum.APPLY,videoRefundRecordOld.getRefundReason());
            logger.info("editRefund end "+videoRefundRecordOld.getId());
        }else {
            VideoRefundRecord videoRefundRecord = new VideoRefundRecord();
            BeanUtil.copyProperties(videoRefundRecordDto, videoRefundRecord);
            //自增Id
//            Long videoRefundRecordId = RedisCacheUtil.incr(ModuleConstants.DB_TABLE_VIDEO_REFUND_RECORD,0L);
//            videoRefundRecord.setId(videoRefundRecordId);
            videoRefundRecord.setRefundStatus(RefundStatusEnum.APPLY.getValue());
            videoRefundRecord.setAuditStatus(RefundAuditStatusEnum.WAIT_AUDIT.getValue());
            videoRefundRecord.setUserId(videoPayRecordDto.getUserId());
            videoRefundRecordService.applyRefund(videoRefundRecord);
            addRefundLog(videoRefundRecord.getId(),videoRefundRecord.getRefundApplyId(),videoRefundRecord.getRefundPrice(),RefundLogStatusEnum.APPLY,videoRefundRecord.getRefundReason());
            logger.info("applyRefund end "+videoRefundRecord.getId());
        }
    }

    private void checkPara(VideoRefundRecordDto videoRefundRecordDto) {
        if(null == videoRefundRecordDto.getTenantId()){
            throw new BusinessException(BusinessExceptionEnum.VIDEO_REFUND_CHECK_NO_TENANTID);
        }
        if(StringUtil.isBlank(videoRefundRecordDto.getPlanId())){
            throw new BusinessException(BusinessExceptionEnum.VIDEO_REFUND_CHECK_NO_PLANID,"planId is blank");
        }
        if(StringUtil.isBlank( videoRefundRecordDto.getOrderId())){
            throw new BusinessException(BusinessExceptionEnum.VIDEO_REFUND_CHECK_NO_ORDERID,"orderId is blank");
        }
        if(StringUtil.isBlank( videoRefundRecordDto.getRefundReason())){
            throw new BusinessException(BusinessExceptionEnum.VIDEO_REFUND_CHECK_NO_REASON,"refund Reason is blank");
        }
        if(null == videoRefundRecordDto.getRefundPrice()){
            throw new BusinessException(BusinessExceptionEnum.VIDEO_REFUND_CHECK_NO_PRICE);
        }
        if(null == videoRefundRecordDto.getRefundApplyId()){
            throw new BusinessException(BusinessExceptionEnum.VIDEO_REFUND_CHECK_NO_APPLYID);
        }
    }

    /**
      * @despriction：退款-审核操作
      * @author  yeshiyuan
      * @created 2018/5/21 15:38
      * @param refundId 退款id
      * @param auditStatus 审核结果
      * @param auditMessage 审核备注
      * @param adminId 操作人id
      * @return
      */
    @Transactional
    public void audit(Long refundId, Integer auditStatus,String auditMessage, Long adminId){
        //校验当前角色是否是超级管理员
        SystemUser user = systemUserService.checkAdminAuth(adminId, AdminTypeEnum.SUPER_ADMIN.getValue());
        //校验订单是否存在
        VideoRefundRecord videoRefundRecord = videoRefundRecordService.getVideoRefundRecordById(refundId);
        if (videoRefundRecord == null){
            throw new BusinessException(BusinessExceptionEnum.REFUND_RECORD_NOT_EXISTS);
        }
        //校验订单是否属于申请中
        if (RefundStatusEnum.APPLY.getValue() != videoRefundRecord.getRefundStatus().intValue()){
            throw new BusinessException(BusinessExceptionEnum.REFUND_RECORD_REFUND_STATUS_ERROR);
        }
        //订单是否属于待审核状态
        if (RefundAuditStatusEnum.WAIT_AUDIT.getValue() != videoRefundRecord.getAuditStatus()){
            throw new BusinessException(BusinessExceptionEnum.REFUND_RECORD_AUDIT_STATUS_ERROR);
        }
        videoRefundRecord.setAuditId(adminId);
        videoRefundRecord.setAuditMessage(auditMessage);
        videoRefundRecord.setAuditName(user.getAdminName());
        videoRefundRecord.setAuditStatus(auditStatus);
        videoRefundRecord.setAuditTime(new Date());
        if (auditStatus == RefundAuditStatusEnum.PASS.getValue()){
            //设置退款状态为paypal退款中
            videoRefundRecord.setRefundStatus(RefundStatusEnum.PAYPAL_REFUND.getValue());
            videoRefundRecordService.updateAuditStatus(videoRefundRecord,RefundAuditStatusEnum.WAIT_AUDIT.getValue());
            this.addRefundLog(refundId,adminId,videoRefundRecord.getRefundPrice(), RefundLogStatusEnum.AUDIT_PASS,auditMessage);
            //退款采用异步处理
            asyncPaypalRefund(videoRefundRecord);
        }else if(auditStatus == RefundAuditStatusEnum.NO_PASS.getValue()){
            videoRefundRecordService.updateAuditStatus(videoRefundRecord,RefundAuditStatusEnum.WAIT_AUDIT.getValue());
            this.addRefundLog(refundId,adminId,videoRefundRecord.getRefundPrice(),RefundLogStatusEnum.AUDIT_NO_PASS,auditMessage);
        }else{
            throw new BusinessException(BusinessExceptionEnum.REFUND_RECORD_AUDIT_STATUS_ERROR);
        }
    }

    /**
     *
     * 描述：查询订单价格
     * @author ouyangjie
     * @created 2018年5月21日 上午10:09:56
     * @since
     * @param orderId
     * @return
     */
    public OrderPriceDto getOrderPrice(String orderId, String userId){
        if(StringUtil.isBlank(orderId) || StringUtil.isBlank(userId)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "orderId is null or blank or userId is null or blank");
        }
        try {
            return videoRefundRecordService.getOrderPrice(orderId, userId);
        }catch (Exception e){
            throw new BusinessException(BusinessExceptionEnum.INTERNAL_ERROR, "internal error");
        }
    }

    /**
     *
     * 描述：查询申请列表
     * @author ouyangjie
     * @created 2018年5月21日 上午10:09:56
     * @since
     * @param searchParam
     * @return
     */
    public PageInfo<RefundListDto> queryRefundApplyList(RefundListSearch searchParam){
        if(null == searchParam){
            throw new BusinessException(BusinessExceptionEnum.OBJECT_IS_NULL, "search param is null");
        }
        if(!RefundStatusEnum.checkRefundStatus(searchParam.getRefundStatus())){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "refundStatus should be 0 1 2 3");
        }
        if(null == searchParam.getRefundApplyTime()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "refundApplyTime is null");
        }
        if(null != searchParam.getOrderId() && searchParam.getOrderId().isEmpty()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "orderId is empty");
        }
        if(null != searchParam.getTenantId() && searchParam.getTenantId().isEmpty()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "tenantId is empty");
        }
        if(null != searchParam.getUserId() && searchParam.getUserId().isEmpty()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "userId is empty");
        }
        PageHelper.startPage(CommonUtil.getPageNum(searchParam), CommonUtil.getPageSize(searchParam),true);
        try {
            return new PageInfo<RefundListDto>(videoRefundRecordService.queryRefundApplyList(searchParam));
        }catch (Exception e){
            throw new BusinessException(BusinessExceptionEnum.INTERNAL_ERROR, "internal error");
        }
    }

    /**
      * @despriction：异步调用paypal进行退款
      * @author  yeshiyuan
      * @created 2018/5/22 17:33
      * @param videoRefundRecord
      * @return
      */
    private void asyncPaypalRefund(VideoRefundRecord videoRefundRecord){
        RefundDto refundDto = new RefundDto(videoRefundRecord.getRefundPrice(),videoRefundRecord.getUserId(),videoRefundRecord.getOrderId(),videoRefundRecord.getTenantId());
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                //1、封装数据
                int refundResult =  RefundStatusEnum.FAIL.getValue();
                RefundLogStatusEnum refundLogResult = RefundLogStatusEnum.FAIL;
                int count =0;
                PayRes payRes = null;
                for (;count<10;count++){
                    //2、调用paypal退款
                    try {
                        payRes = paymentApi.refund(refundDto);
                        logger.info("refundId:{},paypal refund result:{}",videoRefundRecord.getId(),JsonUtil.toJson(payRes));
                        if (payRes!=null && payRes.getCode()==200){
                            refundResult = RefundStatusEnum.SUCCESS.getValue();
                            refundLogResult = RefundLogStatusEnum.SUCCESS;
                            //3、退款成功，需要设置对应订单失效
                            videoManageApi.refundOneOrderOfPlan(videoRefundRecord.getPlanId(),videoRefundRecord.getOrderId(),videoRefundRecord.getTenantId());
                            break;
                        }
                    } catch (Exception e) {
                        logger.error("退款订单号：{}，调用支付服务退款 paymentService->refund 接口出错：",videoRefundRecord.getId(),e);
                    }
                }
                //4、修改退款申请记录的退款状态,记录操作日志
                videoRefundRecordService.updateRefundStatus(videoRefundRecord.getId(),refundResult);
                addRefundLog(videoRefundRecord.getId(),videoRefundRecord.getAuditId(),videoRefundRecord.getRefundPrice(),refundLogResult ,"paypal refund log");
            }
        });
    }

	/**
     *
     * 描述：获取退款记录
     * @author 李帅
     * @created 2018年5月22日 下午5:30:21
     * @since
     * @param orderIDList
     * @return
     */
    public List<VideoRefundRecord> getVideoRefundRecord(List<String> orderIDList){
		List<VideoRefundRecord> payTransations = new ArrayList<VideoRefundRecord>();
		List<List<String>> idsListList = null;
		if(orderIDList.size() > DeleteNum) {
			idsListList = dealBySubList(orderIDList, DeleteNum);
			for(List<String> ids : idsListList) {
				payTransations.addAll(this.videoRefundRecordService.getVideoRefundRecord(ids));
			}
		}else {
			payTransations = this.videoRefundRecordService.getVideoRefundRecord(orderIDList);
		}
		return payTransations;
	}

    /**
	 *
	 * 描述：通过list的     subList(int fromIndex, int toIndex)方法实现
	 * @author 李帅
	 * @created 2017年11月2日 上午10:44:35
	 * @since
	 * @param sourList
	 * @param batchCount
	 */
	public List<List<String>> dealBySubList(List<String> sourList, int batchCount) {
		int sourListSize = sourList.size();
		int subCount = sourListSize % batchCount == 0 ? sourListSize / batchCount : sourListSize / batchCount + 1;
		int startIndext = 0;
		int stopIndext = 0;
		int endIndext = sourListSize % batchCount == 0 ? batchCount : sourListSize % batchCount;
		List<List<String>> tempListList = new ArrayList<List<String>>();
		List<String> tempList = null;
		for (int i = 0; i < subCount; i++) {
			stopIndext = (i == subCount - 1) ? stopIndext + endIndext : stopIndext + batchCount;
			tempList = new ArrayList<String>(sourList.subList(startIndext, stopIndext));
			tempListList.add(tempList);
			startIndext = stopIndext;
		}
		return tempListList;
	}



}

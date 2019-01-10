package com.iot.boss.service.refund.impl;

import com.github.pagehelper.PageInfo;
import com.iot.boss.api.VideoRefundApi;
import com.iot.boss.dto.refund.VideoRefundRecordDto;
import com.iot.boss.entity.refund.VideoRefundRecord;
import com.iot.boss.enums.VideoRecordOrderStatusEnum;
import com.iot.boss.service.refund.VideoRefundService;
import com.iot.boss.vo.refund.RecordDto;
import com.iot.common.beans.BeanUtil;
import com.iot.common.util.StringUtil;
import com.iot.payment.api.PaymentApi;
import com.iot.payment.entity.PayTransation;
import com.iot.payment.enums.PayTransRefundStatusEnum;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.video.api.VideoManageApi;
import com.iot.video.dto.ALLRecordDto;
import com.iot.video.dto.AllRecordSearchParam;
import com.iot.video.dto.VideoPlanOrderDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：IOT云平台
 * 模块名称：BOSS聚合层
 * 功能描述：报障业务
 * 创建人： mao2080@sina.com
 * 创建时间：2018/5/16 9:22
 * 修改人： mao2080@sina.com
 * 修改时间：2018/5/16 9:22
 * 修改描述：
 */
@Service("videoRefundService")
public class VideoRefundServiceImpl implements VideoRefundService{

	@Autowired
    private VideoManageApi videoManageApi;

    @Autowired
    private VideoRefundApi videoRefundApi;
    
    @Autowired
    private UserApi userApi;
    
    @Autowired
    private PaymentApi paymentApi;
    
    //订单状态：0-支付成功，1-服务开通，2-服务开通失败，3-服务已关闭
    private String recordState1 = "1";
    private String recordState2 = "2";
    /**
     * 
     * 描述：订单列表查询
     * @author 李帅
     * @created 2018年5月22日 下午3:14:37
     * @since 
     * @param searchParam
     */
    public PageInfo<RecordDto> queryPayRecordList(AllRecordSearchParam searchParam){

    	//将查询条件中key代表userName转换为uuid
    	FetchUserResp searchUser = null;
    	if(StringUtil.isNotEmpty(searchParam.getKey())) {
    		try {
				searchUser = this.userApi.getUserByUserName(searchParam.getKey());
			} catch (Exception e) {

			}
    		if(searchUser != null) {
    			searchParam.setKey(searchUser.getUuid());
    		}
    	}
    	//查询所有计划购买记录
    	List<ALLRecordDto> aLLRecordList = this.videoManageApi.getAllBuyRecordList(searchParam);
    	
    	List<String> orderIDList = new ArrayList<String>();
    	Map<String, ALLRecordDto> aLLRecordDtoMap = new HashMap<String, ALLRecordDto>();
    	//补充用户名称
    	FetchUserResp user = null;
    	for(ALLRecordDto aLLRecordDto : aLLRecordList) {
    		orderIDList.add(aLLRecordDto.getOrderId());
    		try {
				user = this.userApi.getUserByUuid(aLLRecordDto.getUserId());
			} catch (Exception e) {

			}
    		if(user != null) {
    			aLLRecordDto.setUserName(user.getUserName());
    		}
    		aLLRecordDtoMap.put(aLLRecordDto.getOrderId(), aLLRecordDto);
    	}
    	
    	List<PayTransation> payTransationList = null;//new ArrayList<PayTransation>();
    	List<VideoRefundRecord> videoRefundRecordList = null;
    	Map<String, PayTransation> payTransationMap = null;
    	Map<String, VideoRefundRecord> videoRefundRecordMap = null;
    	
    	if(orderIDList != null && orderIDList.size() > 0) {
    		//获取支付交易信息
    		payTransationList = this.paymentApi.getPayTransation(orderIDList);
    		if(recordState1.equals(searchParam.getRecordState())) {
    			payTransationMap = new HashMap<String, PayTransation>();
        		for(PayTransation payTransation : payTransationList) {
        			//退款状态,0-未退款,1-退款中，2-退款成功,3-退款失败
        			if(payTransation.getRefundStatus() == null || 2 != payTransation.getRefundStatus()) {
        				payTransationMap.put(payTransation.getOrderId(), payTransation);
        			}
        		}
    		}else if(recordState2.equals(searchParam.getRecordState())) {
    			payTransationMap = new HashMap<String, PayTransation>();
        		for(PayTransation payTransation : payTransationList) {
        			//支付状态,0-待支付，1-支付中，2-已支付，3-支付失败
        			if(payTransation.getPayStatus() == null || 3 == payTransation.getPayStatus()) {
        				payTransationMap.put(payTransation.getOrderId(), payTransation);
        			}
        		}
    		}else {
    			payTransationMap = new HashMap<String, PayTransation>();
        		for(PayTransation payTransation : payTransationList) {
        			payTransationMap.put(payTransation.getOrderId(), payTransation);
        		}
    		}

    		//获取退款记录
    		videoRefundRecordList = this.videoRefundApi.getVideoRefundRecord(orderIDList);
    		videoRefundRecordMap = new HashMap<String, VideoRefundRecord>();
    		for(VideoRefundRecord videoRefundRecord : videoRefundRecordList) {
    			videoRefundRecordMap.put(videoRefundRecord.getOrderId(), videoRefundRecord);
    		}
    	}
    	List<RecordDto> recordDtos = new ArrayList<RecordDto>();
    	RecordDto recordDto = null;//new RecordDto();
    	PayTransation payTransation = null;
    	VideoRefundRecord videoRefundRecord = null;
    	for(ALLRecordDto aLLRecordDto : aLLRecordList) {
    		payTransation = payTransationMap.get(aLLRecordDto.getOrderId());
    		if(payTransation == null && (recordState2.equals(searchParam.getRecordState()) || recordState1.equals(searchParam.getRecordState()))) {
    			continue;
    		}
    		recordDto = new RecordDto();
    		BeanUtils.copyProperties(aLLRecordDto, recordDto);

    		if(payTransation != null) {
    			recordDto.setTradeId(payTransation.getTradeId());
    			recordDto.setRefundTime(payTransation.getRefundTime());
    			recordDto.setRefundSum(payTransation.getRefundSum());
    		}
    		
    		videoRefundRecord = videoRefundRecordMap.get(aLLRecordDto.getOrderId());
    		if(videoRefundRecord != null) {
    			recordDto.setRefundApplyId(videoRefundRecord.getRefundApplyId());
    			recordDto.setRefundApplyName(videoRefundRecord.getRefundApplyName());
    			recordDto.setRefundReason(videoRefundRecord.getRefundReason());
    			recordDto.setRefundPrice(videoRefundRecord.getRefundPrice());
    			recordDto.setRefundApplyTime(videoRefundRecord.getRefundApplyTime());
    			recordDto.setAuditId(videoRefundRecord.getAuditId());
    			recordDto.setAuditName(videoRefundRecord.getAuditName());
    			recordDto.setAuditMessage(videoRefundRecord.getAuditMessage());
    			recordDto.setAuditTime(videoRefundRecord.getAuditTime());
    			recordDto.setAuditStatus(videoRefundRecord.getAuditStatus());
    		}
    		recordDtos.add(recordDto);
    	}
    	PageInfo<RecordDto> page = new PageInfo<>();

    	//手动分页

    	page.setList(recordDtos);
    	return page;
    }

    @Override
    public void applyRefund(VideoRefundRecordDto videoRefundRecordDto) {
        videoRefundApi.applyRefund(videoRefundRecordDto);
    }

	/**
	 * @despriction：查看计划的其他订单
	 * @author  yeshiyuan
	 * @created 2018/5/23 14:33
	 * @param orderId 订单id
	 * @param planId 计划id
	 * @return
	 */
	@Override
	public List<RecordDto> queryOtherPayRecord(String orderId, String planId) {
		//先查找计划的其他（续费）订单记录
		List<VideoPlanOrderDto> planOrderDtos = videoManageApi.queryPlanOtherPayRecord(orderId, planId);
		List<RecordDto> recordDtos= null;
		if (planOrderDtos!=null && planOrderDtos.size()>0){
			recordDtos = new ArrayList<>();
			List<String> orderIdList = new ArrayList<>();
			Map<String,RecordDto> recordDtoMap = new HashMap<>();
			for (VideoPlanOrderDto planOrderDto:planOrderDtos) {
				RecordDto recordDto = new RecordDto();
				BeanUtil.copyProperties(planOrderDto,recordDto);
				orderIdList.add(recordDto.getOrderId());
				//查找对应的用户
				FetchUserResp fetchUserResp = userApi.getUserByUuid(recordDto.getUserId());
				recordDto.setUserName(fetchUserResp.getUserName());
				recordDtoMap.put(recordDto.getOrderId(),recordDto);
			}
			//获取支付交易信息
			List<PayTransation> payTransationList = this.paymentApi.getPayTransation(orderIdList);
			//Map<String,Integer> payRefundMap = new HashMap<>();
			for (PayTransation payTransation : payTransationList) {
				if (payTransation.getRefundStatus() == PayTransRefundStatusEnum.FAIL.getValue()){
					RecordDto recordDto = recordDtoMap.get(payTransation.getOrderId());
					recordDto.setOrderStatus(VideoRecordOrderStatusEnum.REFUND_ERROR.getValue());
				}
			}
			for (String orderId_s:orderIdList) {
				recordDtos.add(recordDtoMap.get(orderId_s));
			}
		}
		return recordDtos;
	}

	/**
	 * @despriction：退款审核
	 * @author  yeshiyuan
	 * @created 2018/5/28 17:22
	 * @param refundId 退款订单ID
	 * @param auditStatus 审核状态
	 * @param auditMessage 审核意见
	 * @return
	 */
	@Override
	public void refundAudit(Long refundId, Integer auditStatus, String auditMessage) {
		videoRefundApi.audit(refundId,auditStatus,auditMessage, SaaSContextHolder.getCurrentUserId());
	}
}
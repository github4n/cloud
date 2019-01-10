package com.iot.boss.service.refund.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.boss.dao.refund.VideoRefundRecordMapper;
import com.iot.boss.dto.OrderPriceDto;
import com.iot.boss.dto.RefundListDto;
import com.iot.boss.dto.RefundListSearch;
import com.iot.boss.entity.refund.VideoRefundRecord;
import com.iot.boss.service.refund.VideoRefundRecordService;

/**
 * 项目名称：cloud
 * 功能描述：视频退款记录serviceImpl
 * 创建人： yeshiyuan
 * 创建时间：2018/5/21 11:12
 * 修改人： yeshiyuan
 * 修改时间：2018/5/21 11:12
 * 修改描述：
 */
@Service
public class VideoRefundRecordServiceImpl implements VideoRefundRecordService {

    @Autowired
    private VideoRefundRecordMapper videoRefundRecordMapper;
    /**
     * 描述：插入退款申请记录
     * @author 490485964@qq.com
     * @date 2018/5/21 17:49
     * @param
     * @return
     */
    @Override
    public int applyRefund(VideoRefundRecord videoRefundRecord) {
        return videoRefundRecordMapper.insertVideoRefundRecord(videoRefundRecord);
    }
    /**
     * 描述：用于判断订单是否已经申请退款
     * @author 490485964@qq.com
     * @date 2018/5/21 17:49
     * @param
     * @return
     */
    @Override
    public int checkRefundRecordExist(String orderId, Long tenantId) {
        return videoRefundRecordMapper.checkRefundRecordExist(orderId,tenantId);
    }
    /**
     * 描述：价格和退款理由修改
     * @author 490485964@qq.com
     * @date 2018/5/22 18:50
     * @param
     * @return
     */
    @Override
    public int editRecordPrice(VideoRefundRecord videoRefundRecord) {
        return videoRefundRecordMapper.editRecordPrice(videoRefundRecord);
    }

    /**
     * @despriction：获取退款申请详情
     * @author  yeshiyuan
     * @created 2018/5/21 15:19
     * @param refundId 退款id
     * @return
     */
    @Override
    public VideoRefundRecord getVideoRefundRecordById(Long refundId) {
        return videoRefundRecordMapper.getById(refundId);
    }

    @Override
    public int updateAuditStatus(VideoRefundRecord videoRefundRecord, Integer oldAuditStatus) {
        return videoRefundRecordMapper.updateAuditStatus(videoRefundRecord,oldAuditStatus);
    }

    /**
     *
     * 描述：管理(运维)人员列表查询
     * @author ouyangjie
     * @created 2018年5月21日 上午10:09:56
     * @since
     * @param orderId
     * @return
     */
    @Override
    public OrderPriceDto getOrderPrice(String orderId, String userId){
        return videoRefundRecordMapper.getOrderPrice(orderId, userId);
    }

    /**
     *
     * 描述：管理(运维)人员列表查询
     * @author ouyangjie
     * @created 2018年5月21日 上午10:09:56
     * @since
     * @param searchParam
     * @return
     */
    @Override
    public List<RefundListDto> queryRefundApplyList(RefundListSearch searchParam){
        return videoRefundRecordMapper.queryRefundApplyList(searchParam);
    }

    @Override
    public int updateRefundStatus(Long refundId, Integer refundStatus) {
        return videoRefundRecordMapper.updateRefundStatus(refundId,refundStatus);
    }
	
	/**
     * 
     * 描述：获取退款记录
     * @author 李帅
     * @created 2018年5月22日 下午5:45:04
     * @since 
     * @param orderIDList
     * @return
     */
    @Override
    public List<VideoRefundRecord> getVideoRefundRecord(List<String> orderIDList) {
        return videoRefundRecordMapper.getVideoRefundRecord(orderIDList);
    }
}

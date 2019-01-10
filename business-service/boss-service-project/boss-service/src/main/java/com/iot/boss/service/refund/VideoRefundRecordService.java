package com.iot.boss.service.refund;

import com.iot.boss.dto.OrderPriceDto;
import com.iot.boss.dto.RefundListDto;
import com.iot.boss.dto.RefundListSearch;
import java.util.List;

import com.iot.boss.entity.refund.VideoRefundRecord;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：视频退款记录service
 * 创建人： yeshiyuan
 * 创建时间：2018/5/21 11:11
 * 修改人： yeshiyuan
 * 修改时间：2018/5/21 11:11
 * 修改描述：
 */
public interface VideoRefundRecordService {
    /**
     * 描述：插入退款申请记录
     * @author 490485964@qq.com
     * @date 2018/5/21 16:02
     * @param videoRefundRecord
     * @return
     */
    int applyRefund(VideoRefundRecord videoRefundRecord);
    /**
     * 描述：用于判断订单是否已经申请退款
     * @author 490485964@qq.com
     * @date 2018/5/21 16:03
     * @param
     * @return
     */
    int checkRefundRecordExist(String orderId,Long tenantId);
    /**
     * 描述：价格和退款理由修改
     * @author 490485964@qq.com
     * @date 2018/5/22 18:50
     * @param
     * @return
     */
    int editRecordPrice(VideoRefundRecord videoRefundRecord);
    /**
     * @despriction：获取退款申请详情
     * @author  yeshiyuan
     * @created 2018/5/21 15:19
     * @param refundId 退款id
     * @return
     */
    VideoRefundRecord getVideoRefundRecordById(Long refundId);

    /**
     * @despriction：退款审核操作
     * @author  yeshiyuan
     * @created 2018/5/21 18:12
     * @param null
     * @return
     */
    int updateAuditStatus(VideoRefundRecord videoRefundRecord,Integer oldAuditStatus);

    /**
     *
     * 描述：查询订单价格
     * @author ouyangjie
     * @created 2018年5月21日 上午10:09:56
     * @since
     * @param orderId
     * @return
     */
    OrderPriceDto getOrderPrice(String orderId, String userId);

    /**
     *
     * 描述：退款申请列表查询
     * @author ouyangjie
     * @created 2018年5月21日 上午10:09:56
     * @since
     * @param searchParam
     * @return
     */
    public List<RefundListDto> queryRefundApplyList(RefundListSearch searchParam);

    /**
     * @despriction：修改退款状态
     * @author  yeshiyuan
     * @created 2018/5/22 11:39
     * @param refundId 退款申请记录id
     * @param refundStatus 退款状态
     * @return
     */
    int updateRefundStatus(Long refundId,Integer refundStatus);
	
	/**
     * 
     * 描述：获取退款记录
     * @author 李帅
     * @created 2018年5月22日 下午5:30:21
     * @since 
     * @param orderIDList
     * @return
     */
    List<VideoRefundRecord> getVideoRefundRecord(List<String> orderIDList);
}

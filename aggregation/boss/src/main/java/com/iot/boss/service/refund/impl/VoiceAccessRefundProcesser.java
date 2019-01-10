package com.iot.boss.service.refund.impl;

import com.iot.boss.exception.BossExceptionEnum;
import com.iot.boss.vo.refund.req.RefundOperateReq;
import com.iot.boss.vo.refund.req.RefundReq;
import com.iot.common.exception.BusinessException;
import com.iot.device.api.ProductServiceInfoApi;
import com.iot.device.api.ServiceBuyRecordApi;
import com.iot.device.api.ServiceReviewApi;
import com.iot.device.enums.ServicePayStatusEnum;
import com.iot.device.vo.req.service.UpdateServicePayStatusReq;
import com.iot.device.vo.rsp.ServiceBuyRecordResp;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.api.OrderApi;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.entity.order.OrderRecord;
import com.iot.payment.enums.order.OrderRecordStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目名称：立达信IOT云平台
 * 功能描述：语音接入退款处理
 * 创建人： mao2080@sina.com
 * 创建时间：2018/11/13 20:13
 * 修改人： mao2080@sina.com
 * 修改时间：2018/11/13 20:13
 * 修改描述：
 */
@Service
public class VoiceAccessRefundProcesser extends RefundProcesser {
    @Autowired
    private OrderApi orderApi;
    @Autowired
    private ServiceBuyRecordApi serviceBuyRecordApi;
    @Autowired
    private ServiceReviewApi serviceReviewApi;
    @Autowired
    private GoodsServiceApi goodsServiceApi;
    @Autowired
    private ProductServiceInfoApi productServiceInfoApi; // 第三方增值服务

    @Override
    public RefundOperateReq beforeRefund(RefundReq req) {
        ServiceBuyRecordResp serviceBuyRecordResp = serviceBuyRecordApi.getServiceBuyRecordByOrderId(req.getOrderId(), req.getTenantId());
        if (serviceBuyRecordResp == null) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "order isn't exists");
        }
        Integer payStatus = serviceBuyRecordResp.getPayStatus();
        if (!ServicePayStatusEnum.checkCanRefund(payStatus)) {
            throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "order can't refund, pay status must be pay_success or refundFail");
        }
        RefundOperateReq refundOperateReq = new RefundOperateReq();
        refundOperateReq.setTenantId(req.getTenantId());
        refundOperateReq.setUserId(serviceBuyRecordResp.getUserId());
        refundOperateReq.setRefundObjectId(serviceBuyRecordResp.getServiceId());
        refundOperateReq.setOrderId(req.getOrderId());
        refundOperateReq.setRefundReason(req.getRefundReason());
        refundOperateReq.setRefundSum(req.getRefundSum());
        return refundOperateReq;
    }

    @Override
    public void refundSuccess(RefundOperateReq req) {
        // 1、虚拟服务购买记录表（service_buy_record）支付状态改为“已退款”
        UpdateServicePayStatusReq updateServicePayStatusReq = new UpdateServicePayStatusReq();
        updateServicePayStatusReq.setNewPayStatus(OrderRecordStatusEnum.REFUND_SUCCESS.getCode());
        updateServicePayStatusReq.setOrderId(req.getOrderId());
        updateServicePayStatusReq.setTenantId(req.getTenantId());
        serviceBuyRecordApi.updatePayStatus(updateServicePayStatusReq);
        // 2、修改产品（product）相关信息字段
        List<Long> goodsIdList =  orderApi.getOrderGoodsIds(req.getOrderId(), req.getTenantId());
        GoodsInfo goodsInfo = goodsServiceApi.getOldGoodsInfoByOrderGoodsId(goodsIdList.get(0));
        // 直接删除该第三方关联服务信息,用户再次购买重新插入即可
        productServiceInfoApi.deleteProductServiceInfo(req.getTenantId(), req.getRefundObjectId(), goodsInfo.getId());
        // 3、审核记录（service_review_record）转为历史记录
        serviceReviewApi.invalidRecord(req.getRefundObjectId(), req.getTenantId(), goodsInfo.getId());
    }

    @Override
    public void refundFail(RefundOperateReq req) {
        OrderRecord orderRecord = orderApi.getOrderRecord(req.getOrderId(), req.getTenantId());
        orderApi.updateOrderRecordStatus(req.getOrderId(), req.getTenantId(), OrderRecordStatusEnum.REFUND_FAIL.getCode(),orderRecord.getOrderStatus());
        UpdateServicePayStatusReq updateServicePayStatusReq = new UpdateServicePayStatusReq();
        updateServicePayStatusReq.setNewPayStatus(OrderRecordStatusEnum.REFUND_FAIL.getCode());
        updateServicePayStatusReq.setOrderId(req.getOrderId());
        updateServicePayStatusReq.setTenantId(req.getTenantId());
        serviceBuyRecordApi.updatePayStatus(updateServicePayStatusReq);
    }

}

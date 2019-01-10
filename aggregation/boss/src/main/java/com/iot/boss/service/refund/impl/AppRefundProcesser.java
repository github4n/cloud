package com.iot.boss.service.refund.impl;

import com.iot.boss.exception.BossExceptionEnum;
import com.iot.boss.vo.refund.req.RefundOperateReq;
import com.iot.boss.vo.refund.req.RefundReq;
import com.iot.common.exception.BusinessException;
import com.iot.device.api.ServiceBuyRecordApi;
import com.iot.device.enums.ServicePayStatusEnum;
import com.iot.device.vo.req.service.UpdateServicePayStatusReq;
import com.iot.device.vo.rsp.ServiceBuyRecordResp;
import com.iot.tenant.api.AppApi;
import com.iot.tenant.api.AppReviewApi;
import com.iot.tenant.vo.resp.AppInfoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： mao2080@sina.com
 * 创建时间：2018/11/13 20:13
 * 修改人： mao2080@sina.com
 * 修改时间：2018/11/13 20:13
 * 修改描述：
 */
@Service
public class AppRefundProcesser extends RefundProcesser {


    @Autowired
    private AppReviewApi appReviewApi;

    @Autowired
    private AppApi appApi;

    @Autowired
    private ServiceBuyRecordApi serviceBuyRecordApi;

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
        AppInfoResp appInfoResp = appApi.getAppById(serviceBuyRecordResp.getServiceId());
        if (appInfoResp != null) {
            if (appInfoResp.getStatus() == 1) {
                throw new BusinessException(BossExceptionEnum.PARAM_IS_ERROR, "app is packing now, can't refund");
            }
        }
        //订单修改为退款中
        UpdateServicePayStatusReq payStatusReq = new UpdateServicePayStatusReq(req.getTenantId(), req.getOrderId(), serviceBuyRecordResp.getPayStatus(), ServicePayStatusEnum.REFUNDING.getValue());
        serviceBuyRecordApi.updatePayStatus(payStatusReq);
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
        //审核记录置为失效
        appReviewApi.invalidRecord(req.getRefundObjectId(), req.getTenantId());
        //订单修改为退款成功
        UpdateServicePayStatusReq payStatusReq = new UpdateServicePayStatusReq(req.getTenantId(), req.getOrderId(), null, ServicePayStatusEnum.REFUND_SUCCESS.getValue());
        serviceBuyRecordApi.updatePayStatus(payStatusReq);
        //审核状态置为空
        appApi.updateAuditStatusToNull(req.getRefundObjectId());
    }

    @Override
    public void refundFail(RefundOperateReq req) {
        //订单修改为退款成功
        UpdateServicePayStatusReq payStatusReq = new UpdateServicePayStatusReq(req.getTenantId(), req.getOrderId(), null, ServicePayStatusEnum.REFUND_FAIL.getValue());
        serviceBuyRecordApi.updatePayStatus(payStatusReq);
    }

}

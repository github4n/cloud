package com.iot.boss.service.refund.impl;


import com.iot.boss.service.refund.IRefundProcesser;
import com.iot.boss.vo.refund.req.RefundOperateReq;
import com.iot.boss.vo.refund.req.RefundReq;
import com.iot.common.util.JsonUtil;
import com.iot.payment.api.PaymentApi;
import com.iot.payment.dto.RefundDto;
import com.iot.payment.entity.PayRes;
import com.iot.payment.enums.PayRespCodeEnum;
import com.iot.user.api.UserApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： mao2080@sina.com
 * 创建时间：2018/11/13 20:40
 * 修改人： mao2080@sina.com
 * 修改时间：2018/11/13 20:40
 * 修改描述：
 */
@Service
abstract class RefundProcesser implements IRefundProcesser {

    private static Logger logger = LoggerFactory.getLogger(RefundProcesser.class);

    @Autowired
    private PaymentApi paymentApi;

    @Autowired
    private UserApi userApi;


    @Override
    public String execute(RefundReq req) {
        String message;
        RefundOperateReq refundOperateReq = this.beforeRefund(req);
        if(this.refund(refundOperateReq)){
            this.refundSuccess(refundOperateReq);
            message = "refund success";
        }else{
            this.refundFail(refundOperateReq);
            message = "refund fail";
        }
        return message;
    }

    /**
      * @despriction：发起退款
      * @author  yeshiyuan
      * @created 2018/11/14 9:52
      * @return
      */
    public boolean refund(RefundOperateReq req){
        boolean refundResult = false;
        String userUuid = userApi.getUuid(req.getUserId());
        RefundDto refundDto = new RefundDto(req.getRefundSum(), userUuid, req.getOrderId(), req.getTenantId(), req.getRefundReason());
        try {
            PayRes payRes = paymentApi.refund(refundDto);
            logger.info("refund result:{}", JsonUtil.toJson(payRes));
            if (payRes.getCode().equals(PayRespCodeEnum.SUCCESS.getCode())) {
                refundResult = true;
            }
        } catch (Exception e) {
            logger.error("RefundProcesser -> refund error", e);
        }
        return refundResult;
    }

}

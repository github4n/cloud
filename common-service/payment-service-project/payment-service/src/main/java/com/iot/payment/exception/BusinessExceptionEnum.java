package com.iot.payment.exception;

import com.iot.common.exception.IBusinessException;

/** 
 * 
 * 项目名称：IOT视频云
 * 模块名称：聚合层
 * 功能描述：视频云聚合层异常枚举
 * 创建人： mao2080@sina.com 
 * 创建时间：2018/3/22 15:16
 * 修改人： mao2080@sina.com 
 * 修改时间：2018/3/22 15:16
 * 修改描述：
 */
public enum BusinessExceptionEnum implements IBusinessException {

	CREATE_PAYMENT_FAIL("paymentService.createPayment.fail"),
	
	SET_REDIS_FAIL("paymentService.setRedis.fail"),
	
	EMPTY_OF_TRANSATION("paymentService.transation.empty"),
	
	EMPTY_OF_PAYPRICE("paymentService.payPrice.empty"),
	
	EMPTY_OF_CURRENCY("paymentService.currency.empty"),
	
	EMPTY_OF_CANCELURL("paymentService.cancelUrl.empty"),
	
	EMPTY_OF_RETURNURL("paymentService.returnUrl.empty"),
	
	EMPTY_OF_GOODSID("paymentService.goodsId.empty"),
	
	EMPTY_OF_USERID("paymentService.userId.empty"),
	
	EMPTY_OF_ORDERID("paymentService.orderId.empty"),
	
	TRANSATION_IS_EXIST("paymentService.transation.exist"),
	
	WEB_PAY_FAIL("paymentService.pay.fail"),
	
	EMPTY_OF_PAYERID("paymentService.payerId.empty"),
	
	EMPTY_OF_PAYMENTID("paymentService.paymentId.empty"),
	
	REFUND_FAIL("paymentService.refund.fail"),
	
	EMPTY_OF_REFUND("paymentService.refund.empty"),
	
	EMPTY_OF_REFUNDSUM("paymentService.refundSum.empty"),
	
	EMPTY_OF_PAYID("paymentService.payId.empty"),
	
	RABBITMQ_SEND_FAIL("paymentService.rabbitmq.send.fail"),

	EMPTY_OF_TENANTID("paymentService.tenantId.empty"),
	/**
	 * 交易流水不存在
	 */
    PAY_TRANSATION_EMPTY("paymentService.payTransation.empty"),

	/**
	 * 交易流水已退款
	 */
	PAY_TRANSATION_HAVE_REFUND("paymentService.payTransation.have.refunded"),

	/**
	 * 退款金额错误
	 */
	REFUND_AMOUNT_ERROR("paymentService.refundAmount.error");


    private String messageKey;

    BusinessExceptionEnum(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return 0;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

}
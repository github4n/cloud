package com.iot.payment.exception;

import com.iot.common.exception.IBusinessException;

/** 
 * 
 * 项目名称：IOT视频云
 * 模块名称：聚合层
 * 功能描述：订单异常
 * 创建人： mao2080@sina.com 
 * 创建时间：2018/3/22 15:16
 * 修改人： mao2080@sina.com 
 * 修改时间：2018/3/22 15:16
 * 修改描述：
 */
public enum OrderExceptionEnum implements IBusinessException {

	/**
     * 订单价格为空
	 */
	ORDER_PRICE_EMPTY("paymentService.order.price.empty"),
	/**
	 * 订单价格有误
	 */
	ORDER_PRICE_ERROR("paymentService.order.price.error"),
	/**
	 * 订单对应商品为空
	 */
	ORDER_BUYNUM_EMPTY("paymentService.order.buyNum.empty"),
	/**
	 * 订单类型为空
	 */
	ORDER_TYPE_EMPTY("paymentService.order.orderType.empty"),
	/**
	 * 订单价格有误
	 */
	ORDER_TYPE_ERROR("paymentService.order.orderType.error"),
	/**
	 * 订单商品为空
	 */
	ORDER_GOODS_EMPTY("paymentService.order.goods.empty"),
	/**
	 * 订单商品有误
	 */
	ORDER_GOODS_ERROR("paymentService.order.goods.error"),
	/**
	 * 订单id为空
	 */
	ORDER_ID_ISNULL("paymentService.order.is.null"),
	/**
	 * 订单不存在
	 */
	ORDER_NOT_EXIST("paymentService.order.not.exist"),
	/**
	 * 订单数量不正确
	 */
	ORDER_CREATE_NUM_ERROR("paymentService.order.num.error"),
	/**
	 * 订单状态不正确
	 */
	ORDER_STATUS_ERROR("paymentService.order.status.error"),
	/**
	 * 订单修改失败
	 */
	ORDER_UPDATE_ERROR("paymentService.order.update.error");


    private String messageKey;

    OrderExceptionEnum(String messageKey) {
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
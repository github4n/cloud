package com.iot.portal.exception;

import com.iot.common.exception.IBusinessException;

/**
 * 项目名称：cloud
 * 功能描述：uuid异常
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/22 15:16
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/22 15:16
 * 修改描述：
 */
public enum UuidBusinessExceptionEnum implements IBusinessException {

    /** uuid对应的产品不存在*/
    PRODUCT_NOT_EXIST("uuid.product.notExist"),

    /** uuid对应的产品不属于当前租户*/
    UUID_PRODUCT_NOT_BELONG_YOU("uuid.product.notBelongYou"),

    /** uuid订单支付出错*/
    UUID_ORDER_PAY_ERROR("uuid.order.payError"),

    /** uuid订单不存在*/
    UUID_ORDER_NOT_EXIST("uuid.order.notExist"),

    /** uuid订单已支付*/
    UUID_ORDER_HAD_PAY("uuid.order.hadPay"),

    /** uuid订单没有商品*/
    UUID_ORDER_GOODS_NULL("uuid.order.goodsNull"),

    /** uuid支付回调异常*/
    UUID_PAY_CALLBACK_ERROR("uuid.payCallBack.error"),

    /** uuid申请数量小于1*/
    UUID_NUM_LESS_ONE("uuid.num.lessOne"),

    /** 产品ID不能为NULL*/
    UUID_PRODUCT_ID_NOT_NULL("uuid.product.id.not.null"),

    /** 商品ID不能为NULL*/
    UUID_GOOD_ID_NOT_NULL("uuid.good.id.not.null");

    private String messageKey;

    UuidBusinessExceptionEnum(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return 0;
    }
    
    @Override
    public String getMessageKey() {
        return messageKey;
    }

}
package com.iot.device.exception;

import com.iot.common.exception.IBusinessException;

/**
 * 项目名称：cloud
 * 功能描述：uuid异常
 * 创建人： yeshiyuan
 * 创建时间：2018/6/29 14:36
 * 修改人： yeshiyuan
 * 修改时间：2018/6/29 14:36
 * 修改描述：
 */
public enum UuidExceptionEnum implements IBusinessException {

    /**
     * 订单id为空
     */
    UUID_APPLY_ORDER_ISNULL("uuid.order.is.null"),
    /**
     * 申请记录不存在
     */
    UUID_APPLY_NOT_EXIST("uuid.apply.not.exist"),
    /**
     * 订单数量不正确
     */
    UUID_APPLY_CREATE_NUM_ERROR("uuid.order.num.error"),
    /**
     * uuid参数异常
     */
    UUID_PARAM_ERROR("uuid.param.error"),
    /**
     * uuid申请失败
     */
    UUID_APPLY_FAIL("uuid.apply.fail");

    private int code;

    private String messageKey;

    UuidExceptionEnum(String messageKey) {
        code = 0;
        this.messageKey = messageKey;
    }

    UuidExceptionEnum(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

}

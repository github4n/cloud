package com.iot.ifttt.common;

import com.iot.common.exception.IBusinessException;

/**
 * 描述：异常枚举
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/23 14:42
 */
public enum IftttExceptionEnum implements IBusinessException {
    LATERAL_ULTRA_VIRES(16001, "lateral ultra vires");

    private int code;

    private String messageKey;

    IftttExceptionEnum(int code, String messageKey) {
        this.code = code;
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

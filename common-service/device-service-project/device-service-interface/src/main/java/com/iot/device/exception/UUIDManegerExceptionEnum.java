package com.iot.device.exception;

import com.iot.common.exception.IBusinessException;

/**
 * @Author: chq
 * @Descrpiton:
 * @Date: 14:40 2018/6/27
 * @Modify by:
 */
public enum UUIDManegerExceptionEnum implements IBusinessException {
    LICENSE_USAGE_SAVE_ERROR("uuidmanager.licenseusage.save.error"),
    LICENSE_USAGE_IS_NULL("uuidmanager.licenseusage.is.null");

    private int code;

    private String messageKey;


    UUIDManegerExceptionEnum(String messageKey) {
        code = 0;
        this.messageKey = messageKey;
    }

    UUIDManegerExceptionEnum(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

}

package com.iot.device.exception;

import com.iot.common.exception.IBusinessException;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:46 2018/5/2
 * @Modify by:
 */
public enum VersionExceptionEnum implements IBusinessException {


    AOT_NOT_EXIST("aot.not.exist"),;
    private int code;

    private String messageKey;

    VersionExceptionEnum(String messageKey) {
        code = 0;
        this.messageKey = messageKey;
    }

    VersionExceptionEnum(int code, String messageKey) {
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

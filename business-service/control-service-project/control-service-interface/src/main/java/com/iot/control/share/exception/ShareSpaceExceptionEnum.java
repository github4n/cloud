package com.iot.control.share.exception;

import com.iot.common.exception.IBusinessException;

public enum ShareSpaceExceptionEnum implements IBusinessException {
    SHARE_SPACE_NOT_EXIST_ERROR(130101, "share.space.not.exist"),
    ;

    private int code;

    private String messageKey;

    ShareSpaceExceptionEnum(String messageKey) {
        code = 0;
        this.messageKey = messageKey;
    }

    ShareSpaceExceptionEnum(int code, String messageKey) {
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
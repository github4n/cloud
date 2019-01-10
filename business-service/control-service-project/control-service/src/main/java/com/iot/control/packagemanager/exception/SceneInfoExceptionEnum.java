package com.iot.control.packagemanager.exception;

import com.iot.common.exception.IBusinessException;

public enum SceneInfoExceptionEnum implements IBusinessException {

    QUERY_ERROR("query.error"),
    PARAM_IS_ERROR("param.is.error");


    private int code;
    private String messageKey;

    SceneInfoExceptionEnum(String messageKey) {
        this.messageKey = messageKey;
        this.code = 0;
    }

    @Override
    public int getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }
}

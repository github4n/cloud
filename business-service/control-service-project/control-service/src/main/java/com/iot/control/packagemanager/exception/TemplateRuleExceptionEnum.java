package com.iot.control.packagemanager.exception;

import com.iot.common.exception.IBusinessException;

public enum TemplateRuleExceptionEnum implements IBusinessException {
    PARAM_IS_NULL("param.is.null");


    private int code;
    private String messageKey;

    TemplateRuleExceptionEnum(String messageKey) {
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

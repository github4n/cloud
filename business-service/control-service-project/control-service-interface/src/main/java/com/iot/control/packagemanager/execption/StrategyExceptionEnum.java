package com.iot.control.packagemanager.execption;

import com.iot.common.exception.IBusinessException;

/**
 *@description 策略异常枚举
 *@author wucheng
 *@create 2018/12/5 13:49
 */
public enum StrategyExceptionEnum implements IBusinessException {

    PARAM_IS_NULL("param.is.null"),
    PARAM_IS_ERROR("param.is.error"),
    ATTRTYPE_NOT_EXIST("atttype.not.exist"),
    PROPERTY_ERROR("property.is.error"),
    /**属性不存在*/
    PROPERTY_NOT_EXIST("rule.property.notExist"),
    /**方法不存在*/
    ACTION_NOT_EXIST("rule.action.notExist"),
    /**事件不存在*/
    EVENT_NOT_EXIST("rule.event.notExist");

    private int code;
    private String messageKey;

    StrategyExceptionEnum(String messageKey) {
        this.code = 0;
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

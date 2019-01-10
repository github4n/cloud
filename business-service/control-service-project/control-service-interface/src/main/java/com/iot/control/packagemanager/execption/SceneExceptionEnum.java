package com.iot.control.packagemanager.execption;

import com.iot.common.exception.IBusinessException;

/**
 *@description
 *@author wucheng
 *@params 
 *@create 2018/12/5 13:52
 *@return 
 */
public enum SceneExceptionEnum implements IBusinessException {


    /**参数异常*/
    PARAM_ERROR("scene.param.error"),
    /**属性不存在*/
    PROPERTY_NOT_EXIST("scene.property.notExist"),
    /**方法不存在*/
    ACTION_NOT_EXIST("scene.action.notExist");

    private int code;
    private String messageKey;

    SceneExceptionEnum(String messageKey) {
        this.messageKey = messageKey;
        this.code = 0;
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

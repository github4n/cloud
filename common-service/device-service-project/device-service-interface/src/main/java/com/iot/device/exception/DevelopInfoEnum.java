package com.iot.device.exception;

import com.iot.common.exception.IBusinessException;

/**
 * @Author: xfz @Descrpiton: @Date: 9:54 2018/6/29 @Modify by:
 */
public enum DevelopInfoEnum implements IBusinessException {
    DEVELOP_NOT_EXIST("develop.info.not.exist");

    private int code;

    private String messageKey;

    private DevelopInfoEnum(String messageKey) {
        code = 0;
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
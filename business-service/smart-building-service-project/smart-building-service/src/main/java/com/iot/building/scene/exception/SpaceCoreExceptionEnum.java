package com.iot.building.scene.exception;

import com.iot.common.exception.IBusinessException;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 16:49 2018/6/13
 * @Modify by:
 */
public enum SpaceCoreExceptionEnum implements IBusinessException {

    USER_NOT_DEFAULT_SPACE(12003, "user.not.exist.default.space"),
    USER_DEVICE_NOT_EXIST(12004, "user.device.not.exist"),
    ;
    /**
     * 异常代码
     */
    private int code;

    /**
     * 异常描述
     */
    private String messageKey;


    SpaceCoreExceptionEnum(Integer code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public int getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }
}

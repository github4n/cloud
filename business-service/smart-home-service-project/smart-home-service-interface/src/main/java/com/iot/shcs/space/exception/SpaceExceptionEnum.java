package com.iot.shcs.space.exception;


import com.iot.common.exception.IBusinessException;

public enum SpaceExceptionEnum implements IBusinessException {

    SPACE_ID_IS_NULL(12001, "SpaceId.is.null"),
    SPACE_IS_NULL(12002, "Space.is.null"),
    USER_NOT_DEFAULT_SPACE(12003, "user.not.exist.default.space"),
    SPACE_DEVICE_NOT_EXIT(12004, "space.device.does.not.exist"),
    USER_HOME_IS_EXIST(20130, "User.default.home.is.exist");
    private int code;

    private String messageKey;

    SpaceExceptionEnum(String messageKey) {
        code = 0;
        this.messageKey = messageKey;
    }

    SpaceExceptionEnum(int code, String messageKey) {
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

package com.iot.device.exception;

import com.iot.common.exception.IBusinessException;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:15 2018/4/25
 * @Modify by:
 */
public enum DeviceRemoteExceptionEnum implements IBusinessException {

    DEVICEREMOTE_DEVICEBUSINESSTYPE_EXIST("deviceBusinessType.exist.error");

    private int code;

    private String messageKey;

    DeviceRemoteExceptionEnum(String messageKey) {
        code = 0;
        this.messageKey = messageKey;
    }

    DeviceRemoteExceptionEnum(int code, String messageKey) {
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

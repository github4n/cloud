package com.iot.device.exception;

import com.iot.common.exception.IBusinessException;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:15 2018/4/25
 * @Modify by:
 */
public enum DeviceTypeExceptionEnum implements IBusinessException {

    DEVICETYP_ERROR("deviceType.error"),
    DEVICETYP_DEL_ERROR("deviceType.delete.error"),
    DEVICETYP_IS_USED_PRODUCT("deviceType.is.being.used"),
    DEVICETYP_NOT_EXIST("deviceType.not.exist.error"),
    DEVICETYP_PAGE_ILLEGAL("deviceType.page.illegal"),
    DEVICCATALOG_IS_USED("deviceCatalog.is.being.used"),
    NAME_IS_EXIT("name.already.exists");

    private int code;

    private String messageKey;

    DeviceTypeExceptionEnum(String messageKey) {
        code = 0;
        this.messageKey = messageKey;
    }

    DeviceTypeExceptionEnum(int code, String messageKey) {
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

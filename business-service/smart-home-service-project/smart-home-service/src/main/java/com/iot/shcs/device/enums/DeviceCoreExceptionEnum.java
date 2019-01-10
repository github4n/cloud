package com.iot.shcs.device.enums;

import com.iot.common.exception.IBusinessException;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 16:10 2018/6/13
 * @Modify by:
 */
public enum DeviceCoreExceptionEnum implements IBusinessException {

    USER_DISCONNECTED(400, "user.disconnected"),
    USER_EXIST_DIRECT_DEVICE(400, "device.user.direct.exist"),
    USER_DEVICE_NOT_EXIST(400, "user.device.not.exist"),
    PRODUCT_NOT_EXIST(400, "product.not.exist"),
    DEVICE_BIND_ERROR(400, "device.bind.error"),
    DEVICE_NAME_EXIST(400, "device.name.exist"),
    DEVICE_NOT_EXIST(400, "device.not.exist"),
    SYSTEM_ERROR(400, "system.error"),
    HOMEID_NOT_NULL(400, "homeId.notnull"),
    DEVICE_TENANT_ERROR(400, "device.tenant.error"),
    DEVICE_IS_EXIST(400, "device.is.exist"),
    PRODUCT_COMMODE_IS_ERROR(400, "product.comMode.is.error"),
    SPACE_DEFAULT_NOT_EXIST(400, "default.space.not.exist"),
    DIRECT_DEVICE_IS_NULL(400,"direct.device.is.null");
    /**
     * 异常代码
     */
    private int code;

    /**
     * 异常描述
     */
    private String messageKey;


    DeviceCoreExceptionEnum(Integer code, String messageKey) {
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

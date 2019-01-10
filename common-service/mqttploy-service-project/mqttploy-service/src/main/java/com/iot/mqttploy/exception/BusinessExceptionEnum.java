package com.iot.mqttploy.exception;

import com.iot.common.exception.IBusinessException;

public enum BusinessExceptionEnum implements IBusinessException {

    PARAM_ERROR("mqttploy.param.error"),

    USER_EXIST("mqttploy.user.exist"),

    DEVICE_EXIST("mqttploy.device.exist"),

    DEVICE_NOT_EXIST("mqttploy.device.not.exist"),

    USER_NOT_EXIST("mqttploy.user.not.exist"),

    SAVE_ACLS_FAILED("mqttploy.saveAcls.failed"),

    BATCH_SAVE_ACLS_FAILED("mqttploy.batchSaveAclsInternal.failed"),

    ADD_ACLS_FAILED("mqttploy.addAcls.failed"),

    SEPARATION_ACLS_FAILED("mqttploy.separationAcls.failed"),

    CHANGE_PASSWD_FAILED("mqttploy.changePassword.failed"),

    GENERATE_PLOY_FAILED("mqttploy.generatePloy.failed")

    ;

    private String messageKey;

    BusinessExceptionEnum(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return 0;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }
}

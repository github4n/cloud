package com.iot.device.exception;

import com.iot.common.exception.IBusinessException;

/**
 * @Author: xfz @Descrpiton: @Date: 15:00 2018/7/2 @Modify by:
 */
public enum ServiceModuleExceptionEnum implements IBusinessException {
    SERVICE_MODULE_NOT_EXIST("service.module.not.exist"),
    SERVICE_MODULE_PARENT_ID_DISTORT("service.module.parent.id.distort"),
    MODULE_ACTION_NOT_EXIST("service.module.action.not.exist"),
    MODULE_EVENT_NOT_EXIST("service.module.event.not.exist"),
    MODULE_PROPERTY_NOT_EXIST("service.module.property.not.exist"),
    MODULE_PROPERTY_IS_USED("module.property.is.being.used"),
    MODULE_ACTION_IS_USED("module.action.is.being.used"),
    MODULE_EVENT_IS_USED("module.event.is.being.used"),
    STYLE_TEMPLATE_IS_USED("style.template.is.being.used"),
    SERVICE_MODULE_IS_USED("service.module.is.being.used"),
    MODULE_PORTAL_IFTTTTYPE_ERROR("service.module.portal.iftttType.error");
    private int code;

    private String messageKey;

    private ServiceModuleExceptionEnum(String messageKey) {
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

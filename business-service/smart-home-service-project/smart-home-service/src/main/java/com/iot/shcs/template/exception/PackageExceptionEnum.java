package com.iot.shcs.template.exception;

import com.iot.common.exception.IBusinessException;

public enum PackageExceptionEnum implements IBusinessException {

    PACKAGE_PAGENUM_NULL(10001, "pageNum.is.null"),
    PACKAGE_PAGESIZE_NULL(10002, "pageSize.is.null"),
    PACKAGE_DELETE_IDS_NULL(10003, "ids.is.not.null"),
    PACKAGE_UPDATE_ID_NULL(10004, "id.is.null");

    private int code;
    private String messageKey;

    PackageExceptionEnum(int code, String messageKey) {
        this.code = code;
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

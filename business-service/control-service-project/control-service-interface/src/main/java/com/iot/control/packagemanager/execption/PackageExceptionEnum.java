package com.iot.control.packagemanager.execption;


import com.iot.common.exception.IBusinessException;

public enum PackageExceptionEnum implements IBusinessException {

    PARAM_ERROR("package.param.error"),
    PACKAGE_PAGENUM_NULL("pageNum.is.null"),
    PACKAGE_PAGESIZE_NULL("pageSize.is.null"),
    PACKAGE_DELETE_IDS_NULL("ids.is.not.null"),
    PACKAGE_UPDATE_ID_NULL("id.is.null"),

    PACKAGE_PRODUCT_HAD_ADD("package.product.hadAdd");

    private int code;

    private String messageKey;

    PackageExceptionEnum(String messageKey) {
        code = 0;
        this.messageKey = messageKey;
    }

    PackageExceptionEnum(int code, String messageKey) {
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

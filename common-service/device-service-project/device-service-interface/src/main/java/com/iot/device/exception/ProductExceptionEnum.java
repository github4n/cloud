package com.iot.device.exception;

import com.iot.common.exception.IBusinessException;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:47 2018/4/17
 * @Modify by:
 */
public enum ProductExceptionEnum implements IBusinessException {

    PRODUCT_ADD_ERROR("product.add.error"),
    
    PRODUCT_MODEL_EXIST("product.model.exist"),

    PRODUCT_NOT_EXIST("product.not.exist"),

    PRODUCT_GET_ERROR("product.get.error"),
    
    PRODUCT_UPDATE_ERROR("product.update.error"),
    
    PRODUCT_UPDATE_POINT_ILLEGAL("product.update.point.illegal"),
    
    PRODUCT_PAGE_ILLEGAL("product.page.illegal"),
    
    PRODUCT_ID_NOTNULL("product.id.notnull"),
    
    GOODS_ID_NOTNULL("goods.id.notnull"),

    BASE_NOTNULL("base.notnull"),

    PRODUCT_AUDITED("product.audited"),

    PARAM_IS_ERROR("param.is.error"),

    PRODUCT_RELEASE_FAIL("product.release.fail"),

    ACCESS_DENIED_EXCEPTION("access.denied.exception"),

    PRODUCT_IS_USED_PRODUCT("product.is.being.used");
    private int code;

    private String messageKey;

    ProductExceptionEnum(String messageKey) {
        code = 0;
        this.messageKey = messageKey;
    }

    ProductExceptionEnum(int code, String messageKey) {
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

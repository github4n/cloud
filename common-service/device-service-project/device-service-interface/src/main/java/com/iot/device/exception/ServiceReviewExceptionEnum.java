package com.iot.device.exception;

import com.iot.common.exception.IBusinessException;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:47 2018/4/17
 * @Modify by:
 */
public enum ServiceReviewExceptionEnum implements IBusinessException {

	SERVICE_REVIEW_STATUS_ERROR("service.review.status.error"),
	/**APP审核目标状态不对*/
	SERVICE_REVIEW_TARGET_STATUS_ERROR("service.review.target.status.error"),
	SERVICEID_IS_NULL("service.id.is.null"),
	GOODSINFO_NOT_EXIST("goodsInfo.does.not.exist"),
    
	PRODUCT_NOT_TENANT("Product.does.not.belong.to.the.tenant");
    
    private int code;

    private String messageKey;

    ServiceReviewExceptionEnum(String messageKey) {
        code = 0;
        this.messageKey = messageKey;
    }

    ServiceReviewExceptionEnum(int code, String messageKey) {
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

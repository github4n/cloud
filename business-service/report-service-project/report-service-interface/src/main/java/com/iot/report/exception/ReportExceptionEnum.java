package com.iot.report.exception;

import com.iot.common.exception.IBusinessException;


/**
 * 描述：用户异常枚举类，代码范围（21001~22000）
 * 创建人： laiguiming
 * 创建时间： 2018年4月09日 下午17:13:00
 */
public enum ReportExceptionEnum implements IBusinessException {

	DEVICE_ACTIVE_CACHE_IS_NOT_EXIST(23001, "device.active.cache.is.not.exist"),
	USER_ACTIVE_CACHE_IS_NOT_EXIST(23002, "user.active.cache.is.not.exist"),
    BEGINDATE_IS_NULL(23003, "begin.date.is.null"),
    ENDDATE_IS_NULL(23005, "end.date.is.null"),
    QUERY_TYPE_IS_ERROR(23006, "end.date.is.error"),
    TENANTID_IS_NULL(23004, "tenantId.is.null");
    /**
     * 异常代码
     */
    private int code;

    /**
     * 异常描述
     */
    private String messageKey;

    public int getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }

    ReportExceptionEnum(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }
}

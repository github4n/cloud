package com.iot.portal.exception;

import com.iot.common.exception.IBusinessException;

public enum OtaBusinessExceptionEnum implements IBusinessException {

    MD5_VALUE_ERROR("md5.value.error"),
    UPLOAD_FIRWARE_FIRST("please.upload.firmware.first"),
    INPUT_MD5_VALUE("please.input.md5.value")
    ;

    /**
     * 异常代码
     */
    private int code;

    /**
     * 异常描述
     */
    private String messageKey;

    OtaBusinessExceptionEnum(String messageKey) {
        this.code = 0;
        this.messageKey = messageKey;
    }

    /**
     * 描述：构建异常
     *
     * @param code       错误代码
     * @param messageKey 错误描述
     * @return
     * @author mao2080@sina.com
     * @created 2017年3月21日 上午10:50:58
     * @since
     */
    OtaBusinessExceptionEnum(Integer code, String messageKey) {
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

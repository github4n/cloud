package com.iot.cloud.helper;

import com.iot.common.exception.IBusinessException;

public enum BusinessExceptionEnum implements IBusinessException {


    PARAM_IS_NULL(11321, "param is null"),
    UPLOAD_FILE_EMPTY(11326, "upload file empty");


    /**
     * 异常代码
     */
    private int code;

    /**
     * 异常描述
     */
    private String messageKey;

    BusinessExceptionEnum(String messageKey) {
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
    BusinessExceptionEnum(Integer code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

}

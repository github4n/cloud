package com.iot.device.exception;

import com.iot.common.exception.IBusinessException;

/**
 * 项目名称：cloud
 * 功能描述：虚拟服务购买记录异常
 * 创建人： yeshiyuan
 * 创建时间：2018/9/13 15:51
 * 修改人： yeshiyuan
 * 修改时间：2018/9/13 15:51
 * 修改描述：
 */
public enum  ServiceBuyRecordExceptionEnum implements IBusinessException {

    /**
     * 参数异常
     */
    PARAM_ERROR("serviceBuyRecord.param.error"),
    /**
     * 已经创建
     */
    HAD_CREATE("serviceBuyRecord.haveCreate");

    private int code;

    private String messageKey;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    ServiceBuyRecordExceptionEnum(String messageKey) {
        this.messageKey = messageKey;
    }
}

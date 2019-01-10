package com.iot.device.exception;

import com.iot.common.exception.IBusinessException;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/9/11 16:35
 * 修改人： yeshiyuan
 * 修改时间：2018/9/11 16:35
 * 修改描述：
 */
public enum StepRecordExecptionEnum implements IBusinessException {

    PARAM_ERROR("operate.step.param.error");

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

    StepRecordExecptionEnum(String messageKey) {
        this.messageKey = messageKey;
    }
}

package com.iot.schedule.common;

import com.iot.common.exception.IBusinessException;

/**
 * 描述：异常枚举
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/28 14:44
 */
public enum ScheduleExceptionEnum implements IBusinessException {

    /************************************（27001~28000）************************************/

    ADD_JOB_ERROR(27001, "add.job.error"),
    JOB_NOT_FOUND_ERROR(27002, "job.not.found.error"),
    DELETE_JOB__ERROR(27002, "delete.job.error"),
    UPDATE_JOB__ERROR(27002, "update.job.error"),
    CHECK_JOB_ERROR(27002, "check.job.error");

    /**
     * 异常代码
     */
    private int code;

    /**
     * 异常描述
     */
    private String messageKey;


    ScheduleExceptionEnum(int code, String messageKey) {
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

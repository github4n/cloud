package com.iot.schedule.vo;

import java.io.Serializable;
import java.util.TimeZone;

/**
 * 描述：添加任务请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/28 17:10
 */
public class AddJobReq implements Serializable {
    private String jobClass;
    private String jobName;
    private String cron;
    private Object data;
    private TimeZone timeZone;

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public String toString() {
        return "AddJobReq{" +
                "jobClass='" + jobClass + '\'' +
                ", jobName='" + jobName + '\'' +
                ", cron='" + cron + '\'' +
                ", object=" + data +
                '}';
    }
}

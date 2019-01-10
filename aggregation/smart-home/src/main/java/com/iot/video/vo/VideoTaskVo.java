package com.iot.video.vo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel
public class VideoTaskVo implements Serializable {

    /***/
    private static final long serialVersionUID = 1L;

    /**
     * 计划id
     */
    private String planId;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 执行日期
     */
    private String taskDate;

    /**
     * 执行周期
     */
    private String planCycle;

    /**
     * 执行开始时间
     */
    private String executeStartTime;

    /**
     * 执行结束时间
     */
    private String executeEndTime;

    /**
     * 时程开关
     */
    private String planStatus;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getPlanCycle() {
        return planCycle;
    }

    public void setPlanCycle(String planCycle) {
        this.planCycle = planCycle;
    }

    public String getExecuteStartTime() {
        return executeStartTime;
    }

    public void setExecuteStartTime(String executeStartTime) {
        this.executeStartTime = executeStartTime;
    }

    public String getExecuteEndTime() {
        return executeEndTime;
    }

    public void setExecuteEndTime(String executeEndTime) {
        this.executeEndTime = executeEndTime;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

}

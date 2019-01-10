package com.iot.video.vo;

import io.swagger.annotations.ApiModel;

@ApiModel
public class TaskPlanVo {

    /**
     * 租户id
     */
    private Long tenantId;

    private String userId;

    /**
     * 计划id
     */
    private String planId;

    /**
     * 套餐id
     */
    private String packageId;

    /**
     * 执行周期
     */
    private String planCycle;

    /**
     * 计划开始时间
     */
    private String executeStartTime;

    /**
     * 计划结束时间
     */
    private String executeEndTime;

    /**
     * 时程开关
     */
    private Integer planStatus;

    /**
     * 执行日期
     */
    private String taskDate;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
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

    public Integer getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(Integer planStatus) {
        this.planStatus = planStatus;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

}

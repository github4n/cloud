package com.iot.device.model.ota;

import java.util.Date;

public class StrategyConfig {
    private Long id;

    private Long tenantId;

    private Long planId;

    private Integer strategyGroup;

    private Integer upgradeTotal;

    private Integer threshold;

    private Integer triggerAction;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    private String isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Integer getStrategyGroup() {
        return strategyGroup;
    }

    public void setStrategyGroup(Integer strategyGroup) {
        this.strategyGroup = strategyGroup;
    }

    public Integer getUpgradeTotal() {
        return upgradeTotal;
    }

    public void setUpgradeTotal(Integer upgradeTotal) {
        this.upgradeTotal = upgradeTotal;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Integer getTriggerAction() {
        return triggerAction;
    }

    public void setTriggerAction(Integer triggerAction) {
        this.triggerAction = triggerAction;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}
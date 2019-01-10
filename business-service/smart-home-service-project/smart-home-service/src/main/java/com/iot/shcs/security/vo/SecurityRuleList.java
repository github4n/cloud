package com.iot.shcs.security.vo;

import java.util.Date;

public class SecurityRuleList {
    private Long id;
    private Long spaceId;
    private Long tenantId;
    private String type;
    private Integer enabled;
    private Integer defer;
    private Integer delay;
    private String ifCondition;
    private String thenCondition;
    private Date createTime;
    private Date updateTime;

    @Override
    public String toString() {
        return "SecurityRuleList{" +
                "id=" + id +
                ", spaceId=" + spaceId +
                ", tenantId=" + tenantId +
                ", type='" + type + '\'' +
                ", enabled=" + enabled +
                ", defer=" + defer +
                ", delay=" + delay +
                ", ifCondition='" + ifCondition + '\'' +
                ", thenCondition='" + thenCondition + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getDefer() {
        return defer;
    }

    public void setDefer(Integer defer) {
        this.defer = defer;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public String getIfCondition() {
        return ifCondition;
    }

    public void setIfCondition(String ifCondition) {
        this.ifCondition = ifCondition;
    }

    public String getThenCondition() {
        return thenCondition;
    }

    public void setThenCondition(String thenCondition) {
        this.thenCondition = thenCondition;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}

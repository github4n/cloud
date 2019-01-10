package com.iot.device.vo.req;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class ServiceToEventReq implements Serializable {

    private Long id;
    private Long tenantId;
    private Long serviceModuleId;
    private Long moduleEventId;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private String description;
    private Integer status;

    private Map<String, String> moduleEventIds;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getServiceModuleId() {
        return serviceModuleId;
    }

    public void setServiceModuleId(Long serviceModuleId) {
        this.serviceModuleId = serviceModuleId;
    }

    public Long getModuleEventId() {
        return moduleEventId;
    }

    public void setModuleEventId(Long moduleEventId) {
        this.moduleEventId = moduleEventId;
    }

    public Map<String, String> getModuleEventIds() {
        return moduleEventIds;
    }

    public void setModuleEventIds(Map<String, String> moduleEventIds) {
        this.moduleEventIds = moduleEventIds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

package com.iot.device.vo.req;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ServiceToPropertyReq implements Serializable {

    private Long id;
    private Long tenantId;
    private Long serviceModuleId;
    private Long modulePropertyId;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private String description;
    private Integer status;


    private Map<String,String> modulePropertyIds;

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

    public Long getModulePropertyId() {
        return modulePropertyId;
    }

    public void setModulePropertyId(Long modulePropertyId) {
        this.modulePropertyId = modulePropertyId;
    }

    public Map<String, String> getModulePropertyIds() {
        return modulePropertyIds;
    }

    public void setModulePropertyIds(Map<String, String> modulePropertyIds) {
        this.modulePropertyIds = modulePropertyIds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

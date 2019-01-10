package com.iot.device.vo.rsp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ModuleActionToPropertyRsp implements Serializable {

    private Long id;
    private Long tenantId;
    private Long moduleActionId;
    private Long modulePropertyId;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private String description;


    private List modulePropertyIds;

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

    public Long getModuleActionId() {
        return moduleActionId;
    }

    public void setModuleActionId(Long moduleActionId) {
        this.moduleActionId = moduleActionId;
    }

    public Long getModulePropertyId() {
        return modulePropertyId;
    }

    public void setModulePropertyId(Long modulePropertyId) {
        this.modulePropertyId = modulePropertyId;
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

    public List getModulePropertyIds() {
        return modulePropertyIds;
    }

    public void setModulePropertyIds(List modulePropertyIds) {
        this.modulePropertyIds = modulePropertyIds;
    }
}

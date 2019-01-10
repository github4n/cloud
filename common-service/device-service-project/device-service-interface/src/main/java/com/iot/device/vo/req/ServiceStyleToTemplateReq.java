package com.iot.device.vo.req;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ServiceStyleToTemplateReq implements Serializable {

    private Long id;
    private Long tenantId;
    private Long moduleStyleId;
    private Long styleTemplateId;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private String description;

    private List styleTemplateIds;

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

    public Long getModuleStyleId() {
        return moduleStyleId;
    }

    public void setModuleStyleId(Long moduleStyleId) {
        this.moduleStyleId = moduleStyleId;
    }

    public Long getStyleTemplateId() {
        return styleTemplateId;
    }

    public void setStyleTemplateId(Long styleTemplateId) {
        this.styleTemplateId = styleTemplateId;
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

    public List getStyleTemplateIds() {
        return styleTemplateIds;
    }

    public void setStyleTemplateIds(List styleTemplateIds) {
        this.styleTemplateIds = styleTemplateIds;
    }
}

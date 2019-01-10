package com.iot.device.vo.req;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProductToServiceModuleReq implements Serializable {

    private Long id;
    private Long tenantId;
    private Long productId;
    private Long serviceModuleId;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;

    private List serviceModuleIds;


    private List moduleActionIds;

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

    public Long getServiceModuleId() {
        return serviceModuleId;
    }

    public void setServiceModuleId(Long serviceModuleId) {
        this.serviceModuleId = serviceModuleId;
    }

    public List getModuleActionIds() {
        return moduleActionIds;
    }

    public void setModuleActionIds(List moduleActionIds) {
        this.moduleActionIds = moduleActionIds;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List getServiceModuleIds() {
        return serviceModuleIds;
    }

    public void setServiceModuleIds(List serviceModuleIds) {
        this.serviceModuleIds = serviceModuleIds;
    }
}

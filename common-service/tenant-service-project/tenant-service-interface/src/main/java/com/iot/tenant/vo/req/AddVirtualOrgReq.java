package com.iot.tenant.vo.req;

import java.io.Serializable;

public class AddVirtualOrgReq implements Serializable {
    /**
     * 组织名称
     */
    private String name;
    /**
     * 所属租户id
     */
    private Long tenantId;
    /**
     * 描述
     */
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

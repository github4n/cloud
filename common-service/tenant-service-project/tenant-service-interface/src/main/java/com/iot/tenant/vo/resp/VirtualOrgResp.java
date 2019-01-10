package com.iot.tenant.vo.resp;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 16:50 2018/4/26
 * @Modify by:
 */
public class VirtualOrgResp implements Serializable {

    /**
     * 租户-组织id
     */
    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;

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
}

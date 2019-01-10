package com.iot.tenant.vo.resp;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton: 获取用户对应 租户+组织信息
 * @Date: 16:48 2018/4/26
 * @Modify by:
 */
public class UserDefaultOrgInfoResp implements Serializable {

    private Long userId;

    /**
     * 租户对应的组织id   orgId
     */
    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }
}

package com.iot.tenant.vo.req;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:38 2018/4/26
 * @Modify by:
 */
public class AddUserVirtualOrgReq implements Serializable {

    private Long userId;

    private Long orgId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}

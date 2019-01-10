package com.iot.tenant.vo.resp;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton: 获取用户对应 租户+组织信息
 * @Date: 16:48 2018/4/26
 * @Modify by:
 */
public class UserOrgInfoResp implements Serializable {

    private Long userId;

    private List<VirtualOrgResp> orgRespList;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<VirtualOrgResp> getOrgRespList() {
        return orgRespList;
    }

    public void setOrgRespList(List<VirtualOrgResp> orgRespList) {
        this.orgRespList = orgRespList;
    }
}

package com.iot.tenant.vo.req;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 16:16 2018/4/26
 * @Modify by:
 */
public class AddUserOrgReq implements Serializable {


    private AddVirtualOrgReq orgReq;

    private AddUserReq userReq;

    public AddVirtualOrgReq getOrgReq() {
        return orgReq;
    }

    public void setOrgReq(AddVirtualOrgReq orgReq) {
        this.orgReq = orgReq;
    }

    public AddUserReq getUserReq() {
        return userReq;
    }

    public void setUserReq(AddUserReq userReq) {
        this.userReq = userReq;
    }
}

package com.iot.device.vo.req;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 9:46 2018/4/27
 * @Modify by:
 */
public class AddUserDeviceReq implements Serializable {

    private Long userId;

    //用户类型
    private String userType;

    private Long orgId;

    private String deviceId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}

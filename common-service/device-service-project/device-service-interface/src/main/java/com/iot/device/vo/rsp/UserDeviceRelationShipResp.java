package com.iot.device.vo.rsp;

import java.io.Serializable;
import java.util.Date;

public class UserDeviceRelationShipResp implements Serializable {

    /**
     * 序列id
     */
    private static final long serialVersionUID = 7382612513048681522L;

    /**
     * 设备主键id
     */
    private Long id;
    /**
     * 直连设备（网关）ID
     */
    private String deviceId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 产品id
     */
    private String prodId;

    /**
     * locationId
     */
    private String locationId;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 临时密码
     */
    private String tempPassword;

    /**
     * 局域网访问IPC需要的密码
     */
    private String password;

    private Date createTime;

    private Long tenantId;

    private Long orgId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return "UserDeviceRelationShipResp{" +
                "id=" + id +
                ", deviceId='" + deviceId + '\'' +
                ", userId=" + userId +
                ", prodId='" + prodId + '\'' +
                ", locationId='" + locationId + '\'' +
                ", userType='" + userType + '\'' +
                ", tempPassword='" + tempPassword + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", tenantId=" + tenantId +
                ", orgId=" + orgId +
                '}';
    }
}

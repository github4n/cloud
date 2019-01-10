package com.iot.user.vo;

import java.util.Set;

/**
 * 描述：登录应答类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/9 14:39
 */
public class LoginResp {
    private String accessToken;
    private String refreshToken;
    private Long expireIn;
    private String mqttPassword;
    private Long userId;
    private String userUuid;
    private String nickName;
    private Long tenantId;

    private Boolean improveTenantInfo;
    private String tenantName;
    private String tenantUuId;
    private String userStatus;

    //用户所属组织id
    private Long orgId;
    private Long locationId;

    private Set<String> permissions;

    private Set<String> roles;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(Long expireIn) {
        this.expireIn = expireIn;
    }

    public String getMqttPassword() {
        return mqttPassword;
    }

    public void setMqttPassword(String mqttPassword) {
        this.mqttPassword = mqttPassword;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUUid) {
        this.userUuid = userUUid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Boolean getImproveTenantInfo() {
        return improveTenantInfo;
    }

    public void setImproveTenantInfo(Boolean improveTenantInfo) {
        this.improveTenantInfo = improveTenantInfo;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantUuId() {
        return tenantUuId;
    }

    public void setTenantUuId(String tenantUuId) {
        this.tenantUuId = tenantUuId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }


    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}

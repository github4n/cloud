package com.iot.user.vo;

import java.io.Serializable;

/**
 * 描述： 用户token应答
 * 创建人： laiguiming
 * 创建时间：2018年4月12日 下午3:26:53
 */
public class UserTokenResp implements Serializable {

    /**
     * 访问Token
     */
    private String accessToken;

    /**
     * 刷新Token
     */
    private String refreshToken;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 有效期
     */
    private Long expireIn;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 终端标识
     */
    private String terminalMark;

    /**
     * 用户uuid
     */
    private String userUuid;

    private Long orgId;

    private Long appId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }


    public String getTerminalMark() {
        return terminalMark;
    }

    public void setTerminalMark(String terminalMark) {
        this.terminalMark = terminalMark;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "UserTokenResp{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", tenantId=" + tenantId +
                ", expireIn=" + expireIn +
                ", userId=" + userId +
                ", terminalMark='" + terminalMark + '\'' +
                ", userUuid='" + userUuid + '\'' +
                ", orgId=" + orgId +
                ", appId=" + appId +
                '}';
    }
}

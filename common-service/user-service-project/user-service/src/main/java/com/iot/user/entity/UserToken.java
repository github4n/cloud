package com.iot.user.entity;

/**
 * 描述：用户Token
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/12 16:27
 */
public class UserToken extends Token {

    private String terminalMark;

    private Long tenantId;

    private Long userId;

    private long expireIn;
    /**
     * 用户uuid
     */
    private String userUuid;

    //用户对应的组织id 所属自己的
    private Long orgId;

    /**
     * 如果是app登录则会记录appId
     */
    private Long appId;

    public String getTerminalMark() {
        return terminalMark;
    }

    public void setTerminalMark(String terminalMark) {
        this.terminalMark = terminalMark;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public long getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(long expireIn) {
        this.expireIn = expireIn;
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
        return "UserToken{" +
                ", terminalMark='" + terminalMark + '\'' +
                ", tenantId=" + tenantId +
                ", userId=" + userId +
                ", expireIn=" + expireIn +
                ", orgId=" + orgId +
                '}';
    }
}

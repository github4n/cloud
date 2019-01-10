package com.iot.portal.corpuser.vo;

import com.iot.permission.vo.PermissionDto;

import java.util.List;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：用户VO
 * 创建人： nongchongwei
 * 创建时间：2018年07月04日 10:09
 * 修改人： nongchongwei
 * 修改时间：2018年07月04日 10:09
 */
public class UserLoginResp {

    private String userUuId;

    private String nickName;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 有效期
     */
    private Long expireIn;

    private Boolean improveTenantInfo;

    /**
     * 终端标识
     */
    private String terminalMark;

    private String mqttPassword;

    private Long userId;

    private String tenantUuId;

    private List<PermissionDto> permissionDtoList;

    public String getUserUuId() {
        return userUuId;
    }

    public void setUserUuId(String userUuId) {
        this.userUuId = userUuId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

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

    public String getTerminalMark() {
        return terminalMark;
    }

    public void setTerminalMark(String terminalMark) {
        this.terminalMark = terminalMark;
    }

    public String getMqttPassword() {
        return mqttPassword;
    }

    public void setMqttPassword(String mqttPassword) {
        this.mqttPassword = mqttPassword;
    }

    public Boolean getImproveTenantInfo() {
        return improveTenantInfo;
    }

    public void setImproveTenantInfo(Boolean improveTenantInfo) {
        this.improveTenantInfo = improveTenantInfo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTenantUuId() {
        return tenantUuId;
    }

    public void setTenantUuId(String tenantUuId) {
        this.tenantUuId = tenantUuId;
    }

    public List<PermissionDto> getPermissionDtoList() {
        return permissionDtoList;
    }

    public void setPermissionDtoList(List<PermissionDto> permissionDtoList) {
        this.permissionDtoList = permissionDtoList;
    }
}

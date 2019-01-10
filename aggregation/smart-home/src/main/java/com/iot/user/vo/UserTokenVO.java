package com.iot.user.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：用户token
 * 创建人： mao2080@sina.com
 * 创建时间：2018/4/9 14:54
 * 修改人： mao2080@sina.com
 * 修改时间：2018/4/9 14:54
 * 修改描述：
 */
public class UserTokenVO {

    @JsonProperty("userId")
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

    /**
     * 终端标识
     */
    private String terminalMark;

    private String mqttPassword;

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

}

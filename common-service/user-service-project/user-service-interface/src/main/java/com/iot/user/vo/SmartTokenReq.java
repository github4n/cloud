package com.iot.user.vo;

import java.util.Date;

public class SmartTokenReq {
    public static final int ALEXA      = 1;
    public static final int GOOGLEHOME = 2;
    private Long id;

    private Long userId;
    
    private Integer expiresIn;

    private Integer smart;
	 
    private String accessToken;
	 
    private String refreshToken;
	 
    private Date updateTime;

    private String localAccessToken;

    private String localRefreshToken;

    private Long thirdPartyInfoId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getSmart() {
        return smart;
    }

    public void setSmart(Integer smart) {
        this.smart = smart;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getLocalAccessToken() {
        return localAccessToken;
    }

    public void setLocalAccessToken(String localAccessToken) {
        this.localAccessToken = localAccessToken;
    }

    public String getLocalRefreshToken() {
        return localRefreshToken;
    }

    public void setLocalRefreshToken(String localRefreshToken) {
        this.localRefreshToken = localRefreshToken;
    }

    public Long getThirdPartyInfoId() {
        return thirdPartyInfoId;
    }

    public void setThirdPartyInfoId(Long thirdPartyInfoId) {
        this.thirdPartyInfoId = thirdPartyInfoId;
    }

}

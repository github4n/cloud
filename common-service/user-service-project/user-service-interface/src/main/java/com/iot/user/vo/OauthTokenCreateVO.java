package com.iot.user.vo;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/13 17:09
 * @Modify by:
 */
public class OauthTokenCreateVO {

    // 缓存token用的标识
    private String clientType;
    private String code;
    private String refreshToken;

    private String skillType;
    private Integer smartType;
    private Long thirdPartyInfoId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getSkillType() {
        return skillType;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }

    public Integer getSmartType() {
        return smartType;
    }

    public void setSmartType(Integer smartType) {
        this.smartType = smartType;
    }

    public Long getThirdPartyInfoId() {
        return thirdPartyInfoId;
    }

    public void setThirdPartyInfoId(Long thirdPartyInfoId) {
        this.thirdPartyInfoId = thirdPartyInfoId;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}

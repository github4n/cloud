package com.iot.portal.web.vo;

/**
 * 描述：基础配置请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/9 15:42
 */
public class SetBaseConfigReq {
    private Long appId;
    private Integer hasSecurity;
    private Integer hasPrivacyPolicy;
    private String znCopyright;
    private String enCopyright;
    private String privacyCnLinkId;
    private String privacyEnLinkId;
    private String isFavorite;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Integer getHasSecurity() {
        return hasSecurity;
    }

    public void setHasSecurity(Integer hasSecurity) {
        this.hasSecurity = hasSecurity;
    }

    public Integer getHasPrivacyPolicy() {
        return hasPrivacyPolicy;
    }

    public void setHasPrivacyPolicy(Integer hasPrivacyPolicy) {
        this.hasPrivacyPolicy = hasPrivacyPolicy;
    }

    public String getZnCopyright() {
        return znCopyright;
    }

    public void setZnCopyright(String znCopyright) {
        this.znCopyright = znCopyright;
    }

    public String getEnCopyright() {
        return enCopyright;
    }

    public void setEnCopyright(String enCopyright) {
        this.enCopyright = enCopyright;
    }

    public String getPrivacyCnLinkId() {
        return privacyCnLinkId;
    }

    public void setPrivacyCnLinkId(String privacyCnLinkId) {
        this.privacyCnLinkId = privacyCnLinkId;
    }

    public String getPrivacyEnLinkId() {
        return privacyEnLinkId;
    }

    public void setPrivacyEnLinkId(String privacyEnLinkId) {
        this.privacyEnLinkId = privacyEnLinkId;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }
}

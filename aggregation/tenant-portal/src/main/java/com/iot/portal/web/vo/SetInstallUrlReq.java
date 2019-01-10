package com.iot.portal.web.vo;

/**
 * 描述：设置安装路径请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/9 15:34
 */
public class SetInstallUrlReq {
    private Long appId;
    private String androidUrl;
    private String iosUrl;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAndroidUrl() {
        return androidUrl;
    }

    public void setAndroidUrl(String androidUrl) {
        this.androidUrl = androidUrl;
    }

    public String getIosUrl() {
        return iosUrl;
    }

    public void setIosUrl(String iosUrl) {
        this.iosUrl = iosUrl;
    }
}

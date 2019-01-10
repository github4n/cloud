package com.iot.portal.web.vo;

/**
 * 描述：打包结果通知请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/26 10:52
 */
public class PackNotifyReq {
    private String appId;
    private String rc;
    private String desc;
    private String androidUrl;
    private String iosUrl;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    @Override
    public String toString() {
        return "PackNotifyReq{" +
                "appId=" + appId +
                ", rc=" + rc +
                ", desc='" + desc + '\'' +
                ", androidUrl='" + androidUrl + '\'' +
                ", iosUrl='" + iosUrl + '\'' +
                '}';
    }
}

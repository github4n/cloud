package com.iot.version.vo;

import java.io.Serializable;

/**
 * 描述：检测版本更新请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/12 14:49
 */
public class CheckVersionReq implements Serializable{
    private Long appId;
    private String version;
    private String appKey;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Override
    public String toString() {
        return "CheckVersionReq{" +
                "appId=" + appId +
                ", version='" + version + '\'' +
                ", appKey='" + appKey + '\'' +
                '}';
    }
}

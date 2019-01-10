package com.iot.portal.web.vo;

/**
 * 描述：创建应用请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/9 15:24
 */
public class CreateAppReq {
    private Long id;
    private String appName;
    private String enName;
    private String packName;
    private String appCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
}

package com.iot.tenant.vo.resp;

/**
 * 描述：app打包信息应答
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/21 14:39
 */
public class AppPackResp {

    private String tenantCode;

    private String logo;

    private String appName;

    private String startImg;

    private String lang;

    private Integer appStyle;

    private Integer ver;

    private String fileId;

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getStartImg() {
        return startImg;
    }

    public void setStartImg(String startImg) {
        this.startImg = startImg;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Integer getAppStyle() {
        return appStyle;
    }

    public void setAppStyle(Integer appStyle) {
        this.appStyle = appStyle;
    }

    public Integer getVer() {
        return ver;
    }

    public void setVer(Integer ver) {
        this.ver = ver;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}

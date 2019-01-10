package com.iot.tenant.vo.req;

/**
 * 描述：保存app打包配置请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/15 14:20
 */
public class SaveAppPackReq {
    private Long id;

    private String tenantCode;

    private String appName;

    private String lang;

    private Integer style;

    private Integer verFlag;

    private String fileId;

    private String iosVer;

    private String androidVer;

    private String productIds;

    private String iosAppKey;

    private String androidAppKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Integer getStyle() {
        return style;
    }

    public void setStyle(Integer style) {
        this.style = style;
    }

    public Integer getVerFlag() {
        return verFlag;
    }

    public void setVerFlag(Integer verFlag) {
        this.verFlag = verFlag;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getIosVer() {
        return iosVer;
    }

    public void setIosVer(String iosVer) {
        this.iosVer = iosVer;
    }

    public String getAndroidVer() {
        return androidVer;
    }

    public void setAndroidVer(String androidVer) {
        this.androidVer = androidVer;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getIosAppKey() {
        return iosAppKey;
    }

    public void setIosAppKey(String iosAppKey) {
        this.iosAppKey = iosAppKey;
    }

    public String getAndroidAppKey() {
        return androidAppKey;
    }

    public void setAndroidAppKey(String androidAppKey) {
        this.androidAppKey = androidAppKey;
    }
}

package com.iot.pack.vo;

/**
 * 描述：打包请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/8/13 17:30
 */
public class PackReq {
    private String zipUrl;
    private String logoUrl;
    private String loadingUrl;
    private String pfileUrl;
    private String mfileUrl;
    private String fcmfileUrl;
    private String keystoreUrl;
    private String pushConfigurationUrl;

    public String getZipUrl() {
        return zipUrl;
    }

    public void setZipUrl(String zipUrl) {
        this.zipUrl = zipUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLoadingUrl() {
        return loadingUrl;
    }

    public void setLoadingUrl(String loadingUrl) {
        this.loadingUrl = loadingUrl;
    }

    public String getPfileUrl() {
        return pfileUrl;
    }

    public void setPfileUrl(String pfileUrl) {
        this.pfileUrl = pfileUrl;
    }

    public String getMfileUrl() {
        return mfileUrl;
    }

    public void setMfileUrl(String mfileUrl) {
        this.mfileUrl = mfileUrl;
    }

    public String getFcmfileUrl() {
        return fcmfileUrl;
    }

    public void setFcmfileUrl(String fcmfileUrl) {
        this.fcmfileUrl = fcmfileUrl;
    }

    public String getKeystoreUrl() {
        return keystoreUrl;
    }

    public void setKeystoreUrl(String keystoreUrl) {
        this.keystoreUrl = keystoreUrl;
    }

    public String getPushConfigurationUrl() {
        return pushConfigurationUrl;
    }

    public void setPushConfigurationUrl(String pushConfigurationUrl) {
        this.pushConfigurationUrl = pushConfigurationUrl;
    }

    @Override
    public String toString() {
        return "PackReq{" +
                "zipUrl='" + zipUrl + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", loadingUrl='" + loadingUrl + '\'' +
                ", pfileUrl='" + pfileUrl + '\'' +
                ", mfileUrl='" + mfileUrl + '\'' +
                ", fcmfileUrl='" + fcmfileUrl + '\'' +
                ", keystoreUrl='" + keystoreUrl + '\'' +
                ", pushConfigurationUrl='" + pushConfigurationUrl + '\'' +
                '}';
    }
}

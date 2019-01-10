package com.iot.portal.web.vo;

/**
 * 描述：
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/16 15:38
 */
public class SaveCertificateReq {
    private Long appId;
    private String mfileId;
    private String pfileId;
    private String filePassword;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getMfileId() {
        return mfileId;
    }

    public void setMfileId(String mfileId) {
        this.mfileId = mfileId;
    }

    public String getPfileId() {
        return pfileId;
    }

    public void setPfileId(String pfileId) {
        this.pfileId = pfileId;
    }

    public String getFilePassword() {
        return filePassword;
    }

    public void setFilePassword(String filePassword) {
        this.filePassword = filePassword;
    }

    @Override
    public String toString() {
        return "SaveCertificateReq{" +
                "appId=" + appId +
                ", mfileId='" + mfileId + '\'' +
                ", pfileId='" + pfileId + '\'' +
                ", filePassword='" + filePassword + '\'' +
                '}';
    }
}

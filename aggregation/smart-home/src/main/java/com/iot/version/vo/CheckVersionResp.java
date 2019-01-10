package com.iot.version.vo;

/**
 * 描述：检测更新应答
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/12 17:24
 */
public class CheckVersionResp {
    private Integer rc; // 1增量 2全量
    private String url;
    private String md5;
    private String installMode;
    private String version;
    private String remark;

    public String getInstallMode() {
        return installMode;
    }

    public void setInstallMode(String installMode) {
        this.installMode = installMode;
    }

    public Integer getRc() {
        return rc;
    }

    public void setRc(Integer rc) {
        this.rc = rc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

package com.iot.tenant.vo.req;

/**
 * 描述：保存app版本请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/12 11:17
 */
public class SaveAppVersionReq {
    /**
     * 主键
     */
    private Long id;
    /**
     * 版本号
     */
    private String version;
    /**
     * 应用主键
     */
    private Long appId;
    /**
     * 租户主键
     */
    private Long tenantId;
    /**
     * 安装参数
     */
    private String installMode;
    /**
     * 描述
     */
    private String remark;
    /**
     * 全量包文件ID
     */
    private String fullFileId;

    private String fullFileMd5;
    /**
     * 增量包文件ID
     */
    private String incrFileId;

    private String incrFileMd5;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getInstallMode() {
        return installMode;
    }

    public void setInstallMode(String installMode) {
        this.installMode = installMode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFullFileId() {
        return fullFileId;
    }

    public void setFullFileId(String fullFileId) {
        this.fullFileId = fullFileId;
    }

    public String getFullFileMd5() {
        return fullFileMd5;
    }

    public void setFullFileMd5(String fullFileMd5) {
        this.fullFileMd5 = fullFileMd5;
    }

    public String getIncrFileId() {
        return incrFileId;
    }

    public void setIncrFileId(String incrFileId) {
        this.incrFileId = incrFileId;
    }

    public String getIncrFileMd5() {
        return incrFileMd5;
    }

    public void setIncrFileMd5(String incrFileMd5) {
        this.incrFileMd5 = incrFileMd5;
    }

    @Override
    public String toString() {
        return "SaveAppVersionReq{" +
                "id=" + id +
                ", version='" + version + '\'' +
                ", appId=" + appId +
                ", tenantId=" + tenantId +
                ", installMode='" + installMode + '\'' +
                ", remark='" + remark + '\'' +
                ", fullFileId='" + fullFileId + '\'' +
                ", fullFileMd5='" + fullFileMd5 + "\'" +
                ", incrFileId='" + incrFileId + '\'' +
                ", incrFileMd5='" + incrFileMd5 + "\'" +
                '}';
    }
}

package com.iot.device.vo.req.ota;

import java.util.Date;

/**
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：OTA版本请求对象
 * 创建人： maochengyuan
 * 创建时间：2018/7/24 19:23
 * 修改人： maochengyuan
 * 修改时间：2018/7/24 19:23
 * 修改描述：
 */
public class FirmwareVersionReqDto {

    /**版本id*/
    private Long id;

    /**租户id*/
    private Long tenantId;

    /**产品id*/
    private Long productId;

    /**版本号*/
    private String otaVersion;

    /**升级包类型*/
    private String otaType;

    /**升级文件id*/
    private String otaFileId;

    /**MD5值*/
    private String otaMd5;

    /**分位的类型*/
    private String fwType;

    /**创建时间*/
    private Date createTime;

    /**创建人*/
    private Long createBy;

    /**修改时间*/
    private Date updateTime;

    /**修改人*/
    private Long updateBy;

    public FirmwareVersionReqDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getOtaVersion() {
        return otaVersion;
    }

    public void setOtaVersion(String otaVersion) {
        this.otaVersion = otaVersion;
    }

    public String getOtaType() {
        return otaType;
    }

    public void setOtaType(String otaType) {
        this.otaType = otaType;
    }

    public String getOtaFileId() {
        return otaFileId;
    }

    public void setOtaFileId(String otaFileId) {
        this.otaFileId = otaFileId;
    }

    public String getOtaMd5() {
        return otaMd5;
    }

    public void setOtaMd5(String otaMd5) {
        this.otaMd5 = otaMd5;
    }

    public String getFwType() {
        return fwType;
    }

    public void setFwType(String fwType) {
        this.fwType = fwType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

}

package com.iot.device.vo.req;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 13:54 2018/4/24
 * @Modify by:
 */
public class CommDeviceExtendReq implements Serializable {

    private String deviceUUId;

    /**
     * 批次 extend cust_uuid_manage  table  id
     * batch_num
     */
    private Long batchNumId;
    /**
     * 创建时间
     * create_time
     */
    private Date createTime;
    /**
     * p2pid  extend custP2pid  table  id
     * p2p_id
     */
    private Long p2pId;
    /**
     * 有效时长
     * uuid_validity_days
     */
    private BigDecimal uuidValidityDays;
    /**
     * 设备密码
     * device_cipher
     */
    private String deviceCipher;
    /**
     * 租户id
     * tenant_id
     */
    private Long tenantId;
    /**
     * 是否首次上传子设备,1是,0否(带套包的网关需要)
     * first_upload_sub_dev
     */
    private Integer firstUploadSubDev;

    private Boolean unbindFlag;

    private Boolean resetFlag;

    /**
     * 地区
     */
    private String area;

    public String getDeviceUUId() {
        return deviceUUId;
    }

    public void setDeviceUUId(String deviceUUId) {
        this.deviceUUId = deviceUUId;
    }

    public Long getBatchNumId() {
        return batchNumId;
    }

    public void setBatchNumId(Long batchNumId) {
        this.batchNumId = batchNumId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getP2pId() {
        return p2pId;
    }

    public void setP2pId(Long p2pId) {
        this.p2pId = p2pId;
    }

    public BigDecimal getUuidValidityDays() {
        return uuidValidityDays;
    }

    public void setUuidValidityDays(BigDecimal uuidValidityDays) {
        this.uuidValidityDays = uuidValidityDays;
    }

    public String getDeviceCipher() {
        return deviceCipher;
    }

    public void setDeviceCipher(String deviceCipher) {
        this.deviceCipher = deviceCipher;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getFirstUploadSubDev() {
        return firstUploadSubDev;
    }

    public void setFirstUploadSubDev(Integer firstUploadSubDev) {
        this.firstUploadSubDev = firstUploadSubDev;
    }

    public Boolean getUnbindFlag() {
        return unbindFlag;
    }

    public void setUnbindFlag(Boolean unbindFlag) {
        this.unbindFlag = unbindFlag;
    }

    public Boolean getResetFlag() {
        return resetFlag;
    }

    public void setResetFlag(Boolean resetFlag) {
        this.resetFlag = resetFlag;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}

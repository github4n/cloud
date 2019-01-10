package com.iot.device.vo.rsp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DeviceExtendResp implements Serializable {

    /***/
    private static final long serialVersionUID = 5694232069085088357L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 批次id(主键)
     */
    private String batchNumId;

    /**
     * p2pid(主键)
     */
    private String p2pId;

    /**
     * p2pid(主键)
     */
    private BigDecimal uuidValidityDays;

    /**
     * 设备密码
     */
    private String deviceCipher;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否首次上传子设备,1是,0否(带套包的网关需要)
     * first_upload_sub_dev
     */
    private Integer firstUploadSubDev;

    private Integer unbindFlag;

    private Integer resetFlag;

    /**
     * 地区
     */
    private String area;

    /**
     * 产品类型
     */
    private String CommType;

    /**
     * 时区
     */
    private String timezone;

    /**
     * 服务器IP
     */
    private String serverIp;

    /**
     * 服务器端口
     */
    private Long serverPort;

    /**
     * 上传间隔
     */
    private Long reportInterval;

    private Long address;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public String getBatchNumId() {
        return batchNumId;
    }

    public void setBatchNumId(String batchNumId) {
        this.batchNumId = batchNumId;
    }

    public String getP2pId() {
        return p2pId;
    }

    public void setP2pId(String p2pId) {
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getFirstUploadSubDev() {
        return firstUploadSubDev;
    }

    public void setFirstUploadSubDev(Integer firstUploadSubDev) {
        this.firstUploadSubDev = firstUploadSubDev;
    }

    public Integer getUnbindFlag() {
        return unbindFlag;
    }

    public void setUnbindFlag(Integer unbindFlag) {
        this.unbindFlag = unbindFlag;
    }

    public Integer getResetFlag() {
        return resetFlag;
    }

    public void setResetFlag(Integer resetFlag) {
        this.resetFlag = resetFlag;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCommType() {
        return CommType;
    }

    public void setCommType(String commType) {
        CommType = commType;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Long getServerPort() {
        return serverPort;
    }

    public void setServerPort(Long serverPort) {
        this.serverPort = serverPort;
    }

    public Long getReportInterval() {
        return reportInterval;
    }

    public void setReportInterval(Long reportInterval) {
        this.reportInterval = reportInterval;
    }

    public Long getAddress() {
        return address;
    }

    public void setAddress(Long address) {
        this.address = address;
    }
}

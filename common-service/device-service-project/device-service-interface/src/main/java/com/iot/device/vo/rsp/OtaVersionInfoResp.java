package com.iot.device.vo.rsp;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:18 2018/5/2
 * @Modify by:
 */
public class OtaVersionInfoResp implements Serializable {
    /**
     * 版本id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 分位的类型  固件类型
     * fw_type
     * 0:所有的模块在一个分位里面  1:wifi 模块的分位  2: zigbee模块的分位 3: z-wave模块的分位 4：ble模块的分位 不填默认值为0.
     */
    private Integer fwType;
    /**
     * 升级状态类型
     * upgrade_status
     * <p>
     * 0
     */
    private Integer upgradeStatus;
    /**
     * 设备版本号
     * version_num
     */
    private String versionNum;

    /**
     * 硬件版本
     */
    private String hwVersion;
    /**
     * MD5值
     */
    private String md5;
    /**
     * 创建时间 --设备
     * create_time
     */
    private Date createTime;
    /**
     * 设备类型
     * device_type_id
     */
    private Long deviceTypeId;
    /**
     * 产品id
     * product_id
     */
    private Long productId;
    /**
     * 版本下载URL
     * version_url
     */
    private String versionUrl;

    /**
     * 文件url对应的fileId
     */
    private String fileId;

    /**
     * 厂商代码
     * cust_code
     */
    private String custCode;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 当前用户选择的升级模式
     * 0：普通升级 1：静默升级
     */
    private Integer mode;

    /**
     * 阶段 0: 空闲 1:下载阶段 2:烧录fw阶段 3:成功 4:失败
     * ota上报状态
     * stage
     */
    private Integer stage;
    /**
     * 百分比
     */
    private Integer percent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFwType() {
        return fwType;
    }

    public void setFwType(Integer fwType) {
        this.fwType = fwType;
    }

    public Integer getUpgradeStatus() {
        return upgradeStatus;
    }

    public void setUpgradeStatus(Integer upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public String getHwVersion() {
        return hwVersion;
    }

    public void setHwVersion(String hwVersion) {
        this.hwVersion = hwVersion;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }
}

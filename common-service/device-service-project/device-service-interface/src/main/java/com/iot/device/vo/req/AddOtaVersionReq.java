package com.iot.device.vo.req;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:21 2018/5/2
 * @Modify by:
 */
public class AddOtaVersionReq implements Serializable {

    //升级无需通知用户
    public static final int UPGRADE_NOT_NOTIFY = 0;
    //升级需通知用户 不可控设备 (不升级)
    public static final int NOT_UPGRADE_NOTIFY_NOT_CONTROL = 1;
    //升级需通知用户 可控设备(不升级)
    public static final int NOT_UPGRADE_NOTIFY_CONTROL = 2;

    //主键 添加后返回
    private Long id;
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
     * 名称
     */
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

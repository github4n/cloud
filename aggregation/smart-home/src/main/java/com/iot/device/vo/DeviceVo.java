package com.iot.device.vo;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 13:57 2018/4/17
 * @Modify by:
 */
public class DeviceVo implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 父id
     */
    private String parentId;

    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备图片
     */
    private String icon;
    /**
     * ip
     */
    private String ip;
    /**
     * mac地址
     */
    private String mac;
    /**
     * 设备序列号
     */
    private String sn;
    /**
     * 租户ID
     * tenant_id
     */
    private Long tenantId;
    /**
     * 是否直连设备0否、1是
     * is_direct_device
     */
    private Integer isDirectDevice;
    /**
     * 业务类型
     * business_type_id
     */
    private String businessTypeId;
    /**
     * MAC 地址 hue网关用
     * reality_id
     */
    private String realityId;
    /**
     * 扩展名 hue用
     * extra_name
     */
    private String extraName;
    /**
     * 设置类型id
     * device_type_id
     */
    private Long deviceTypeId;
    /**
     * 产品id
     * product_id
     */
    private Long productId;
    /**
     * wifi名称
     * ssid
     */
    private String ssid;
    /**
     * 重置标识
     * reset_random
     */
    private String resetRandom;
    /**
     * 设备版本号
     */
    private String version;
    /**
     * 空间名称
     */
    private String spaceName;

    /**
     * 房间名称
     */
    private String roomName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getIsDirectDevice() {
        return isDirectDevice;
    }

    public void setIsDirectDevice(Integer isDirectDevice) {
        this.isDirectDevice = isDirectDevice;
    }

    public String getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(String businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getRealityId() {
        return realityId;
    }

    public void setRealityId(String realityId) {
        this.realityId = realityId;
    }

    public String getExtraName() {
        return extraName;
    }

    public void setExtraName(String extraName) {
        this.extraName = extraName;
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

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getResetRandom() {
        return resetRandom;
    }

    public void setResetRandom(String resetRandom) {
        this.resetRandom = resetRandom;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}

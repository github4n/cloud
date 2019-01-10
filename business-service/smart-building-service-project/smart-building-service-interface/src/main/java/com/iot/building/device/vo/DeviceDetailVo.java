package com.iot.building.device.vo;

import java.io.Serializable;

public class DeviceDetailVo implements Serializable {

	/**
     * id
     */
    private Long id;
    /**
     * ip
     */
    private String ip;
    /**
     * mac
     */
    private String mac;
    /**
     * parent_id  extends device_id
     */
    private String parentId;
    /**
     * 网关名称
     */
    private String parentName;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备图片
     */
    private String businessType;
    /**
     * 业务类型
     * business_type_id
     */
    private Long businessTypeId;
    /**
     * 设置类型id  extend device_type table id
     * device_type_id
     */
    private Long deviceTypeId;
    /**
     * 设置类型id  extend device_type table id
     * device_type_id
     */
    private String deviceType;
    /**
     * onlineStatus
     */
    private String onlineStatus;
    /**
     * 租户ID
     * tenant_id
     */
    private Long tenantId;
    /**
     * 产品id  extend product table id
     * product_id
     */
    private Long productId;
    /**
     * productName
     * 
     */
    private String productName;
    /**
     * 设备版本号
     */
    private String version;
    /**
     * 最后跟新时间
     * location_id
     */
    private Long locationId;

    private String hwVersion;

    private String model;
    
    private Long roomId;
    
    private String roomName;
    
    private String floorName;
    
    private String buildName;
    
    /**
     * report_interval
     */
    private Long reportInterval;

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

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public Long getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getHwVersion() {
		return hwVersion;
	}

	public void setHwVersion(String hwVersion) {
		this.hwVersion = hwVersion;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
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

	public Long getReportInterval() {
		return reportInterval;
	}

	public void setReportInterval(Long reportInterval) {
		this.reportInterval = reportInterval;
	}
}

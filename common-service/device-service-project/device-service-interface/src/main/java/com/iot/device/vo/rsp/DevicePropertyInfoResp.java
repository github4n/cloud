package com.iot.device.vo.rsp;

import java.io.Serializable;

public class DevicePropertyInfoResp implements Serializable {

	/***/
	private static final long serialVersionUID = -8604468728688599063L;

	/**设备id*/
	private String deviceId;

	/**父类id*/
	private String parentId;

	/**设备名称*/
	private String deviceName;

	
	/**mac地址*/
	private String macAddress;
	
	/**ip地址*/
	private String ipAddress;

	/**设备图标名称*/
	private String deviceIcon;
	
	/**用户id*/
	private Long userId;

	
	/**是否直连设备*/
	private String isDirectDevice;

	private String resetRandom;


	/**设备在线状态*/
	private String onlineStatus;

	/** WIFI名称 **/
	private String ssid;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getDeviceIcon() {
		return deviceIcon;
	}

	public void setDeviceIcon(String deviceIcon) {
		this.deviceIcon = deviceIcon;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getIsDirectDevice() {
		return isDirectDevice;
	}

	public void setIsDirectDevice(String isDirectDevice) {
		this.isDirectDevice = isDirectDevice;
	}

	public String getResetRandom() {
		return resetRandom;
	}

	public void setResetRandom(String resetRandom) {
		this.resetRandom = resetRandom;
	}

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
}

package com.iot.shcs.helper;

import java.util.Map;

public class DevicePropertyDTO {

	private String deviceId; //设备id
	 
	private Map<String,Object> propertyMap;//设备的目标属性

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Map<String, Object> getPropertyMap() {
		return propertyMap;
	}

	public void setPropertyMap(Map<String, Object> propertyMap) {
		this.propertyMap = propertyMap;
	}
	
}

package com.iot.building.device.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created Time by sheting on 2018/10/24
 * Created by sheting on ${UAER}
 */
public class DevicePropertyVo {

    /**
     * 设备id
     */
    private String deviceId;
 
    private Map<String,Object> property;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Map<String, Object> getProperty() {
		return property;
	}

	public void setProperty(Map<String, Object> property) {
		this.property = property;
	}
    
}

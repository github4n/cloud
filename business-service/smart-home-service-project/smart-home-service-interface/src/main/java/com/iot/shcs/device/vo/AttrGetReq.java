package com.iot.shcs.device.vo;

public class AttrGetReq extends RobotReq {
	
	private String sensorType;
	
	private String[] key;

	public String[] getKey() {
		return key;
	}

	public void setKey(String[] key) {
		this.key = key;
	}

	public String getSensorType() {
		return sensorType;
	}

	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}
}

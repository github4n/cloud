package com.iot.robot.norm;


public class OnlineKeyValue extends KeyValue<Boolean> {

	private String key = ONLINE;
	private Boolean fixedValue;
	private Boolean deltaValue;

	@Override
	public String getKey() {
		return key;
	}

	public Boolean getFixedValue() {
		return fixedValue;
	}

	public void setFixedValue(Boolean fixedValue) {
		this.fixedValue = fixedValue;
	}

	public Boolean getDeltaValue() {
		return deltaValue;
	}

	public void setDeltaValue(Boolean deltaValue) {
	}
}

package com.iot.robot.norm;


public class IPCTurnOnOffKeyValue extends KeyValue<Boolean> {

	private String key = TURN_ON_OFF;
	private Boolean fixedValue;

	@Override
	public String getKey() {
		return key;
	}

	public Boolean getFixedValue() {
		return fixedValue;
	}

	public void setFixedValue(Boolean fixedValue) {
		if (fixedValue == null) {
			throw new IllegalStateException("illegal fixedValue, fixedValue is null");
		}
		this.fixedValue = fixedValue;
	}

	public Boolean getDeltaValue() {
		return null;
	}

	public void setDeltaValue(Boolean deltaValue) {
	}
}

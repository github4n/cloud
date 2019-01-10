package com.iot.robot.norm;


public class CctKeyValue extends KeyValue<Integer> {

	private String key = CCT;
	private Integer fixedValue;
	private Integer deltaValue;

	@Override
	public String getKey() {
		return key;
	}

	public Integer getFixedValue() {
		return fixedValue;
	}

	public void setFixedValue(Integer fixedValue) {
		this.fixedValue = fixedValue;
	}

	public Integer getDeltaValue() {
		return deltaValue;
	}

	public void setDeltaValue(Integer deltaValue) {
		this.deltaValue = deltaValue;
	}
}

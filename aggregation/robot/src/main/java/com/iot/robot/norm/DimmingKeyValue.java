package com.iot.robot.norm;


public final class DimmingKeyValue extends KeyValue<Integer> {

	private String key = DIMMING;
	//固定值
	private Integer fixedValue;
	//增量值
	private Integer deltaValue;
	
	@Override
	public String getKey() {
		return key;
	}

	public Integer getFixedValue() {
		return fixedValue;
	}

	public void setFixedValue(Integer fixedValue) {
		if (fixedValue < 0 || fixedValue > 100) {
			throw new IllegalStateException("illegal fixedValue, only support fixedValue(0,100)");
		}
		this.fixedValue = fixedValue;
		super.isDelat = false;
	}

	public Integer getDeltaValue() {
		return deltaValue;
	}

	public void setDeltaValue(Integer deltaValue) {
		this.deltaValue = deltaValue;
		super.isDelat = true;
	}

}

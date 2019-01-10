package com.iot.robot.norm;


public class OnOffKeyValue extends KeyValue<Byte> {

	private String key = ONOFF;
	private Byte fixedValue;
	private Byte deltaValue;

	@Override
	public String getKey() {
		return key;
	}

	public Byte getFixedValue() {
		return fixedValue;
	}

	public void setFixedValue(Byte fixedValue) {
		if (fixedValue < 0 || fixedValue > 1) {
			throw new IllegalStateException("illegal fixedValue, only support 0 or 1");
		}
		this.fixedValue = fixedValue;
	}

	public Byte getDeltaValue() {
		return deltaValue;
	}

	public void setDeltaValue(Byte deltaValue) {
	}
}

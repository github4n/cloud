package com.iot.robot.norm;

public class QueryKeyValue extends KeyValue<Integer> {
	private String key = QUERY;
	private Integer fixedValue;
	private Integer deltaValue;

	@Override
	public Integer getFixedValue() {
		return null;
	}

	@Override
	public void setFixedValue(Integer fixedValue) {
		
	}

	@Override
	public Integer getDeltaValue() {
		return null;
	}

	@Override
	public void setDeltaValue(Integer deltaValue) {
		
	}

	@Override
	public String getKey() {
		return key;
	}

}

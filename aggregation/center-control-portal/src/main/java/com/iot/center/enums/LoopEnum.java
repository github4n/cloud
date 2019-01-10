package com.iot.center.enums;

public enum LoopEnum {
	
	WEEK("1", 1), SINGLEWEEK("0", 0);
	
	private String code;
	private int value;
	
	private LoopEnum(String code, int value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}

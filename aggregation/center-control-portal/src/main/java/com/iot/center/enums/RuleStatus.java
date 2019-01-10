package com.iot.center.enums;

public enum RuleStatus {
	STOP("stop", 0), RUNNING("running", 1);

	private String code;
	private int value;
	
	private RuleStatus(String code, int value) {
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

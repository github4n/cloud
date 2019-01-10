package com.iot.payment.entity;

public enum PaySource {
	
	/** 来自web*/
	FROM_WEB(0,"from web"),
	
	/** 来自app*/
	FROM_APP(1,"from app"),
	
	
	;
	
	
	/** 编码*/
	private int code;
	
	/** 描述*/
	private String desc;
	
	
	private PaySource(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	

}

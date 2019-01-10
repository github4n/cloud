package com.iot.payment.entity;

public class PayRes {
	
	/** 编码*/
	private Integer code;
	
	/** 描述*/
	private String desc;
	
	public PayRes() {
		
	}
	
	public PayRes(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	

}

package com.iot.payment.entity;

public enum PayType {
	
	/** 付款*/
	PAY(0,"pay"),
	
	/** 退款*/
	REFUND(1,"refund"),
	
	
	;
	
	
	/** 编码*/
	private int code;
	
	/** 描述*/
	private String desc;
	
	
	private PayType(int code, String desc) {
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

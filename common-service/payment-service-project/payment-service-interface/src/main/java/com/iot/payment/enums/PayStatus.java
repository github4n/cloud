package com.iot.payment.enums;

public enum PayStatus {
	
	/** 待支付*/
	TO_BE_PAID(0,"to be paid"),
	
	/** 支付中*/
	IN_PAYMENT(1,"in payment"),
	
	/** 已支付*/
	ALREADY_PAY(2,"already pay"),
	
	/** 支付失败*/
	PAY_FAIL(3,"pay fail"),
	
	;
	
	
	/** 编码*/
	private int code;
	
	/** 描述*/
	private String desc;
	
	
	private PayStatus(int code, String desc) {
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

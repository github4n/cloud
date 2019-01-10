package com.iot.payment.entity;

public enum RefundStatus {
	
	/** 未退款*/
	NO_REFUND(0,"no refund"),
	
	/** 退款中*/
	IN_REFUND(1,"refunding"),
	
	/** 退款成功*/
	REFUND_SUCCESS(2,"already refund"),
	
	/** 退款失败*/
	REFUND_FAIL(3,"refund fail"),
	
	;
	
	
	/** 编码*/
	private int code;
	
	/** 描述*/
	private String desc;
	
	
	private RefundStatus(int code, String desc) {
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

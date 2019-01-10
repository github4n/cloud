package com.iot.payment.enums;

public enum PayRespCodeEnum {

	SUCCESS(200,"成功"),

	FAIL(404,"失败")

	;


	/** 编码*/
	private int code;

	/** 描述*/
	private String desc;


	private PayRespCodeEnum(int code, String desc) {
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

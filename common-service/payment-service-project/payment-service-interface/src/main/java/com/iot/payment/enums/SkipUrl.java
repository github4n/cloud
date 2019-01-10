package com.iot.payment.enums;

public enum SkipUrl {

	/** 成功URL*/
	SUCCESS_URL("successUrl","成功URL"),

	/** 取消URL*/
	CANCEL_URL("cancelUrl","取消URL"),

	/** 失败URL*/
	FAILED_URL("failedUrl","失败URL"),

	;


	/** 编码*/
	private String code;

	/** 描述*/
	private String desc;


	private SkipUrl(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	

}

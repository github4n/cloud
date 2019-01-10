package com.iot.report.enums;

public enum QueryTypeEnums {

	/**Activate */
	ACTIVATE(0,"Activate"),

	/** active*/
	ACTIVE(1,"active"),


	;


	/** 编码*/
	private int code;

	/** 描述*/
	private String desc;


	private QueryTypeEnums(int code, String desc) {
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

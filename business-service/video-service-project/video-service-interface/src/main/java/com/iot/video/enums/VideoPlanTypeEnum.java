package com.iot.video.enums;

public enum VideoPlanTypeEnum {

	ALL_TIME(0,"全时录影"),

	EVENT(1,"事件录影")

	;

	VideoPlanTypeEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	/** 类型编码 */
	private Integer code;

	/** 描述 */
	private String desc;

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}

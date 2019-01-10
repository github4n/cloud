package com.iot.video.enums;

public enum PlanCycleEnum {
	
	/** 天 */
	DAY("D","day"),
	
	/** 周 */
	WEEK("W","week"),
	
	/** 月 */
	MONTH("M","Month"),
	
	;

	private PlanCycleEnum(String code,String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	/** 类型编码 */
	private String code;

	/** 描述 */
	private String desc;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}

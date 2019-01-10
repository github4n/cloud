package com.iot.video.enums;

public enum PlanStatusEnum {
	/** 停止 */
	STOP(0, "stop"),

	/** 开启 */
	START(1, "start"),

	;

	private PlanStatusEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	/** 类型编码 */
	private int code;

	/** 描述 */
	private String desc;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}

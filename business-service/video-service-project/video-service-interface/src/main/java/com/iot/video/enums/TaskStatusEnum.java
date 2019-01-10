package com.iot.video.enums;

/** 
 * 
 * 项目名称：OT云平台
 * 模块名称：视频服务
 * 功能描述：解绑任务状态枚举
 * 创建人： mao2080@sina.com 
 * 创建时间：2018/6/6 10:26
 * 修改人： mao2080@sina.com 
 * 修改时间：2018/6/6 10:26
 * 修改描述：
 */
public enum TaskStatusEnum {

	/** 未开始 */
	NOT_STARTED(0,"未开始"),

	/** 执行中 */
	STARTED(1,"执行中"),
	;

	TaskStatusEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	/** 类型编码 */
	private int code;

	/** 描述 */
	private String name;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

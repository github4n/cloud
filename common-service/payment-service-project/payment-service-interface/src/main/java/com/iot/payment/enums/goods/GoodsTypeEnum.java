package com.iot.payment.enums.goods;

/**
 * 项目名称：cloud
 * 功能描述：商品类型
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 11:59
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 11:59
 * 修改描述：
 */
public enum GoodsTypeEnum {

	VIDEO_PLAN_ALL_TIME(0,"全时录影"),
	VIDEO_PLAN_ENEVT(1,"事件录影"),
	UUID(2,"uuid方案"),
	APP_PACKAGE(3,"APP打包"),
	VoiceService(4,"语音接入")
	;


	/** 编码*/
	private Integer code;

	/** 描述*/
	private String desc;


	GoodsTypeEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}



}

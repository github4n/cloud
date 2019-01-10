package com.iot.payment.enums.goods;

/**
 * 项目名称：cloud
 * 功能描述：商品编码
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 11:59
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 11:59
 * 修改描述：
 */
public enum GoodsCoodEnum {

	C0001("0001","直连方案"),
	C0002("0002","蓝牙方案"),
	C0003("0003","网关方案"),
	C0004("0004","IPC方案"),
	C0005("0005","app打包"),
	C0006("0006","全时录影"),
	C0007("0007","300事件录影"),
	C0008("0008","600事件录影"),
	C0009("0009","1000事件录影"),
	C0010("0010","谷歌语音接入"),
	C0011("0011","亚马逊语音接入");


	/**
	 * 编码
	 */
	private String code;

	/**
	 * 描述
	 */
	private String desc;


	GoodsCoodEnum(String code, String desc) {
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

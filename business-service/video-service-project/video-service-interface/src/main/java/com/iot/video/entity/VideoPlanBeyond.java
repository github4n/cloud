package com.iot.video.entity;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：事件录影超出量
 * 功能描述：
 * 创建人： 李帅
 * 创建时间：2018年3月27日 下午6:06:26
 * 修改人：李帅
 * 修改时间：2018年3月27日 下午6:06:26
 */
public class VideoPlanBeyond {

	/** 计划id */
	private String planId;

	/** 套餐超出量 */
	private int beyondAmount;

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public int getBeyondAmount() {
		return beyondAmount;
	}

	public void setBeyondAmount(int beyondAmount) {
		this.beyondAmount = beyondAmount;
	}

}

package com.iot.video.entity;

import java.io.Serializable;

/**
 * 
 * 项目名称：IOT云平台 模块名称：录影计划 功能描述： 创建人： wujianlong 创建时间：2017年8月10日 上午10:37:46
 * 修改人： wujianlong 修改时间：2017年8月10日 上午10:37:46
 */
public class VideoOrder implements Serializable {

	private static final long serialVersionUID = -6605703057339673972L;

	/** 计划id */
	private String planId;

	/** 排序字段 */
	private int planOrder;

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public int getPlanOrder() {
		return planOrder;
	}

	public void setPlanOrder(int planOrder) {
		this.planOrder = planOrder;
	}
}

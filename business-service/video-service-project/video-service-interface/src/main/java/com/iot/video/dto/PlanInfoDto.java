package com.iot.video.dto;

import java.io.Serializable;

/**
 * 
 * 项目名称：IOT云平台 模块名称：录影计划 功能描述： 创建人： wujianlong 创建时间：2017年8月10日 上午10:37:46
 * 修改人： wujianlong 修改时间：2017年8月10日 上午10:37:46
 */
public class PlanInfoDto implements Serializable {

	private static final long serialVersionUID = -6605703057339673972L;

	/** 计划id */
	private String planId;

	/** 套餐类型,0-全时,1-事件 */
	private String packageType;

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

}

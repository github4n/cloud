package com.iot.video.dto;

import io.swagger.annotations.ApiModel;

@ApiModel
public class PlanNameParam {

	/** 租户id */
	private Long tenantId;

	/** 用户id */
	private String userId;

	/** 计划id */
	private String planId;

	/** 计划名称 */
	private String planName;

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}
	
}

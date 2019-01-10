package com.iot.video.dto;

import com.iot.common.beans.SearchParam;

import io.swagger.annotations.ApiModel;

@ApiModel
public class PlanSearchParam extends SearchParam {

	/** 租户id */
	private Long tenantId;

	/** 用户id */
	private String userId;

	/** 计划id */
	private String planId;

	/** 设备id */
	private String deviceId;

	/** 是否展示 */
	private boolean isShow;

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

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}

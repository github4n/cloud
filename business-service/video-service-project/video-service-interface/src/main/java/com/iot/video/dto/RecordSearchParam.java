package com.iot.video.dto;

import com.iot.common.beans.SearchParam;

import io.swagger.annotations.ApiModel;

@ApiModel
public class RecordSearchParam extends SearchParam {

	/** 用户id */
	private String userId;

	/** 租户id */
	private Long tenantId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

}

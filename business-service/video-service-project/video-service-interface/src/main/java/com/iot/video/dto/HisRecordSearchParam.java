package com.iot.video.dto;

import com.iot.common.beans.SearchParam;

import io.swagger.annotations.ApiModel;

@ApiModel
public class HisRecordSearchParam extends SearchParam {
	
	/** 计划id*/
	private String planId;
	
	/** 订单id*/
	private String orderId;
	
	/** 用户id*/
	private String userId;
	
	/** 租户id*/
	private Long tenantId;

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

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

package com.iot.video.dto;

import io.swagger.annotations.ApiModel;

@ApiModel
public class PlanParam {
	
	/** 租户id*/
	private Long tenantId;

	/** 套餐id */
	private Long packageId;

	/** 购买数量 */
	private int counts;

	/** 用户id */
	private String userId;

	/** 套餐名 */
	private String packageName;

	/**
	 * 订单id
	 */
	private String orderId;

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}

	public int getCounts() {
		return counts;
	}

	public void setCounts(int counts) {
		this.counts = counts;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}

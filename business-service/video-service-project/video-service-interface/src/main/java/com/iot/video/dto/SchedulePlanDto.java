package com.iot.video.dto;

import java.io.Serializable;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： 李帅
 * 创建时间：2017年11月3日 下午3:48:44
 * 修改人：李帅
 * 修改时间：2017年11月3日 下午3:48:44
 */
public class SchedulePlanDto implements Serializable {

	private static final long serialVersionUID = -6605703057339673972L;

	/**
	 *  计划id
	 */
	private String planId;
	
	/**
	 *  计划名称
	 */
	private String planName;
	
	/**
	 *  计划开始时间
	 */
	private String planStartTime;
	
	/**
	 *  计划结束时间
	 */
	private String planEndTime;
	
	/**
	 *  用户ID
	 */
	private String userId;
	
	/**
	 *  租户ID
	 */
	private Long tenantId;
	
	/**
	 *  设备ID
	 */
	private String deviceId;
	
	/**
	 *  计划详情
	 */
	private String packageName;

	/**
	 * 套餐id
	 */
	private Long packageId;
	
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

	public String getPlanStartTime() {
		return planStartTime;
	}

	public void setPlanStartTime(String planStartTime) {
		this.planStartTime = planStartTime;
	}

	public String getPlanEndTime() {
		return planEndTime;
	}

	public void setPlanEndTime(String planEndTime) {
		this.planEndTime = planEndTime;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}
}

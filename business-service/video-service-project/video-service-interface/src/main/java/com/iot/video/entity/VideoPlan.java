package com.iot.video.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 
 * 项目名称：IOT云平台 模块名称：录影计划 功能描述： 创建人： wujianlong 创建时间：2017年8月10日 上午10:37:46
 * 修改人： wujianlong 修改时间：2017年8月10日 上午10:37:46
 */
public class VideoPlan {

	private long id;

	private String userId;

	/** 计划id */
	private String planId;

	/** 套餐id */
	private long packageId;

	/** 设备id */
	private String deviceId;

	/** 计划开始时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date planStartTime;

	/** 计划结束时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date planEndTime;

	/** 执行周期 */
	private String planCycle;

	/** 时程开关 */
	private int planStatus;

	/** 套餐名称 */
	private String packageName;

	/** 排序字段 */
	private int planOrder;

	/** 计划执行状态 */
	private int planExecStatus;

	/** 计划名称 */
	private String planName;

	/** 租户ID */
	private long tenantId;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 订单id
	 */
	private String orderId;

	public void setId(long id)
	{
		this.id = id;
	}

	public long getId()
	{
		return this.id;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Date getPlanStartTime() {
		return planStartTime;
	}

	public void setPlanStartTime(Date planStartTime) {
		this.planStartTime = planStartTime;
	}

	public Date getPlanEndTime() {
		return planEndTime;
	}

	public void setPlanEndTime(Date planEndTime) {
		this.planEndTime = planEndTime;
	}

	public String getPlanCycle() {
		return planCycle;
	}

	public void setPlanCycle(String planCycle) {
		this.planCycle = planCycle;
	}

	public int getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(int planStatus) {
		this.planStatus = planStatus;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getPlanOrder() {
		return planOrder;
	}

	public void setPlanOrder(int planOrder) {
		this.planOrder = planOrder;
	}

	public int getPlanExecStatus() {
		return planExecStatus;
	}

	public void setPlanExecStatus(int planExecStatus) {
		this.planExecStatus = planExecStatus;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public long getTenantId() {
		return tenantId;
	}

	public void setTenantId(long tenantId) {
		this.tenantId = tenantId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}

package com.iot.video.dto;

import java.io.Serializable;

public class VideoPlanTaskDto implements Serializable{

	/***/
	private static final long serialVersionUID = 1L;

	/** 计划id */
	private String planId;

	/** 计划执行状态 */
	private Integer planExecStatus;

	/** 时程开关 */
	private Integer planStatus;

	/** 套餐类型 */
	private Integer packageType;

	/** 执行周期 */
	private String planCycle;

	/** 执行日期 */
	private String taskDate;

	/** 执行开始时间 */
	private String executeStartTime;

	/** 计划结束时间 */
	private String executeEndTime;

	/** 事件或全时量 */
	private Integer amount;

	/** 套餐id */
	private Long packageId;

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public Integer getPackageType() {
		return packageType;
	}

	public void setPackageType(Integer packageType) {
		this.packageType = packageType;
	}

	public String getPlanCycle() {
		return planCycle;
	}

	public void setPlanCycle(String planCycle) {
		this.planCycle = planCycle;
	}

	public String getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}

	public String getExecuteStartTime() {
		return executeStartTime;
	}

	public void setExecuteStartTime(String executeStartTime) {
		this.executeStartTime = executeStartTime;
	}

	public String getExecuteEndTime() {
		return executeEndTime;
	}

	public void setExecuteEndTime(String executeEndTime) {
		this.executeEndTime = executeEndTime;
	}

	public Integer getPlanExecStatus() {
		return planExecStatus;
	}

	public void setPlanExecStatus(Integer planExecStatus) {
		this.planExecStatus = planExecStatus;
	}

	public Integer getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(Integer planStatus) {
		this.planStatus = planStatus;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}
}

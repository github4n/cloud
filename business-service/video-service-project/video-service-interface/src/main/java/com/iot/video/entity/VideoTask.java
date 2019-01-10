package com.iot.video.entity;

public class VideoTask {

	/** 租户id */
	private Long tenantId;

	/** 用户id */
	private String userId;

	/**
	 * 任务id
	 */
	private Long taskId;

	/**
	 * 计划id
	 */
	private String planId;

	/**
	 * 执行日期
	 */
	private String taskDate;

	/**
	 * 执行开始时间
	 */
	private String executeStartTime;

	/**
	 * 执行结束时间
	 */
	private String executeEndTime;

	/**
	 * 计划状态
	 */
	private Integer taskStatus;

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

	/**
	 * 任务id
	 *
	 * @return task_id - 任务id
	 */
	public Long getTaskId() {
		return taskId;
	}

	/**
	 * 任务id
	 *
	 * @param taskId
	 *            任务id
	 */
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	/**
	 * 计划id
	 *
	 * @return plan_id - 计划id
	 */
	public String getPlanId() {
		return planId;
	}

	/**
	 * 计划id
	 *
	 * @param planId
	 *            计划id
	 */
	public void setPlanId(String planId) {
		this.planId = planId;
	}

	/**
	 * 执行日期
	 *
	 * @return task_date - 执行日期
	 */
	public String getTaskDate() {
		return taskDate;
	}

	/**
	 * 执行日期
	 *
	 * @param taskDate
	 *            执行日期
	 */
	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}

	/**
	 * 执行开始时间
	 *
	 * @return execute_start_time - 执行开始时间
	 */
	public String getExecuteStartTime() {
		return executeStartTime;
	}

	/**
	 * 执行开始时间
	 *
	 * @param executeStartTime
	 *            执行开始时间
	 */
	public void setExecuteStartTime(String executeStartTime) {
		this.executeStartTime = executeStartTime;
	}

	/**
	 * 执行结束时间
	 *
	 * @return execute_end_time - 执行结束时间
	 */
	public String getExecuteEndTime() {
		return executeEndTime;
	}

	/**
	 * 执行结束时间
	 *
	 * @param executeEndTime
	 *            执行结束时间
	 */
	public void setExecuteEndTime(String executeEndTime) {
		this.executeEndTime = executeEndTime;
	}

	/**
	 * 计划状态
	 *
	 * @return task_status - 计划状态
	 */
	public Integer getTaskStatus() {
		return taskStatus;
	}

	/**
	 * 计划状态
	 *
	 * @param taskStatus
	 *            计划状态
	 */
	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}
}
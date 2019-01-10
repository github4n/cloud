package com.iot.video.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class VideoPayRecord {

	private Long id;

	/**
	 * 订单id
	 */
	private String orderId;

	/**
	 * 计划id
	 */
	private String planId;

	/**
	 * 套餐标识
	 */
	private Long packageId;

	/**
	 * 用户uuid
	 */
	private String userId;

	/**
	 * 购买数量（月数）
	 */
	private Integer counts;

	/**
	 * 货币代码
	 */
	private String currency;

	/**
	 * 计划开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date planStartTime;

	/**
	 * 计划结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date planEndTime;

	/**
	 * 创建事件
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 计划状态(定时扫描,1-使用中 2-到期 3-过期定时扫描需要分布式锁 4:退款，计划失效)
	 */
	private int planStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getCounts() {
		return counts;
	}

	public void setCounts(Integer counts) {
		this.counts = counts;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(int planStatus) {
		this.planStatus = planStatus;
	}
}

package com.iot.video.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ALLRecordDto {

	/**
	 * 租户ID
	 */
	private Long tenantId;

	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 
	 */
	private String payRecordId;

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
	private String packageId;

	/**
	 * 套餐名称
	 */
	private String packageName;

	/**
	 * 套餐单价
	 */
	private float packagePrice;

	/**
	 * 购买数量（月数）
	 */
	private int counts;

	/**
	 * 交易时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date payTime;

	/**
	 * 实付价格
	 */
	private Double payPrice;

	/**
	 * 付费方式
	 */
	private String payType;

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
	 * 续费标识:1-可续费，0-不可续费
	 */
	private String renewMark;

	/**
	 * 计划状态
	 */
	private int planStatus;

	/**
	 * 交易状态
	 */
	private String payStatus;

	/**
	 * 退款状态
	 */
	private String refundStatus;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPayRecordId() {
		return payRecordId;
	}

	public void setPayRecordId(String payRecordId) {
		this.payRecordId = payRecordId;
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

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public float getPackagePrice() {
		return packagePrice;
	}

	public void setPackagePrice(float packagePrice) {
		this.packagePrice = packagePrice;
	}

	public int getCounts() {
		return counts;
	}

	public void setCounts(int counts) {
		this.counts = counts;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public void setPlanStartTime(Date planStartTime) {
		this.planStartTime = planStartTime;
	}

	public void setPlanEndTime(Date planEndTime) {
		this.planEndTime = planEndTime;
	}

	public Double getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Double payPrice) {
		this.payPrice = payPrice;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getRenewMark() {
		return renewMark;
	}

	public void setRenewMark(String renewMark) {
		this.renewMark = renewMark;
	}

	public int getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(int planStatus) {
		this.planStatus = planStatus;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public Date getPlanStartTime() {
		return planStartTime;
	}

	public Date getPlanEndTime() {
		return planEndTime;
	}

}

package com.iot.boss.vo.refund;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iot.boss.enums.VideoRecordOrderStatusEnum;

import java.math.BigDecimal;
import java.util.Date;

public class RecordDto {
	
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date planStartTime;

	/**
	 * 计划结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
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

	/** 交易id */
	private String tradeId;

	/** 退款时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date refundTime;

	/** 退款金额 */
	private BigDecimal refundSum;

	private Long refundApplyId;

	private String refundApplyName;

	private String refundReason;

	private BigDecimal refundPrice;

	private Date refundApplyTime;

	private Long auditId;

	private String auditName;

	private String auditMessage;

	private Date auditTime;

	private Integer auditStatus;

	//订单状态（其值对应VideoRecordOrderStatusEnum枚举类）
	private Integer orderStatus;

	private String orderStatusStr;

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

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public BigDecimal getRefundSum() {
		return refundSum;
	}

	public void setRefundSum(BigDecimal refundSum) {
		this.refundSum = refundSum;
	}

	public Long getRefundApplyId() {
		return refundApplyId;
	}

	public void setRefundApplyId(Long refundApplyId) {
		this.refundApplyId = refundApplyId;
	}

	public String getRefundApplyName() {
		return refundApplyName;
	}

	public void setRefundApplyName(String refundApplyName) {
		this.refundApplyName = refundApplyName;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public BigDecimal getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(BigDecimal refundPrice) {
		this.refundPrice = refundPrice;
	}

	public Date getRefundApplyTime() {
		return refundApplyTime;
	}

	public void setRefundApplyTime(Date refundApplyTime) {
		this.refundApplyTime = refundApplyTime;
	}

	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	public String getAuditMessage() {
		return auditMessage;
	}

	public void setAuditMessage(String auditMessage) {
		this.auditMessage = auditMessage;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatusStr() {
		if (getOrderStatus()!=null){
			orderStatusStr = VideoRecordOrderStatusEnum.getDescByValue(getOrderStatus());
		}
		return orderStatusStr;
	}

	public void setOrderStatusStr(String orderStatusStr) {
		this.orderStatusStr = orderStatusStr;
	}
}

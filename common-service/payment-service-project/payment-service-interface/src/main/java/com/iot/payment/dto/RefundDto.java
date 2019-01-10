package com.iot.payment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel(description = "退款入参")
public class RefundDto {
	
	@ApiModelProperty(name = "refundSum", value = "退款金额", dataType = "BigDecimal")
	private BigDecimal refundSum;
	

	@ApiModelProperty(name = "userId", value = "用户id", dataType = "String")
	private String userId;
	

	@ApiModelProperty(name = "orderId", value = "订单id", dataType = "String")
	private String orderId;


	@ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
	private Long tenantId;

	@ApiModelProperty(name = "refundReason", value = "退款原因", dataType = "String")
	private String refundReason;

	public BigDecimal getRefundSum() {
		return refundSum;
	}

	public void setRefundSum(BigDecimal refundSum) {
		this.refundSum = refundSum;
	}



	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public RefundDto() {
	}


	public RefundDto(BigDecimal refundSum, String userId, String orderId,Long tenantId) {
		this.refundSum = refundSum;
		this.userId = userId;
		this.orderId = orderId;
		this.tenantId = tenantId;
	}

	public RefundDto(BigDecimal refundSum, String userId, String orderId, Long tenantId, String refundReason) {
		this.refundSum = refundSum;
		this.userId = userId;
		this.orderId = orderId;
		this.tenantId = tenantId;
		this.refundReason = refundReason;
	}
}

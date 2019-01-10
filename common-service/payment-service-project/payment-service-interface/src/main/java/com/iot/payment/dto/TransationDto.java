package com.iot.payment.dto;

import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;

@ApiModel
public class TransationDto {
	
	/** 支付价格*/
	private BigDecimal payPrice;
	
	/** 货币代码*/
	private String currency;
	
	/** 取消url*/
	private String cancelUrl;
	
	/** 回调url*/
	private String returnUrl;
	
	/** 商品id*/
	private Long goodsId;
	
	/** 用户id*/
	private String userId;
	
	/** 订单id*/
	private String orderId;

	/**
	 * 支付成功跳转url
	 */
	private String successUrl;

	/**
	 * 支付过程出错跳转url
	 */
	private String errorUrl;

	/**
	 * 租户id
	 */
	private Long tenantId;



	public BigDecimal getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
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


	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getErrorUrl() {
		return errorUrl;
	}

	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public TransationDto() {
	}


}

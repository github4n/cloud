package com.iot.payment.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PayTransation {

	/** 支付交易id */
	private long id;

	/** 用户id */
	private String userId;

	/** 支付id */
	private String payId;

	/** 商品id */
	private Long goodsId;

	/** 支付价格 */
	private double payPrice;

	/** 订单id */
	private String orderId;

	/** 交易id */
	private String tradeId;

	/** 支付类别 */
	private Integer payType;

	/** 创建时间 */
	private Date createTime;

	/** 支付时间 */
	private Date payTime;

	/** 退款时间 */
	private Date refundTime;

	/** 退款失败原因 */
	private String payFailReason;

	/** 支付状态 */
	private Integer payStatus;

	/** 退款状态 */
	private Integer refundStatus;

	/** 退款金额 */
	private BigDecimal refundSum;

	/** 退款原因 */
	private String refundReason;

	/** 货币代码 */
	private String currency;

	/** 支付来源 */
	private Integer paySource;

	/** 预支付id */
	private String paymentId;

	private String refundFailReason;

	/**
	 * 租户id
	 */
	private Long tenantId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public double getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(double payPrice) {
		this.payPrice = payPrice;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getPayFailReason() {
		return payFailReason;
	}

	public void setPayFailReason(String payFailReason) {
		this.payFailReason = payFailReason;
	}

	public BigDecimal getRefundSum() {
		return refundSum;
	}

	public void setRefundSum(BigDecimal refundSum) {
		this.refundSum = refundSum;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getPaySource() {
		return paySource;
	}

	public void setPaySource(Integer paySource) {
		this.paySource = paySource;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getRefundFailReason() {
		return refundFailReason;
	}

	public void setRefundFailReason(String refundFailReason) {
		this.refundFailReason = refundFailReason;
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
}

package com.iot.boss.vo.uuid;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "UUID订单信息",description = "UUID订单信息")
public class UUIDOrderResp {

    @ApiModelProperty(value="用户ID",name="userId")
    private Long userId;

    @ApiModelProperty(value="批次号",name="batchNumId")
    private Long batchNumId;

    @ApiModelProperty(value="订单id",name="orderId")
    private String orderId;

    @ApiModelProperty(value="本批次UUID的申请人（客户帐号）",name="clientAccount")
    private String clientAccount;

    @ApiModelProperty(value="UUID所属产品",name="productModel")
    private String productModel;

    @ApiModelProperty(value="UUID商品ID",name="goodsId")
    private Long goodsId;

    @ApiModelProperty(value="UUID所属产品方案",name="productSchema")
    private String productSchema;

    @ApiModelProperty(value="订单状态",name="applyStatus")
    private Integer applyStatus;

    @ApiModelProperty(value="支付状态",name="payStatus")
    private Integer payStatus;

    @ApiModelProperty(value="申请时间",name="createTime")
    private Date createTime;

    @ApiModelProperty(value="订单总价",name="totalPrice")
    private BigDecimal totalPrice;
    
    @ApiModelProperty(value="货币单位",name="currency")
    private String currency;
    
    @ApiModelProperty(value="租户ID",name="tenantId")
    private Long tenantId;
    
    public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getBatchNumId() {
        return batchNumId;
    }

    public void setBatchNumId(Long batchNumId) {
        this.batchNumId = batchNumId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClientAccount() {
        return clientAccount;
    }

    public void setClientAccount(String clientAccount) {
        this.clientAccount = clientAccount;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getProductSchema() {
        return productSchema;
    }

    public void setProductSchema(String productSchema) {
        this.productSchema = productSchema;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
    
}

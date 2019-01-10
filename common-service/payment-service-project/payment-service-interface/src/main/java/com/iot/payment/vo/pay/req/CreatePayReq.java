package com.iot.payment.vo.pay.req;

import com.iot.payment.vo.order.req.CreateOrderRecordReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 项目名称：cloud
 * 功能描述：创建支付入参
 * 创建人： yeshiyuan
 * 创建时间：2018/7/17 17:29
 * 修改人： yeshiyuan
 * 修改时间：2018/7/17 17:29
 * 修改描述：
 */
@ApiModel(description = "创建支付入参")
public class CreatePayReq {

    @ApiModelProperty(name = "payPrice", value = "支付价格", dataType = "BigDecimal")
    private BigDecimal payPrice;

    @ApiModelProperty(name = "currency", value = "货币代码", dataType = "String")
    private String currency;

    @ApiModelProperty(name = "cancelUrl", value = "取消url", dataType = "String")
    private String cancelUrl;

    @ApiModelProperty(name = "returnUrl", value = "回调url", dataType = "String")
    private String returnUrl;

    @ApiModelProperty(name = "goodsId", value = "商品id", dataType = "Long")
    private Long goodsId;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "String")
    private String userId;

    @ApiModelProperty(name = "orderId", value = "订单id", dataType = "String")
    private String orderId;

    @ApiModelProperty(name = "successUrl", value = "支付成功跳转url", dataType = "String")
    private String successUrl;

    @ApiModelProperty(name = "errorUrl", value = "支付过程出错跳转url", dataType = "String")
    private String errorUrl;

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "createOrderRecordReq", value = "订单信息", dataType = "com.iot.payment.vo.order.req.CreateOrderRecordReq")
    private CreateOrderRecordReq createOrderRecordReq;


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

    public CreateOrderRecordReq getCreateOrderRecordReq() {
        return createOrderRecordReq;
    }

    public void setCreateOrderRecordReq(CreateOrderRecordReq createOrderRecordReq) {
        this.createOrderRecordReq = createOrderRecordReq;
    }

    public CreatePayReq() {
    }

    public CreatePayReq(BigDecimal payPrice, String currency, String cancelUrl, String returnUrl, Long goodsId, String userId, String orderId, String successUrl, String errorUrl, Long tenantId) {
        this.payPrice = payPrice;
        this.currency = currency;
        this.cancelUrl = cancelUrl;
        this.returnUrl = returnUrl;
        this.goodsId = goodsId;
        this.userId = userId;
        this.orderId = orderId;
        this.successUrl = successUrl;
        this.errorUrl = errorUrl;
        this.tenantId = tenantId;
    }
}

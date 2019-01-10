package com.iot.portal.uuid.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 项目名称：cloud
 * 功能描述：uuid订单信息
 * 创建人： yeshiyuan
 * 创建时间：2018/7/5 9:48
 * 修改人： yeshiyuan
 * 修改时间：2018/7/5 9:48
 * 修改描述：
 */
@ApiModel(value = "uuid订单信息", description = "uuid订单信息")
public class UuidOrderInfoResp {

    @ApiModelProperty(name = "orderId", value = "订单id", dataType = "string")
    private String orderId;

    @ApiModelProperty(name = "goodsPrice", value = "商品价格", dataType = "bigdecimal")
    private BigDecimal goodsPrice;

    @ApiModelProperty(name = "totalPrice", value = "订单总价", dataType = "bigdecimal")
    private BigDecimal totalPrice;

    @ApiModelProperty(name = "buyNum", value = "购买数量", dataType = "Integer")
    private Integer buyNum;

    @ApiModelProperty(name = "goodsName", value = "商品名称", dataType = "String")
    private String goodsName;

    @ApiModelProperty(name = "currency", value = "货币代码", dataType = "String")
    private String currency;

    @ApiModelProperty(name = "payStatusStr", value = "支付状态", dataType = "String")
    private String payStatusStr;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayStatusStr() {
        return payStatusStr;
    }

    public void setPayStatusStr(String payStatusStr) {
        this.payStatusStr = payStatusStr;
    }

    public UuidOrderInfoResp() {
    }

    public UuidOrderInfoResp(String orderId, BigDecimal goodsPrice, BigDecimal totalPrice, Integer buyNum, String goodsName, String currency,String payStatus) {
        this.orderId = orderId;
        this.goodsPrice = goodsPrice;
        this.totalPrice = totalPrice;
        this.buyNum = buyNum;
        this.goodsName = goodsName;
        this.currency = currency;
        this.payStatusStr = payStatus;
    }
}

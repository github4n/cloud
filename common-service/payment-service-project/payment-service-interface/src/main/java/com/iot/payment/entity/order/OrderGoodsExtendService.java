package com.iot.payment.entity.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 项目名称：cloud
 * 功能描述：订单商品-附加服务关系
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 11:59
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 11:59
 * 修改描述：
 */
@ApiModel(value = "订单商品-附加服务关系", description = "订单商品-附加服务关系")
public class OrderGoodsExtendService {
    @ApiModelProperty(name = "id", value = "订单商品附加服务id", dataType = "Long")
    private Long id;

    @ApiModelProperty(name = "orderId", value = "订单id", dataType = "string")
    private String orderId;

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "orderGoodsId", value = "对应订单商品id", dataType = "Long")
    private Long orderGoodsId;

    @ApiModelProperty(name = "goodsExId", value = "原商品附加服务id，goods_extend_service主键", dataType = "Long")
    private Long goodsExId;

    @ApiModelProperty(name = "goodsExName", value = "商品附加服务名称", dataType = "string")
    private String goodsExName;

    @ApiModelProperty(name = "goodsExPrice", value = "商品附加服务价格", dataType = "BigDecimal")
    private BigDecimal goodsExPrice;

    @ApiModelProperty(name = "goodsExCurrency", value = "货币单位", dataType = "string")
    private String goodsExCurrency;

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

    public Long getOrderGoodsId() {
        return orderGoodsId;
    }

    public void setOrderGoodsId(Long orderGoodsId) {
        this.orderGoodsId = orderGoodsId;
    }

    public Long getGoodsExId() {
        return goodsExId;
    }

    public void setGoodsExId(Long goodsExId) {
        this.goodsExId = goodsExId;
    }

    public String getGoodsExName() {
        return goodsExName;
    }

    public void setGoodsExName(String goodsExName) {
        this.goodsExName = goodsExName;
    }

    public BigDecimal getGoodsExPrice() {
        return goodsExPrice;
    }

    public void setGoodsExPrice(BigDecimal goodsExPrice) {
        this.goodsExPrice = goodsExPrice;
    }

    public String getGoodsExCurrency() {
        return goodsExCurrency;
    }

    public void setGoodsExCurrency(String goodsExCurrency) {
        this.goodsExCurrency = goodsExCurrency;
    }

    public OrderGoodsExtendService() {
    }

    public OrderGoodsExtendService(String orderId, Long goodsExId, String goodsExName, BigDecimal goodsExPrice, String goodsExCurrency,Long tenantId) {
        this.orderId = orderId;
        this.goodsExId = goodsExId;
        this.goodsExName = goodsExName;
        this.goodsExPrice = goodsExPrice;
        this.goodsExCurrency = goodsExCurrency;
        this.tenantId = tenantId;
    }
}

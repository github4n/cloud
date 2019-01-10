package com.iot.payment.vo.order.req;

import com.iot.payment.vo.goods.req.GoodsExtendServiceReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 项目名称：cloud
 * 功能描述：创建订单记录的入参
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 14:09
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 14:09
 * 修改描述：
 */
@ApiModel(value = "创建订单记录的入参", description = "创建订单记录的入参")
public class CreateOrderRecordReq {

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Long")
    private Long userId;

    @ApiModelProperty(name = "totalPrice", value = "订单总价", dataType = "BigDecimal")
    private BigDecimal totalPrice;

    @ApiModelProperty(name = "currency", value = "货币单位", dataType = "string")
    private String currency;

    @ApiModelProperty(name = "orderType", value = "订单类型，对应字典表sys_dict_item的type_id=2的记录", dataType = "int")
    private Integer orderType;

    @ApiModelProperty(name = "goodsExtendServiceReq", value = "商品与附加服务", dataType = "list")
    private List<GoodsExtendServiceReq> goodsExtendServiceReq;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public List<GoodsExtendServiceReq> getGoodsExtendServiceReq() {
        return goodsExtendServiceReq;
    }

    public void setGoodsExtendServiceReq(List<GoodsExtendServiceReq> goodsExtendServiceReq) {
        this.goodsExtendServiceReq = goodsExtendServiceReq;
    }

    public CreateOrderRecordReq() {
    }

    public CreateOrderRecordReq(Long tenantId, Long userId, BigDecimal totalPrice, String currency, Integer orderType) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.orderType = orderType;
    }
}

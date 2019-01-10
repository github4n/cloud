package com.iot.payment.entity.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 项目名称：cloud
 * 功能描述：订单商品关系表
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 11:50
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 11:50
 * 修改描述：
 */
@ApiModel(value = "订单商品关系表", description = "订单商品关系表")
public class OrderGoods {

    @ApiModelProperty(name = "id", value = "订单商品id", dataType = "Long")
    private Long id;

    @ApiModelProperty(name = "orderId", value = "订单id", dataType = "string")
    private String orderId;

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "num", value = "购买数量", dataType = "Integer")
    private Integer num;

    @ApiModelProperty(name = "goodsId", value = "原商品id,关联goods_info表", dataType = "Long")
    private Long goodsId;

    @ApiModelProperty(name = "goodsName", value = "商品名称", dataType = "string")
    private String goodsName;

    @ApiModelProperty(name = "goodsStandard", value = "商品规格（描述商品某种属性）", dataType = "string")
    private String goodsStandard;

    @ApiModelProperty(name = "goodsStandardUnit", value = "商品规格单位（比如时间期限：年/月等等，对应字典表sys_dict_item的type_id=1的记录）", dataType = "Integer")
    private Integer goodsStandardUnit;

    @ApiModelProperty(name = "goodsPrice", value = "商品价格", dataType = "BigDecimal")
    private BigDecimal goodsPrice;

    @ApiModelProperty(name = "goodsCurrency", value = "货币单位", dataType = "string")
    private String goodsCurrency;

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

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsStandard() {
        return goodsStandard;
    }

    public void setGoodsStandard(String goodsStandard) {
        this.goodsStandard = goodsStandard;
    }

    public Integer getGoodsStandardUnit() {
        return goodsStandardUnit;
    }

    public void setGoodsStandardUnit(Integer goodsStandardUnit) {
        this.goodsStandardUnit = goodsStandardUnit;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getGoodsCurrency() {
        return goodsCurrency;
    }

    public void setGoodsCurrency(String goodsCurrency) {
        this.goodsCurrency = goodsCurrency;
    }

    public OrderGoods(String orderId, Integer num, Long goodsId, String goodsName, String goodsStandard, Integer goodsStandardUnit, BigDecimal goodsPrice, String currency, Long tenantId) {
        this.orderId = orderId;
        this.num = num;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsStandard = goodsStandard;
        this.goodsStandardUnit = goodsStandardUnit;
        this.goodsPrice = goodsPrice;
        this.goodsCurrency = currency;
        this.tenantId = tenantId;
    }

    public OrderGoods() {
    }
}

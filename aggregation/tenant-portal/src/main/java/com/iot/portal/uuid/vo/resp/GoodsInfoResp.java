package com.iot.portal.uuid.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 项目名称：cloud
 * 功能描述：商品信息
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 11:32
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 11:32
 * 修改描述：
 */
@ApiModel(value = "商品信息",description = "商品信息")
public class GoodsInfoResp {

    @ApiModelProperty(value="商品id",name="id")
    private String id;

    @ApiModelProperty(value="商品图片",name="icon")
    private String icon;

    @ApiModelProperty(value="商品名称",name="goodsName")
    private String goodsName;

    @ApiModelProperty(value="商品描述",name="description")
    private String description;

    @ApiModelProperty(value="商品规格（描述商品某种属性）",name="standard")
    private String standard;

    @ApiModelProperty(value="商品规格单位",name="standardUnit")
    private Integer standardUnit;

    @ApiModelProperty(value="商品价格",name="price")
    private BigDecimal price;

    @ApiModelProperty(value="货币单位",name="currency")
    private String currency;

    public GoodsInfoResp() {

    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public Integer getStandardUnit() {
        return standardUnit;
    }

    public void setStandardUnit(Integer standardUnit) {
        this.standardUnit = standardUnit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}

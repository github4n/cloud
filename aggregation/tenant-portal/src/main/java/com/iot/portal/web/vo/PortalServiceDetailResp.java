package com.iot.portal.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/** 
 * 
 * 项目名称：IOT云平台
 * 模块名称：Portal
 * 功能描述：虚拟服务详情VO
 * 创建人： maochengyuan 
 * 创建时间：2018/9/13 16:41
 * 修改人： maochengyuan
 * 修改时间：2018/9/13 16:41
 * 修改描述：
 */
@ApiModel("虚拟服务详情VO")
public class PortalServiceDetailResp implements Serializable {

    @ApiModelProperty(value="商品ID",name="goodsId")
    private String goodsId;

    @ApiModelProperty(value="商品icon",name="icon")
    private String icon;

    @ApiModelProperty(value="商品价格",name="price")
    private BigDecimal price;

    @ApiModelProperty(value="商品描述",name="description")
    private String description;

    @ApiModelProperty(value="订单ID",name="orderId")
    private String orderId;

    @ApiModelProperty(value="订单创建时间",name="orderTime")
    private Date orderTime;

    @ApiModelProperty(value="支付状态",name="payStatus")
    private Integer payStatus;

    @ApiModelProperty(value="服务附加信息（app信息，产品信息）",name="servcieInfo")
    private Object servcieInfo;

    @ApiModelProperty(name = "currency", value="货币单位", dataType = "String")
    private String currency;

    public PortalServiceDetailResp() {

    }

    public PortalServiceDetailResp(String goodsId, String icon, BigDecimal price) {
        this.goodsId = goodsId;
        this.icon = icon;
        this.price = price;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Object getServcieInfo() {
        return servcieInfo;
    }

    public void setServcieInfo(Object servcieInfo) {
        this.servcieInfo = servcieInfo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

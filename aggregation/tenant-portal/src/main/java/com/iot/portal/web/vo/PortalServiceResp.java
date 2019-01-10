package com.iot.portal.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/** 
 * 
 * 项目名称：IOT云平台
 * 模块名称：Portal
 * 功能描述：虚拟服务VO
 * 创建人： maochengyuan 
 * 创建时间：2018/9/13 16:41
 * 修改人： maochengyuan
 * 修改时间：2018/9/13 16:41
 * 修改描述：
 */
@ApiModel("虚拟服务VO")
public class PortalServiceResp implements Serializable {
    @ApiModelProperty(value="商品名称",name="goodsName")
    private String goodsName;

    @ApiModelProperty(value="商品ID",name="goodsId")
    private String goodsId;

    @ApiModelProperty(value="商品类别ID",name="goodsTypeId")
    private String goodsTypeId;

    @ApiModelProperty(value="商品icon",name="icon")
    private String icon;

    @ApiModelProperty(value="商品价格",name="price")
    private BigDecimal price;

    @ApiModelProperty(value="商品描述",name="description")
    private String description;

    @ApiModelProperty(value="商品详细描述（二级界面）",name="detailDesc")
    private String detailDesc;

    @ApiModelProperty(value="支付状态（1:待付款；2：已付款）",name="payStatus")
    private Integer payStatus;

    @ApiModelProperty(name = "currency", value="货币单位", dataType = "String")
    private String currency;

    /**
     * 订单id，对应order_record表主键
     */
    @ApiModelProperty(name = "orderId", value="订单id", dataType = "String")
    private String orderId;
    
    public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public PortalServiceResp() {

    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
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

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getDetailDesc() {
        return detailDesc;
    }

    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc;
    }

}

package com.iot.portal.uuid.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 项目名称：cloud
 * 功能描述：uuid申请入参
 * 创建人： yeshiyuan
 * 创建时间：2018/6/29 10:54
 * 修改人： yeshiyuan
 * 修改时间：2018/6/29 10:54
 * 修改描述：
 */
@ApiModel(description = "uuid申请入参")
public class UuidApplyReq implements Serializable {

    @ApiModelProperty(name = "totalPrice", value = "订单总价", dataType = "BigDecimal")
    private BigDecimal totalPrice;

    @ApiModelProperty(name = "currency", value = "货币单位", dataType = "string")
    private String currency;

    @ApiModelProperty(name = "createNum", value = "生成数量", dataType = "int")
    private Integer createNum;

    @ApiModelProperty(name = "productId", value = "产品id", dataType = "String")
    private String productId;

    /*@ApiModelProperty(name = "goodsExServiceIds", value = "商品服务服务id列表", dataType = "list")
    private List<String> goodsExServiceIds;*/



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

    public Integer getCreateNum() {
        return createNum;
    }

    public void setCreateNum(Integer createNum) {
        this.createNum = createNum;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}

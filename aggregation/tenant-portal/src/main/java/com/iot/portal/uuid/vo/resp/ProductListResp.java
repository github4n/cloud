package com.iot.portal.uuid.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：产品列表出参
 * 创建人： yeshiyuan
 * 创建时间：2018/7/9 10:58
 * 修改人： yeshiyuan
 * 修改时间：2018/7/9 10:58
 * 修改描述：
 */
@ApiModel(value = "产品列表出参",description = "产品列表出参")
public class ProductListResp {

    @ApiModelProperty(name = "productId", value = "产品id", dataType = "string")
    private String productId;

    @ApiModelProperty(name = "productName", value = "产品名称", dataType = "string")
    private String productName;

    @ApiModelProperty(value = "技术实现方案 ", allowableValues = "1 WIFI 2 蓝牙 3 网关 4 IPC")
    private Integer communicationMode;

    public Integer getCommunicationMode() {
        return communicationMode;
    }

    public void setCommunicationMode(Integer communicationMode) {
        this.communicationMode = communicationMode;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductListResp() {
    }

    public ProductListResp(String productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    public ProductListResp(String productId, String productName, Integer communicationMode) {
        this.productId = productId;
        this.productName = productName;
        this.communicationMode = communicationMode;
    }
}

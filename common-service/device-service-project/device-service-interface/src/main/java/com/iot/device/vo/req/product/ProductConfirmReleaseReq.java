package com.iot.device.vo.req.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：产品确认发布入参
 * 创建人： yeshiyuan
 * 创建时间：2018/9/12 14:30
 * 修改人： yeshiyuan
 * 修改时间：2018/9/12 14:30
 * 修改描述：
 */
@ApiModel(value = "产品确认发布入参")
public class ProductConfirmReleaseReq {

    @ApiModelProperty(name = "productId", value = "产品id", dataType = "String")
    private Long productId;

    @ApiModelProperty(name = "productName", value = "产品名称", dataType = "String")
    private String productName;

    @ApiModelProperty(name = "model", value = "产品型号", dataType = "String")
    private String model;

    @ApiModelProperty(name = "remark", value = "产品备注", dataType = "String")
    private String remark;

    @ApiModelProperty(name = "oldModel", value = "旧型号", dataType = "String")
    private String oldModel;

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "icon", value = "图标", dataType = "String")
    private String icon;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Long")
    private Long userId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOldModel() {
        return oldModel;
    }

    public void setOldModel(String oldModel) {
        this.oldModel = oldModel;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ProductConfirmReleaseReq() {
    }

    public ProductConfirmReleaseReq(Long productId, String productName, String model, String remark, String icon) {
        this.productId = productId;
        this.productName = productName;
        this.model = model;
        this.remark = remark;
        this.icon = icon;
    }
}

package com.iot.boss.vo.product.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：产品详情
 * 创建人： yeshiyuan
 * 创建时间：2018/10/25 10:03
 * 修改人： yeshiyuan
 * 修改时间：2018/10/25 10:03
 * 修改描述：
 */
@ApiModel(description = "产品详情")
public class ProductDetailInfoResp {
    @ApiModelProperty(name = "deviceTypeName", value = "设备类型名称", dataType = "String")
    private String deviceTypeName;
    @ApiModelProperty(name = "model", value = "产品型号", dataType = "String")
    private String model;
    @ApiModelProperty(name = "productName", value = "产品名称", dataType = "String")
    private String productName;
    @ApiModelProperty(name = "phone", value = "联系电话", dataType = "String")
    private String phone;
    @ApiModelProperty(name = "email", value = "邮箱", dataType = "String")
    private String email;

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

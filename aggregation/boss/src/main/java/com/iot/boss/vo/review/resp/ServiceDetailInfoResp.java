package com.iot.boss.vo.review.resp;

import java.math.BigDecimal;

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
public class ServiceDetailInfoResp {
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
    @ApiModelProperty(name = "contacts", value = "联系人", dataType = "String")
    private String contacts;
    @ApiModelProperty(name = "trackeNumber", value = "快递单号", dataType = "String")
    private String trackeNumber;
    @ApiModelProperty(name = "paymentMethod", value = "支付方式", dataType = "String")
    private String paymentMethod;
    @ApiModelProperty(name = "functionalAttribute", value = "功能属性", dataType = "String")
    private String functionalAttribute;
    @ApiModelProperty(name = "paymentAmount", value = "支付金额", dataType = "BigDecimal")
    private BigDecimal paymentAmount;
    
    public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getTrackeNumber() {
		return trackeNumber;
	}

	public void setTrackeNumber(String trackeNumber) {
		this.trackeNumber = trackeNumber;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getFunctionalAttribute() {
		return functionalAttribute;
	}

	public void setFunctionalAttribute(String functionalAttribute) {
		this.functionalAttribute = functionalAttribute;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

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

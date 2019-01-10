package com.iot.device.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:51 2018/6/28
 * @Modify by:
 */
@ApiModel("产品详细")
public class AddProductReq implements Serializable {

    //产品id
    @ApiModelProperty(value = "产品id")
    private Long id;
    //租户id
    @ApiModelProperty(value = "租户id")
    private Long tenantId;
    //设备类型id
    @ApiModelProperty(value = "设备类型id")
    private Long deviceTypeId;
    //产品名称
    @Length(max = 20, message = "productName max length 20")
    @ApiModelProperty(value = "产品名称")
    private String productName;
    //icon
    @ApiModelProperty(value = "icon")
    private String icon;
    //技术实现方案 0 WIFI 1 蓝牙 2 网关 3 IPC
    @ApiModelProperty(value = "技术实现方案 ", allowableValues = "0 WIFI 1 蓝牙 2 网关 3 IPC")
    private Integer communicationMode;

    @ApiModelProperty(value = "数据传输方式")
    private Integer transmissionMode;
    //产品唯一标示 model
    @ApiModelProperty(value = "产品唯一标示 model")
    private String model;

    @ApiModelProperty(value = "配置网络方式")
    private String configNetModes;
    //备注
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 企业开发者表关联的id
     */
    @ApiModelProperty(value = "企业开发者表关联的id")
    private Long enterpriseDevelopId;
    /**
     * 是否套包产品
     */
    private Integer isKit;
    /**
     * 是否直连设备
     */
    private Integer isDirectDevice;

    /**
	 * audit_status
	 */
	private Integer auditStatus;
	
	/**
	 * service_goo_audit_status
	 */
	private Integer serviceGooAuditStatus;
	
	/**
	 * service_alx_audit_status
	 */
	private Integer serviceAlxAuditStatus;

	private Long createBy;

	private Long updateBy;
	
    public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Integer getServiceGooAuditStatus() {
		return serviceGooAuditStatus;
	}

	public void setServiceGooAuditStatus(Integer serviceGooAuditStatus) {
		this.serviceGooAuditStatus = serviceGooAuditStatus;
	}

	public Integer getServiceAlxAuditStatus() {
		return serviceAlxAuditStatus;
	}

	public void setServiceAlxAuditStatus(Integer serviceAlxAuditStatus) {
		this.serviceAlxAuditStatus = serviceAlxAuditStatus;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getCommunicationMode() {
        return communicationMode;
    }

    public void setCommunicationMode(Integer communicationMode) {
        this.communicationMode = communicationMode;
    }

    public Integer getTransmissionMode() {
        return transmissionMode;
    }

    public void setTransmissionMode(Integer transmissionMode) {
        this.transmissionMode = transmissionMode;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getConfigNetModes() {
        return configNetModes;
    }

    public void setConfigNetModes(String configNetModes) {
        this.configNetModes = configNetModes;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getEnterpriseDevelopId() {
        return enterpriseDevelopId;
    }

    public void setEnterpriseDevelopId(Long enterpriseDevelopId) {
        this.enterpriseDevelopId = enterpriseDevelopId;
    }

    public Integer getIsKit() {
        return isKit;
    }

    public void setIsKit(Integer isKit) {
        this.isKit = isKit;
    }

    public Integer getIsDirectDevice() {
        return isDirectDevice;
    }

    public void setIsDirectDevice(Integer isDirectDevice) {
        this.isDirectDevice = isDirectDevice;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }
}

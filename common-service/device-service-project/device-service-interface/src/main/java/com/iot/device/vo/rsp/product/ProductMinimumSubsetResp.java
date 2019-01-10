package com.iot.device.vo.rsp.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： wucheng
 * 创建时间：${date}
 */
@ApiModel("最小功能子集网关子设备列表出参")
public class ProductMinimumSubsetResp  implements Serializable{

    @ApiModelProperty(name = "id", value = "产品id", dataType = "Long")
    private Long id;
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    @ApiModelProperty(value = "'设备类型id'")
    private Long deviceTypeId;

    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "通信类型")
    private String communicationMode;

    @ApiModelProperty(value = "'数据传输方式")
    private String transmissionMode;

    @ApiModelProperty(value = "修改时间",dataType = "Date")
    private Date createTime;

    @ApiModelProperty(value = "'修改时间'", dataType = "Date")
    private Date updateTime;

    @ApiModelProperty(value = "'产品型号'")
    private String model;

    @ApiModelProperty(name = "configNetMode", value = "配置网络方式", dataType = "String")
    private String configNetMode;

    @ApiModelProperty(value = "是否套包产品,1是,0否")
    private int isKit;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否直连设备0否、1是")
    private int isDirectDevice;

    @ApiModelProperty(value = "图片")
    private String icon;

    @ApiModelProperty(value = "开发状态,0:未开发,1:开发中,2:已上线,3:发布中")
    private  int developStatus;

    @ApiModelProperty(value = "企业开发者id")
    private long enterpriseDevelopId;

    @ApiModelProperty(value = "0:未审核 1:审核未通过 2:审核通过")
    private int auditStatus;

    @ApiModelProperty(value = "Google语音服务审核状态，0:未审核 1:审核未通过 2:审核通过")
    private int serviceGooAuditStatus;

    @ApiModelProperty(value = "Aleax语音服务审核状态，0:未审核 1:审核未通过 2:审核通过")
    private int serviceAlxAuditStatus;

    @ApiModelProperty(value = "0：表示默认设备，1：表示自定义设备")
    private int myDeviceType;

    @ApiModelProperty(value = "表示设备是否选中 0：没有选中，1：选中")
    private int isSelected;

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

    public String getCommunicationMode() {
        return communicationMode;
    }

    public void setCommunicationMode(String communicationMode) {
        this.communicationMode = communicationMode;
    }

    public String getTransmissionMode() {
        return transmissionMode;
    }

    public void setTransmissionMode(String transmissionMode) {
        this.transmissionMode = transmissionMode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getConfigNetMode() {
        return configNetMode;
    }

    public void setConfigNetMode(String configNetMode) {
        this.configNetMode = configNetMode;
    }

    public int getIsKit() {
        return isKit;
    }

    public void setIsKit(int isKit) {
        this.isKit = isKit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsDirectDevice() {
        return isDirectDevice;
    }

    public void setIsDirectDevice(int isDirectDevice) {
        this.isDirectDevice = isDirectDevice;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getDevelopStatus() {
        return developStatus;
    }

    public void setDevelopStatus(int developStatus) {
        this.developStatus = developStatus;
    }

    public long getEnterpriseDevelopId() {
        return enterpriseDevelopId;
    }

    public void setEnterpriseDevelopId(long enterpriseDevelopId) {
        this.enterpriseDevelopId = enterpriseDevelopId;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public int getServiceGooAuditStatus() {
        return serviceGooAuditStatus;
    }

    public void setServiceGooAuditStatus(int serviceGooAuditStatus) {
        this.serviceGooAuditStatus = serviceGooAuditStatus;
    }

    public int getServiceAlxAuditStatus() {
        return serviceAlxAuditStatus;
    }

    public void setServiceAlxAuditStatus(int serviceAlxAuditStatus) {
        this.serviceAlxAuditStatus = serviceAlxAuditStatus;
    }

    public int getMyDeviceType() {
        return myDeviceType;
    }

    public void setMyDeviceType(int myDeviceType) {
        this.myDeviceType = myDeviceType;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }
}

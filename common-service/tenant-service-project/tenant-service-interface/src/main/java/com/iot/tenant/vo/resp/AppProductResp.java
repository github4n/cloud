package com.iot.tenant.vo.resp;

import java.util.List;

/**
 * 描述：app关联产品应答
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/9 19:30
 */
public class AppProductResp {
    private Long id;
    private Long productId;
    private String icon;
    private String deviceTypeIcon;
    private String catalogName;
    private String productName;
    private String deviceTypeName;
    private String deviceType;
    private Integer isDirectDevice;
    private Integer communicationMode;
    private Long deviceTypeId;
    private Long catalogId;
    private Integer catalogOrder;

    private List protocolType; //协议类型
    private String model; //产品型号

    private String configNetMode;

    public Integer getCatalogOrder() {
        return catalogOrder;
    }

    public void setCatalogOrder(Integer catalogOrder) {
        this.catalogOrder = catalogOrder;
    }

    public String getConfigNetMode() {
        return configNetMode;
    }

    public void setConfigNetMode(String configNetMode) {
        this.configNetMode = configNetMode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDeviceTypeIcon() {
        return deviceTypeIcon;
    }

    public void setDeviceTypeIcon(String deviceTypeIcon) {
        this.deviceTypeIcon = deviceTypeIcon;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(List protocolType) {
        this.protocolType = protocolType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getIsDirectDevice() {
        return isDirectDevice;
    }

    public void setIsDirectDevice(Integer isDirectDevice) {
        this.isDirectDevice = isDirectDevice;
    }

    public Integer getCommunicationMode() {
        return communicationMode;
    }

    public void setCommunicationMode(Integer communicationMode) {
        this.communicationMode = communicationMode;
    }

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }
}

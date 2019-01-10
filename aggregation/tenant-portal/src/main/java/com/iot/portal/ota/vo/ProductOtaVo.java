package com.iot.portal.ota.vo;

public class ProductOtaVo {

    /**
     * 产品id
     */
    private String productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * model
     */
    private String model;
    /**
     * 图标
     */
    private String icon;
    /**
     * 容灾升级状态 S：成功 F：失败
     */
    private String upgradeStatus;
    /**
     * 是否是直连设备
     */
    private Integer isDirectDevice;

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUpgradeStatus() {
        return upgradeStatus;
    }

    public void setUpgradeStatus(String upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public Integer getIsDirectDevice() {
        return isDirectDevice;
    }

    public void setIsDirectDevice(Integer isDirectDevice) {
        this.isDirectDevice = isDirectDevice;
    }
}
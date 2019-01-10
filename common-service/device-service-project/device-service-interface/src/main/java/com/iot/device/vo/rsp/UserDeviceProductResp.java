package com.iot.device.vo.rsp;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:41 2018/4/25
 * @Modify by:
 */
public class UserDeviceProductResp implements Serializable {

    private static final long serialVersionUID = 1L;

    private String deviceName;

    private String parentId;

    private String type;

    private String productName;

    private Long deviceTypeId;

    private Long productId;

    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

}

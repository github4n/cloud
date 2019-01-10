package com.iot.device.utils;


import com.iot.device.comm.utils.exceltools.annotation.MapperCell;

import java.io.Serializable;

public class DeviceImportDataVo implements Serializable {

    @MapperCell(cellName = "device_id", order = 0)
    private String deviceId;

    @MapperCell(cellName = "is_direct_device", order = 1)
    private String isDirectDevice;

    @MapperCell(cellName = "product_id", order = 2)
    private String productId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIsDirectDevice() {
        return isDirectDevice;
    }

    public void setIsDirectDevice(String isDirectDevice) {
        this.isDirectDevice = isDirectDevice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
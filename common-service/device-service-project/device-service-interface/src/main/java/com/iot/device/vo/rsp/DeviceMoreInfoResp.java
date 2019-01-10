package com.iot.device.vo.rsp;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:00 2018/5/9
 * @Modify by:
 */
public class DeviceMoreInfoResp implements Serializable {

    private Long productId;

    private String mac;

    private String modelId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
}

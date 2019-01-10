package com.iot.device.vo;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:16 2018/5/21
 * @Modify by:
 */
public class DevOtaVo implements Serializable {

    private String productId;

    private String version;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}

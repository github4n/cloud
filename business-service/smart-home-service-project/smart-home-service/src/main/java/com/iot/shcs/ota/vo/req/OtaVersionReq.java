package com.iot.shcs.ota.vo.req;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:04 2018/5/29
 * @Modify by:
 */
public class OtaVersionReq implements Serializable {

    private List<String> devId;

    private String productId;

    private String devType;

    private String homeId;

    private String timestamp;

    public List<String> getDevId() {
        return devId;
    }

    public void setDevId(List<String> devId) {
        this.devId = devId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

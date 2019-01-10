package com.iot.device.vo.req;

import com.iot.device.vo.rsp.DataPointResp;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 10:44 2018/8/1
 * @Modify by:
 */
public class ProductInfoResp implements Serializable {

    /**
     * 产品名称
     */
    private String productName;


    private String displayName;

    private String model;

    private String devType;

    private Integer networkType;

    private String configNetMode;

    private List<DataPointResp> attrs;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public Integer getNetworkType() {
        return networkType;
    }

    public void setNetworkType(Integer networkType) {
        this.networkType = networkType;
    }

    public String getConfigNetMode() {
        return configNetMode;
    }

    public void setConfigNetMode(String configNetMode) {
        this.configNetMode = configNetMode;
    }

    public List<DataPointResp> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<DataPointResp> attrs) {
        this.attrs = attrs;
    }
}

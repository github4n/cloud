package com.iot.portal.ota.vo;

import com.iot.common.beans.SearchParam;

public class ProductOtaSearchReq extends SearchParam {
    /**
     *  产品名
     */
    private String productName;

    /**
     * model
     */
    private String model;

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
}
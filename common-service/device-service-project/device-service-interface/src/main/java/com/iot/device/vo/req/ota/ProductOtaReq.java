package com.iot.device.vo.req.ota;

import com.iot.common.beans.SearchParam;

public class ProductOtaReq extends SearchParam {
    /**
     *  产品名
     */
    private String productName;
    /**
     * 租户Id
     */
    private Long tenantId;
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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
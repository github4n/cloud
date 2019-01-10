package com.iot.report.dto.resp;

import java.util.Map;

public class DevDistributionResp {
    private String code;
    private Map<Long,Long> productTypeIdAmountMap;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<Long, Long> getProductTypeIdAmountMap() {
        return productTypeIdAmountMap;
    }

    public void setProductTypeIdAmountMap(Map<Long, Long> productTypeIdAmountMap) {
        this.productTypeIdAmountMap = productTypeIdAmountMap;
    }
}

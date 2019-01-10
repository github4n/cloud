package com.iot.report.dto.resp;

public class DevPageAmount {
    private Long productTypeId;
    private Long amount = 0L;

    public DevPageAmount() {
    }

    public DevPageAmount(Long productTypeId, Long amount) {
        this.productTypeId = productTypeId;
        this.amount = amount;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}

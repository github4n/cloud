package com.iot.report.dto.resp;

public class DistributionResp {
    private String countryCode;
    private Long active;
    private Long activate;
    private Long productTypeId;
    private Long amount;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    public Long getActivate() {
        return activate;
    }

    public void setActivate(Long activate) {
        this.activate = activate;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}

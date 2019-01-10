package com.iot.report.dto.resp;

public class DevPageBean {
    private Long productTypeId;
    private String dateField;
    private Long count;

    public DevPageBean() {
    }

    public DevPageBean(Long productTypeId, String dateField, Long count) {
        this.productTypeId = productTypeId;
        this.dateField = dateField;
        this.count = count;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }


    public String getDateField() {
        return dateField;
    }

    public void setDateField(String dateField) {
        this.dateField = dateField;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

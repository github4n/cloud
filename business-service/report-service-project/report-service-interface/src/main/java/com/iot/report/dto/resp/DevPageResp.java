package com.iot.report.dto.resp;

import java.util.List;

public class DevPageResp {

    private String date;
    private Long total = 0L;
    private List<DevPageAmount> amount;

    public DevPageResp(String date, Long total, List<DevPageAmount> amount) {
        this.date = date;
        this.total = total;
        this.amount = amount;
    }

    public DevPageResp() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<DevPageAmount> getAmount() {
        return amount;
    }

    public void setAmount(List<DevPageAmount> amount) {
        this.amount = amount;
    }
}

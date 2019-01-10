package com.iot.boss.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iot.common.beans.SearchParam;

import java.util.Date;

public class RefundListSearch extends SearchParam {

    private String orderId;

    private String tenantId;

    private String userId;

    private int refundStatus;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date refundApplyTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Date getRefundApplyTime() {
        return refundApplyTime;
    }

    public void setRefundApplyTime(Date refundApplyTime) {
        this.refundApplyTime = refundApplyTime;
    }
}

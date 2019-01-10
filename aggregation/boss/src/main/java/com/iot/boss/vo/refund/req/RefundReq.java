package com.iot.boss.vo.refund.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 功能描述：退款入参
 * 创建人： mao2080@sina.com
 * 创建时间：2018/11/14 9:17
 * 修改人： mao2080@sina.com
 * 修改时间：2018/11/14 9:17
 * 修改描述：
 */
@ApiModel(description = "退款入参")
public class RefundReq {

    @ApiModelProperty(name = "refundSum", value = "退款金额", dataType = "BigDecimal")
    private BigDecimal refundSum;

    @ApiModelProperty(name = "orderId", value = "订单id", dataType = "String")
    private String orderId;

    @ApiModelProperty(name = "refundReason", value = "退款原因", dataType = "String")
    private String refundReason;

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    public BigDecimal getRefundSum() {
        return refundSum;
    }

    public void setRefundSum(BigDecimal refundSum) {
        this.refundSum = refundSum;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}

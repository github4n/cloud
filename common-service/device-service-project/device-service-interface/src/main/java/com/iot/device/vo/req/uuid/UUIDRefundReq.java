package com.iot.device.vo.req.uuid;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 功能描述：退款入参
 * 创建人： mao2080@sina.com
 * 创建时间：2018/11/14 9:17
 * 修改人： mao2080@sina.com
 * 修改时间：2018/11/14 9:17
 * 修改描述：
 */
@ApiModel(description = "退款入参")
public class UUIDRefundReq {

    @ApiModelProperty(name = "orderId", value = "订单id", dataType = "String")
    private String orderId;


    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

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

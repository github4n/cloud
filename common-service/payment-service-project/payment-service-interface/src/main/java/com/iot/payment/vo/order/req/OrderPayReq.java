package com.iot.payment.vo.order.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：订单支付入参
 * 创建人： yeshiyuan
 * 创建时间：2018/9/14 14:45
 * 修改人： yeshiyuan
 * 修改时间：2018/9/14 14:45
 * 修改描述：
 */
@ApiModel(description = "订单支付入参")
public class OrderPayReq {

    @ApiModelProperty(name = "orderId", value = "订单id", dataType = "string")
    private String orderId;

    @ApiModelProperty(name = "successUrl", value = "支付成功跳转url", dataType = "string")
    private String successUrl;

    @ApiModelProperty(name = "errorUrl", value = "支付过程出错跳转url", dataType = "string")
    private String errorUrl;

    @ApiModelProperty(name = "cancelUrl", value = "取消url", dataType = "string")
    private String cancelUrl;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }
}

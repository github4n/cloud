package com.iot.payment.vo.pay.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：创建支付结果
 * 创建人： yeshiyuan
 * 创建时间：2018/7/17 17:25
 * 修改人： yeshiyuan
 * 修改时间：2018/7/17 17:25
 * 修改描述：
 */
@ApiModel(description = "创建支付结果")
public class CreatePayResp {

    @ApiModelProperty(name = "orderId", value = "订单id", dataType = "String")
    private String orderId;

    @ApiModelProperty(name = "payUrl", value = "支付url", dataType = "String")
    private String payUrl;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayUrl() {
        return payUrl;
    }


    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public CreatePayResp() {
    }


    public CreatePayResp(String orderId, String payUrl) {
        this.orderId = orderId;
        this.payUrl = payUrl;
    }


}

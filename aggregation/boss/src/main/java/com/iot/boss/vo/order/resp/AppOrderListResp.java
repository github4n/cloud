package com.iot.boss.vo.order.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：app订单列表出参
 * 创建人： yeshiyuan
 * 创建时间：2018/11/14 14:27
 * 修改人： yeshiyuan
 * 修改时间：2018/11/14 14:27
 * 修改描述：
 */
@ApiModel(description = "app订单列表出参")
public class AppOrderListResp {

    @ApiModelProperty(name = "account", value = "账号", dataType = "String")
    private String account;
    @ApiModelProperty(name = "tenantName", value = "租户名称", dataType = "String")
    private String tenantName;
    @ApiModelProperty(name = "appName", value = "app名字", dataType = "String")
    private String appName;
    @ApiModelProperty(name = "totalPrice", value = "订单价格", dataType = "BigDecimal")
    private BigDecimal totalPrice;
    @ApiModelProperty(name = "payWay", value = "支付方式", dataType = "String")
    private String payWay;
    @ApiModelProperty(name = "payTime", value = "支付时间", dataType = "Date")
    private Date payTime;
    @ApiModelProperty(name = "appPackStatus", value = "app打包状态", dataType = "Integer")
    private Integer appPackStatus;
    @ApiModelProperty(name = "payStatus(2：付款成功；4：退款中；5：退款成功；6：退款失败)", value = "支付状态", dataType = "Integer")
    private Integer payStatus;
    @ApiModelProperty(name = "orderId", value = "订单号", dataType = "String")
    private String orderId;
    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getAppPackStatus() {
        return appPackStatus;
    }

    public void setAppPackStatus(Integer appPackStatus) {
        this.appPackStatus = appPackStatus;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
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

    public AppOrderListResp() {
    }


}

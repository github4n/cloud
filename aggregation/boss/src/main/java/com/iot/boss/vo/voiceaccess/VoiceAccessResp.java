package com.iot.boss.vo.voiceaccess;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目名称：IOT云平台
 * 模块名称：订单管理
 * 功能描述：google、alexa订单查询
 * 创建人： wucheng
 * 创建时间：2018-11-14
 */
@ApiModel(value = "语音接入订单信息",description = "语音接入订单信息")
public class VoiceAccessResp {

    @ApiModelProperty(name = "tenantId", value = "租户id")
    private Long tenantId;

    @ApiModelProperty(name = "userId", value = "用户id")
    private Long userId;

    @ApiModelProperty(name = "orderId", value = "订单id")
    private String orderId;

    @ApiModelProperty(name = "totalPrice", value = "总金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(name = "payStatus", value = "支付状态")
    private Integer payStatus;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "accountName", value = "账户名称")
    private String accountName;

    @ApiModelProperty(name = "tenantName", value = "企业名称")
    private String tenantName;

    @ApiModelProperty(name = "serviceType", value = "语音类型（google alexa）")
    private String serviceType;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}

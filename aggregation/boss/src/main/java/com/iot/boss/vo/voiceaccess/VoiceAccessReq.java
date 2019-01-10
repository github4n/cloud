package com.iot.boss.vo.voiceaccess;

import com.iot.common.beans.SearchParam;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：IOT云平台
 * 模块名称：订单管理
 * 功能描述：google、alexa订单查询
 * 创建人： wucheng
 * 创建时间：2018-11-14
 */
public class VoiceAccessReq extends SearchParam{

    @ApiModelProperty(name ="orderId", value = "订单号")
    private String orderId;
    @ApiModelProperty(name ="serviceType", value = "服务类型")
    private Long serviceType;
    @ApiModelProperty(name ="playStatus", value = "订单状态")
    private Integer playStatus;
    @ApiModelProperty(name ="tenantName", value = "企业名")
    private String tenantName;
    @ApiModelProperty(name="accountName", value = "账号名称")
    private String accountName;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getServiceType() {
        return serviceType;
    }

    public void setServiceType(Long serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getPlayStatus() {
        return playStatus;
    }

    public void setPlayStatus(Integer playStatus) {
        this.playStatus = playStatus;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}

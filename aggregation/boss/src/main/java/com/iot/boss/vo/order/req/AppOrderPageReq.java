package com.iot.boss.vo.order.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：app订单分页入参
 * 创建人： yeshiyuan
 * 创建时间：2018/11/14 16:04
 * 修改人： yeshiyuan
 * 修改时间：2018/11/14 16:04
 * 修改描述：
 */
@ApiModel(description = "app订单分页入参")
public class AppOrderPageReq {

    @ApiModelProperty(name = "pageNum", value = "页码", dataType = "Integer")
    private Integer pageNum;

    @ApiModelProperty(name = "pageSize", value = "页大小", dataType = "Integer")
    private Integer pageSize;

    @ApiModelProperty(name = "payStatus(2：付款成功；4：退款中；5：退款成功；6：退款失败)", value = "支付状态", dataType = "Integer")
    private Integer payStatus;

    @ApiModelProperty(name = "appPackStatus", value = "app打包状态", dataType = "Integer")
    private Integer appPackStatus;

    @ApiModelProperty(name = "orderId", value = "订单id", dataType = "String")
    private String orderId;

    @ApiModelProperty(name = "userName", value = "搜索账号", dataType = "String")
    private String userName;

    @ApiModelProperty(name = "tenantName", value = "租户名字", dataType = "String")
    private String tenantName;

    @ApiModelProperty(name = "appName", value = "App名字", dataType = "String")
    private String appName;
    
    public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getAppPackStatus() {
        return appPackStatus;
    }

    public void setAppPackStatus(Integer appPackStatus) {
        this.appPackStatus = appPackStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
}

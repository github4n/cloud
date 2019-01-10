package com.iot.device.vo.req.service;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：虚拟服务购买记录分页入参
 * 创建人： yeshiyuan
 * 创建时间：2018/11/14 14:48
 * 修改人： yeshiyuan
 * 修改时间：2018/11/14 14:48
 * 修改描述：
 */
@ApiModel(description = "虚拟服务购买记录分页入参")
public class ServiceBuyRecordPageReq {

    @ApiModelProperty(name = "tenantIds", value = "租户id集合", dataType = "List")
    private List<Long> tenantIds;
    @ApiModelProperty(name = "payStatus", value = "支付状态", dataType = "Integer")
    private Integer payStatus;
    @ApiModelProperty(name = "serviceIds", value = "虚拟服务id（产品Id，appId）", dataType = "List")
    private List<Long> serviceIds;

    @ApiModelProperty(name = "pageNum", value = "页码", dataType = "Integer")
    private Integer pageNum;

    @ApiModelProperty(name = "pageSize", value = "页大小", dataType = "Integer")
    private Integer pageSize;

    @ApiModelProperty(name = "goodsTypeId", value = "商品类型id", dataType = "Integer")
    private Integer goodsTypeId;


    @ApiModelProperty(name = "orderId", value = "订单id", dataType = "String")
    private String orderId;

    public Integer getPageNum() {
        if (pageNum == null || pageNum==0) {
            pageNum = 1;
        }
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        if (pageSize == null || pageSize==0) {
            pageSize = 20;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<Long> getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(List<Long> tenantIds) {
        this.tenantIds = tenantIds;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public List<Long> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<Long> serviceIds) {
        this.serviceIds = serviceIds;
    }

    public Integer getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(Integer goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

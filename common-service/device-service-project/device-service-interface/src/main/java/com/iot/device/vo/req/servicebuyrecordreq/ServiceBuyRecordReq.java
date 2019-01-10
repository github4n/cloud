package com.iot.device.vo.req.servicebuyrecordreq;

import com.iot.common.beans.SearchParam;
import io.swagger.annotations.ApiImplicitParam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： wucheng
 * 创建时间：2018-11-14
 */
public class ServiceBuyRecordReq extends SearchParam implements Serializable{

    /**
     * 支付状态（1:待付款；2：已付款）
     */
    private Integer payStatus;
    /**
     * googl、alexa 对应的good_info的ids
     */
    private List<Long> goodsIds;
    /**
     * 租户id集合
     */
    private List<Long> tenantIds;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 订单号
     */
    private String orderId;

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public List<Long> getGoodsIds() {
        return goodsIds;
    }

    public void setGoodsIds(List<Long> goodsIds) {
        this.goodsIds = goodsIds;
    }

    public List<Long> getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(List<Long> tenantIds) {
        this.tenantIds = tenantIds;
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
}

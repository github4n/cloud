package com.iot.device.vo.rsp;

import java.util.Date;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：虚拟服务购买记录
 * 创建人： maochengyuan
 * 创建时间：2018/9/13 17:44
 * 修改人： maochengyuan
 * 修改时间：2018/9/13 17:44
 * 修改描述：
 */
public class ServiceBuyRecordResp {

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 订单id，对应order_record表主键
     */
    private String orderId;

    /**
     * 虚拟服务id（产品Id，appId）
     */
    private Long serviceId;

    /**
     * 商品id，对应goods_info主键
     */
    private Long goodsId;

    /**
     * 购买数量
     */
    private Integer goodsNum;

    /**
     * 支付状态（1:待付款；2：已付款）
     */
    private Integer payStatus;

    /**
     * 附加需求描述
     */
    private String addDemandDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getAddDemandDesc() {
        return addDemandDesc;
    }

    public void setAddDemandDesc(String addDemandDesc) {
        this.addDemandDesc = addDemandDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

package com.iot.video.vo;

import java.util.Date;

/**
 * 项目名称：立达信IOT视频云
 * 模块名称：聚合层
 * 功能描述：购买历史查询结果VO
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/26 16:56
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/26 16:56
 * 修改描述：
 */
public class BuyHisRecordListVO {

    /**
     * 购买数量
     */
    private int counts;

    /**
     * 货币代码
     */
    private String currency;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 套餐id
     */
    private String packageId;

    /**
     * 套餐名称
     */
    private String packageName;

    /**
     * 套餐价格
     */
    private Double packagePrice;

    /**
     * 支付金额
     */
    private Double payPrice;

    /**
     * 支付记录id
     */
    private String payRecordId;

    /**
     * 支付状态
     */
    private String payStatus;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付类型
     */
    private String payType;

    /**
     * 购买结束时间
     */
    private Date planStartTime;

    /**
     * 购买开始时间
     */
    private Date planEndTime;

    /**
     * 计划id
     */
    private String planId;

    /**
     * 退款状态
     */
    private String refundStatus;

    /**
     * 计划状态
     */
    private String planStatus;
    
    private String renewMark;

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Double getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(Double packagePrice) {
        this.packagePrice = packagePrice;
    }

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }

    public String getPayRecordId() {
        return payRecordId;
    }

    public void setPayRecordId(String payRecordId) {
        this.payRecordId = payRecordId;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Date getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }

    public Date getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public String getRenewMark() {
        return renewMark;
    }

    public void setRenewMark(String renewMark) {
        this.renewMark = renewMark;
    }

}

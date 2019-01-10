package com.iot.video.vo;

import io.swagger.annotations.ApiModel;

import java.util.Date;

@ApiModel
public class VideoPlanVo {

    /**
     * 计划id
     */
    private String planId;

    /**
     * 套餐id
     */
    private String packageId;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 计划开始时间
     */
    private Date planStartTime;

    /**
     * 计划结束时间
     */
    private Date planEndTime;

    /**
     * 执行周期
     */
    private String planCycle;

    /**
     * 时程开关
     */
    private String planStatus;

    /**
     * 设备名字
     */
    private String deviceName;

    /**
     * 套餐名称
     */
    private String packageName;

    /**
     * 排序字段
     */
    private int planOrder;

    /**
     * 计划执行状态
     */
    private String planExecStatus;

    /**
     * 设备状态
     */
    private String deviceStatus;

    /**
     * 过期状态
     */
    private String overdueStatus;

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 套餐类型,0-全时,1-事件
     */
    private String packageType;

    /**
     * 事件或全时量
     */
    private Integer eventOrFulltimeAmount;

    /**
     * 设备在线状态
     */
    private String onlineStatus;

    /**
     * 续费标识:1-可续费，0-不可续费
     */
    private String renewMark;

    /**
     * 套餐使用量
     */
    private Integer useQuantity;

    /**
     * 套餐价格
     */
    private Double packagePrice;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public String getPlanCycle() {
        return planCycle;
    }

    public void setPlanCycle(String planCycle) {
        this.planCycle = planCycle;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getPlanOrder() {
        return planOrder;
    }

    public void setPlanOrder(int planOrder) {
        this.planOrder = planOrder;
    }

    public String getPlanExecStatus() {
        return planExecStatus;
    }

    public void setPlanExecStatus(String planExecStatus) {
        this.planExecStatus = planExecStatus;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getOverdueStatus() {
        return overdueStatus;
    }

    public void setOverdueStatus(String overdueStatus) {
        this.overdueStatus = overdueStatus;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public Integer getEventOrFulltimeAmount() {
        return eventOrFulltimeAmount;
    }

    public void setEventOrFulltimeAmount(Integer eventOrFulltimeAmount) {
        this.eventOrFulltimeAmount = eventOrFulltimeAmount;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getRenewMark() {
        return renewMark;
    }

    public void setRenewMark(String renewMark) {
        this.renewMark = renewMark;
    }

    public Integer getUseQuantity() {
        return useQuantity;
    }

    public void setUseQuantity(Integer useQuantity) {
        this.useQuantity = useQuantity;
    }

    public Double getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(Double packagePrice) {
        this.packagePrice = packagePrice;
    }

}

package com.iot.video.vo;

import io.swagger.annotations.ApiModel;

@ApiModel
public class VideoPackageVo {

    /**
     * 套餐id
     */
    private String packageId;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 套餐名称
     */
    private String packageName;

    /**
     * 套餐描述
     */
    private String packageDesc;

    /**
     * 套餐价格
     */
    private String packagePrice;

    /**
     * 货币代码
     */
    private String currency;

    /**
     * 套餐类型,0-全时录影，1-事件录影
     */
    private String packageType;

    /**
     * 事件或全时量
     */
    private int eventOrFulltimeAmount;

    /**
     * 套餐名称描述
     */
    private String packageNameDesc;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageDesc() {
        return packageDesc;
    }

    public void setPackageDesc(String packageDesc) {
        this.packageDesc = packageDesc;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public int getEventOrFulltimeAmount() {
        return eventOrFulltimeAmount;
    }

    public void setEventOrFulltimeAmount(int eventOrFulltimeAmount) {
        this.eventOrFulltimeAmount = eventOrFulltimeAmount;
    }

    public String getPackageNameDesc() {
        return packageNameDesc;
    }

    public void setPackageNameDesc(String packageNameDesc) {
        this.packageNameDesc = packageNameDesc;
    }

    public VideoPackageVo() {
    }

    public VideoPackageVo(String deviceType, String packageName, String packageDesc, String packagePrice, String currency, String packageType, int eventOrFulltimeAmount, String packageNameDesc) {
        this.deviceType = deviceType;
        this.packageName = packageName;
        this.packageDesc = packageDesc;
        this.packagePrice = packagePrice;
        this.currency = currency;
        this.packageType = packageType;
        this.eventOrFulltimeAmount = eventOrFulltimeAmount;
        this.packageNameDesc = packageNameDesc;
    }
}

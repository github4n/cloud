package com.iot.video.vo;

import io.swagger.annotations.ApiModel;

@ApiModel
public class AppPlanVo {

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 计划id
     */
    private String planId;

    /**
     * 套餐id
     */
    private String packageId;

    /**
     * 购买数量
     */
    private Integer counts;

    /**
     * 支付金额
     */
    private Double payPrice;

    /**
     * 货币代码
     */
    private String currency;

    private String packageName;

    private String payId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

}

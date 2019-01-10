package com.iot.device.vo.rsp;

/**
 * @Author: Xieby
 * @Date: 2018/11/6
 * @Description: *
 */
public class DailyElectricityStatisticsResp {

    private String id;

    private Long tenantId;

    private String deviceId;

    private Long userId;

    private Double electricValue;

    private String day;

    private Long orgId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getElectricValue() {
        return electricValue;
    }

    public void setElectricValue(Double electricValue) {
        this.electricValue = electricValue;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}

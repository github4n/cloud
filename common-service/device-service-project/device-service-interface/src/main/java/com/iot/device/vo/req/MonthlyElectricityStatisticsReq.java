package com.iot.device.vo.req;

import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/11/6
 * @Description: *
 */
public class MonthlyElectricityStatisticsReq {

    private Long tenantId;

    private String deviceId;

    private Long userId;

    private Integer year;

    private Integer month;

    private Long orgId;

    private Double electricValue;

    private List<String> deviceIds;

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Double getElectricValue() {
        return electricValue;
    }

    public void setElectricValue(Double electricValue) {
        this.electricValue = electricValue;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }
}

package com.iot.device.vo.req;

import java.util.Date;
import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/11/6
 * @Description: *
 */
public class DailyElectricityStatisticsReq {

    private Long tenantId;

    private String deviceId;

    private Long userId;

    private Double electricValue;

    private Date day;

    private String dayStr;

    private Long orgId;

    private String monthPrefix;

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

    public Double getElectricValue() {
        return electricValue;
    }

    public void setElectricValue(Double electricValue) {
        this.electricValue = electricValue;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getMonthPrefix() {
        return monthPrefix;
    }

    public void setMonthPrefix(String monthPrefix) {
        this.monthPrefix = monthPrefix;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public String getDayStr() {
        return dayStr;
    }

    public void setDayStr(String dayStr) {
        this.dayStr = dayStr;
    }
}

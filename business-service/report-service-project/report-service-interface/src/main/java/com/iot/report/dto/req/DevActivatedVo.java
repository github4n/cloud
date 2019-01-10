package com.iot.report.dto.req;

import java.util.Date;

public class DevActivatedVo {
    private String devId;
    private Date time;
    private String parentDevId;
    private Long deviceTypeId;

    public DevActivatedVo() {
    }

    public DevActivatedVo(String devId, Date time) {
        this.devId = devId;
        this.time = time;
    }

    public DevActivatedVo(String devId, Date time, String parentDevId) {
        this.devId = devId;
        this.time = time;
        this.parentDevId = parentDevId;
    }

    public DevActivatedVo(String devId, Date time, String parentDevId, Long deviceTypeId) {
        this.devId = devId;
        this.time = time;
        this.parentDevId = parentDevId;
        this.deviceTypeId = deviceTypeId;
    }

    public DevActivatedVo(String devId, Date time, Long deviceTypeId) {
        this.devId = devId;
        this.time = time;
        this.deviceTypeId = deviceTypeId;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getParentDevId() {
        return parentDevId;
    }

    public void setParentDevId(String parentDevId) {
        this.parentDevId = parentDevId;
    }

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }
}

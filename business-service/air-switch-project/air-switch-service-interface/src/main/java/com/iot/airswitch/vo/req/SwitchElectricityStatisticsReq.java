package com.iot.airswitch.vo.req;

import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/11/7
 * @Description: *
 */
public class SwitchElectricityStatisticsReq {

    /**
     * type : hour,day,month
     */
    private String type;

    private Long spaceId;

    private Long tenantId;

    private Long buildId;

    private Long floorId;

    private String date;

    private Integer top;

    /**
     * 1. 预警
     * 2. 告警
     */
    private Integer alarmType;

    private List<String> deviceIds;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getBuildId() {
        return buildId;
    }

    public void setBuildId(Long buildId) {
        this.buildId = buildId;
    }

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
    }
}

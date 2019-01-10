package com.iot.video.dto;

import io.swagger.annotations.ApiModel;

import java.util.Date;
import java.util.List;

/**
 * 项目名称：IOT视频云
 * 模块名称：服务层
 * 功能描述：事件查询参数VO
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/23 15:18
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/23 15:18
 * 修改描述：
 */
@ApiModel
public class EventParamDto extends BaseDto {

    /**计划id*/
    private String planId;

    /**事件代码*/
    private String eventCode;

    /**设备id*/
    private String deviceId;

    /**页码*/
    private int pageNum;

    /**每页数目*/
    private int pageSize;

    /** 事件开始时间 */
    private Date eventStartTime;

    /** 事件结束时间 */
    private Date eventEndTime;

    private List<String> eventCodeList;

    public EventParamDto() {

    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        if (0 == pageSize) {
            pageSize=10;
        }
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<String> getEventCodeList() {
        return eventCodeList;
    }

    public void setEventCodeList(List<String> eventCodeList) {
        this.eventCodeList = eventCodeList;
    }

    public Date getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(Date eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public Date getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(Date eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    @Override
    public String toString() {
        return "EventParamDto{" +
                "planId='" + planId + '\'' +
                ", eventCode='" + eventCode + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", eventStartTime=" + eventStartTime +
                ", eventEndTime=" + eventEndTime +
                ", eventCodeList=" + eventCodeList +
                '}';
    }
}

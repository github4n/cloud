package com.iot.video.vo;

import com.iot.vo.AggBaseVO;
import io.swagger.annotations.ApiModel;

import java.util.Date;

/**
 * 项目名称：立达信IOT视频云
 * 模块名称：聚合层
 * 功能描述：事件查询参数VO
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/23 15:18
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/23 15:18
 * 修改描述：
 */
@ApiModel
public class EventParamVO extends AggBaseVO {

    /**
     * 计划id
     */
    private String planId;

    /**
     * 事件代码
     */
    private String eventCode;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 页码
     */
    private int pageNum;

    /**
     * 每页数目
     */
    private int pageSize;

    /**
     * 事件开始时间
     */
    private Date eventStartTime;

    /**
     * 事件结束时间
     */
    private Date eventEndTime;

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
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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
}

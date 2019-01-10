package com.iot.video.vo;

import java.util.Date;

/**
 * 项目名称：立达信IOT视频云
 * 模块名称：聚合层
 * 功能描述：视频事件出参VO
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/23 11:34
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/23 11:34
 * 修改描述：
 */
public class VideoEventVO {

    /**
     * 事件id
     */
    private String eventId;

    /**
     * 计划id
     */
    private String planId;

    /**
     * 事件代码
     */
    private String eventCode;

    /**
     * 事件描述
     */
    private String eventDesc;

    /**
     * 事件触发事件
     */
    private Date eventOddurTime;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public Date getEventOddurTime() {
        return eventOddurTime;
    }

    public void setEventOddurTime(Date eventOddurTime) {
        this.eventOddurTime = eventOddurTime;
    }

}

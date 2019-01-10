package com.iot.video.vo;

import java.util.Date;

/**
 * 项目名称：立达信IOT视频云
 * 模块名称：聚合层
 * 功能描述：事件VO
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/23 15:18
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/23 15:18
 * 修改描述：
 */
public class EventVO {

    /**
     * 事件id
     */
    private String eventId;

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

    /**
     * 图片url
     */
    private String url;

    /**
     * 图片文件id
     */
    private String fileId;

    /**
     * 旋转角度
     */
    private Integer rotation;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }
}

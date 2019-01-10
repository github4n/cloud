package com.iot.video.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述： 获取事件录影文件入参
 * 创建人： yeshiyuan
 * 创建时间：2018/8/29 14:59
 * 修改人： yeshiyuan
 * 修改时间：2018/8/29 14:59
 * 修改描述：
 */
@ApiModel(value = "获取事件录影文件入参")
public class GetEventVideoFileReq {

    @ApiModelProperty(name = "planId", value = "计划id", dataType = "String")
    private String planId;

    @ApiModelProperty(name = "eventId", value = "事件uuid", dataType = "String")
    private String eventId;

    @ApiModelProperty(name = "fileType", value = "文件类型", dataType = "String")
    private String fileType;

    @ApiModelProperty(name = "eventOddurTime", value = "事件发生时间", dataType = "Date")
    private Date eventOddurTime;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Date getEventOddurTime() {
        return eventOddurTime;
    }

    public void setEventOddurTime(Date eventOddurTime) {
        this.eventOddurTime = eventOddurTime;
    }

    public GetEventVideoFileReq() {
    }

    public GetEventVideoFileReq(String planId, String eventId, String fileType, Date eventOddurTime) {
        this.planId = planId;
        this.eventId = eventId;
        this.fileType = fileType;
        this.eventOddurTime = eventOddurTime;
    }
}

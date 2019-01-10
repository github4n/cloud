package com.iot.video.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;

/**
 * 项目名称：立达信IOT视频云
 * 模块名称：服务层
 * 功能描述：视频事件查询参数VO
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/23 11:34
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/23 11:34
 * 修改描述：
 */
@ApiModel
public class VideoEventParamVo extends BaseVo{

    /**计划ID*/
    private String planId;

    /**设备ID*/
    private String deviceId;

    /**开始时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;

    /**截止时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;

    public VideoEventParamVo() {

    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}

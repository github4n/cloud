package com.iot.video.timer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 项目名称：cloud
 * 功能描述：定时任务时间配置
 * 创建人： yeshiyuan
 * 创建时间：2018/6/1 10:44
 * 修改人： yeshiyuan
 * 修改时间：2018/6/1 10:44
 * 修改描述：
 */
@Configuration
@ConfigurationProperties(prefix = "job.time")
public class JobTimeConfig {

    /**
     * 修改视频事件
     */
    private String updateEvent;


    private String updateHour;

    /**
     * 修改计划状态
     */
    private String updatePlan;

    /**
     * IPC解绑删除垃圾数据
     */
    private String dealInvalidVideoData;

    /**
     * #处理未上报信息的文件
     */
    private String dealUnUploadFileInfo;

    public String getUpdateEvent() {
        return updateEvent;
    }

    public void setUpdateEvent(String updateEvent) {
        this.updateEvent = updateEvent;
    }

    public String getUpdateHour() {
        return updateHour;
    }

    public void setUpdateHour(String updateHour) {
        this.updateHour = updateHour;
    }

    public String getUpdatePlan() {
        return updatePlan;
    }

    public void setUpdatePlan(String updatePlan) {
        this.updatePlan = updatePlan;
    }

    public String getDealInvalidVideoData() {
        return dealInvalidVideoData;
    }

    public void setDealInvalidVideoData(String dealInvalidVideoData) {
        this.dealInvalidVideoData = dealInvalidVideoData;
    }

    public String getDealUnUploadFileInfo() {
        return dealUnUploadFileInfo;
    }

    public void setDealUnUploadFileInfo(String dealUnUploadFileInfo) {
        this.dealUnUploadFileInfo = dealUnUploadFileInfo;
    }
}

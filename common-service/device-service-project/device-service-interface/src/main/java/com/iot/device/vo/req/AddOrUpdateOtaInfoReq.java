package com.iot.device.vo.req;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:38 2018/5/2
 * @Modify by:
 */
public class AddOrUpdateOtaInfoReq implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 版本id
     * version_id   extends device_version  id
     */
    private Long versionId;

    /**
     * 设备id
     * device_id   extends device uuid
     */
    private String deviceId;
    /**
     * 用户id
     * user_id
     */
    private Long userId;

    /**
     * 当前用户选择的升级模式
     * 0：普通升级 1：静默升级
     */
    private Integer mode;

    /**
     * 阶段 空闲 ==0    正在下载 ==1  下载完成==2   等待升级==3   正在安装==4    完成安装(重启)==5    升级失败==6    设备在忙==7
     * ota上报状态
     * stage
     */
    private Integer stage;
    /**
     * 百分比
     */
    private Integer percent;
    /**
     * 描述
     */
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
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

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

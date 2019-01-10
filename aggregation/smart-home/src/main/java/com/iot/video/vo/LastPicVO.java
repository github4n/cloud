package com.iot.video.vo;

/**
 * 项目名称：立达信IOT视频云
 * 模块名称：聚合层
 * 功能描述：视频最后一帧VO
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/23 15:18
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/23 15:18
 * 修改描述：
 */
public class LastPicVO {

    /**
     * 计划id
     */
    private String planId;

    /**
     * 最后一帧图片URL
     */
    private String planLastPicUrl;

    private String deviceId;
    
    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanLastPicUrl() {
        return planLastPicUrl;
    }

    public void setPlanLastPicUrl(String planLastPicUrl) {
        this.planLastPicUrl = planLastPicUrl;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public LastPicVO(String planId, String planLastPicUrl, String deviceId) {
        this.planId = planId;
        this.planLastPicUrl = planLastPicUrl;
        this.deviceId = deviceId;
    }
}

package com.iot.video.vo;

import com.iot.vo.AggBaseVO;
import io.swagger.annotations.ApiModel;

import java.util.Date;

/**
 * 项目名称：立达信IOT视频云
 * 模块名称：聚合层
 * 功能描述：视频文件查询参数VO
 * 创建人： mao2080@sina.com
 * 创建时间：2018/3/23 14:24
 * 修改人： mao2080@sina.com
 * 修改时间：2018/3/23 14:24
 * 修改描述：
 */
@ApiModel
public class VideoFileParamVO extends AggBaseVO {

    /**
     * 计划id
     */
    private String planId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 是否需要url
     */
    private boolean needUrl;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isNeedUrl() {
        return needUrl;
    }

    public void setNeedUrl(boolean needUrl) {
        this.needUrl = needUrl;
    }
}

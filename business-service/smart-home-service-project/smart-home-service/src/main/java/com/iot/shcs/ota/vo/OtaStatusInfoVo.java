package com.iot.shcs.ota.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 16:07 2018/5/2
 * @Modify by:
 */
public class OtaStatusInfoVo implements Serializable {

    private String devId;

    private Integer fwType;

    private Integer stage;

    private Integer percent;

    private String version;

    private Date timestamp;

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public Integer getFwType() {
        return fwType;
    }

    public void setFwType(Integer fwType) {
        this.fwType = fwType;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

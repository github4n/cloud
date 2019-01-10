package com.iot.shcs.ota.vo.req;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 13:57 2018/5/29
 * @Modify by:
 */
public class OtaUpdateStatusReq implements Serializable {

    private String devId;

    private String msg;//内容

    private Integer stage;

    private Integer fwType = 0;

    private Integer percent;

    private String version;

    private String timestamp;

    public OtaUpdateStatusReq() {
    }

    public OtaUpdateStatusReq(String devId, String msg, Integer stage, Integer fwType, Integer percent, String version, String timestamp) {
        this.devId = devId;
        this.msg = msg;
        this.stage = stage;
        this.fwType = fwType;
        this.percent = percent;
        this.version = version;
        this.timestamp = timestamp;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getFwType() {
        if (fwType == null) {
            fwType = 0;//默认分位 添加的版本也是这个
        }
        return fwType;
    }

    public void setFwType(Integer fwType) {
        this.fwType = fwType;
    }

    public Integer getPercent() {
        if (percent == null) {
            percent = 0;
        }
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

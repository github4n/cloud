package com.iot.shcs.ota.vo.resp;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 9:54 2018/5/29
 * @Modify by:
 */
public class OtaVersionResp implements Serializable {

    private String oldVersion;

    private String newVersion;

    private String devId;

    private Integer stage;

    private Integer percent;

    private Integer fwType;

    private String msg;

    public OtaVersionResp(String oldVersion, String newVersion, String devId, Integer stage, Integer percent, Integer fwType, String msg) {
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
        this.devId = devId;
        this.stage = stage;
        this.percent = percent;
        this.fwType = fwType;
        this.msg = msg;
    }

    public String getOldVersion() {
        return oldVersion;
    }

    public void setOldVersion(String oldVersion) {
        this.oldVersion = oldVersion;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
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

    public Integer getFwType() {
        return fwType;
    }

    public void setFwType(Integer fwType) {
        this.fwType = fwType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

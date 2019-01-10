package com.iot.shcs.ota.vo.req;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:10 2018/5/29
 * @Modify by:
 */
public class OtaUpdateVersionInfoReq implements Serializable {

    private String devId;

    private String version;

    private Integer fwType;

    private Integer stage;

    private String msg;

    public OtaUpdateVersionInfoReq() {
    }

    public OtaUpdateVersionInfoReq(String devId, String version, Integer fwType) {
        this.devId = devId;
        this.version = version;
        this.fwType = fwType;
    }

    public OtaUpdateVersionInfoReq(String devId, String version, Integer fwType, Integer stage, String msg) {
        this.devId = devId;
        this.version = version;
        this.fwType = fwType;
        this.stage = stage;
        this.msg = msg;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

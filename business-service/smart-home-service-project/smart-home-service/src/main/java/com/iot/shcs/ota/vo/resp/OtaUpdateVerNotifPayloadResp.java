package com.iot.shcs.ota.vo.resp;

import com.iot.shcs.ota.vo.BasePayload;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:14 2018/5/29
 * @Modify by:
 */
public class OtaUpdateVerNotifPayloadResp extends BasePayload implements Serializable {

    private String devId;

    private String version;

    private String name;

    public OtaUpdateVerNotifPayloadResp() {
    }

    public OtaUpdateVerNotifPayloadResp(String devId, String version, String name) {
        this.devId = devId;
        this.version = version;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

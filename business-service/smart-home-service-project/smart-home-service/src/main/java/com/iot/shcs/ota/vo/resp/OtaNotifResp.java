package com.iot.shcs.ota.vo.resp;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 9:42 2018/5/29
 * @Modify by:
 */
public class OtaNotifResp implements Serializable {

    private List<String> devId;

    private Integer fwType;

    private String version;

    private String md5;

    private String url;

    private Integer mode;


    public OtaNotifResp(List<String> devId, Integer fwType, String version, String md5, String url, Integer mode) {
        this.devId = devId;
        this.fwType = fwType;
        this.version = version;
        this.md5 = md5;
        this.url = url;
        this.mode = mode;
    }

    public List<String> getDevId() {
        return devId;
    }

    public void setDevId(List<String> devId) {
        this.devId = devId;
    }

    public Integer getFwType() {
        return fwType;
    }

    public void setFwType(Integer fwType) {
        this.fwType = fwType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }
}

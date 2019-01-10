package com.iot.device.vo.rsp.ota;

import java.io.Serializable;

/**
 * @Author: nongchongwei
 * @Descrpiton:
 * @Date: 13:57 2018/4/17
 * @Modify by:
 */
public class SubOtaDeviceInfo implements Serializable {

    /**
     * 设备id
     */
    private String devId;
    /**
     * 父id
     */
    private String pdevId;
    /**
     * 版本
     */
    private String ver;

    public SubOtaDeviceInfo() {

    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getPdevId() {
        return pdevId;
    }

    public void setPdevId(String pdevId) {
        this.pdevId = pdevId;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

}

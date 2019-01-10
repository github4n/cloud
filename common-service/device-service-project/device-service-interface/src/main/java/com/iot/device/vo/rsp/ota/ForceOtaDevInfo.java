package com.iot.device.vo.rsp.ota;

/**
 * 项目名称：IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： 490485964@qq.com
 * 创建时间：2018年08月01日 11:06
 * 修改人： 490485964@qq.com
 * 修改时间：2018年08月01日 11:06
 */
public class ForceOtaDevInfo {
    /**
     * 设备Id
     */
    private String devId;
    /**
     * 版本号
     */
    private String ver;

    public ForceOtaDevInfo() {

    }

    public ForceOtaDevInfo(String devId, String ver) {
        this.devId = devId;
        this.ver = ver;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }
}

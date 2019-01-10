package com.iot.device.vo.rsp.ota;

import java.io.Serializable;

/**
 * @Author: nongchongwei
 * @Descrpiton:
 * @Date: 13:57 2018/4/17
 * @Modify by:
 */
public class OtaDeviceInfo implements Serializable {

    /**
     * 设备id
     */
    private String devId;
    /**
     * 父id
     */
    private String pdevId;

    /**
     * 设备名称
     */
    private String devName;

    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 版本
     */
    private String ver;

    /**
     * 租户ID
     * tenant_id
     */
    private Long tenantId;


    public OtaDeviceInfo() {

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

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}

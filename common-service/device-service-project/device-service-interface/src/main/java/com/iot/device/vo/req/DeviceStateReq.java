package com.iot.device.vo.req;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 16:54 2018/4/24
 * @Modify by:
 */
public class DeviceStateReq implements Serializable {


    private Long tenantId;

    private String deviceId;

    private boolean removeCache = false;

    private List<DeviceStateInfoReq> deviceStateInfoReqList;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isRemoveCache() {
        return removeCache;
    }

    public void setRemoveCache(boolean removeCache) {
        this.removeCache = removeCache;
    }

    public List<DeviceStateInfoReq> getDeviceStateInfoReqList() {
        return deviceStateInfoReqList;
    }

    public void setDeviceStateInfoReqList(List<DeviceStateInfoReq> deviceStateInfoReqList) {
        this.deviceStateInfoReqList = deviceStateInfoReqList;
    }
}

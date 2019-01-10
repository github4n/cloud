package com.iot.robot.vo.conrad;

import java.io.Serializable;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/11/28 15:01
 * @Modify by:
 */
public class ConradEndpoint implements Serializable {

    private static final long serialVersionUID = 2025580783894328456L;

    private String device;
    private String deviceVersion;
    private String id;
    private String lastSyncTime;
    // 设备类型
    private String type;


    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(String lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

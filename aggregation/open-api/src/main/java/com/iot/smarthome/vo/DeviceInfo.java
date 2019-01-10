package com.iot.smarthome.vo;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DeviceInfo implements Serializable {

    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备分类code(deviceClassifyCode)
     */
    private String type;

    /**
     * 设备版本
     */
    private String deviceVersion;

    /**
     *  设备支持的属性
     */
    private List<Map<String, Object>> attrList;


    public DeviceInfo() {
        attrList = Lists.newArrayList();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public List<Map<String, Object>> getAttrList() {
        return attrList;
    }

    public void addAttrMap(Map<String, Object> attrMap) {
        this.attrList.add(attrMap);
    }
}

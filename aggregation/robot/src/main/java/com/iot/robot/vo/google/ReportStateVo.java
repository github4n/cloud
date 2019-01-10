package com.iot.robot.vo.google;

import java.util.Map;

/**
 * @Descrpiton: 封装 单个设备 需要 上报状态的信息
 * @Author: yuChangXing
 * @Date: 2018/8/21 14:42
 * @Modify by:
 */
public class ReportStateVo {
    // 设备id
    private String deviceId;
    // 设备状态
    private Map<String, Object> attrMap;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Map<String, Object> getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map<String, Object> attrMap) {
        this.attrMap = attrMap;
    }
}

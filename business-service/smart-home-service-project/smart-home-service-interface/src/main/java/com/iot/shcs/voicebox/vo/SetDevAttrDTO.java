package com.iot.shcs.voicebox.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * @Descrpiton: 封装 控制设备属性
 * @Author: yuChangXing
 * @Date: 2018/10/10 19:03
 * @Modify by:
 */
public class SetDevAttrDTO implements Serializable {

    /**
     * 序列
     */
    private static final long serialVersionUID = -8965296513558252955L;

    private String deviceId;

    private Map<String, Object> payloadMap;

    private Map<String, Object> attr;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Map<String, Object> getPayloadMap() {
        return payloadMap;
    }

    public void setPayloadMap(Map<String, Object> payloadMap) {
        this.payloadMap = payloadMap;
    }

    public Map<String, Object> getAttr() {
        return attr;
    }

    public void setAttr(Map<String, Object> attr) {
        this.attr = attr;
    }
}

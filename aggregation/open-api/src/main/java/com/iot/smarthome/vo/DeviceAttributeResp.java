package com.iot.smarthome.vo;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Descrpiton: 获取设备最新属性值 返回的数据
 * @Author: yuChangXing
 * @Date: 2018/12/14 15:10
 * @Modify by:
 */
public class DeviceAttributeResp extends Resp implements Serializable {
    private static final long serialVersionUID = 2025580783894328456L;

    private String deviceId;

    private Map<String, Object> attrMap;

    public DeviceAttributeResp() {
    }

    public Map<String, Object> getAttrMap() {
        return attrMap == null ? Maps.newHashMap() : attrMap;
    }

    public void setAttrMap(Map<String, Object> attrMap) {
        this.attrMap = attrMap;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public Map<String, Object> getPayload() {
        return payload;
    }

    @Override
    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    @Override
    public Map<String, Object> buildMsg() {
        payload.put("deviceId", getDeviceId());
        payload.put("attr", getAttrMap());

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("payload", getPayload());
        return resultMap;
    }

    public static void main(String[] args) {
        Map<String, Object> attrMap = Maps.newHashMap();
        attrMap.put("OnOff", 1);
        attrMap.put("CCT", 4500);
        attrMap.put("RGBW", 100);

        DeviceAttributeResp deviceAttributeResp = new DeviceAttributeResp();
        deviceAttributeResp.setAttrMap(attrMap);
        deviceAttributeResp.setDeviceId("001");

        Map<String, Object> resultMap = deviceAttributeResp.buildMsg();
        JSONObject resultJsonObj = new JSONObject(resultMap);
        System.out.println(resultJsonObj);
    }
}

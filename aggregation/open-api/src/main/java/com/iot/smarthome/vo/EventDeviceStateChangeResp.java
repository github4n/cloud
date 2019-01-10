package com.iot.smarthome.vo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.iot.smarthome.util.DateTimeUtil;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/19 14:52
 * @Modify by:
 */
public class EventDeviceStateChangeResp extends Resp implements Serializable {
    private static final long serialVersionUID = 2025580783894328456L;

    private String deviceId;
    private Map<String, Object> changeAttr;

    @Override
    public Map<String, Object> getPayload() {
        return payload;
    }

    @Override
    public void setPayload(Map<String, Object> payload) {

    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Map<String, Object> getChangeAttr() {
        return changeAttr;
    }

    public void setChangeAttr(Map<String, Object> changeAttr) {
        this.changeAttr = changeAttr;
    }

    @Override
    public Map<String, Object> buildMsg() {
        Map<String, Object> header = Maps.newHashMap();
        header.put("messageId", UUID.randomUUID().toString());
        header.put("event", "device.state.change");
        header.put("time", DateTimeUtil.getNowDateTime());

        payload.put("header", header);

        Map<String, Object> device = Maps.newHashMap();
        device.put("deviceId", getDeviceId());
        device.put("changeAttr", getChangeAttr());
        payload.put("device", device);

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("payload", getPayload());
        return resultMap;
    }

    public static void main(String[] args) {
        Map<String, Object> attrMap = Maps.newHashMap();
        attrMap.put("OnOff", 1);

        EventDeviceStateChangeResp stateChangeResp = new EventDeviceStateChangeResp();
        stateChangeResp.setDeviceId("1001");
        stateChangeResp.setChangeAttr(attrMap);

        Map<String, Object> resultMap = stateChangeResp.buildMsg();
        String jsonContent = JSON.toJSONString(resultMap);
        System.out.println(jsonContent);
    }
}

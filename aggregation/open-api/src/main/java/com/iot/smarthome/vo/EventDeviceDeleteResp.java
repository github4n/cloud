package com.iot.smarthome.vo;

import com.google.common.collect.Maps;
import com.iot.smarthome.util.DateTimeUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/19 14:52
 * @Modify by:
 */
public class EventDeviceDeleteResp extends Resp implements Serializable {
    private static final long serialVersionUID = 2025580783894328456L;

    private List<String> deviceIds;

    @Override
    public Map<String, Object> getPayload() {
        return payload;
    }

    @Override
    public void setPayload(Map<String, Object> payload) {

    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    @Override
    public Map<String, Object> buildMsg() {
        Map<String, Object> header = Maps.newHashMap();
        header.put("messageId", UUID.randomUUID().toString());
        header.put("event", "device.delete");
        header.put("time", DateTimeUtil.getNowDateTime());

        payload.put("header", header);
        payload.put("deviceIds", getDeviceIds());

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("payload", getPayload());
        return resultMap;
    }
}

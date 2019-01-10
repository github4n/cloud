package com.iot.smarthome.vo;

import com.google.common.collect.Maps;
import com.iot.smarthome.util.DateTimeUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Descrpiton:
 *      Add Or Update Device Notify 返回的数据
 *
 * @Author: yuChangXing
 * @Date: 2018/12/19 10:23
 * @Modify by:
 */
public class EventDeviceAddOrUpdateResp extends Resp implements Serializable {
    private static final long serialVersionUID = 2025580783894328456L;

    private List<DeviceInfo> deviceList;

    public EventDeviceAddOrUpdateResp() {

    }

    @Override
    public Map<String, Object> getPayload() {
        return payload;
    }

    @Override
    public void setPayload(Map<String, Object> payload) {

    }

    public List<DeviceInfo> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceInfo> deviceList) {
        this.deviceList = deviceList;
    }

    @Override
    public Map<String, Object> buildMsg() {
        Map<String, Object> header = Maps.newHashMap();
        header.put("messageId", UUID.randomUUID().toString());
        header.put("event", "device.addOrUpdate");
        header.put("time", DateTimeUtil.getNowDateTime());

        payload.put("header", header);
        payload.put("devices", getDeviceList());

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("payload", getPayload());
        return resultMap;
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString());
    }
}

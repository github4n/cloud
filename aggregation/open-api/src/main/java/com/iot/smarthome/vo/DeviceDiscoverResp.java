package com.iot.smarthome.vo;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Descrpiton:
 *      同步设备 返回的数据
 *
 * @Author: yuChangXing
 * @Date: 2018/12/14 15:10
 * @Modify by:
 */
public class DeviceDiscoverResp extends Resp implements Serializable {
    private static final long serialVersionUID = 2025580783894328456L;

    private List<DeviceInfo> deviceList;

    public DeviceDiscoverResp() {
        deviceList = Lists.newArrayList();
        payload.put("devices", getDeviceList());
    }

    public List<DeviceInfo> getDeviceList() {
        return deviceList;
    }

    public void addDeviceList(List<DeviceInfo> deviceList) {
        this.deviceList.addAll(deviceList);
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
        /*JSONObject jsonObject = new JSONObject();
        jsonObject.put("payload", getPayload());
        return jsonObject;*/
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("payload", getPayload());
        return resultMap;
    }

    public static void main(String[] args) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceId("001");
        deviceInfo.setName("meeting plug");
        deviceInfo.setType("plug");
        deviceInfo.setDeviceVersion("v1.0");
        List<DeviceInfo> deviceInfoList = Lists.newArrayList();
        deviceInfoList.add(deviceInfo);

        DeviceDiscoverResp deviceDiscoverResp = new DeviceDiscoverResp();
        deviceDiscoverResp.addDeviceList(deviceInfoList);

        Map<String, Object> resultMap = deviceDiscoverResp.buildMsg();
        System.out.println(resultMap);
    }
}

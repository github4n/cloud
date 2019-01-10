package com.iot.smarthome.vo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Descrpiton:
 *      执行设备 返回的数据
 *
 * @Author: yuChangXing
 * @Date: 2018/12/18 10:45
 * @Modify by:
 */
public class DeviceExecuteResp extends Resp implements Serializable {
    private static final long serialVersionUID = 2025580783894328456L;

    private List<Map<String,Object>> deviceList;

    public DeviceExecuteResp() {
        deviceList = Lists.newArrayList();
        payload.put("devices", getDeviceList());
    }

    @Override
    public Map<String, Object> getPayload() {
        return payload;
    }

    @Override
    public void setPayload(Map<String, Object> payload) {

    }

    public List<Map<String, Object>> getDeviceList() {
        return deviceList;
    }

    public void addDevice(Map<String, Object> device) {
        this.deviceList.add(device);
    }

    @Override
    public Map<String, Object> buildMsg() {
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("payload", getPayload());
        return resultMap;
    }
}

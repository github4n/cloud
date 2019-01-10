package com.iot.smarthome.vo;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Descrpiton: 获取 设备历史日志 返回的数据
 * @Author: yuChangXing
 * @Date: 2018/12/18 14:22
 * @Modify by:
 */
public class DeviceAttributeHistoricalResp extends Resp implements Serializable {
    private static final long serialVersionUID = 2025580783894328456L;

    private String deviceId;
    private List<ActivityVo> activityList;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<ActivityVo> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<ActivityVo> activityList) {
        this.activityList = activityList;
    }

    @Override
    public Map<String, Object> getPayload() {
        return payload;
    }

    @Override
    public void setPayload(Map<String, Object> payload) {

    }

    @Override
    public Map<String, Object> buildMsg() {
        payload.put("deviceId", getDeviceId());
        payload.put("activityList", getActivityList());

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("payload", getPayload());
        return resultMap;
    }

    public static void main(String[] args) {
        List<ActivityVo> activityList = Lists.newArrayList();
        ActivityVo vo = new ActivityVo();
        vo.setTime("2018-12-18 17:44:32");
        vo.setActivity("device a closed.");
        activityList.add(vo);

        DeviceAttributeHistoricalResp historicalResp = new DeviceAttributeHistoricalResp();
        historicalResp.setDeviceId("001");
        historicalResp.setActivityList(activityList);

        System.out.println(new JSONObject(historicalResp.buildMsg()));
    }
}

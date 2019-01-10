package com.iot.robot.vo.google;

import com.google.common.collect.Maps;
import com.iot.common.util.StringUtil;

import java.util.Map;
import java.util.UUID;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/8/21 10:46
 * @Modify by:
 */
public class GoogleHomeReportState {
    private String requestId;
    private String agent_user_id;
    private Map<String, Object> payload = Maps.newHashMap();
    private Map<String, Object> devices = Maps.newHashMap();
    private Map<String, Object> states = Maps.newHashMap();


    public GoogleHomeReportState() {
        this.requestId = UUID.randomUUID().toString();
        devices.put("states", states);
        payload.put("devices", devices);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAgent_user_id() {
        return agent_user_id;
    }

    public void setAgent_user_id(String agent_user_id) {
        this.agent_user_id = agent_user_id;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    /*public Map<String, Object> getDevices() {
        return devices;
    }

    public void setDevices(Map<String, Object> devices) {
        this.devices = devices;
    }

    public Map<String, Object> getStates() {
        return states;
    }

    public void setStates(Map<String, Object> states) {
        this.states = states;
    }*/

    public void addDeviceState(String deviceUuid, Map<String, Object> state) {
        if (StringUtil.isBlank(deviceUuid) || state == null || state.size() == 0) {
            return ;
        }
        this.states.put(deviceUuid, state);
    }

    public int deviceStateSize() {
        return this.states.size();
    }
}

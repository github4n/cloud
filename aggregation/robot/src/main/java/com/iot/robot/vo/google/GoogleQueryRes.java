package com.iot.robot.vo.google;

import java.util.HashMap;
import java.util.Map;

public class GoogleQueryRes extends AbstractGoogleRes {

	private Map<String,Object> devices = new HashMap<>();
	
	public GoogleQueryRes() {
		payload.put("devices", devices);
	}
	
	public void add(String endpointId, Map<String,Object> deviceState) {
		devices.put(endpointId, deviceState);
	}

	public Map<String, Object> getPayload() {
		return payload;
	}
}

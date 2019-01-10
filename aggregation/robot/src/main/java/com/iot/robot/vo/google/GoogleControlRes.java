package com.iot.robot.vo.google;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoogleControlRes extends AbstractGoogleRes {

	private List<Map<String,Object>> commands = new ArrayList<Map<String,Object>>();
	
	public GoogleControlRes() {
		payload.put("commands", commands);
	}
	
	public void add(Map<String, Object> data) {
		commands.add(data);
	}
	public Map<String, Object> getPayload() {
		return payload;
	}
}

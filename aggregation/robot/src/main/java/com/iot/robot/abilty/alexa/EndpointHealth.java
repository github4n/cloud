package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class EndpointHealth extends Alexa {

	private JSONObject properties;
	public EndpointHealth() {
		super.setInterface(EndpointHealth.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("name", "connectivity");
		arr.add(json);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static EndpointHealth getInstance() {
		return new EndpointHealth();
	}
	public JSONObject getProperties() {
		return properties;
	}
}

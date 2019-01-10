package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class TemperatureSensor extends Alexa {

	private JSONObject properties;
	private static TemperatureSensor ability = new TemperatureSensor();
	private TemperatureSensor() {
		super.setInterface(TemperatureSensor.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("name", "temperature");
		arr.add(json);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static TemperatureSensor getInstance() {
		return ability;
	}
}

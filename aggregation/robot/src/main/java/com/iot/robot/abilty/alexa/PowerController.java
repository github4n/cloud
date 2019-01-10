package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class PowerController extends Alexa {

	private JSONObject properties;
	private PowerController() {
		super.setInterface(PowerController.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("name", "powerState");
		arr.add(json);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static PowerController getInstance() {
		return new PowerController();
	}
	public JSONObject getProperties() {
		return properties;
	}
}

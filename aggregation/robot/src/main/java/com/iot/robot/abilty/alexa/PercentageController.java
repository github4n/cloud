package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class PercentageController extends Alexa {

	private JSONObject properties;
	private static PercentageController ability = new PercentageController();
	private PercentageController() {
		super.setInterface(PercentageController.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("name", "percentage");
		arr.add(json);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static PercentageController getInstance() {
		return ability;
	}
	public JSONObject getProperties() {
		return properties;
	}
}

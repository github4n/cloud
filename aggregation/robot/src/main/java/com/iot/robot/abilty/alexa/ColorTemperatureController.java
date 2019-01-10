package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class ColorTemperatureController extends Alexa {

	private JSONObject properties;
	private ColorTemperatureController() {
		super.setInterface(ColorTemperatureController.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("name", "colorTemperatureInKelvin");
		arr.add(json);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static ColorTemperatureController getInstance() {
		return new ColorTemperatureController();
	}
	public JSONObject getProperties() {
		return properties;
	}
}

package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class ColorController extends Alexa {

	private JSONObject properties;
	private ColorController() {
		super.setInterface(ColorController.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("name", "color");
		arr.add(json);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static ColorController getInstance() {
		return new ColorController();
	}
	public JSONObject getProperties() {
		return properties;
	}
}

package com.iot.robot.abilty.alexa;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
public final class BrightnessController extends Alexa {

	private JSONObject properties;
	private BrightnessController() {
		super.setInterface(BrightnessController.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("name", "brightness");
		arr.add(json);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
		
	}
	public static BrightnessController getInstance() {
		return new BrightnessController();
	}
	public JSONObject getProperties() {
		return properties;
	}
}

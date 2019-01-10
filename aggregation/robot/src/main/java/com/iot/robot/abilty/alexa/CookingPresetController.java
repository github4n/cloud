package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class CookingPresetController extends Alexa {

	private JSONObject properties;
	private static CookingPresetController ability = new CookingPresetController();
	private CookingPresetController() {
		super.setInterface("Cooking.PresetController");
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("name", "presetName");
		arr.add(json);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static CookingPresetController getInstance() {
		return ability;
	}
}

package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class CookingTimeController extends Alexa {

	private JSONObject properties;
	private static CookingTimeController ability = new CookingTimeController();
	private CookingTimeController() {
		super.setInterface("Cooking.TimeController");
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("name", "requestedCookTime");
		JSONObject json2 = new JSONObject();
		json.put("name", "cookingPowerLevel");
		arr.add(json);
		arr.add(json2);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static CookingTimeController getInstance() {
		return ability;
	}
}

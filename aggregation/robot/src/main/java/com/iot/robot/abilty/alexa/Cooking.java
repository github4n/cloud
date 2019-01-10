package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class Cooking extends Alexa {

	private JSONObject properties;
	private static Cooking ability = new Cooking();
	private Cooking() {
		super.setInterface(Cooking.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("name", "cookingMode");
		JSONObject json2 = new JSONObject();
		json2.put("name", "cookStartTime");
		JSONObject json3 = new JSONObject();
		json3.put("name", "cookCompletionTime");
		JSONObject json4 = new JSONObject();
		json4.put("name", "isCookCompletionTimeEstimated");
		JSONObject json5 = new JSONObject();
		json5.put("name", "foodItem");
		arr.add(json);
		arr.add(json2);
		arr.add(json3);
		arr.add(json4);
		arr.add(json5);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static Cooking getInstance() {
		return ability;
	}
}

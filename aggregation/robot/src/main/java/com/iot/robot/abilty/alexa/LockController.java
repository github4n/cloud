package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class LockController extends Alexa {

	private JSONObject properties;
	private static LockController ability = new LockController();
	private LockController() {
		super.setInterface(LockController.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("name", "lockState");
		arr.add(json);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static LockController getInstance() {
		return ability;
	}
	public JSONObject getProperties() {
		return properties;
	}
}

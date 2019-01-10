package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class CameraStreamController extends Alexa {

	private JSONObject properties;
	private static CameraStreamController ability = new CameraStreamController();
	private CameraStreamController() {
		super.setInterface(CameraStreamController.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static CameraStreamController getInstance() {
		return ability;
	}
	public JSONObject getProperties() {
		return properties;
	}
}

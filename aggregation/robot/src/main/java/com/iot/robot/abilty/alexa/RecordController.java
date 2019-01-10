package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class RecordController extends Alexa {

	private JSONObject properties;
	private static RecordController ability = new RecordController();
	private RecordController() {
		super.setInterface(RecordController.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("name", "RecordingState");
		arr.add(json);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static RecordController getInstance() {
		return ability;
	}
}

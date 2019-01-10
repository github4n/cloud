package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class MeetingClientController extends Alexa {

	private JSONObject properties;
	private static MeetingClientController ability = new MeetingClientController();
	private MeetingClientController() {
		super.setInterface(MeetingClientController.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static MeetingClientController getInstance() {
		return ability;
	}
}

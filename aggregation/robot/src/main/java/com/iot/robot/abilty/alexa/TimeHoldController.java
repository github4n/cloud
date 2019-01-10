package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class TimeHoldController extends Alexa {

	private JSONObject properties;
	private static TimeHoldController ability = new TimeHoldController();
	private TimeHoldController() {
		super.setInterface(TimeHoldController.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static TimeHoldController getInstance() {
		return ability;
	}
}

package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class StepSpeaker extends Alexa {

	private JSONObject properties;
	private static StepSpeaker ability = new StepSpeaker();
	private StepSpeaker() {
		super.setInterface(StepSpeaker.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static StepSpeaker getInstance() {
		return ability;
	}
}

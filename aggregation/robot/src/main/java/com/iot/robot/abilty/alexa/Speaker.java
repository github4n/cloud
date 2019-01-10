package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class Speaker extends Alexa {

	private JSONObject properties;
	private static Speaker ability = new Speaker();
	private Speaker() {
		super.setInterface(Speaker.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject json2 = new JSONObject();
		json.put("name", "volume");
		json2.put("name", "muted");
		arr.add(json);
		arr.add(json2);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static Speaker getInstance() {
		return ability;
	}
}

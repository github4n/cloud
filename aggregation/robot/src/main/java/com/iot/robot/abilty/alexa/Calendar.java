package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public final class Calendar extends Alexa {

	private JSONObject properties;
	private static Calendar ability = new Calendar();
	private Calendar() {
		super.setInterface(Calendar.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("name", "organizerName");
		JSONObject json2 = new JSONObject();
		json2.put("name", "calendarEventId");
		arr.add(json);
		arr.add(json2);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static Calendar getInstance() {
		return ability;
	}
	public JSONObject getProperties() {
		return properties;
	}
}

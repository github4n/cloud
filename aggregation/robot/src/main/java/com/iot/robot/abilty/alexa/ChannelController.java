package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class ChannelController extends Alexa {

	private JSONObject properties;
	private static ChannelController ability = new ChannelController();
	private ChannelController() {
		super.setInterface(ChannelController.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("name", "channel");
		arr.add(json);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static ChannelController getInstance() {
		return ability;
	}
	public JSONObject getProperties() {
		return properties;
	}
}

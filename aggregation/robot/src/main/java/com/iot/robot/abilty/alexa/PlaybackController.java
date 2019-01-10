package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class PlaybackController extends Alexa {

	private JSONObject properties;
	private static PlaybackController ability = new PlaybackController();
	private PlaybackController() {
		super.setInterface(PlaybackController.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static PlaybackController getInstance() {
		return ability;
	}
}

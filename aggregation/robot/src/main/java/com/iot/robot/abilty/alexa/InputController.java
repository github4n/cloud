package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class InputController extends Alexa {

	private JSONObject properties;
	private static InputController ability = new InputController();
	private InputController() {
		super.setInterface(InputController.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("name", "input");
		arr.add(json);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static InputController getInstance() {
		return ability;
	}
}

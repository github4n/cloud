package com.iot.robot.abilty.alexa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public final class ThermostatController extends Alexa {

	private JSONObject properties;
	private static ThermostatController ability = new ThermostatController();
	private ThermostatController() {
		super.setInterface(ThermostatController.class.getSimpleName());
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		JSONObject json2 = new JSONObject();
		JSONObject json3 = new JSONObject();
		json.put("name", "targetSetpoint");
		json.put("name", "lowerSetpoint");
		json.put("name", "upperSetpoint");
		json.put("name", "thermostatMode");
		arr.add(json);
		arr.add(json1);
		arr.add(json2);
		arr.add(json3);
		properties.put("supported", arr);
		properties.put("retrievable", true);
		properties.put("proactivelyReported", true);
	}
	public static ThermostatController getInstance() {
		return ability;
	}
}

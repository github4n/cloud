package com.iot.robot.transform;

import com.alibaba.fastjson.JSONObject;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.robot.abilty.google.GoogleHome;
import com.iot.robot.norm.*;
import com.iot.robot.service.CommonService;
import com.iot.robot.vo.DeviceInfo;
import com.iot.robot.vo.Endpoint;
import com.iot.robot.vo.google.GoogleEndpoint;
import com.iot.robot.vo.google.GoogleHomeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创建人：chenxiaolin
 * 创建时间：2018年4月26日 下午5:11:08
 * 修改人： chenxiaolin
 * 修改时间：2018年4月26日 下午5:11:08
 */

@Service("googleTransfor")
public class GoogleTransfor extends AbstractTransfor{
	private Logger log = LoggerFactory.getLogger(GoogleTransfor.class);
	private final String BASENAME = GoogleHome.class.getPackage().getName() + ".";
	private final String KEY = GoogleHome.class.getSimpleName();
	@Autowired
	private CommonService commonService;

	private GoogleTransfor() {
	}

	@Override
	public GoogleHomeResponse getResponse(List<Object>[] resources) {
		return super.getResponse(resources, GoogleHomeResponse.class);
	}
	@Override
	public Endpoint deviceHandle(DeviceInfo device) {
		return super.deviceHandle(device, GoogleEndpoint.class);
	}
	public Endpoint sceneHandle(SceneResp scene) {
		return super.sceneHandle(scene, GoogleEndpoint.class);
	}
	
	/**
     * 将第三方 控制属性 --> robot控制属性 
     */
	@Override
	public KeyValue toCommonKeyVal(JSONObject req) {
		String commondStr = req.getString("command");
		JSONObject payload = req.getJSONObject("params");
		String endpointId = req.getString("endpointId");
		log.info("***** toCommonKeyVal, commondStr={}, payload={}", commondStr, payload);

		switch (commondStr) {
			case "action.devices.commands.BrightnessAbsolute": {
				Integer value = payload.getInteger("brightness");
				DimmingKeyValue kv = new DimmingKeyValue();
				kv.setFixedValue(value);
				return kv;
			}
			case "action.devices.commands.ColorAbsolute": {
				JSONObject color = payload.getJSONObject("color");
				String colorName = color.getString("name");
				// 色温
				Integer v = color.getInteger("temperature");
				// 颜色
				Integer v2 = color.getInteger("spectrumRGB");
				KeyValue<Integer> kv = null;
				if (v != null) {
					kv = new CctKeyValue();
					kv.setFixedValue(v);
				} else {
					kv = new RgbwKeyValue();
					kv.setFixedValue(v2);
				}
				return kv;
			}
			case "action.devices.commands.OnOff": {
				Boolean value = payload.getBoolean("on");
				KeyValue kv = null;
				if (commonService.isIPC(endpointId)) {
					kv = new IPCTurnOnOffKeyValue();
					kv.setFixedValue(value);
				} else {
					kv = new OnOffKeyValue();
					kv.setFixedValue((byte) 0);
					if (value) {
						kv.setFixedValue((byte) 1);
					}
				}
				return kv;
			}
			case "action.devices.commands.ActivateScene": {
				SceneKeyValue kv = new SceneKeyValue();
				return kv;
			}
			case "action.devices.commands.StartStop": {
				// 未实现此功能点
				log.info("***** 未实现此功能点");
			}
			case "action.devices.commands.ThermostatTemperatureSetpoint": {
				// 未实现此功能点
				log.info("***** 未实现此功能点");
			}
			case "action.devices.commands.SetToggles": {
				// 未实现此功能点
				log.info("***** 未实现此功能点");
			}
			case "action.devices.commands.Dock": {
				// 未实现此功能点
				log.info("***** 未实现此功能点");
			} 
			case "action.devices.commands.SetModes": {
				// 未实现此功能点
				log.info("***** 未实现此功能点");
			}
			default:
				return null;
	    }
	}
	
	@Override
	public JSONObject getSelfKeyVal(KeyValue kv) {
		JSONObject res = new JSONObject();
		String key = kv.getKey();
		switch (key) {
			case KeyValue.CCT: {
				JSONObject data = new JSONObject();
				data.put("temperature", kv.getFixedValue());
				res.put("key", "color");
				res.put("value", data);
				break;
			}
			case KeyValue.BLINK: {
				break;
			}
			case KeyValue.CONTROL: {
				break;
			}
			case KeyValue.DIMMING: {
				res.put("key", "brightness");
				res.put("value", kv.getFixedValue());
				break;
			}
			case KeyValue.GRAD_COLOR_TEMPERATURE: {
				break;
			}
			case KeyValue.LIFT: {
				break;
			}
			case KeyValue.ONOFF: {
				res.put("key", "on");
				res.put("value", false);
				if (((OnOffKeyValue)kv).getFixedValue() == 1) {
					res.put("value", true);
				}
				break;
			}
			case KeyValue.TURN_ON_OFF: {
				res.put("key", "on");
				res.put("value", false);
				if (((IPCTurnOnOffKeyValue)kv).getFixedValue()) {
					res.put("value", true);
				}
				break;
			}
			case KeyValue.ONLINE: {
				Boolean onlineStatus = ((OnlineKeyValue) kv).getFixedValue();
				if (onlineStatus == null) {
					return null;
				}

				res.put("key", "online");
				res.put("value", false);
				if (onlineStatus) {
					res.put("value", true);
				} else {
					res.put("value", false);
				}
				break;
			}
			case KeyValue.RGBW: {
				JSONObject data = new JSONObject();
				data.put("spectrumRGB", kv.getFixedValue());
				res.put("key", "color");
				res.put("value", data);
				break;
			}
			default:
				return null;
		}
		return res;
	}
	@Override
	public String getKey() {
		return KEY;
	}
	@Override
	public String getBasePrefix() {
		return BASENAME;
	}
	@Override
	public Integer getKeyEnum() {
		return 2;
	}

	@Override
	public JSONObject getConnectivity(String onlineStatus) {
		return null;
	}

	@Override
	public Endpoint addAttributes(Endpoint e) {
		log.info("***** GoogleTransfor, addAttributes");

		GoogleEndpoint endpoint = (GoogleEndpoint) e;

		// 处理功能点 action.devices.traits.ColorTemperature
		if (endpoint.getTraits().contains("action.devices.traits.ColorTemperature")) {
			log.info("***** 包含功能点: action.devices.traits.ColorTemperature");
			endpoint.addAttributes("temperatureMinK", 2700);
			endpoint.addAttributes("temperatureMaxK", 6500);
		}

		// 处理功能点 action.devices.traits.ColorSpectrum
		if (endpoint.getTraits().contains("action.devices.traits.ColorSpectrum")) {
			log.info("***** 包含功能点: action.devices.traits.ColorSpectrum");
			endpoint.addAttributes("colorModel", "rgb");
		}

		return endpoint;
	}

	@Override
	public String getSceneCapabilitiesSuffix() {
		return "Scene";
	}

	@Override
	public String getSceneCategory() {
		return "action.devices.types.SCENE";
	}
}

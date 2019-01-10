package com.iot.robot.transform;

import com.alibaba.fastjson.JSONObject;
import com.google.api.client.util.Maps;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.robot.abilty.alexa.Alexa;
import com.iot.robot.abilty.alexa.BrightnessController;
import com.iot.robot.abilty.alexa.ColorController;
import com.iot.robot.abilty.alexa.ColorTemperatureController;
import com.iot.robot.abilty.alexa.PowerController;
import com.iot.robot.common.constant.DeviceOnlineStatusEnum;
import com.iot.robot.norm.*;
import com.iot.robot.service.CommonService;
import com.iot.robot.utils.ColorConvertor;
import com.iot.robot.vo.DeviceInfo;
import com.iot.robot.vo.Endpoint;
import com.iot.robot.vo.ResponsePost;
import com.iot.robot.vo.alexa.AlexaDiscoveryResponse;
import com.iot.robot.vo.alexa.AlexaEndpoint;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * 创建人：chenxiaolin
 * 创建时间：2018年4月26日 下午5:11:08
 * 修改人： chenxiaolin
 * 修改时间：2018年4月26日 下午5:11:08
 */

@Service("alexaTransfor")
public final class AlexaTransfor extends AbstractTransfor {
	private Logger log = LoggerFactory.getLogger(AlexaTransfor.class);
	private final String BASENAME = Alexa.class.getPackage().getName() + ".";
	private final String KEY = Alexa.class.getSimpleName();
	@Autowired
	private CommonService commonService;

	private AlexaTransfor() {
	}

	@Override
	public ResponsePost getResponse(List<Object>[] resources) {
		return super.getResponse(resources, AlexaDiscoveryResponse.class);
	}
	
	public Endpoint deviceHandle(DeviceInfo device) {
		return super.deviceHandle(device, AlexaEndpoint.class);
	}
	
	public Endpoint sceneHandle(SceneResp scene) {
		return super.sceneHandle(scene, AlexaEndpoint.class);
	}
	
	/**
	 * 将第三方 控制属性 --> robot控制属性 
	 */
	@Override
	public KeyValue toCommonKeyVal(JSONObject req) {
		String commondStr = req.getString("name");
		String endpointId = req.getString("endpointId");
		JSONObject payload = req.getJSONObject("payload");
		switch (commondStr) {
			case "TurnOn": {
				KeyValue kv = null;
				if (commonService.isIPC(endpointId)) {
					kv = new IPCTurnOnOffKeyValue();
					kv.setFixedValue(true);
				} else {
					kv = new OnOffKeyValue();
					kv.setFixedValue((byte) 1);
				}
				return kv;
			}
			case "TurnOff": {
				KeyValue kv = null;
				if (commonService.isIPC(endpointId)) {
					kv = new IPCTurnOnOffKeyValue();
					kv.setFixedValue(false);
				} else {
					kv = new OnOffKeyValue();
					kv.setFixedValue((byte) 0);
				}
				return kv;
			}
			case "AdjustBrightness": {
				Integer delta = payload.getInteger("brightnessDelta");
				DimmingKeyValue kv = new DimmingKeyValue();
				kv.setDeltaValue(delta);
				return kv;
			}
			case "SetBrightness": {
				Integer value = payload.getInteger("brightness");
				DimmingKeyValue kv = new DimmingKeyValue();
				kv.setFixedValue(value);
				return kv;
			}
			case "SetColor": {
				JSONObject color = payload.getJSONObject("color");
				Float hue = color.getFloat("hue");
				Float saturation = color.getFloat("saturation");
				Float brightness = color.getFloat("brightness");
				int[] rgbs = ColorConvertor.hsb2rgb(hue, saturation, brightness);
				Color c = new Color(rgbs[0], rgbs[1], rgbs[2]);
				RgbwKeyValue kv = new RgbwKeyValue();
				kv.setFixedValue(c.getRGB()<<8);
				return kv;
			}
			case "DecreaseColorTemperature": {
				CctKeyValue kv = new CctKeyValue();
				kv.setDelat(true);
				kv.setDeltaValue(-500);
				return kv;
			}
			case "IncreaseColorTemperature": {
				CctKeyValue kv = new CctKeyValue();
				kv.setDelat(true);
				kv.setDeltaValue(500);
				return kv;
			}
			case "SetColorTemperature": {
				Integer kelvin = payload.getInteger("colorTemperatureInKelvin");
				CctKeyValue kv = new CctKeyValue();
				kv.setFixedValue(kelvin);
				return kv;
			}
			case "Lock": {
				// 未实现此功能点
				log.info("***** 未实现此功能点");
				return null;
			} 
			case "Unlock": {
				// 未实现此功能点
				log.info("***** 未实现此功能点");
				return null;
			}
			case "SetPercentage": {
				// 未实现此功能点
				log.info("***** 未实现此功能点");
				return null;
			}
			case "AdjustPercentage": {
				// 未实现此功能点
				log.info("***** 未实现此功能点");
				return null;
			}
			//情景指令
			case "Activate": {
				SceneKeyValue kv = new SceneKeyValue();
				return kv;
			}
			//状态查询指令
			case "ReportState": {
				QueryKeyValue kv = new QueryKeyValue();
				return kv;
			}
			default:
				return null;
	    }
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
	public JSONObject getSelfKeyVal(KeyValue kv) {
		JSONObject res = new JSONObject();
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sim.setTimeZone(TimeZone.getTimeZone("GMT"));
		res.put("timeSample", sim.format(new Date()));
		if (kv.getClass().isAssignableFrom(DimmingKeyValue.class)) {
			res.put("namespace", BrightnessController.getInstance().getInterface());
			res.put("name", "brightness");
			res.put("value", kv.getFixedValue());
		} else if (kv.getClass().isAssignableFrom(CctKeyValue.class)) {
			res.put("namespace",  ColorTemperatureController.getInstance().getInterface());
			res.put("name", "colorTemperatureInKelvin");
			res.put("value", kv.getFixedValue());
		} else if (kv.getClass().isAssignableFrom(OnOffKeyValue.class)) {
			OnOffKeyValue kvo = (OnOffKeyValue) kv;
			res.put("namespace",  PowerController.getInstance().getInterface());
			res.put("name", "powerState");
			if (kvo.getFixedValue() == 1) {
				res.put("value", "ON");
			} else {
				res.put("value", "OFF");
			}
		} else if (kv.getClass().isAssignableFrom(IPCTurnOnOffKeyValue.class)) {
			IPCTurnOnOffKeyValue kvo = (IPCTurnOnOffKeyValue) kv;
			res.put("namespace",  PowerController.getInstance().getInterface());
			res.put("name", "powerState");
			if (kvo.getFixedValue()) {
				res.put("value", "ON");
			} else {
				res.put("value", "OFF");
			}
		} else if (kv.getClass().isAssignableFrom(RgbwKeyValue.class)) {
			res.put("namespace",  ColorController.getInstance().getInterface());
			RgbwKeyValue kvr = (RgbwKeyValue) kv;
			int rgbw = kvr.getFixedValue();
			Color color = new Color(rgbw>>>8);
			Map<String, Float> hsbm = new HashMap<>();
			float[] hsb = ColorConvertor.rgb2hsb(color.getRed(), color.getGreen(), color.getBlue());
			hsbm.put("hue", hsb[0]);
			hsbm.put("saturation", hsb[1]);
			hsbm.put("brightness", hsb[2]);
			res.put("name", "color");
			res.put("value", hsbm);
		}  else if (kv.getClass().isAssignableFrom(SceneKeyValue.class)) {

		}
		return res;
	}
	@Override
	public Integer getKeyEnum() {
		return 1;
	}

	@Override
	public JSONObject getConnectivity(String onlineStatus) {
		JSONObject res = new JSONObject();
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sim.setTimeZone(TimeZone.getTimeZone("GMT"));
		res.put("timeSample", sim.format(new Date()));

		res.put("namespace",  "Alexa.EndpointHealth");
		res.put("name", "connectivity");

		Map<String, String> connectivityMap = Maps.newHashMap();
		if (StringUtils.isBlank(onlineStatus)) {
			// 不在线
			connectivityMap.put("value", "UNREACHABLE");
		}else if (DeviceOnlineStatusEnum.CONNECTED.getCode().equals(onlineStatus)) {
			// 在线
			connectivityMap.put("value", "OK");
		} else {
			// 不在线
			connectivityMap.put("value", "UNREACHABLE");
		}
		res.put("value", connectivityMap);

		return res;
	}

	@Override
	public Endpoint addAttributes(Endpoint e) {
		log.info("***** AlexaTransfor, addAttributes");
		return e;
	}

	@Override
	public String getSceneCapabilitiesSuffix() {
		return "SceneController";
	}

	@Override
	public String getSceneCategory() {
		return "ACTIVITY_TRIGGER";
	}
}

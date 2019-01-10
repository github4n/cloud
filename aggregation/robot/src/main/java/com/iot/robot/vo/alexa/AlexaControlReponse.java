package com.iot.robot.vo.alexa;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.iot.robot.norm.CctKeyValue;
import com.iot.robot.norm.DimmingKeyValue;
import com.iot.robot.norm.KeyValue;
import com.iot.robot.norm.OnOffKeyValue;
import com.iot.robot.norm.RgbwKeyValue;
import com.iot.robot.utils.ColorConvertor;

/**
 * 创建人：chenxiaolin
 * 创建时间：2018年4月26日 下午5:11:08
 * 修改人： chenxiaolin
 * 修改时间：2018年4月26日 下午5:11:08
 */
@SuppressWarnings("rawtypes")
public class AlexaControlReponse {

	private String name;
	private Object value;
	
	public AlexaControlReponse(KeyValue kv) {
		if (kv.getClass().isAssignableFrom(DimmingKeyValue.class)) {
			this.name = "brightness";
			this.value = kv.getFixedValue();
		} else if (kv.getClass().isAssignableFrom(CctKeyValue.class)) {
			this.name = "colorTemperatureInKelvin";
			this.value = kv.getFixedValue();
		} else if (kv.getClass().isAssignableFrom(OnOffKeyValue.class)) {
			OnOffKeyValue kvo = (OnOffKeyValue) kv;
			this.name = "powerState";
			if (kvo.getFixedValue() == 1) {
				this.value = "ON";
			} else {
				this.value = "OFF";
			}
		} else if (kv.getClass().isAssignableFrom(RgbwKeyValue.class)) {
			RgbwKeyValue kvr = (RgbwKeyValue) kv;
			int rgb = kvr.getFixedValue();
			Color color = new Color(rgb);
			Map<String, Float> hsbm = new HashMap<>();
			float[] hsb = ColorConvertor.rgb2hsb(color.getRed(), color.getGreen(), color.getBlue());
			this.name = "color";
			hsbm.put("hue", hsb[0]);
			hsbm.put("saturation", hsb[1]);
			hsbm.put("brightness", hsb[2]);
			this.value = hsbm;
		} 
	}
	public String getName() {
		return name;
	}
	public Object getValue() {
		return value;
	}
}

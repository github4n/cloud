package com.iot.robot.transform.convertor;

import com.iot.robot.norm.KeyValue;
import com.iot.robot.norm.RgbwKeyValue;
import org.springframework.stereotype.Component;

@Component
public class RgbwConvertor extends AbstractConvertor<Integer, Integer> {

	private static final String KEY = "RGBW";
	private static final String COMMAND = "RGBW";
	@Override
	public YunKeyValue<Integer> valueConvert(KeyValue<Integer> kv, String endpointId) {
		YunKeyValue<Integer> keyValue = new YunKeyValue<>();
		keyValue.setKey(COMMAND);
		keyValue.setEndpointId(endpointId);
		keyValue.setValue(kv.getFixedValue());
		return keyValue;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj.equals(KeyValue.RGBW);
	}
	@Override
	public String toString() {
		return KEY;
	}
	@Override
	public KeyValue<Integer> toCommonKV(YunKeyValue value) {
		KeyValue<Integer> kv = new RgbwKeyValue();
		kv.setFixedValue(Integer.valueOf(String.valueOf(value.getValue())));
		return kv;
	}

	@Override
	public String getCommandKey() {
		return COMMAND;
	}
}

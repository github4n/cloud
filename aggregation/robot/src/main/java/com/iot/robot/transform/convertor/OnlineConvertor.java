package com.iot.robot.transform.convertor;

import com.iot.robot.norm.KeyValue;
import com.iot.robot.norm.OnlineKeyValue;
import org.springframework.stereotype.Component;

@Component
public class OnlineConvertor extends AbstractConvertor<Boolean, Boolean> {
    //云端的指令
	private static final String KEY = "online";
	//Robot定义的中间指令
	private static final String COMMAND = "online";
	@Override
	public YunKeyValue<Boolean> valueConvert(KeyValue<Boolean> kv, String endpointId) {
		YunKeyValue<Boolean> keyValue = new YunKeyValue<>();
		keyValue.setKey(COMMAND);
		keyValue.setEndpointId(endpointId);
		keyValue.setValue(kv.getFixedValue());
		return keyValue;
	}
	@Override
	public boolean equals(Object obj) {
		return obj.equals(KeyValue.ONLINE);
	}
	@Override
	public String toString() {
		return KEY;
	}
	@Override
	public KeyValue<Boolean> toCommonKV(YunKeyValue value) {
		if (value.getValue() == null) {
			return null;
		}

		KeyValue<Boolean> kv = new OnlineKeyValue();
		kv.setFixedValue(Boolean.valueOf(String.valueOf(value.getValue())));
		return kv;
	}

	@Override
	public String getCommandKey() {
		return COMMAND;
	}
}

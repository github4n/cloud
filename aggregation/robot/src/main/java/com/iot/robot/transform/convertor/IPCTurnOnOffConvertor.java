package com.iot.robot.transform.convertor;

import com.iot.robot.norm.IPCTurnOnOffKeyValue;
import com.iot.robot.norm.KeyValue;
import com.iot.robot.norm.OnOffKeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IPCTurnOnOffConvertor extends AbstractConvertor<Boolean, Boolean> {
    public Logger LOGGER = LoggerFactory.getLogger(IPCTurnOnOffConvertor.class);

    //云端的指令
    private static final String KEY = KeyValue.TURN_ON_OFF;
    //Robot定义的中间指令
    private static final String COMMAND = "TurnOnOff";

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
        return obj.equals(KEY);
    }

    @Override
    public String toString() {
        return KEY;
    }

    @Override
    public KeyValue<Boolean> toCommonKV(YunKeyValue value) {
        KeyValue<Boolean> kv = new IPCTurnOnOffKeyValue();
        kv.setFixedValue(Boolean.valueOf(value.getValue() + ""));
        return kv;
    }

    @Override
    public String getCommandKey() {
        return COMMAND;
    }
}

package com.iot.robot.transform.convertor;

import com.iot.robot.norm.KeyValue;
import com.iot.robot.norm.OnOffKeyValue;
import org.springframework.stereotype.Component;

@Component
public class OnOffConvertor extends AbstractConvertor<Byte, Byte> {
    //云端的指令
    private static final String KEY = "OnOff";
    //Robot定义的中间指令
    private static final String COMMAND = "OnOff";

    @Override
    public YunKeyValue<Byte> valueConvert(KeyValue<Byte> kv, String endpointId) {
        YunKeyValue<Byte> keyValue = new YunKeyValue<>();
        keyValue.setKey(COMMAND);
        keyValue.setEndpointId(endpointId);
        keyValue.setValue(kv.getFixedValue());
        return keyValue;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.equals(KeyValue.ONOFF);
    }

    @Override
    public String toString() {
        return KEY;
    }

    @Override
    public KeyValue<Byte> toCommonKV(YunKeyValue value) {
        KeyValue<Byte> kv = new OnOffKeyValue();
        kv.setFixedValue(Byte.valueOf(value.getValue() + ""));
        return kv;
    }

    @Override
    public String getCommandKey() {
        return COMMAND;
    }
}

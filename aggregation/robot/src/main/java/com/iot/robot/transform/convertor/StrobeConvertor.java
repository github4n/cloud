package com.iot.robot.transform.convertor;

import com.iot.robot.norm.KeyValue;
import com.iot.robot.norm.StrobeKeyValue;

//报警的属性已经取消使用
@Deprecated
public class StrobeConvertor extends AbstractConvertor<String, String> {
    
    private static final String KEY = KeyValue.STROBE;//公共定义的功能属性名
    private static final String COMMAND = "strobe";//平台指令实际属性名
    
    @Override
    public YunKeyValue<String> valueConvert(KeyValue<String> kv, String endpointId) {
        YunKeyValue<String> keyValue = new YunKeyValue<>();
        keyValue.setKey(COMMAND);
        keyValue.setEndpointId(endpointId);
        keyValue.setValue(kv.getFixedValue());
        return keyValue;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.equals(COMMAND);
    }
    //选取作用
    @Override
    public String toString() {
        return KEY;
    }



    @Override
    public KeyValue<String> toCommonKV(YunKeyValue<String> value) {
        KeyValue<String> kv = new StrobeKeyValue();
        kv.setFixedValue(value.getValue());
        return kv;
    }

    @Override
    public String getCommandKey() {
        return COMMAND;
    }

}

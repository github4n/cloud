package com.iot.robot.transform.convertor;

import com.iot.robot.norm.CctKeyValue;
import com.iot.robot.norm.KeyValue;
import com.iot.robot.service.CommonService;
import com.iot.saas.SaaSContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CctConvertor extends AbstractConvertor<Integer, Integer> {

    private static final String KEY = KeyValue.CCT;//公共定义的功能属性名
    private static final String COMMAND = "CCT";//平台指令实际属性名
    private static final int MAX = 6500;
    private static final int MIN = 2700;
    @Autowired
    private CommonService commonService;

    @Override
    public YunKeyValue<Integer> valueConvert(KeyValue<Integer> kv, String endpointId) {
        YunKeyValue<Integer> keyValue = new YunKeyValue<>();
        keyValue.setKey(COMMAND);
        keyValue.setEndpointId(endpointId);
        if (kv.isDelat()) {
            Object attrVal = getAttrVal(endpointId, COMMAND);
            LOGGER.info("***** CctConvertor, attrVal={}", attrVal);
            Integer cv = null;
            if (attrVal != null) {
                cv = Integer.parseInt(String.valueOf(attrVal));
            }
            if (cv == null) {
                cv = 0;
            }
            cv = cv + kv.getDeltaValue();
            kv.setFixedValue(cv);
        }
        if (kv.getFixedValue() < MIN) {
            kv.setFixedValue(MIN);
        } else if (kv.getFixedValue() > MAX) {
            kv.setFixedValue(MAX);
        }
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
    public KeyValue<Integer> toCommonKV(YunKeyValue value) {
        KeyValue<Integer> kv = new CctKeyValue();
        kv.setFixedValue(Integer.valueOf(String.valueOf(value.getValue())));
        return kv;
    }

    @Override
    public String getCommandKey() {
        return COMMAND;
    }

    public Object getAttrVal(String deviceId, String key) {
        Long tenantId = SaaSContextHolder.currentTenantId();
        LOGGER.info("***** SaaSContextHolder.currentTenantId={}", tenantId);
        Map<String, Object> attrMap = commonService.getDeviceStatus(tenantId, deviceId);
        if (attrMap != null) {
            return attrMap.get(key);
        }
        return null;
    }
}

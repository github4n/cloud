package com.iot.robot.transform.convertor;

import com.iot.robot.norm.DimmingKeyValue;
import com.iot.robot.norm.KeyValue;
import com.iot.robot.service.CommonService;
import com.iot.saas.SaaSContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DimmingConvertor extends AbstractConvertor<Integer, Integer> {
    private static final String KEY = "Dimming";
    private static final String COMMAND = "Dimming";
    private static final int MAX = 100;
    private static final int MIN = 5;

    @Autowired
    private CommonService commonService;

    @Override
    public YunKeyValue<Integer> valueConvert(KeyValue<Integer> kv, String endpointId) {
        YunKeyValue<Integer> keyValue = new YunKeyValue<>();
        keyValue.setKey(COMMAND);
        keyValue.setEndpointId(endpointId);

        Integer targetVal = kv.getFixedValue();
        if (kv.isDelat()) {
            Object attrVal = getAttrVal(endpointId, COMMAND);

            LOGGER.info("***** DimmingConvertor, attrVal={}", attrVal);
            Integer cv = null;
            if (attrVal != null) {
                cv = Integer.parseInt(String.valueOf(attrVal));
            }
            if (cv == null) {
                cv = 0;
            }

            targetVal = cv + kv.getDeltaValue();
        }

        if (targetVal < MIN) {
            targetVal = MIN;
        } else if (targetVal > MAX) {
            targetVal = MAX;
        }

        kv.setFixedValue(targetVal);

        keyValue.setValue(kv.getFixedValue());
        return keyValue;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.equals(KeyValue.DIMMING);
    }

    @Override
    public String toString() {
        return KEY;
    }

    @Override
    public String getCommandKey() {
        return COMMAND;
    }

    @Override
    public KeyValue<Integer> toCommonKV(YunKeyValue value) {
        KeyValue<Integer> kv = new DimmingKeyValue();
        if (value.getValue().getClass() == Integer.class) {
            kv.setFixedValue((Integer) value.getValue());
        } else {
            kv.setFixedValue(Integer.valueOf(String.valueOf(value.getValue())));
        }
        return kv;
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

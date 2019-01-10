package com.iot.building.device.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.building.helper.DispatcherRouteHelper;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;

@Service("subDeviceMQTTService")
public class SubDeviceMQTTService implements CallBackProcessor {

    private final static Logger LOGGER = LoggerFactory.getLogger(SubDeviceMQTTService.class);

    public static final int QOS = 1;

    @Autowired
    private CenterDeviceMQTTService centerDeviceMQTTService;

    @Override
    public void onMessage(MqttMsg mqttMsg) {
    	try {
            if (mqttMsg == null) {
                return;
            }
            DispatcherRouteHelper.dispatch(centerDeviceMQTTService, mqttMsg);
    	}catch (Exception e) {
			e.printStackTrace();
		}

    }
}
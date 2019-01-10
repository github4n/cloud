package com.iot.building.scene.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.building.helper.DispatcherRouteHelper;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/4/24 10:28
 * 修改人:
 * 修改时间：
 */

@Service("scene")
public class SceneMQTTService implements CallBackProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SceneMQTTService.class);

    @Autowired
    private CenterSceneMQTTService centerSceneMQTTService;
    
    @Override
    public void onMessage(MqttMsg mqttMsg) {
    	try {
            if (mqttMsg == null) {
                return;
            }
            DispatcherRouteHelper.dispatch(centerSceneMQTTService, mqttMsg);
    	}catch (Exception e) {
			e.printStackTrace();
		}

    }
    
}

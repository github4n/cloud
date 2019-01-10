package com.iot.shcs.device.service.impl;

import com.iot.mqttploy.api.MqttPloyApi;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.shcs.device.utils.DeviceConstants;
import com.iot.shcs.listener.MQTTClientListener;
import com.iot.shcs.space.service.ShareSpaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 14:57 2018/10/12
 * @Modify by:
 */
@Slf4j
@Component
public class MqttCoreService {
    @Autowired
    private MqttPloyApi mqttPloyApi;

    @Autowired
    private MqttSdkService mqttSdkService;

    @Autowired
    private ShareSpaceService shareSpaceService;

    public void addAcls(Long tenantId, Long userId, String userUUID, String deviceId) {
        int acls = mqttPloyApi.addAcls(userUUID, deviceId);
        //新增业务，绑定封箱机啊业务用户的设备策略
        shareSpaceService.addMultiAcls(tenantId, userId, deviceId);
        log.info("mqttPloyApi.addAcls ok, {}, {}, {}", userId, deviceId, acls);
    }

    public void separationAcls(Long tenantId, Long userId, String unbindUserId, String unbindDevId) {
        mqttPloyApi.separationAcls(unbindUserId, unbindDevId);
        //新增业务，解绑分享家业务用户的设备策略
        shareSpaceService.unbindMultiAcls(tenantId, userId, unbindDevId);
        log.info("mqttPloyApi.separationAcls ok, {}, {}, {}", unbindUserId, unbindDevId);
    }

    public void sendMessage(MqttMsg mqttMsg) {
        mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, DeviceConstants.QOS);
    }

}

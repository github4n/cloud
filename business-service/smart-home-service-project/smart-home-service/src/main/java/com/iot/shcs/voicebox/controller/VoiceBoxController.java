package com.iot.shcs.voicebox.controller;

import com.alibaba.fastjson.JSON;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.device.service.impl.DeviceMQTTService;
import com.iot.shcs.scene.service.impl.SceneMQTTService;
import com.iot.shcs.security.service.impl.SecurityMqttService;
import com.iot.shcs.voicebox.api.VoiceBoxApi;
import com.iot.shcs.voicebox.constant.VoiceBoxTopic;
import com.iot.shcs.voicebox.vo.SetDevAttrDTO;
import com.iot.shcs.voicebox.vo.VoiceBoxMqttMsg;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Descrpiton: 音箱请求控制器(robot服务的请求控制器)
 * @Author: yuChangXing
 * @Date: 2018/10/8 13:47
 * @Modify by:
 */

@RestController
public class VoiceBoxController implements VoiceBoxApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoiceBoxController.class);

    @Autowired
    private SceneMQTTService sceneMQTTService;
    @Autowired
    private DeviceMQTTService deviceMQTTService;
    @Autowired
    private SecurityMqttService securityMQTTService;
    @Autowired
    private UserApi userApi;


    // 注入 tenantId
    private void parseTenantId(String userUuid) {
        FetchUserResp userResp = userApi.getUserByUuid(userUuid);
        if (userResp != null) {
            SaaSContextHolder.setCurrentTenantId(userResp.getTenantId());
        }
    }

    /**
     * 执行场景请求
     *
     * @param voiceBoxMqttMsg mqtt消息内容
     */
    @Override
    public void excSceneReq(@RequestBody VoiceBoxMqttMsg voiceBoxMqttMsg) {
        parseTenantId(voiceBoxMqttMsg.getUserUuid());
        MqttMsg mqttMsg = JSON.parseObject(voiceBoxMqttMsg.getMqttMsgContent(), MqttMsg.class);
        sceneMQTTService.excSceneReq(mqttMsg, VoiceBoxTopic.getSceneTopicExcSceneReq(voiceBoxMqttMsg.getUserUuid()));
    }

    /**
     *  布置安防请求
     *
     * @param voiceBoxMqttMsg   mqtt消息内容
     */
    @Override
    public void setArmModeReq(@RequestBody VoiceBoxMqttMsg voiceBoxMqttMsg) {
        parseTenantId(voiceBoxMqttMsg.getUserUuid());
        MqttMsg mqttMsg = JSON.parseObject(voiceBoxMqttMsg.getMqttMsgContent(), MqttMsg.class);
        securityMQTTService.setArmModeReq(mqttMsg, VoiceBoxTopic.getSecurityTopicSetArmModeReq(voiceBoxMqttMsg.getUserUuid()));
    }

    /**
     *  获取安防状态请求
     *
     * @param voiceBoxMqttMsg   mqtt消息内容
     */
    @Override
    public void getStatusReq(@RequestBody VoiceBoxMqttMsg voiceBoxMqttMsg) {
        parseTenantId(voiceBoxMqttMsg.getUserUuid());
        MqttMsg mqttMsg = JSON.parseObject(voiceBoxMqttMsg.getMqttMsgContent(), MqttMsg.class);
        securityMQTTService.getStatusReq(mqttMsg, VoiceBoxTopic.getSecurityTopicGetStatusReq(voiceBoxMqttMsg.getUserUuid()));
    }


    /**
     *  设备控制(设置设备属性)
     */
    @Override
    public void setDevAttrReq(@RequestBody SetDevAttrDTO devAttrDTO) {
        LOGGER.info("***** VoiceBoxController, setDevAttr start, deviceId={}, attr={}, payloadMap={}", devAttrDTO.getDeviceId(), devAttrDTO.getAttr(), devAttrDTO.getPayloadMap());
        deviceMQTTService.setDevAttr(devAttrDTO.getDeviceId(), devAttrDTO.getPayloadMap(), devAttrDTO.getAttr());
    }
}

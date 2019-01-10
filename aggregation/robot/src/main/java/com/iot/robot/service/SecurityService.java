package com.iot.robot.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.iot.common.util.StringUtil;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.robot.common.exception.RobotException;
import com.iot.robot.utils.ErrorCodeKeys;
import com.iot.robot.utils.LockHelper;
import com.iot.robot.utils.LockObject;
import com.iot.robot.utils.MD5Utils;
import com.iot.robot.utils.VoiceBoxUtil;
import com.iot.robot.vo.SecurityCommand;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.security.api.SecurityApi;
import com.iot.shcs.security.vo.rsp.SecurityResp;
import com.iot.shcs.space.api.SmarthomeSpaceApi;
import com.iot.shcs.space.vo.SpaceResp;
import com.iot.shcs.voicebox.api.VoiceBoxApi;
import com.iot.shcs.voicebox.vo.VoiceBoxMqttMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/10/11 11:35
 * @Modify by:
 */

@Service
public class SecurityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);
    private static final String MQTT_SERVICE = "security";
    @Autowired
    private SmarthomeSpaceApi smarthomeSpaceApi;
    @Autowired
    private SecurityApi securityApi;
    @Autowired
    private VoiceBoxApi voiceBoxApi;

    /**
     * 安防状态查询
     */
    public MqttMsg getStatusReq(Long userId, String userUuid) {
        Long tenantId = SaaSContextHolder.currentTenantId();
        SpaceResp defaultSpace = smarthomeSpaceApi.findUserDefaultSpace(userId, tenantId);
        if (defaultSpace == null) {
            throw new RobotException(ErrorCodeKeys.INVALID_USER);
        }

        // 直连设备deviceId
        List<String> directDeviceUuidList = smarthomeSpaceApi.getDirectDeviceUuidBySpaceId(tenantId, defaultSpace.getId());
        if (directDeviceUuidList.size() < 1) {
            LOGGER.error("***** getStatusReq, directDeviceUuidList.size=0");
            throw new RobotException(ErrorCodeKeys.UNBOUND_GATEWAY);
        }

        // 填充mqtt数据
        String seq = VoiceBoxUtil.generateRobotSeq();

        MqttMsg mqttMsgReq = new MqttMsg();
        mqttMsgReq.setService(MQTT_SERVICE);
        mqttMsgReq.setMethod("getStatusReq");
        mqttMsgReq.setSeq(seq);
        mqttMsgReq.setSrcAddr("0." + userUuid);

        Map<String, Object> payload = Maps.newHashMap();
        payload.put("homeId", defaultSpace.getId());
        mqttMsgReq.setPayload(payload);

        String jsonStr = JSON.toJSONString(mqttMsgReq);
        LOGGER.info("getStatusReq, jsonStr={}", jsonStr);

        // 包装到 VoiceBoxMqttMsg
        VoiceBoxMqttMsg voiceBoxMqttMsg = new VoiceBoxMqttMsg(jsonStr, userUuid);

        try {
            LockObject lo = new LockObject(seq);
            synchronized (lo) {
                LockHelper.put(seq, lo);

                voiceBoxApi.getStatusReq(voiceBoxMqttMsg);

                // 挂起
                LOGGER.info("getStatusReq, lock id={}, LockObject start", lo.getId());
                // 最多等待5秒
                lo.wait(5000);
                LOGGER.info("getStatusReq, lock id={}, LockObject end, costTime = {}", lo.getId(), lo.costTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RobotException(ErrorCodeKeys.UN_KNOW_ERROR);
        }

        Object respObj = LockHelper.remove(seq);
        if (respObj == null) {
            // 超时
            throw new RobotException(ErrorCodeKeys.TIMEOUT);
        }

        if (respObj instanceof MqttMsg) {
            // resp返回的数据
            return (MqttMsg) respObj;
        } else {
            // 超时
            throw new RobotException(ErrorCodeKeys.TIMEOUT);
        }
    }

    /**
     * 布置安防请求
     *
     * @param userId
     * @param userUuid
     * @param securityCommand
     * @return
     */
    public MqttMsg setArmModeReq(Long userId, String userUuid, SecurityCommand securityCommand) {
        if (securityCommand == null ||
                StringUtil.isBlank(securityCommand.getCommand()) ||
                StringUtil.isBlank(securityCommand.getArmMode())) {
            LOGGER.info("setArmModeReq, securityCommand is not correct, securityCommand={}", JSON.toJSONString(securityCommand));
        }

        Long tenantId = SaaSContextHolder.currentTenantId();
        SpaceResp defaultSpace = smarthomeSpaceApi.findUserDefaultSpace(userId, tenantId);
        if (defaultSpace == null) {
            throw new RobotException(ErrorCodeKeys.INVALID_USER);
        }

        // 直连设备deviceId
        List<String> directDeviceUuidList = smarthomeSpaceApi.getDirectDeviceUuidBySpaceId(tenantId, defaultSpace.getId());
        if (directDeviceUuidList.size() < 1) {
            LOGGER.error("***** setArmModeReq, directDeviceUuidList.size=0");
            throw new RobotException(ErrorCodeKeys.UNBOUND_GATEWAY);
        }

        String armMode = securityCommand.getArmMode();
        if ("disarm".equals(securityCommand.getCommand())) {
            // 校验撤防口令
            SecurityResp securityResp = securityApi.getBySpaceId(defaultSpace.getId());
            String pw = MD5Utils.to32String(securityCommand.getPassword());
            if (!securityResp.getPassword().equals(pw)) {
                throw new RobotException(ErrorCodeKeys.PWD_ERROR);
            }

            armMode = "off";
        }

        // 填充mqtt数据
        String seq = VoiceBoxUtil.generateRobotSeq();

        MqttMsg mqttMsgReq = new MqttMsg();
        mqttMsgReq.setService(MQTT_SERVICE);
        mqttMsgReq.setMethod("setArmModeReq");
        mqttMsgReq.setSeq(seq);
        mqttMsgReq.setSrcAddr("0." + userUuid);

        Map<String, Object> payload = Maps.newHashMap();
        payload.put("homeId", defaultSpace.getId());
        payload.put("armMode", armMode);
        mqttMsgReq.setPayload(payload);

        String jsonStr = JSON.toJSONString(mqttMsgReq);
        LOGGER.info("setArmModeReq, jsonStr={}", jsonStr);

        // 包装到 VoiceBoxMqttMsg
        VoiceBoxMqttMsg voiceBoxMqttMsg = new VoiceBoxMqttMsg(jsonStr, userUuid);

        try {
            LockObject lo = new LockObject(seq);
            synchronized (lo) {
                LockHelper.put(seq, lo);

                voiceBoxApi.setArmModeReq(voiceBoxMqttMsg);

                // 挂起
                LOGGER.info("setArmModeReq, lock id={}, LockObject start", lo.getId());
                // 最多等待5秒
                lo.wait(5000);
                LOGGER.info("setArmModeReq, lock id={}, LockObject end, costTime = {}", lo.getId(), lo.costTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RobotException(ErrorCodeKeys.UN_KNOW_ERROR);
        }

        Object respObj = LockHelper.remove(seq);
        if (respObj == null) {
            // 超时
            throw new RobotException(ErrorCodeKeys.TIMEOUT);
        }

        if (respObj instanceof MqttMsg) {
            // resp返回的数据
            return (MqttMsg) respObj;
        } else {
            // 超时
            throw new RobotException(ErrorCodeKeys.TIMEOUT);
        }
    }
}

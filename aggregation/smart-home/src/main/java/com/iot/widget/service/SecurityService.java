package com.iot.widget.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.shcs.space.api.SmarthomeSpaceApi;
import com.iot.shcs.voicebox.api.VoiceBoxApi;
import com.iot.shcs.voicebox.vo.VoiceBoxMqttMsg;
import com.iot.widget.exception.UserWidgetExceptionEnum;
import com.iot.widget.utils.LockHelper;
import com.iot.widget.utils.LockObject;
import com.iot.widget.utils.WidgetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/7 14:50
 * 修改人:
 * 修改时间：
 */

@Service("widgetSecurityService")
public class SecurityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);

    @Autowired
    private SmarthomeSpaceApi smarthomeSpaceApi;
    @Autowired
    private VoiceBoxApi voiceBoxApi;

    /**
     *  切换安防模式
     *
     * @param tenantId
     * @param userUuid
     * @param homeId
     * @param armMode
     */
    public Map<String, Object> setArmModeReq(Long tenantId, String userUuid, String homeId, String armMode) {
        Long homeIdLong = null;
        try {
            homeIdLong = Long.parseLong(homeId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(UserWidgetExceptionEnum.PARAMETER_ERROR);
        }

        // 直连设备deviceId
        List<String> directDeviceUuidList = smarthomeSpaceApi.getDirectDeviceUuidBySpaceId(tenantId, homeIdLong);
        if (directDeviceUuidList.size() < 1) {
            LOGGER.error("***** setArmModeReq, directDeviceUuidList.size=0");
            throw new BusinessException(UserWidgetExceptionEnum.UNBOUND_GATEWAY);
        }

        // 填充mqtt数据
        String seq = WidgetUtil.generateRobotSeq();

        MqttMsg mqttMsgReq = new MqttMsg();
        mqttMsgReq.setService("security");
        mqttMsgReq.setMethod("setArmModeReq");
        mqttMsgReq.setSeq(seq);
        mqttMsgReq.setSrcAddr("0." + userUuid);

        Map<String, Object> payload = Maps.newHashMap();
        payload.put("homeId", homeIdLong);
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
            throw new BusinessException(UserWidgetExceptionEnum.UN_KNOWN_ERROR);
        }

        Object respObj = LockHelper.remove(seq);
        if (respObj == null) {
            // 超时
            throw new BusinessException(UserWidgetExceptionEnum.TIMEOUT);
        }

        if (respObj instanceof MqttMsg) {
            // resp返回的数据
            Object respPayload = ((MqttMsg) respObj).getPayload();
            return (Map<String, Object>)respPayload;
        } else {
            // 超时
            throw new BusinessException(UserWidgetExceptionEnum.TIMEOUT);
        }
    }
}

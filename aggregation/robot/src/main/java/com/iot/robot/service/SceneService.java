package com.iot.robot.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.robot.common.exception.RobotException;
import com.iot.robot.utils.ErrorCodeKeys;
import com.iot.robot.utils.LockHelper;
import com.iot.robot.utils.LockObject;
import com.iot.robot.utils.VoiceBoxUtil;
import com.iot.shcs.voicebox.api.VoiceBoxApi;
import com.iot.shcs.voicebox.vo.VoiceBoxMqttMsg;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Descrpiton: scene相关操作service
 * @Author: yuChangXing
 * @Date: 2018/10/8 11:47
 * @Modify by:
 */

@Service
public class SceneService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SceneService.class);

    @Autowired
    private SceneApi sceneApi;
    @Autowired
    private VoiceBoxApi voiceBoxApi;

    /**
     * 获取用户的 scene信息
     *
     * @param userId
     * @return
     */
    public List<SceneResp> findSceneRespListByUserId(Long tenantId, Long userId) {
        SceneReq sceneReq = new SceneReq();
        sceneReq.setCreateBy(userId);
        sceneReq.setTenantId(tenantId);
        return sceneApi.sceneByParam(sceneReq);
    }

    /**
     * 执行场景请求
     */
    public void excSceneReq(String sceneId, String userUuid, Long tenantId) {
        Long sceneIdLong = null;
        try {
            sceneIdLong = Long.parseLong(sceneId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RobotException(ErrorCodeKeys.DEVICE_NOT_FOUND);
        }

        // 判断scene是否存在
        SceneReq sceneReq = new SceneReq();
        sceneReq.setId(sceneIdLong);
        sceneReq.setTenantId(tenantId);
        SceneResp sceneResp = sceneApi.sceneById(sceneReq);
        if (sceneResp == null) {
            LOGGER.info("excSceneReq, sceneId={} sceneResp is null");
            throw new RobotException(ErrorCodeKeys.DEVICE_NOT_FOUND);
        }

        // 判断scene是否有明细
        SceneDetailReq sceneDetailReq = new SceneDetailReq();
        sceneDetailReq.setSceneId(sceneIdLong);
        sceneDetailReq.setTenantId(tenantId);
        List<SceneDetailResp> sceneDetailRespList = sceneApi.sceneDetailByParam(sceneDetailReq);
        if (CollectionUtils.isEmpty(sceneDetailRespList)) {
            LOGGER.info("excSceneReq, sceneId={} sceneDetailRespList is empty");
            throw new RobotException(ErrorCodeKeys.NOT_SUPPORTED, "Scene is not associated with any device.");
        }

        // 填充mqtt数据
        String seq = VoiceBoxUtil.generateRobotSeq();

        MqttMsg mqttMsgReq = new MqttMsg();
        mqttMsgReq.setService("scene");
        mqttMsgReq.setMethod("excSceneReq");
        mqttMsgReq.setSeq(seq);
        mqttMsgReq.setSrcAddr("0." + userUuid);

        Map<String, Object> payload = Maps.newHashMap();
        payload.put("sceneId", sceneId);
        mqttMsgReq.setPayload(payload);

        String jsonStr = JSON.toJSONString(mqttMsgReq);
        LOGGER.info("excSceneReq, jsonStr={}", jsonStr);

        // 包装到 VoiceBoxMqttMsg
        VoiceBoxMqttMsg voiceBoxMqttMsg = new VoiceBoxMqttMsg(jsonStr, userUuid);

        try {
            LockObject lo = new LockObject(seq);
            synchronized (lo) {
                LockHelper.put(seq, lo);

                voiceBoxApi.excSceneReq(voiceBoxMqttMsg);

                // 挂起
                LOGGER.info("excSceneReq, lock id={}, LockObject start", lo.getId());
                // 最多等待5秒
                lo.wait(5000);
                LOGGER.info("excSceneReq, lock id={}, LockObject end, costTime = {}", lo.getId(), lo.costTime());
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
            MqttMsg mqttMsgResp = (MqttMsg) respObj;
            if (mqttMsgResp.getAck() != null && mqttMsgResp.getAck().getCode() != MqttMsgAck.SUCCESS) {
                throw new RobotException(ErrorCodeKeys.UN_KNOW_ERROR, mqttMsgResp.getAck().getDesc());
            }
        } else {
            // 超时
            throw new RobotException(ErrorCodeKeys.TIMEOUT);
        }
    }
}

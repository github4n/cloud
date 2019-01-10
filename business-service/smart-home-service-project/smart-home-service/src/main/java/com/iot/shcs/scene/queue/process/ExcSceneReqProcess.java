package com.iot.shcs.scene.queue.process;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.common.util.MQTTUtils;
import com.iot.shcs.helper.Constants;
import com.iot.shcs.listener.MQTTClientListener;
import com.iot.shcs.scene.queue.bean.ExcSceneMessage;
import com.iot.shcs.scene.service.SceneService;
import com.iot.shcs.scene.service.impl.SceneMQTTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class ExcSceneReqProcess extends AbsMessageProcess<ExcSceneMessage> {

    private static final Logger logger = LoggerFactory.getLogger(ExcSceneReqProcess.class);
    private static final int QOS = 1;
    private final String SCENE_ID = "sceneId";

    @Autowired
    private MqttSdkService mqttSdkService;

    @Autowired
    private SceneService sceneService;

    @Override
    public void processMessage(ExcSceneMessage message) {
        logger.info("excSceneReq: ({})", JSONObject.toJSONString(message));
        MqttMsg mqttMsg = message.getData();
        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        Long sceneId = MQTTUtils.getMustLong(payload, SCENE_ID);
        CommonResponse commonResponse = sceneService.excScene(sceneId, mqttMsg.getSeq(), message.getTenantId());
        if (commonResponse.getCode() != ResultMsg.SUCCESS.getCode()) {
            MqttMsg result = new MqttMsg();
            MqttMsgAck ack = MqttMsgAck.failureAck(commonResponse.getCode(), commonResponse.getDesc());
            result.setAck(ack);
            result.setMethod("excSceneResp");
            result.setService("scene");
            result.setSeq(mqttMsg.getSeq());
            result.setPayload(mqttMsg.getPayload());
            //循环获取deviceid
            List<String> list = sceneService.findSceneDirectDeviceUuidListBySceneId(sceneId,message.getTenantId());
            for (String directDeviceId : list) {
                logger.info("---------topic:" + Constants.TOPIC_CLIENT_PREFIX + directDeviceId + "====msg:" + result);

                result.setTopic(Constants.TOPIC_CLIENT_PREFIX + directDeviceId + "/scene/" + Constants.EXC_SCENEREQ);

                result.setSrcAddr("0." + directDeviceId);

                mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), result, QOS);

                logger.info("---------发送完成----------");
            }
        }
    }
}

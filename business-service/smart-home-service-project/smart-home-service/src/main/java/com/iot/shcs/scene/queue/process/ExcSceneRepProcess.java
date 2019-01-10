package com.iot.shcs.scene.queue.process;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.control.scene.exception.SceneExceptionEnum;
import com.iot.device.api.DeviceApi;
import com.iot.ifttt.api.IftttAccessApi;
import com.iot.ifttt.common.ServiceEnum;
import com.iot.ifttt.vo.CheckNotifyReq;
import com.iot.locale.LocaleMessageSourceService;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.shcs.common.exception.BusinessExceptionEnum;
import com.iot.shcs.common.util.MQTTUtils;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.helper.Constants;
import com.iot.shcs.listener.MQTTClientListener;
import com.iot.shcs.scene.queue.bean.ExcSceneMessage;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class ExcSceneRepProcess extends AbsMessageProcess<ExcSceneMessage> {

    private static final Logger logger = LoggerFactory.getLogger(ExcSceneRepProcess.class);
    private static final int QOS = 1;
    private final String SCENE_ID = "sceneId";

    @Autowired
    private MqttSdkService mqttSdkService;

    @Autowired
    private DeviceApi deviceApi;

    @Autowired
    private DeviceCoreService deviceCoreService;

    @Autowired
    private UserApi userApi;

    @Autowired
    private LocaleMessageSourceService localeMessageSourceService;

    @Autowired
    private IftttAccessApi iftttAccessApi;

    @Override
    public void processMessage(ExcSceneMessage message) {
        logger.info("excSceneRep({}, {})", JSONObject.toJSONString(message.getData()),JSONObject.toJSONString(message.getTopic()));
        MqttMsg mqttMsg = message.getData();
        String topic = message.getTopic();
        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        String sceneId =  String.valueOf(payload.get("sceneId"));
        MqttMsgAck ack = MqttMsgAck.successAck();
        String deviceUuid = MQTTUtils.parseReqTopic(topic);
        String userUuid = null;

        try {
            FetchUserResp userResp = getUserByDeviceUuid(new Long(message.getTenantId()),deviceUuid);
            userUuid = userResp.getUuid();

            ack.setCode(mqttMsg.getAck().getCode());
            ack.setDesc(mqttMsg.getAck().getDesc());
        } catch (BusinessException e) {
            ack.setCode(e.getCode());
            ack.setDesc(localeMessageSourceService.getMessage(e.getMessage()));
        } catch (Exception e) {
            logger.error("excSceneResp-system-error", e);
            ack.setCode(MqttMsgAck.ERROR);
            ack.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            mqttMsg.setTopic(Constants.TOPIC_CLIENT_PREFIX + userUuid + "/scene/excSceneResp");
            mqttMsg.setMethod("excSceneResp");
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);

            //情景执行判断
            CheckNotifyReq checkNotifyReq = new CheckNotifyReq();
            Map<String,Object> fields = Maps.newHashMap();
            fields.put("sceneId",sceneId);
            checkNotifyReq.setFields(fields);
            checkNotifyReq.setType(ServiceEnum.SCENE.getCode());
            iftttAccessApi.checkNotify(checkNotifyReq);
        }
    }

    /**
     * 获取 用户
     *
     * @param deviceUuid
     * @return
     */
    private FetchUserResp getUserByDeviceUuid(Long tenantId,String deviceUuid) {
        List<ListUserDeviceInfoRespVo> userDeviceInfoRespList = deviceCoreService.listUserDevices(tenantId,deviceUuid);
        if (CollectionUtils.isEmpty(userDeviceInfoRespList)) {
            logger.error("***** getUserByDeviceUuid() error! userDeviceInfoRespList is empty");
            throw new BusinessException(SceneExceptionEnum.USER_UNBOUND_GATEWAY);
        }

        return userApi.getUser(userDeviceInfoRespList.get(0).getUserId());
    }
}

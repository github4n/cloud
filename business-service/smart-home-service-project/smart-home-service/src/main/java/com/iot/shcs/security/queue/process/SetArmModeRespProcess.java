package com.iot.shcs.security.queue.process;

import com.google.common.collect.Maps;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.ifttt.api.IftttAccessApi;
import com.iot.ifttt.common.ServiceEnum;
import com.iot.ifttt.vo.CheckNotifyReq;
import com.iot.locale.LocaleMessageSourceService;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.shcs.common.exception.BusinessExceptionEnum;
import com.iot.shcs.common.util.MQTTUtils;
import com.iot.shcs.helper.Constants;
import com.iot.shcs.listener.MQTTClientListener;
import com.iot.shcs.security.queue.bean.SetArmModeRespMessage;
import com.iot.user.api.UserApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SetArmModeRespProcess extends AbsMessageProcess<SetArmModeRespMessage> {

    private static final Logger logger = LoggerFactory.getLogger(SetArmModeRespProcess.class);
    private static final int QOS = 1;
    @Autowired
    private MqttSdkService mqttSdkService;
    @Autowired
    private LocaleMessageSourceService localeMessageSourceService;
    @Autowired
    private IftttAccessApi iftttAccessApi;
    @Autowired
    private UserApi userApi;

    @Override
    public void processMessage(SetArmModeRespMessage message) {
        logger.info("setArmModeResp({}, {})", message.getMqttMsg(), message.getTopic());
        MqttMsg mqttMsg = message.getMqttMsg();
        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        String topic = message.getTopic();
        String userUuid = message.getUserUuid();
        String deviceUuid = message.getDeviceUuid();

        MqttMsgAck ack = MqttMsgAck.successAck();

        try {
            if (mqttMsg.getAck() != null && mqttMsg.getAck().getCode() == 200) {

                // ********** 云端不需要操作，以 8.5安防状态更新通知 为准(只在这个里面 执行“布置安防模式”) **********
                //安防类型设置通知 IFTTT
                CheckNotifyReq checkNotifyReq = new CheckNotifyReq();
                checkNotifyReq.setType(ServiceEnum.SECURITY_MODE.getCode());
                Map<String, Object> fields = Maps.newHashMap();
                fields.put("userId", String.valueOf(userApi.getUserId(userUuid)));
                fields.put("status", payload.get("status"));
                checkNotifyReq.setFields(fields);
                iftttAccessApi.checkNotify(checkNotifyReq);
            } else {
                ack = mqttMsg.getAck() != null ? mqttMsg.getAck() : MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, "system unknown ex");
            }

        } catch (Exception e) {
            logger.error("setArmModeResp-system-error", e);
            ack = MqttMsgAck.failureAck(MqttMsgAck.ERROR, BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            //响应给app用户
            mqttMsg.setTopic(buildClientSecurityTopic(MQTTUtils.getMethodFromTopic(topic), userUuid));
            mqttMsg.setAck(ack);
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }
    }

    private String buildClientSecurityTopic(String method, String objUUid) {
        return Constants.TOPIC_CLIENT_PREFIX + objUUid + "/" + "security" + "/" + method;
    }
}

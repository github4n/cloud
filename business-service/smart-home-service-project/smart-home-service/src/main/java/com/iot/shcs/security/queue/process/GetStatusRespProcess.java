package com.iot.shcs.security.queue.process;

import com.iot.common.exception.BusinessException;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.locale.LocaleMessageSourceService;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.shcs.common.exception.BusinessExceptionEnum;
import com.iot.shcs.common.util.MQTTUtils;
import com.iot.shcs.helper.Constants;
import com.iot.shcs.listener.MQTTClientListener;
import com.iot.shcs.security.queue.bean.GetStatusRespMessage;
import com.iot.user.vo.FetchUserResp;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GetStatusRespProcess extends AbsMessageProcess<GetStatusRespMessage> {

    private static final Logger logger = LoggerFactory.getLogger(GetStatusRespProcess.class);

    private static final int QOS = 1;
    @Autowired
    private MqttSdkService mqttSdkService;
    @Autowired
    private LocaleMessageSourceService localeMessageSourceService;

    @Override
    public void processMessage(GetStatusRespMessage message) {
        logger.info("getStatusResp({}, {})", message.getMqttMsg(), message.getTopic());
        MqttMsg mqttMsg = message.getMqttMsg();
        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        String topic=message.getTopic();
        String userUuid=message.getUserUuid();
        String deviceUuid=message.getDeviceUuid();
        try {
            if (mqttMsg.getAck().getCode() == 200) {
                // 安防状态, 0:撤防,1:在家布防,3:离家布防,4:离家布防延迟,5:在家布防延迟,6:未就绪
                //11.在家延时报警，12.离家延时报警
                String status = String.valueOf(payload.get("status"));
                logger.info("*****getStatusResp() 获取到的 status={}", status);

                if ("4".equals(status) || "5".equals(status)) {
                    // 布防延迟剩余时间(秒), 当status=4或5时, remaining有效.其他状态,可以不回该字段
                    int remaining = MQTTUtils.getMustInteger(payload, "remaining");
                    logger.info("*****getStatusResp() 获取到的 remaining={}", remaining);
                }
                if ("11".equals(status) || "12".equals(status)) {
                    // 布防延迟剩余时间(秒), 当status=4或5时, remaining有效.其他状态,可以不回该字段
                    int delay = MQTTUtils.getMustInteger(payload, "delay");
                    logger.info("*****getStatusResp() 获取到的 remaining={}", delay);
                }

            } else {
                logger.error("*****getStatusResp() error! --> errorCode={}, errorMsg={}", mqttMsg.getAck().getCode(), mqttMsg.getAck().getDesc());
            }

        } catch (BusinessException e) {
            mqttMsg.setAck(MqttMsgAck.failureAck(e.getCode(), localeMessageSourceService.getMessage(e.getMessage())));
        } catch (Exception e) {
            e.printStackTrace();
            mqttMsg.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey()));
        } finally {
            mqttMsg.setTopic(buildClientSecurityTopic(MQTTUtils.getMethodFromTopic(topic), userUuid));
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }
    }
    /**
     * 构建 security模块的 topic
     *
     * @param method  方法名
     * @param objUUid 对象uuid(userUuid、deviceUuid)
     * @return
     */
    private String buildClientSecurityTopic(String method, String objUUid) {
        return Constants.TOPIC_CLIENT_PREFIX + objUUid + "/" + "security" + "/" + method;
    }
}

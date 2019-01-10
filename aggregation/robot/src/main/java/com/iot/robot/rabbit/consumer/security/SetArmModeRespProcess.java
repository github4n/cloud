package com.iot.robot.rabbit.consumer.security;

import com.alibaba.fastjson.JSON;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.robot.utils.LockHelper;
import com.iot.shcs.security.queue.bean.SetArmModeRespMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Descrpiton: 布置安防响应 处理器
 * @Author: yuChangXing
 * @Date: 2018/10/13 14:06
 * @Modify by:
 */

@Slf4j
@Component
public class SetArmModeRespProcess extends AbsMessageProcess<SetArmModeRespMessage> {
    @Override
    public void processMessage(SetArmModeRespMessage message) {
        log.debug("***** SetArmModeRespProcess, message={}", JSON.toJSONString(message));
        try {
            Integer errorCode = message.getErroCode();
            if (errorCode != null && errorCode.compareTo(200) != 0) {
                log.debug("***** SetArmModeRespProcess, errorCode={}", errorCode);
            }

            MqttMsg mqttMsg = message.getMqttMsg();
            LockHelper.processMessage(mqttMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

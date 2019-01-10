package com.iot.widget.rabbit.consumer.scene;

import com.alibaba.fastjson.JSON;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.shcs.scene.queue.bean.ExcSceneMessage;
import com.iot.widget.utils.LockHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Descrpiton: 执行情景响应 处理器
 * @Author: yuChangXing
 * @Date: 2018/10/13 14:06
 * @Modify by:
 */

@Slf4j
@Component
public class ExcSceneRespProcess extends AbsMessageProcess<ExcSceneMessage> {
    @Override
    public void processMessage(ExcSceneMessage message) {
        try {
            log.debug("***** ExcSceneRespProcess, message.jsonString={}", JSON.toJSONString(message));
            MqttMsg mqttMsg = message.getData();
            LockHelper.processMessage(mqttMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

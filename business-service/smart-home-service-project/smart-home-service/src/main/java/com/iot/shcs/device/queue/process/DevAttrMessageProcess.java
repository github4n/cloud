package com.iot.shcs.device.queue.process;

import com.alibaba.fastjson.JSON;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.shcs.device.queue.bean.DevAttrMessage;
import com.iot.shcs.device.service.impl.DeviceMQTTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: lucky @Descrpiton: @Date: 15:34 2018/10/11 @Modify by:
 */
@Slf4j
@Component
public class DevAttrMessageProcess extends AbsMessageProcess<DevAttrMessage> {
    @Autowired
    private DeviceMQTTService deviceMQTTService;

    public void processMessage(DevAttrMessage message) {

        log.debug("消息中心接收到 dev attr 发送请求-> 信息体：{} ", JSON.toJSONString(message));
        deviceMQTTService.setDevAttrNotif(message.getMsg(), message.getReqTopic());
        log.debug("调用 {} 处理完毕，耗时 {}毫秒", new Date().getTime() - message.getCreateTime().getTime());
    }

    ;


}

package com.iot.shcs.device.queue.sender;

import com.iot.common.mq.core.AbsSender;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.shcs.device.queue.bean.DeviceDisconnectMessage;
import com.iot.shcs.device.queue.utils.DeviceQueueConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: lucky
 * @Descrpiton 设备上下线状态发送
 * @Date: 10:41 2018/8/10
 * @Modify by:
 */
@Slf4j
@Component
public class DeviceDisconnectSender extends AbsSender<DeviceDisconnectMessage> {

    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * 必须实现的
     */
    @PostConstruct
    public void init() {
        super.preInit();
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return DeviceQueueConstants.DEVICE_DISCONNECT_EXCHANGE;
    }

    @Override
    public String routing() {
        return DeviceQueueConstants.DEVICE_DISCONNECT_ROUTING;
    }

    @Override
    public String queue() {
        return null;
    }

    @Override
    public ExchangeTypeEnum type() {
        return ExchangeTypeEnum.FANOUT;
    }

}

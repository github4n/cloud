package com.iot.device.queue.consumer;


import com.iot.common.mq.core.AbsPoolConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.device.queue.bean.DeviceStateMessage;
import com.iot.device.queue.process.DeviceStateMessageProcess;
import com.iot.device.queue.utils.QueueConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 10:42 2018/8/10
 * @Modify by:
 */
@Slf4j
@Component
public class DeviceStatePoolConsumer extends AbsPoolConsumer<DeviceStateMessage> {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private DeviceStateMessageProcess deviceStateMessageProcess;

    @Override
    public Class<DeviceStateMessage> initClazz() {
        return DeviceStateMessage.class;
    }

    @PostConstruct
    public void init() {
        super.preInit();
    }

    @Override
    public AbsMessageProcess<DeviceStateMessage> messageProcess() {
        return deviceStateMessageProcess;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return QueueConstants.DEV_STATE_EXCHANGE;
    }

    @Override
    public String routing() {
        return QueueConstants.DEV_STATE_ROUTING;
    }

    @Override
    public String queue() {
        return QueueConstants.DEV_STATE_QUEUE;
    }

    @Override
    public ExchangeTypeEnum type() {
        return ExchangeTypeEnum.DIRECT;
    }

    @Override
    public int setThreadCount() {
        return 10;
    }

    @Override
    public int setIntervalMils() {
        return 0;
    }

}

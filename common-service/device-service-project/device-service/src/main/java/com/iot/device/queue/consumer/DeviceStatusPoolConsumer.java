package com.iot.device.queue.consumer;


import com.iot.common.mq.core.AbsPoolConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.device.queue.bean.DeviceStatusMessage;
import com.iot.device.queue.process.DeviceStatusMessageProcess;
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
public class DeviceStatusPoolConsumer extends AbsPoolConsumer<DeviceStatusMessage> {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private DeviceStatusMessageProcess deviceStatusMessageProcess;

    @Override
    public Class<DeviceStatusMessage> initClazz() {
        return DeviceStatusMessage.class;
    }

    @PostConstruct
    public void init() {
        super.preInit();
    }

    @Override
    public AbsMessageProcess<DeviceStatusMessage> messageProcess() {
        return deviceStatusMessageProcess;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return QueueConstants.DEV_STATUS_EXCHANGE;
    }

    @Override
    public String routing() {
        return QueueConstants.DEV_STATUS_ROUTING;
    }

    @Override
    public String queue() {
        return QueueConstants.DEV_STATUS_QUEUE;
    }

    @Override
    public ExchangeTypeEnum type() {
        return ExchangeTypeEnum.DIRECT;
    }

    @Override
    public int setThreadCount() {
        return 5;
    }

    @Override
    public int setIntervalMils() {
        return 0;
    }

}

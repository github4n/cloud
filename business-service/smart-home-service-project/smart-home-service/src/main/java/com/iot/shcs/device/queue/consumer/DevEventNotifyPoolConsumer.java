package com.iot.shcs.device.queue.consumer;


import com.iot.common.mq.core.AbsPoolConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.shcs.device.queue.bean.DevEventMessage;
import com.iot.shcs.device.queue.process.DevEventProcess;
import com.iot.shcs.device.queue.utils.DeviceQueueConstants;
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
public class DevEventNotifyPoolConsumer extends AbsPoolConsumer<DevEventMessage> {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private DevEventProcess devEventProcess;

    @Override
    public Class<DevEventMessage> initClazz() {
        return DevEventMessage.class;
    }

    @PostConstruct
    public void init() {
        super.preInit();
    }

    @Override
    public AbsMessageProcess<DevEventMessage> messageProcess() {
        return devEventProcess;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return DeviceQueueConstants.DEV_EVENT_NOTIF_EXCHANGE;
    }

    @Override
    public String routing() {
        return "";
    }

    @Override
    public String queue() {
        return DeviceQueueConstants.DEV_TAMPER_EVENT_NOTIFY_QUEUE;
    }

    @Override
    public ExchangeTypeEnum type() {
        return ExchangeTypeEnum.FANOUT;
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

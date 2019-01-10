package com.iot.shcs.device.queue.consumer;


import com.iot.common.mq.core.AbsPoolConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.shcs.device.queue.bean.SetDeviceAttrMessage;
import com.iot.shcs.device.queue.process.SetDeviceAttrProcess;
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
public class SetDeviceAttrNotifyPoolConsumer extends AbsPoolConsumer<SetDeviceAttrMessage> {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private SetDeviceAttrProcess setDeviceAttrProcess;

    @Override
    public Class<SetDeviceAttrMessage> initClazz() {
        return SetDeviceAttrMessage.class;
    }

    @PostConstruct
    public void init() {
        super.preInit();
    }

    @Override
    public AbsMessageProcess<SetDeviceAttrMessage> messageProcess() {
        return setDeviceAttrProcess;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return DeviceQueueConstants.SET_DEV_ATTR_NOTIFY_EXCHANGE;
    }

    @Override
    public String routing() {
        return "";
    }

    @Override
    public String queue() {
        return DeviceQueueConstants.TWO_C_SET_DEV_ATTR_NOTIFY_QUEUE;
    }

    @Override
    public ExchangeTypeEnum type() {
        return ExchangeTypeEnum.FANOUT;
    }

    @Override
    public int setThreadCount() {
        return 30;
    }

    @Override
    public int setIntervalMils() {
        return 0;
    }

}

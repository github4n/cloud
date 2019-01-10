package com.iot.robot.rabbit.consumer.device;

import com.iot.common.mq.core.AbsPoolConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.robot.rabbit.constant.DeviceQueueConstant;
import com.iot.shcs.device.queue.bean.DeviceDisconnectMessage;
import com.iot.shcs.device.queue.utils.DeviceQueueConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Descrpiton: 订阅 网关离线 队列
 * @Author: yuChangXing
 * @Date: 2018/10/13 13:54
 * @Modify by:
 */

@Slf4j
@Component
public class DisConnectConsumer extends AbsPoolConsumer<DeviceDisconnectMessage> {

    @Autowired
    ConnectionFactory connectionFactory;
    @Autowired
    DisConnectProcess disConnectProcess;

    @Override
    public Class<DeviceDisconnectMessage> initClazz() {
        return DeviceDisconnectMessage.class;
    }

    @Override
    public int setThreadCount() {
        return 10;
    }

    @Override
    public int setIntervalMils() {
        return 0;
    }

    @Override
    public AbsMessageProcess<DeviceDisconnectMessage> messageProcess() {
        return disConnectProcess;
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
        return "";
    }

    @Override
    public String queue() {
        return DeviceQueueConstant.DEV_DISCONNECT_QUEUE;
    }

    @Override
    public ExchangeTypeEnum type() {
        return ExchangeTypeEnum.FANOUT;
    }

    @Override
    @PostConstruct
    public void init() {
        super.preInit();
    }
}

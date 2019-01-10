package com.iot.robot.rabbit.consumer.device;

import com.iot.common.mq.core.AbsPoolConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.robot.rabbit.constant.DeviceQueueConstant;
import com.iot.shcs.device.queue.bean.DeviceAddOrUpdateMessage;
import com.iot.shcs.device.queue.utils.DeviceQueueConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Descrpiton: 订阅 设备新增、更新 队列
 * @Author: yuChangXing
 * @Date: 2018/10/13 13:54
 * @Modify by:
 */

@Slf4j
@Component
public class DeviceAddOrUpdateConsumer extends AbsPoolConsumer<DeviceAddOrUpdateMessage> {

    @Autowired
    ConnectionFactory connectionFactory;
    @Autowired
    DeviceAddOrUpdateProcess deviceAddOrUpdateProcess;

    @Override
    public Class<DeviceAddOrUpdateMessage> initClazz() {
        return DeviceAddOrUpdateMessage.class;
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
    public AbsMessageProcess<DeviceAddOrUpdateMessage> messageProcess() {
        return deviceAddOrUpdateProcess;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return DeviceQueueConstants.DEVICE_ADD_OR_UPDATE_EXCHANGE;
    }

    @Override
    public String routing() {
        return "";
    }

    @Override
    public String queue() {
        return DeviceQueueConstant.DEVICE_ADD_OR_UPDATE_QUEUE;
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

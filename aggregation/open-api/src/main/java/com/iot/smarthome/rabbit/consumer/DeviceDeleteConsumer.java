package com.iot.smarthome.rabbit.consumer;

import com.iot.common.mq.core.AbsPoolConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.common.util.StringUtil;
import com.iot.shcs.device.queue.bean.DeviceDeleteMessage;
import com.iot.shcs.device.queue.utils.DeviceQueueConstants;
import com.iot.smarthome.rabbit.constant.DeviceQueueConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Descrpiton: 订阅 设备删除 队列
 * @Author: yuChangXing
 * @Date: 2018/10/13 13:54
 * @Modify by:
 */

@Slf4j
@Component
public class DeviceDeleteConsumer extends AbsPoolConsumer<DeviceDeleteMessage> {

    @Autowired
    ConnectionFactory connectionFactory;
    @Autowired
    DeviceDeleteProcess deviceDeleteProcess;

    @Override
    public Class<DeviceDeleteMessage> initClazz() {
        return DeviceDeleteMessage.class;
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
    public AbsMessageProcess<DeviceDeleteMessage> messageProcess() {
        return deviceDeleteProcess;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return DeviceQueueConstants.DEVICE_DELETE_EXCHANGE;
    }

    @Override
    public String routing() {
        return "";
    }

    @Override
    public String queue() {
        return DeviceQueueConstant.DEVICE_DELETE_QUEUE;
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

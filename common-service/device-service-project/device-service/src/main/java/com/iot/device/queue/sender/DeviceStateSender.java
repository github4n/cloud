package com.iot.device.queue.sender;

import com.iot.common.mq.core.AbsSender;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.device.queue.bean.DeviceStateMessage;
import com.iot.device.queue.utils.QueueConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: lucky @Descrpiton: @Date: 16:42 2018/8/10 @Modify by:
 */
@Slf4j
@Component
public class DeviceStateSender extends AbsSender<DeviceStateMessage> {

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

}

package com.iot.shcs.common.queue.sender;

import com.iot.common.mq.core.AbsSender;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.shcs.common.queue.bean.DemoMessage;
import com.iot.shcs.common.queue.utils.DemoQueueConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: lucky @Descrpiton: @Date: 9:32 2018/8/16 @Modify by:
 */
@Slf4j
@Component
public class DemoSender extends AbsSender<DemoMessage> {

    @Autowired
    ConnectionFactory connectionFactory;

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
        return DemoQueueConstants.EXCHANGE;
    }

    @Override
    public String routing() {
        return DemoQueueConstants.ROUTING;
    }

    @Override
    public String queue() {
        return DemoQueueConstants.QUEUE;
    }

    @Override
    public ExchangeTypeEnum type() {
        return ExchangeTypeEnum.DIRECT;
    }

}

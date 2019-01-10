package com.iot.shcs.device.queue.consumer;


import com.iot.common.mq.core.AbsPoolConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.shcs.device.queue.bean.EmailTemplateMessage;
import com.iot.shcs.device.queue.process.EmailTemplateMessageProcess;
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
public class EmailPoolConsumer extends AbsPoolConsumer<EmailTemplateMessage> {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private EmailTemplateMessageProcess emailTemplateMessageProcess;

    @Override
    public Class<EmailTemplateMessage> initClazz() {
        return EmailTemplateMessage.class;
    }

    @PostConstruct
    public void init() {
        super.preInit();
    }

    @Override
    public AbsMessageProcess<EmailTemplateMessage> messageProcess() {
        return emailTemplateMessageProcess;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return DeviceQueueConstants.DEV_EMAIL_WARN_EXCHANGE;
    }

    @Override
    public String routing() {
        return DeviceQueueConstants.DEV_EMAIL_WARN_ROUTING;
    }

    @Override
    public String queue() {
        return DeviceQueueConstants.DEV_EMAIL_WARN_QUEUE;
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

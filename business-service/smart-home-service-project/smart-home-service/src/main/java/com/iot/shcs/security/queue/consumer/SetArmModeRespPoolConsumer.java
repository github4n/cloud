package com.iot.shcs.security.queue.consumer;

import com.iot.common.mq.core.AbsPoolConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.shcs.security.queue.bean.GetStatusRespMessage;
import com.iot.shcs.security.queue.bean.SetArmModeRespMessage;
import com.iot.shcs.security.queue.process.GetStatusRespProcess;
import com.iot.shcs.security.queue.process.SetArmModeRespProcess;
import com.iot.shcs.security.queue.utils.SecurityQueueConstants;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SetArmModeRespPoolConsumer extends AbsPoolConsumer<SetArmModeRespMessage> {
    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private SetArmModeRespProcess setArmModeRespProcess;

    @Override
    public Class<SetArmModeRespMessage> initClazz() {
        return SetArmModeRespMessage.class;
    }

    @PostConstruct
    public void init() {
        super.preInit();
    }

    @Override
    public AbsMessageProcess<SetArmModeRespMessage> messageProcess() {
        return setArmModeRespProcess;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return SecurityQueueConstants.SET_ARM_MODE_RESP_EXCHANGE;
    }

    @Override
    public String routing() {
        return "";
    }

    @Override
    public String queue() {
        return SecurityQueueConstants.TWO_C_SET_ARM_MODE_RESP_QUEUE;
    }

    @Override
    public ExchangeTypeEnum type() {
        return ExchangeTypeEnum.FANOUT;
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

package com.iot.building.ifttt.service.impl;


import com.iot.common.mq.core.AbsPoolConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.ifttt.common.ExchangeConstants;
import com.iot.ifttt.vo.ActionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

/**
 * 声明队列
 * 描述：ifttt消息接收器
 * 创建人： huangxu
 * 创建时间：  2018/10/16
 */
@Slf4j
@Component
public class IftttTobPoolConsumer extends AbsPoolConsumer<ActionMessage> {

    @Autowired
    ConnectionFactory connectionFactory;

    @Autowired
    private IftttMessageProcess iftttMessageProcess;

    @Override
    public Class<ActionMessage> initClazz() {
        return ActionMessage.class;
    }

    @PostConstruct
    public void init() {
        super.preInit();
    }

    @Override
    public AbsMessageProcess<ActionMessage> messageProcess() {
        return iftttMessageProcess;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return ExchangeConstants.IFTTT_EXCHANGE;
    }

    @Override
    public String routing() {
        return "";
    }

    @Override
    public String queue() {
        return QueueConstants.IFTTT_QUEUE;
    }

    @Override
    public ExchangeTypeEnum type() {
        return ExchangeTypeEnum.FANOUT;
    }

    @Override
    public int setThreadCount() {
        return 0;
    }

    @Override
    public int setIntervalMils() {
        return 0;
    }

}

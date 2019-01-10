package com.iot.user.rabbit.consumer;

import com.iot.common.mq.core.AbsPoolConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.user.rabbit.bean.UserLoginAfterMessage;
import com.iot.user.rabbit.constant.UserQueueConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Descrpiton: 订阅 userLoginAfter 队列
 * @Author: yuChangXing
 * @Date: 2018/10/13 14:34
 * @Modify by:
 */

@Slf4j
@Component
public class UserLoginAfterConsumer extends AbsPoolConsumer<UserLoginAfterMessage> {

    @Autowired
    ConnectionFactory connectionFactory;
    @Autowired
    private UserLoginAfterProcess userLoginAfterProcess;

    @Override
    public int setThreadCount() {
        return 1;
    }

    @Override
    public int setIntervalMils() {
        return 0;
    }

    @Override
    public Class<UserLoginAfterMessage> initClazz() {
        return UserLoginAfterMessage.class;
    }

    @Override
    public AbsMessageProcess<UserLoginAfterMessage> messageProcess() {
        return userLoginAfterProcess;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return UserQueueConstant.USER_LOGIN_AFTER_EXCHANGE;
    }

    @Override
    public String routing() {
        return UserQueueConstant.USER_LOGIN_AFTER_ROUTING;
    }

    @Override
    public String queue() {
        return UserQueueConstant.USER_LOGIN_AFTER_QUEUE;
    }

    @Override
    public ExchangeTypeEnum type() {
        return ExchangeTypeEnum.DIRECT;
    }

    @Override
    @PostConstruct
    public void init() {
        super.preInit();
    }
}

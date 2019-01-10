package com.iot.user.rabbit.sender;

import com.iot.common.mq.core.AbsSender;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.user.rabbit.bean.UserLoginAfterMessage;
import com.iot.user.rabbit.constant.UserQueueConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/10/15 13:49
 * @Modify by:
 */

@Slf4j
@Component
public class UserLoginAfterSender extends AbsSender<UserLoginAfterMessage> {
    @Autowired
    private ConnectionFactory connectionFactory;

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

    @PostConstruct
    @Override
    public void init() {
        super.preInit();
    }
}

package com.iot.shcs.security.queue.sender;

import com.iot.common.mq.core.AbsSender;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.shcs.security.queue.bean.GetStatusRespMessage;
import com.iot.shcs.security.queue.utils.SecurityQueueConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class GetStatusRespSender extends AbsSender<GetStatusRespMessage> {

    @Autowired
    private ConnectionFactory connectionFactory;

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
        return SecurityQueueConstants.GET_STATUS_RESP_EXCHANGE;
    }

    @Override
    public String routing() {
        return "";
    }

    @Override
    public String queue() {
        return null;
    }

    @Override
    public ExchangeTypeEnum type() {
        return ExchangeTypeEnum.FANOUT;
    }


}

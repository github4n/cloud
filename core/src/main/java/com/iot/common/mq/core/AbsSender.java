package com.iot.common.mq.core;

import com.alibaba.fastjson.JSON;
import com.iot.common.mq.MQAccessBuilder;
import com.iot.common.mq.bean.BaseMessageEntity;
import com.iot.common.mq.sender.MessageSender;
import com.iot.common.util.StringUtil;
import com.iot.saas.SaaSContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 17:57 2018/8/31
 * @Modify by:
 */
@Slf4j
public abstract class AbsSender<T extends BaseMessageEntity> extends BaseAccessBuilder {


    private MessageSender messageSender;

    public void preInit() {
        try {
            if (connectionFactory() == null) {
                log.debug("connectionFactory not null");
                return;
            }
            MQAccessBuilder mqAccessBuilder = new MQAccessBuilder(connectionFactory(), getEnvironment());
            if (StringUtils.isEmpty(getExchange())) {
                log.debug("exchange not null");
                return;
            }
            messageSender = mqAccessBuilder.buildMessageSender(getExchange(), routing(), queue(), getType());
        } catch (Exception e) {
            log.error("pre init sender error.", e);
        }
    }


    public void send(T message) {
        fillLogRequestId(message);
        messageSender.send(message);
        log.debug("rabbit send message: {}", JSON.toJSON(message));
    }

    @PreDestroy
    public void destory(){
        messageSender.close();
    }

    private void fillLogRequestId(T message) {
        String logRequestId = SaaSContextHolder.getLogRequestId();
        if (StringUtil.isNotEmpty(logRequestId)) {
            message.setLogRequestId(logRequestId);
        }
    }
}

package com.iot.common.mq.core;

import com.iot.common.mq.MQAccessBuilder;
import com.iot.common.mq.bean.BaseMessageEntity;
import com.iot.common.mq.consumer.MessageConsumer;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.common.mq.process.DefaultMessageProcess;
import com.iot.common.mq.utils.DetailRes;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 14:06 2018/9/3
 * @Modify by:
 */
@Slf4j
public abstract class AbsConsumer<T extends BaseMessageEntity> extends BaseAccessBuilder {

    MessageConsumer messageConsumer;

    private boolean autoDelete = false;

    protected AbsMessageProcess<T> process() {
        if (messageProcess() == null) {
            return new DefaultMessageProcess();
        }
        return messageProcess();
    }

    public abstract AbsMessageProcess<T> messageProcess();

    public void preInit() {
        try {
            if (connectionFactory() == null) {
                log.debug("init connectionFactory not null");
            }
            MQAccessBuilder mqAccessBuilder = new MQAccessBuilder(connectionFactory(), getEnvironment());
            messageConsumer = mqAccessBuilder.buildMessageConsumer(getExchange(), routing(),
                    queue(), autoDelete, process(), getType(), initClazz());

        } catch (Exception e) {
            log.error("init consumer error", e);
        }
    }

    public abstract Class<T> initClazz();

    protected DetailRes consume() {
        return messageConsumer.consume();
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }
}

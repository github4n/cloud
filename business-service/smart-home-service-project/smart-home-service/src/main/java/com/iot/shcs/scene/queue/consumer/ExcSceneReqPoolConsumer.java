package com.iot.shcs.scene.queue.consumer;


import com.iot.common.mq.core.AbsPoolConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.shcs.scene.queue.bean.ExcSceneMessage;
import com.iot.shcs.scene.queue.process.ExcSceneReqProcess;
import com.iot.shcs.scene.queue.utils.SceneQueueConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * zhangyue
 */
@Slf4j
@Component
public class ExcSceneReqPoolConsumer extends AbsPoolConsumer<ExcSceneMessage> {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private ExcSceneReqProcess excSceneReqProcess;

    @Override
    public Class<ExcSceneMessage> initClazz() {
        return ExcSceneMessage.class;
    }

    @PostConstruct
    public void init() {
        super.preInit();
    }

    @Override
    public AbsMessageProcess<ExcSceneMessage> messageProcess() {
        return excSceneReqProcess;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return SceneQueueConstants.SET_SCENE_REQ_EXCHANGE;
    }

    @Override
    public String routing() {
        return "";
    }

    @Override
    public String queue() {
        return SceneQueueConstants.SET_SCENE_REQ_QUEUE;
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

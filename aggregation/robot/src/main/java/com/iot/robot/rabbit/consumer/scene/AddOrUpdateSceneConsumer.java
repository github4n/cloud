package com.iot.robot.rabbit.consumer.scene;

import com.iot.common.mq.core.AbsPoolConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.shcs.voicebox.queue.bean.SceneMessage;
import com.iot.shcs.voicebox.queue.constant.SceneQueueConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Descrpiton: 订阅 执行情景响应 队列
 * @Author: yuChangXing
 * @Date: 2018/10/13 14:34
 * @Modify by:
 */

@Slf4j
@Component
public class AddOrUpdateSceneConsumer extends AbsPoolConsumer<SceneMessage> {

    @Autowired
    ConnectionFactory connectionFactory;
    @Autowired
    private AddOrUpdateSceneProcess addOrUpdateSceneProcess;

    @Override
    public Class<SceneMessage> initClazz() {
        return SceneMessage.class;
    }

    @Override
    public int setThreadCount() {
        return 10;
    }

    @Override
    public int setIntervalMils() {
        return 0;
    }

    @Override
    public AbsMessageProcess<SceneMessage> messageProcess() {
        return addOrUpdateSceneProcess;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return SceneQueueConstant.ADD_OR_UPDATE_SCENE_EXCHANGE;
    }

    @Override
    public String routing() {
        return SceneQueueConstant.ADD_OR_UPDATE_SCENE_ROUTING;
    }

    @Override
    public String queue() {
        return SceneQueueConstant.ADD_OR_UPDATE_SCENE_QUEUE;
    }

    @Override
    public ExchangeTypeEnum type() {
        return ExchangeTypeEnum.TOPIC;
    }

    @Override
    @PostConstruct
    public void init() {
        super.preInit();
    }
}

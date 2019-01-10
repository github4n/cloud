package com.iot.widget.rabbit.consumer.scene;

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
public class DeleteSceneConsumer extends AbsPoolConsumer<SceneMessage> {

    @Autowired
    ConnectionFactory connectionFactory;
    @Autowired
    private DeleteSceneProcess deleteSceneProcess;

    @Override
    public Class<SceneMessage> initClazz() {
        return SceneMessage.class;
    }

    @Override
    public int setThreadCount() {
        return 5;
    }

    @Override
    public int setIntervalMils() {
        return 0;
    }

    @Override
    public AbsMessageProcess<SceneMessage> messageProcess() {
        return deleteSceneProcess;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return SceneQueueConstant.DELETE_SCENE_EXCHANGE;
    }

    @Override
    public String routing() {
        return "";
    }

    @Override
    public String queue() {
        return com.iot.widget.rabbit.constant.SceneQueueConstant.DELETE_SCENE_QUEUE;
    }

    @Override
    public ExchangeTypeEnum type() {
        return ExchangeTypeEnum.FANOUT;
    }

    @Override
    @PostConstruct
    public void init() {
        super.preInit();
    }
}

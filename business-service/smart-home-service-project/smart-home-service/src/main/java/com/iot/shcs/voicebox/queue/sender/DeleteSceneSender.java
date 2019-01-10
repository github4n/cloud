package com.iot.shcs.voicebox.queue.sender;

import com.iot.common.mq.core.AbsSender;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.shcs.voicebox.queue.bean.SceneMessage;
import com.iot.shcs.voicebox.queue.constant.SceneQueueConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/10/15 13:48
 * @Modify by:
 */

@Slf4j
@Component
public class DeleteSceneSender extends AbsSender<SceneMessage> {
    @Autowired
    private ConnectionFactory connectionFactory;


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
        return null;
    }

    @Override
    public ExchangeTypeEnum type() {
        return ExchangeTypeEnum.FANOUT;
    }

    @PostConstruct
    @Override
    public void init() {
        super.preInit();
    }
}

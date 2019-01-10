package com.iot.widget.rabbit.consumer.scene;

import com.iot.common.mq.core.AbsPoolConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.common.util.StringUtil;
import com.iot.shcs.scene.queue.bean.ExcSceneMessage;
import com.iot.shcs.scene.queue.utils.SceneQueueConstants;
import com.iot.widget.rabbit.constant.SceneQueueConstant;
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
public class ExcSceneRespConsumer extends AbsPoolConsumer<ExcSceneMessage> {

    @Autowired
    ConnectionFactory connectionFactory;
    @Autowired
    ExcSceneRespProcess excSceneRespProcess;

    @Override
    public Class<ExcSceneMessage> initClazz() {
        return ExcSceneMessage.class;
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
    public AbsMessageProcess<ExcSceneMessage> messageProcess() {
        return excSceneRespProcess;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return SceneQueueConstants.SET_SCENE_REP_EXCHANGE;
    }

    @Override
    public String routing() {
        return "";
    }

    @Override
    public String queue() {
        return SceneQueueConstant.EXC_SCENE_RESP_QUEUE + "_" + StringUtil.getRandomString(8);
    }

    @Override
    public ExchangeTypeEnum type() {
        return ExchangeTypeEnum.FANOUT;
    }

    @Override
    @PostConstruct
    public void init() {
        setAutoDelete(true);
        super.preInit();
    }
}

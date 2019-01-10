package com.iot.widget.rabbit.consumer.security;

import com.iot.common.mq.core.AbsPoolConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.common.util.StringUtil;
import com.iot.shcs.security.queue.bean.SetArmModeRespMessage;
import com.iot.shcs.security.queue.utils.SecurityQueueConstants;
import com.iot.widget.rabbit.constant.SecurityQueueConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Descrpiton: 订阅 布置安防响应 队列
 * @Author: yuChangXing
 * @Date: 2018/10/13 14:34
 * @Modify by:
 */

@Slf4j
@Component
public class SetArmModeRespConsumer extends AbsPoolConsumer<SetArmModeRespMessage> {

    @Autowired
    ConnectionFactory connectionFactory;
    @Autowired
    SetArmModeRespProcess setArmModeRespProcess;

    @Override
    public Class<SetArmModeRespMessage> initClazz() {
        return SetArmModeRespMessage.class;
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
    public AbsMessageProcess<SetArmModeRespMessage> messageProcess() {
        return setArmModeRespProcess;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return SecurityQueueConstants.SET_ARM_MODE_RESP_EXCHANGE;
    }

    @Override
    public String routing() {
        return "";
    }

    @Override
    public String queue() {
        return SecurityQueueConstant.SET_ARM_MODE_RESP_QUEUE + "_" + StringUtil.getRandomString(8);
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

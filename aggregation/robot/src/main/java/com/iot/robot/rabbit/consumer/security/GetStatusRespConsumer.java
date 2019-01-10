package com.iot.robot.rabbit.consumer.security;

import com.iot.common.mq.core.AbsPoolConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.common.util.StringUtil;
import com.iot.robot.rabbit.constant.SecurityQueueConstant;
import com.iot.shcs.security.queue.bean.GetStatusRespMessage;
import com.iot.shcs.security.queue.utils.SecurityQueueConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Descrpiton: 订阅 获取安防状态响应 队列
 * @Author: yuChangXing
 * @Date: 2018/10/13 14:34
 * @Modify by:
 */

@Slf4j
@Component
public class GetStatusRespConsumer extends AbsPoolConsumer<GetStatusRespMessage> {

    @Autowired
    ConnectionFactory connectionFactory;
    @Autowired
    GetStatusRespProcess getStatusRespProcess;

    @Override
    public Class<GetStatusRespMessage> initClazz() {
        return GetStatusRespMessage.class;
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
    public AbsMessageProcess<GetStatusRespMessage> messageProcess() {
        return getStatusRespProcess;
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
        return SecurityQueueConstant.GET_STATUS_RESP_QUEUE + "_" + StringUtil.getRandomString(8);
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

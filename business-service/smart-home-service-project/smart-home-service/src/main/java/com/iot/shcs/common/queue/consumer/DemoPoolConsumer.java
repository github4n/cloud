//package com.iot.shcs.common.queue.consumer;
//
//
//import com.iot.common.mq.core.AbsPoolConsumer;
//import com.iot.common.mq.enums.ExchangeTypeEnum;
//import com.iot.common.mq.process.AbsMessageProcess;
//import com.iot.shcs.common.queue.bean.DemoMessage;
//import com.iot.shcs.common.queue.process.DemoMessageProcess;
//import com.iot.shcs.common.queue.utils.DemoQueueConstants;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
///**
// * @Author: lucky
// * @Descrpiton:
// * @Date: 10:42 2018/8/10
// * @Modify by:
// */
//@Slf4j
//@Component
//public class DemoPoolConsumer extends AbsPoolConsumer<DemoMessage> {
//
//    @Autowired
//    ConnectionFactory connectionFactory;
//
//
//    @PostConstruct
//    public void init() {
//        super.preInit();
//    }
//
//    @Override
//    public AbsMessageProcess<DemoMessage> messageProcess() {
//        return new DemoMessageProcess();
//    }
//
//    @Override
//    public ConnectionFactory connectionFactory() {
//        return connectionFactory;
//    }
//
//    @Override
//    public String exchange() {
//        return DemoQueueConstants.EXCHANGE;
//    }
//
//    @Override
//    public String routing() {
//        return DemoQueueConstants.ROUTING;
//    }
//
//    @Override
//    public String queue() {
//        return DemoQueueConstants.QUEUE;
//    }
//
//    @Override
//    public ExchangeTypeEnum type() {
//        return ExchangeTypeEnum.DIRECT;
//    }
//
//    @Override
//    public int setThreadCount() {
//        return 0;
//    }
//
//    @Override
//    public int setIntervalMils() {
//        return 0;
//    }
//
//}

/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.iot.comm.listener;


import com.iot.comm.bean.MobileMsgTemplate;
import com.iot.comm.handler.SmsMessageHandler;
import com.iot.comm.utils.MqQueueConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监听服务状态改变发送请求
 */
@Component
@RabbitListener(queues = MqQueueConstant.MOBILE_SERVICE_STATUS_CHANGE)
public class MobileServiceChangeReceiveListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileServiceChangeReceiveListener.class);
    @Autowired
    private Map<String, SmsMessageHandler> messageHandlerMap;


    @RabbitHandler
    public void receive(MobileMsgTemplate mobileMsgTemplate) {
        long startTime = System.currentTimeMillis();
        LOGGER.info("消息中心接收到短信发送请求-> 手机号：{} -> 信息体：{} ", mobileMsgTemplate.getMobile(), mobileMsgTemplate.getContext());
        String channel = mobileMsgTemplate.getChannel();
        SmsMessageHandler messageHandler = messageHandlerMap.get(channel);
        if (messageHandler == null) {
            LOGGER.error("没有找到指定的路由通道，不进行发送处理完毕！");
            return;
        }

        messageHandler.execute(mobileMsgTemplate);
        long useTime = System.currentTimeMillis() - startTime;
        LOGGER.info("调用 {} 短信网关处理完毕，耗时 {}毫秒", mobileMsgTemplate.getType(), useTime);
    }
}

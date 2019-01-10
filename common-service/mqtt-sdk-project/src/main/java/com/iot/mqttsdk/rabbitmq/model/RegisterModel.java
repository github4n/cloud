package com.iot.mqttsdk.rabbitmq.model;


import com.iot.mqttsdk.rabbitmq.monitor.MQMessageProcessor;

/**
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： 490485964@qq.com
 * 创建时间：2018年04月18日 9:17
 * 修改人： 490485964@qq.com
 * 修改时间：2018年04月18日 9:17
 */
public class RegisterModel {

    private String queueUrl;

    private MQMessageProcessor mqMessageProcessor;

    public RegisterModel(String queueUrl) {
        this.queueUrl = queueUrl;
    }

    public RegisterModel(String queueUrl, MQMessageProcessor mqMessageProcessor) {
        this.queueUrl = queueUrl;
        this.mqMessageProcessor = mqMessageProcessor;
    }

    public MQMessageProcessor getMqMessageProcessor() {
        return mqMessageProcessor;
    }

    public void setMqMessageProcessor(MQMessageProcessor mqMessageProcessor) {
        this.mqMessageProcessor = mqMessageProcessor;
    }

    public String getQueueUrl() {
        return queueUrl;
    }

    public void setQueueUrl(String queueUrl) {
        this.queueUrl = queueUrl;
    }

}

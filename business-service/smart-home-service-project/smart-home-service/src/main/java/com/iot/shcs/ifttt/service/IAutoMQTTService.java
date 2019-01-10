package com.iot.shcs.ifttt.service;

import com.iot.mqttsdk.common.MqttMsg;

/**
 * 描述：联动 MQTT 服务接口
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/30 15:07
 */
public interface IAutoMQTTService {

    /**
     * 7.4 删除Automation请求
     *
     * @param message
     * @param topic
     */
    void delAutoReq(MqttMsg message, String topic);

    /**
     * 7.5 删除Automation应答
     *
     * @param message
     * @param topic
     */
    void delAutoResp(MqttMsg message, String topic);


    /**
     * 7.6 获取Automation规则请求
     *
     * @param message
     * @param topic
     */
    void getAutoRuleReq(MqttMsg message, String topic);


    /**
     * 7.8 添加Automation规则请求
     *
     * @param message
     * @param topic
     */
    void addAutoRuleReq(MqttMsg message, String topic);

    /**
     * 7.9 添加Automation规则响应
     *
     * @param message
     * @param topic
     */
    void addAutoRuleResp(MqttMsg message, String topic);

    /**
     * 7.10 编辑Automation规则请求
     *
     * @param message
     * @param topic
     */
    void editAutoRuleReq(MqttMsg message, String topic);

    /**
     * 7.11 编辑Automation规则响应
     *
     * @param message
     * @param topic
     */
    void editAutoRuleResp(MqttMsg message, String topic);

    /**
     * 7.12 设置Automation使能（开关）请求
     *
     * @param message
     * @param topic
     */
    void setAutoEnableReq(MqttMsg message, String topic);

    /**
     * 7.13 设置Automation使能（开关）响应
     *
     * @param message
     * @param topic
     */
    void setAutoEnableResp(MqttMsg message, String topic);

    /**
     * 7.16 执行Automation通知
     *
     * @param message
     * @param topic
     */
    void excAutoNotif(MqttMsg message, String topic);
}

package com.iot.listener;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.iot.callback.UserCallBackService;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.helper.Constants;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.ConfigInfo;
import com.iot.mqttsdk.mqtt.MqttProperty;

@Component
public class MQTTClientListener implements ApplicationListener<ApplicationReadyEvent> {

    public static final Logger LOGGER = LoggerFactory.getLogger(MQTTClientListener.class);

    @Autowired
    private MqttSdkService messageService;

    @Autowired
    private static Environment environment = ApplicationContextHelper.getBean(Environment.class);

    private static String mqttClientId; // 云端MQTT连接的客户端ID

    public static String getMqttClientId() {
        return mqttClientId;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent arg0) {
        // 创建mqtt连接
        initMqttConnection();

        // 注册mqtt回调
        registerMqttCallBack();

        // 订阅mqtt主题
        subscribeMqttTopic();

    }

    /**
     * 创建连接
     */
    private void initMqttConnection() {
        if (environment == null) {
            environment = ApplicationContextHelper.getBean(Environment.class);
        }
        /** 设置使用Rabbitmq拉取消息的开关 **/
        ConfigInfo configInfo = commonMqttsetting();
        /** 创建连接 **/
        messageService.createConnections(configInfo);
    }

    /**
     * common(2b 2c) mqtt参数设置
     *
     * @param rabbitmqOnOff
     * @return
     */
    private ConfigInfo commonMqttsetting() {
        ConfigInfo configInfo = new ConfigInfo();
        List<MqttProperty> mqttPropertyList = new ArrayList<MqttProperty>();
        /** 设置Mqtt连接参数 依次为：broker ip地址、clientId、用户名、密码、端口 **/
        mqttClientId = environment.getProperty(Constants.MQTT_CLIENTID);
        MqttProperty mqttProperty = new MqttProperty(environment.getProperty(Constants.MQTT_HOST), mqttClientId,
                environment.getProperty(Constants.MQTT_USERNAME), environment.getProperty(Constants.MQTT_PASSWORD),
                environment.getProperty(Constants.MQTT_PORT));
        mqttPropertyList.add(mqttProperty);
        /** Mqtt连接参数添加进configInfo **/
        configInfo.setMqttPropertyList(mqttPropertyList);
        return configInfo;
    }

    /**
     * 注册回调
     */
    private void registerMqttCallBack() {
        /** 设定队列名为device，回调实例 调用deviceMQTTService */
        messageService.addCallBack("user", ApplicationContextHelper.getBean(UserCallBackService.class));
    }

    /**
     * 订阅mqtt主题
     */
    private void subscribeMqttTopic() {
        messageService.subscribe(mqttClientId, environment.getProperty(Constants.MQTT_CLIENTBROADCAST),
                Integer.valueOf(environment.getProperty(Constants.MQTT_QOS)));

    }
}

package com.iot.building.listener;

import java.util.ArrayList;
import java.util.List;

import com.iot.building.device.service.IFTTTMQTTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.iot.building.device.service.DeviceMQTTService;
import com.iot.building.device.service.SubDeviceMQTTService;
import com.iot.building.group.service.impl.GroupMQTTService;
import com.iot.building.helper.Constants;
import com.iot.building.mqtt.local.MeetingMqttCallBack;
import com.iot.building.mqtt.network.NetWorkMqttCallBack;
import com.iot.building.ota.service.OTAMQTTService;
import com.iot.building.scene.service.impl.SceneMQTTService;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.ConfigInfo;
import com.iot.mqttsdk.mqtt.MqttProperty;
import com.iot.mqttsdk.rabbitmq.model.RabbitmqProperty;

@Component
public class MQTTClientListener implements ApplicationListener<ApplicationReadyEvent> {

    public static final Logger LOGGER = LoggerFactory.getLogger(MQTTClientListener.class);
    @Autowired
    private static Environment environment = ApplicationContextHelper.getBean(Environment.class);
    private static String mqttClientId; // 云端MQTT连接的客户端ID
    @Autowired
    private MqttSdkService messageService;

	public static String getMqttClientId() {
        return mqttClientId;
    }

    public void onApplicationEvent(ApplicationReadyEvent arg0) {
        // 创建mqtt连接
        initMqttConnection();
        // 注册mqtt回调
        registerMqttCallBack();
        // 订阅mqtt主题
        subscribeMqttTopic();
        // 中控开关
        SystembufferInit.init();
    }

    /**
     * 创建连接
     */
    protected void initMqttConnection() {
        if (environment == null) {
            environment = ApplicationContextHelper.getBean(Environment.class);
        }
        /** 设置使用Rabbitmq拉取消息的开关 **/
        Boolean rabbitmqOnOff = Boolean.valueOf(environment.getProperty(Constants.RABBIT_MQ_ON_OFF));
        ConfigInfo configInfo = commonMqttsetting(rabbitmqOnOff);
        if (rabbitmqOnOff) {
            String username = environment.getProperty(Constants.RABBIT_MQ_USERNAME);
            String pwd = environment.getProperty(Constants.RABBIT_MQ_PWD);
            Integer port = Integer.parseInt(environment.getProperty(Constants.RABBIT_MQ_PORT));
            String host = environment.getProperty(Constants.RABBIT_MQ_HOST);
            /** 设置rabbitmq连接参数，依次为用户名、密码、rabbitmq服务ip地址、端口 **/
            RabbitmqProperty rabbitmqProperty = new RabbitmqProperty(username, pwd, host, port);
            /** rabbitmq连接参数添加进configInfo **/
            configInfo.setRabbitmqProperty(rabbitmqProperty);
        }

        /** 创建连接 **/
        messageService.createConnections(configInfo);
    }

    /**
     * common(2b) mqtt参数设置
     *
     * @param rabbitmqOnOff
     * @return
     */
    private ConfigInfo commonMqttsetting(Boolean rabbitmqOnOff) {
        ConfigInfo configInfo = new ConfigInfo();
        configInfo.setUseRabbitMQ(rabbitmqOnOff);
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
        messageService.addCallBack("device", ApplicationContextHelper.getBean(DeviceMQTTService.class));
        /** 中控会议  */
		messageService.addCallBack("center", ApplicationContextHelper.getBean(MeetingMqttCallBack.class));
        // 中控外网开启回调
        messageService.addCallBack("network", ApplicationContextHelper.getBean(NetWorkMqttCallBack.class));
        // group 回调
        messageService.addCallBack("group", ApplicationContextHelper.getBean(GroupMQTTService.class));
        // subdevice 回调
        messageService.addCallBack("subdevice", ApplicationContextHelper.getBean(SubDeviceMQTTService.class));
        // scene 回调
        messageService.addCallBack("scene", ApplicationContextHelper.getBean(SceneMQTTService.class));
        // ota 回调
        messageService.addCallBack("ota", ApplicationContextHelper.getBean(OTAMQTTService.class));
        // ifttt回调
        messageService.addCallBack("ifttt",ApplicationContextHelper.getBean(IFTTTMQTTService.class));
    }

    /**
     * 订阅mqtt主题
     */
    private void subscribeMqttTopic() {
        messageService.subscribe(mqttClientId, environment.getProperty(Constants.MQTT_CLIENTTOPIC),
                Integer.valueOf(environment.getProperty(Constants.MQTT_QOS)));
        messageService.subscribe(mqttClientId, environment.getProperty(Constants.MQTT_CLIENTBROADCAST),
                Integer.valueOf(environment.getProperty(Constants.MQTT_QOS)));
    }

}

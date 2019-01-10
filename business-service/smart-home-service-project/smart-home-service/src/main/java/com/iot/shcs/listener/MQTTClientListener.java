package com.iot.shcs.listener;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.Arguments;
import com.iot.mqttsdk.common.ConfigInfo;
import com.iot.mqttsdk.mqtt.MqttProperty;
import com.iot.mqttsdk.rabbitmq.model.RabbitmqProperty;
import com.iot.shcs.common.contants.Constants;
import com.iot.shcs.device.service.impl.DeviceMQTTService;
import com.iot.shcs.device.service.impl.GroupMQTTService;
import com.iot.shcs.ifttt.service.impl.AutoMQTTServiceImpl;
import com.iot.shcs.ipc.service.impl.IpcMQTTServiceImpl;
import com.iot.shcs.ota.service.OTADeadMQTTService;
import com.iot.shcs.ota.service.OTAMQTTService;
import com.iot.shcs.scene.service.impl.SceneMQTTService;
import com.iot.shcs.security.service.impl.SecurityMqttService;
import com.iot.shcs.user.service.UserMQTTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
     * common(2b 2c) mqtt参数设置
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
        /** 建立连接之后再注册处理类回调 */

        /** 设定队列名为automation，回调实例 AutoMQTTServiceImpl */
        messageService.addCallBack("automation", ApplicationContextHelper.getBean(AutoMQTTServiceImpl.class));

        /** 建立连接之后再注册处理类回调 */

        SceneMQTTService sceneMQTTService = ApplicationContextHelper.getBean(SceneMQTTService.class);
        AutoMQTTServiceImpl iftttMQTTService = ApplicationContextHelper.getBean(AutoMQTTServiceImpl.class);

        /** 设定队列名为 scene，回调实例 sceneMQTTService */
        messageService.addCallBack("scene", sceneMQTTService);

        /** 设定队列名为automation，回调实例 iftttMQTTService */
        messageService.addCallBack("automation", iftttMQTTService);

        /** 设定队列名为 user，回调实例 UserMQTTService */
        messageService.addCallBack("user", ApplicationContextHelper.getBean(UserMQTTService.class));

        /** 设定队列名为ipcManage，回调实例 调用ipcMQTTService */
        messageService.addCallBack("IPC", ApplicationContextHelper.getBean(IpcMQTTServiceImpl.class));

        /** 设定队列名为device，回调实例 调用deviceMQTTService */
        messageService.addCallBack("device", ApplicationContextHelper.getBean(DeviceMQTTService.class));

        /** 设定队列名为 security，回调实例 调用 securityMqttService */
        messageService.addCallBack("security", ApplicationContextHelper.getBean(SecurityMqttService.class));



        /** 设定队列名为 ota，回调实例 调用 OTAMQTTService */
        messageService.addCallBack("ota", ApplicationContextHelper.getBean(OTAMQTTService.class));

        /** 设定死信队列名为 dead-ota，回调实例 调用 OTADeadMQTTService */
        Arguments args = new Arguments("dead-ota","dead-ota",2*24*60*60*1000L,
        true,false,true,ApplicationContextHelper.getBean(OTADeadMQTTService.class));
        messageService.addCallBack("ota-dead", args);

        /** 设置队列名为 group，回调实例 调用 groupMQTTService */
        messageService.addCallBack("group",ApplicationContextHelper.getBean(GroupMQTTService.class));
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

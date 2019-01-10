package com.iot.mqtt;

import com.alibaba.fastjson.JSON;
import com.iot.common.helper.ApplicationContextHelper;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 项目名称: IOT云平台
 * 模块名称：常用工具
 * 功能描述：mqtt默认的回调类
 * 创建人: yuChangXing
 * 创建时间: 2018/3/22 20:02
 * 修改人:
 * 修改时间：
 */
public class DefaultCallBack implements MqttCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCallBack.class);

    // Mqtt 属性信息
    private MqttProperties mqttProperties;

    public DefaultCallBack() {
    }

    public DefaultCallBack(MqttProperties mqttProperties) {
        this.mqttProperties = mqttProperties;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        throwable.printStackTrace();

        LOGGER.info("***** clientId={} 连接丢失, 原因:{} *****", mqttProperties.getClientId(), throwable.getMessage());
        LOGGER.info("***** 正在重新连接中............ *****");
        MqttClientUtil.connectMqttServer(mqttProperties);
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String message = mqttMessage.toString();

        LOGGER.info("***** clientId={} 收到消息 *****", mqttProperties.getClientId());
        LOGGER.info("***** 收到的 topics={}", topic);
        LOGGER.info("***** 收到的 message={}", message);

        MqttMsg mqttMsg = null;
        try {
            mqttMsg = JSON.parseObject(message, MqttMsg.class);
        } catch (Exception e) {
            LOGGER.error("***** json消息 --> MqttMsg.java 转换失败, 原因:{}", e.getMessage());
            return;
        }

        try {
            if (mqttMsg != null) {
                String serviceName = mqttMsg.getService();
                String methodName = mqttMsg.getMethod();

                Object service = ApplicationContextHelper.getBean(serviceName);
                if (service != null) {
                    Object[] methodParameters = new Object[]{mqttMsg, topic};
                    Class[] parameterClasses = new Class[]{MqttMsg.class, String.class};
                    Method method = service.getClass().getMethod(methodName, parameterClasses);
                    method.setAccessible(true);
                    method.invoke(service, methodParameters);
                }
            }
        } catch (Exception e) {
            LOGGER.error("***** 回调消息 处理失败, 原因:{}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        LOGGER.info("***** clientId={} 发送消息成功 *****", iMqttDeliveryToken.getClient().getClientId());
        //LOGGER.info("***** toString={}", iMqttDeliveryToken.toString());
        //LOGGER.info("***** isComplete={}", iMqttDeliveryToken.isComplete());

        /*String topicStr = "";
        String[] topics = iMqttDeliveryToken.getTopics();
        if(topics != null && topics.length> 0){
            for(String topic : topics){
                topicStr = topicStr + topic + ",";
            }
        }

        try {
            LOGGER.info("***** 发送的 topics={}", topicStr);
            LOGGER.info("***** 发送的 message={}", iMqttDeliveryToken.getMessage());
        } catch (MqttException e) {
            e.printStackTrace();
        }*/
    }
}

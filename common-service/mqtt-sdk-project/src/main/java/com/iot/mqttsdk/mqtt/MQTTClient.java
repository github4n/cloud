package com.iot.mqttsdk.mqtt;


import com.alibaba.fastjson.JSON;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.ModuleConstants;
import com.iot.mqttsdk.common.MqttMsg;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 项目名称：IOT云平台
 * 模块名称：mqsdk
 * 功能描述：mqtt操作接口
 * 创建人： 490485964@qq.com
 * 创建时间：2018年04月20日 10:20
 * 修改人： 490485964@qq.com
 * 修改时间：2018年04月20日 10:20
 */
@Component
public class MQTTClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQTTClient.class);

    private Map<String, CallBackProcessor> callBackProcessorMap = new HashMap<String, CallBackProcessor>();

    private Map<String, MqttClient> mqttClientMap = new HashMap<String, MqttClient>();

    private ClientCloseListener listener;
    
    public void connectMqtt(List<MqttProperty> mqttPropertyList, ClientCloseListener listener) {
        this.listener = listener;
        for (MqttProperty mqttProperty : mqttPropertyList) {
            connect(mqttProperty);
        }
    }
    /**  
     * 描述：批量连接mqtt服务
     * @author 490485964@qq.com  
     * @date 2018/4/20 15:48  
     * @param mqttPropertyList 配置信息
     * @return   
     */ 
    public void connectMqtt(List<MqttProperty> mqttPropertyList) {
        for (MqttProperty mqttProperty : mqttPropertyList) {
            connect(mqttProperty);
        }
    }
    /**  
     * 描述：连接mqtt服务
     * @author 490485964@qq.com  
     * @date 2018/4/20 15:48
     * @param mqttProperty 配置信息
     * @return   
     */ 
    private void connect(final MqttProperty mqttProperty){
        MqttClient client = this.mqttClientMap.get(mqttProperty.getClientId());
        MqttConnectOptions conOptions = new MqttConnectOptions();
        conOptions.setUserName(mqttProperty.getUsername());
        conOptions.setPassword(mqttProperty.getPassword().toCharArray());
        //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        conOptions.setCleanSession(true);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        conOptions.setKeepAliveInterval(50);
        conOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        // 设置超时时间 单位为秒
        conOptions.setConnectionTimeout(10);
        conOptions.setAutomaticReconnect(true);
        conOptions.setMaxInflight(100);
        try {
            if(client == null){
                client = new MqttClient("tcp://"+mqttProperty.getHost()+":"+mqttProperty.getPort(), mqttProperty.getClientId(),null);
                mqttClientMap.put(mqttProperty.getClientId(), client);
            }
            client.setCallback(new MqttCallback() {
                public void messageArrived(String topic, MqttMessage mqttMessage) {
                    String message = mqttMessage.toString();
                    LOGGER.info("***** clientId={} 收到消息 *****", mqttProperty.getClientId());
                    LOGGER.info("***** 收到的 topics={}", topic);
                    LOGGER.info("***** 收到的 message={}", message);
                    String serviceName = getServiceName(topic);
                    LOGGER.info("messageArrived serviceName->"+serviceName);
                    if(null!=serviceName && !"".equals(serviceName) && callBackProcessorMap.get(serviceName) != null) {
                        CallBackProcessor callBackProcessor = callBackProcessorMap.get(serviceName);
                        MqttMsg mqttMsg = null;
                        try {
                            mqttMsg = JSON.parseObject(message, MqttMsg.class);
                            mqttMsg.setClientId(mqttProperty.getClientId());
                            mqttMsg.setTopic(topic);
                        } catch (Exception e) {
                            LOGGER.error("***** json消息 --> MqttMsg.java 转换失败, 原因:{}", e);
                            return;
                        }
                        callBackProcessor.onMessage(mqttMsg);
                    }else {
                        LOGGER.info(serviceName+" no callBackProcessor");
                    }
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //publish后会执行到这里  不做特殊处理
                    LOGGER.info("delivery is Complete:"+token.isComplete()+" and delivery response:"+token.getResponse());
                }
                @Override
                public void connectionLost(Throwable cause) {
                    if (mqttProperty.getClientId().contains("alexa") || mqttProperty.getClientId().contains("googlehome")) {
                        if (listener != null) {
                            LOGGER.info(mqttProperty.getClientId() +" 连接丢失通知" );
                            listener.closeNotify(mqttProperty.getClientId());
                        }
                    } else {
                        LOGGER.info(mqttProperty.getClientId() +" connectionLost" );
                        connect(mqttProperty);
                    }
                }

            });
            if(!client.isConnected()){
                client.connect(conOptions);
                LOGGER.info("MQTT Connection Success  "+"host->"+mqttProperty.getHost()+" port->"+mqttProperty.getPort()+" username->"+mqttProperty.getUsername()+" Password->"+mqttProperty.getPassword()+"  ClientId->"+ mqttProperty.getClientId());
            }
            CallBackProcessor callBackProcessor = mqttProperty.getCallBackProcessor();
            if(null != callBackProcessor){
                callBackProcessor.onConnected();
            }
        }catch(Exception e) {
            LOGGER.error("create mqtt connection fail.. ->"+"host->"+mqttProperty.getHost()+" port->"+mqttProperty.getPort()+" username->"+mqttProperty.getUsername()+" Password->"+mqttProperty.getPassword()+"  ClientId->"+ mqttProperty.getClientId(),e);
            try {
                if(!e.getMessage().contains(ModuleConstants.MQTT_CONNECT_PASS_ERROR)){
                    if (mqttProperty.getClientId().contains("alexa") || mqttProperty.getClientId().contains("googlehome")) { 
                        //无需重连操作
                        LOGGER.info("connect error ->"+e.getMessage()+"  Non reconnection");
                    } else {
                        TimeUnit.SECONDS.sleep(1);
                        LOGGER.info("正在尝试重新连接......." );
                        connect(mqttProperty);
                    }
                }else {
                    LOGGER.info("connect error ->"+e.getMessage()+"  Non reconnection");
                }
            } catch (InterruptedException e1) {
                LOGGER.error("InterruptedException.. ", e);
                e1.printStackTrace();
            }
        }
    }
    /**
     * 描述：订阅
     * @author 490485964@qq.com
     * @date 2018/4/20 15:50
     * @param clientId
     * @param topic
     * @param qos
     * @return
     */
    public boolean subscribe(String clientId, String topic, int qos) {
        if (StringUtils.isEmpty(clientId)) {
            LOGGER.error("MQTTClient subscribe error, clientId is null");
            return false;
        }
        if (StringUtils.isEmpty(topic)) {
            LOGGER.error("MQTTClient subscribe error, topic is null");
            return false;
        }
        MqttClient mqttClient = mqttClientMap.get(clientId);
        if (null == mqttClient){
            LOGGER.error("MQTTClient subscribe error, mqttClient is null");
            return false;
        }
        boolean result = false;
        try {
            mqttClient.subscribe(topic, qos);
            result = true;
            LOGGER.info("MQTTClient subscribe success! topic={}", topic);
        } catch (MqttException e) {
            LOGGER.error("MQTTClient subscribe error->", e);
        }
        return result;
    }
    /**
     * 描述：发布
     * @author 490485964@qq.com
     * @date 2018/4/20 15:50
     * @param clientId
     * @param topic
     * @param message
     * @param qos
     * @return
     */
    public boolean publish(String clientId, String topic, String message, int qos) {
        if (StringUtils.isEmpty(clientId)) {
            LOGGER.error("MQTTClient publish error, clientId is null");
            return false;
        }
        if (StringUtils.isEmpty(topic)) {
            LOGGER.error("MQTTClient publish error, topic is null");
            return false;
        }
        if (StringUtils.isEmpty(message)) {
            LOGGER.error("MQTTClient publish error, message is null");
            return false;
        }
        MqttClient mqttClient = mqttClientMap.get(clientId);
        if (null == mqttClient || !mqttClient.isConnected()){
            LOGGER.error("MQTTClient publish error, mqttClient is null");
            return false;
        }
        boolean result = false;
        try {
//            mqttClient.publish(topic, message.getBytes("utf-8"), qos, false);
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(message.getBytes("utf-8"));
            mqttMessage.setQos(qos);
            mqttMessage.setRetained(false);
            MqttDeliveryToken token = mqttClient.getTopic(topic).publish(mqttMessage);
            MqttWireMessage mqttWireMessage = token.getResponse();
            result = true;
            LOGGER.info("MqttClient publish success! topic={}, message={},ack={},msgId={},isComplete={}", topic, message,mqttWireMessage,token.getMessageId(),token.isComplete());
        } catch (Exception e) {
            LOGGER.error("MQTTClient publish error->", e);
        }
        return result;
    }
    /**  
     * 描述：取消订阅
     * @author 490485964@qq.com  
     * @date 2018/4/20 15:51
     * @param clientId
     * @param topic
     * @return   
     */ 
    public boolean unSubscribe(String clientId, String topic) {
        if (StringUtils.isEmpty(clientId)) {
            LOGGER.error("MQTTClient unSubscribe error, clientId is null");
            return false;
        }
        if (StringUtils.isEmpty(topic)) {
            LOGGER.error("MQTTClient unSubscribe error, topic is null");
            return false;
        }
        MqttClient mqttClient = mqttClientMap.get(clientId);
        if (null == mqttClient){
            LOGGER.error("MQTTClient unSubscribe error, mqttClient is null");
            return false;
        }
        boolean result = false;
        try {
            mqttClient.unsubscribe(topic);
            result = true;
            LOGGER.info("MqttClient unSubscribe success! topic={}", topic);
        } catch (MqttException e) {
            LOGGER.error("MqttClient unSubscribe error ", e);
        }
        return result;
    }
    /**  
     * 描述：断开mqtt连接
     * @author 490485964@qq.com  
     * @date 2018/4/25 9:25
     * @param clientId
     * @return   
     */ 
    public boolean disconnect(String clientId) {
        if (StringUtils.isEmpty(clientId)) {
            LOGGER.error("MQTTClient publish error, clientId is null");
            return false;
        }
        MqttClient mqttClient = mqttClientMap.get(clientId);
        if (null == mqttClient){
            LOGGER.error("MQTTClient disconnect error, mqttClient is null");
            return false;
        }
        boolean result = false;
        try {
            mqttClient.disconnect();
            for (Map.Entry<String, MqttClient> item : mqttClientMap.entrySet()){
                if(clientId.equals(item.getKey())){
                    mqttClientMap.remove(item.getKey());
                    break;
                }
            }
            result = true;
            LOGGER.info("MqttClient disconnect success! clientId={}", clientId);
        } catch (MqttException e) {
            LOGGER.error("MqttClient disconnect error ", e);
        }
        return result;
    }
    /**  
     * 描述：设置回调
     * @author 490485964@qq.com  
     * @date 2018/4/20 15:53
     * @param queueName
     * @param callBackProcessor
     * @return   
     */ 
    public void registerCallBack(String queueName,CallBackProcessor callBackProcessor){
        callBackProcessorMap.put(queueName,callBackProcessor);
    }
    /**  
     * 描述：获取服务名
     * @author 490485964@qq.com  
     * @date 2018/4/20 15:53  
     * @param topic
     * @return   
     */ 
    private String getServiceName(String topic) {
        String[] topicArray=topic.split("/");
        if (null==topicArray || topicArray.length<2){
            return "";
        }
        String serviceName=topicArray[topicArray.length-2];
        return serviceName;
    }

    public Set<String> getMqttClientIds() {
        if (mqttClientMap != null && mqttClientMap.size() > 0) {
            return mqttClientMap.keySet();
        }
        return null;
    }
}

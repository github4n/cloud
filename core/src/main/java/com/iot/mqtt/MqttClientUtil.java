package com.iot.mqtt;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.iot.common.helper.ApplicationContextHelper;
import org.apache.commons.lang.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 项目名称: IOT云平台
 * 模块名称： 常用工具
 * 功能描述：连接mqtt操作工具类
 * 创建人: yuChangXing
 * 创建时间: 2018/3/22 10:51
 * 修改人:
 * 修改时间：
 */
public class MqttClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttClientUtil.class);

    // MqttClient集合, <clientId, MqttClient>
    private static Map<String, MqttClient> mqttClientMap = Maps.newHashMap();


    /**
     * 获取一个 连接到MqttServer的 MqttClient
     *
     * @return
     */
    public static MqttClient connectMqttServer() {
        MqttProperties mqttProperties = ApplicationContextHelper.getBean(MqttProperties.class);
        return connectMqttServer(mqttProperties);
    }

    /**
     * 获取一个 连接到MqttServer的 MqttClient
     *
     * @param mqttProperties Mqtt 属性信息
     * @return
     */
    public static MqttClient connectMqttServer(MqttProperties mqttProperties) {
        if (mqttProperties == null) {
            LOGGER.error("***** connect MqttServer error, because mqttProperties is null *****");
            return null;
        }

        if (mqttProperties.getClientId() == null) {
            LOGGER.error("***** connect MqttServer error, because clientId is null *****");
            return null;
        }

        MqttClient mqttClient = null;

        if (mqttProperties.isCacheMqttClient()) {
            // 从缓存获取 mqttClient
            mqttClient = getMqttClientFromCache(mqttProperties.getClientId());
            if (mqttClient != null) {
                return mqttClient;
            }
        }

        try {
            // MemoryPersistence设置clientId的保存形式，默认以内存保存
            mqttClient = new MqttClient("tcp://" + mqttProperties.getHost(), mqttProperties.getClientId(), new MemoryPersistence());

            MqttConnectOptions options = getMqttConnectOptions(mqttProperties);

            mqttClient.setCallback(new DefaultCallBack(mqttProperties));

            mqttClient.connect(options);

            if (mqttProperties.isCacheMqttClient()) {
                // 放入缓存
                putMqttClientToCache(mqttProperties.getClientId(), mqttClient);
            }

            LOGGER.info("***** connect mqtt success! *****");
        } catch (MqttException e) {
            LOGGER.error("***** connect mqtt error:{} *****", e.getMessage());
            e.printStackTrace();

            try {
                LOGGER.info("***** 准备30s后重连.............");
                Thread.sleep(30000L);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            connectMqttServer(mqttProperties);
        }

        return mqttClient;
    }

    /**
     * 把MqttClient放入缓存
     *
     * @param clientId
     * @param mqttClient
     */
    public static void putMqttClientToCache(String clientId, MqttClient mqttClient) {
        if (clientId == null) {
            LOGGER.error("***** putMqttClientToCache error, because key(clientId) is null *****");
            return;
        }

        if (mqttClient == null) {
            LOGGER.error("***** putMqttClientToCache error, because value(mqttClient) is null *****");
            return;
        }

        mqttClientMap.put(clientId, mqttClient);
    }

    /**
     * 从缓存中获取 MqttClient
     *
     * @param clientId 客戶端id
     * @return
     * @throws MqttException
     */
    public static MqttClient getMqttClientFromCache(String clientId) {
        MqttClient mqttClient = null;

        if (clientId != null) {
            mqttClient = mqttClientMap.get(clientId);

            if (mqttClient != null && !mqttClient.isConnected()) {
                LOGGER.info("***** 缓存获取到的 MqttClient 是未连接状态, 将进行 重新连接...... *****");

                try {
                    // 重新连接
                    mqttClient.reconnect();
                } catch (MqttException e) {
                    mqttClient = null;
                    mqttClientMap.remove(clientId);

                    LOGGER.error("***** MqttClient reconnect error:{} *****", e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return mqttClient;
    }


    /**
     * mqtt的连接设置
     *
     * @param mqttProperties Mqtt 属性信息
     * @return
     */
    public static MqttConnectOptions getMqttConnectOptions(MqttProperties mqttProperties) {
        MqttConnectOptions options = new MqttConnectOptions();

        if (StringUtils.isNotBlank(mqttProperties.getUsername())) {
            options.setUserName(mqttProperties.getUsername());
        }

        if (StringUtils.isNotBlank(mqttProperties.getPassword())) {
            options.setPassword(mqttProperties.getPassword().toCharArray());
        }

        options.setCleanSession(mqttProperties.isCleanSession());

        options.setConnectionTimeout(mqttProperties.getConnectionTimeout());

        options.setKeepAliveInterval(mqttProperties.getKeepAliveInterval());

        options.setAutomaticReconnect(mqttProperties.isAutomaticReconnect());

        return options;
    }

    /**
     * 订阅消息
     *
     * @param topic 主题
     * @return
     */
    public static boolean subscribe(String topic) {
        MqttProperties mqttProperties = ApplicationContextHelper.getBean(MqttProperties.class);
        return subscribe(connectMqttServer(mqttProperties), topic, mqttProperties.getQos());
    }

    /**
     * 订阅消息
     *
     * @param mqttClient
     * @param topic      主题
     * @param qos        传输消息等级
     *                   level 0：最多一次的传输
     *                   level 1：至少一次的传输
     *                   level 2： 只有一次的传输
     * @return 订阅消息执行是否成功(不代表 订阅消息成功, 只代表 本次代码执行是否成功)
     */
    public static boolean subscribe(MqttClient mqttClient, String topic, int qos) {
        if (mqttClient == null) {
            LOGGER.error("***** MqttClient subscribe error, because mqttClient is null *****");
            return false;
        }

        if (StringUtils.isBlank(topic)) {
            LOGGER.error("***** MqttClient subscribe error, because topic is null *****");
            return false;
        }

        boolean subscribeResult = false;

        try {
            mqttClient.subscribe(topic, qos);
            subscribeResult = true;
            LOGGER.info("***** MqttClient subscribe success! topic={} *****", topic);
        } catch (MqttException e) {
            LOGGER.error("***** MqttClient subscribe error, because: {} *****", e.getMessage());
            e.printStackTrace();
        }

        return subscribeResult;
    }

    /**
     * 取消订阅消息
     *
     * @param topic 主题
     */
    public static void unSubscribe(String topic) {
        MqttProperties mqttProperties = ApplicationContextHelper.getBean(MqttProperties.class);

        try {
            connectMqttServer(mqttProperties).unsubscribe(topic);
            LOGGER.info("***** MqttClient unSubscribe success! topic={} *****", topic);
        } catch (MqttException e) {
            LOGGER.error("***** MqttClient unSubscribe error, because: {} *****", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 取消订阅消息
     *
     * @param mqttClient
     * @param topic      主题
     */
    public static void unSubscribe(MqttClient mqttClient, String topic) {
        try {
            mqttClient.unsubscribe(topic);
            LOGGER.info("***** MqttClient unSubscribe success! topic={} *****", topic);
        } catch (MqttException e) {
            LOGGER.error("***** MqttClient unSubscribe error, because: {} *****", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 发布消息
     *
     * @param topic   主题
     * @param service 回调service名称
     * @param method  回调方法名称
     * @param msgBody 消息内容
     * @return
     */
    public static boolean publish(String topic, String service, String method, Object msgBody) {
        MqttMsg mqttMsg = new MqttMsg(service, method, msgBody);
        String jsonMessage = JSON.toJSONString(mqttMsg);
        return publish(topic, jsonMessage);
    }

    /**
     * 发布消息
     *
     * @param topic   主题
     * @param message 消息内容
     * @return 发布消息执行是否成功(不代表 发布消息成功, 只代表 本次代码执行是否成功)
     */
    public static boolean publish(String topic, String message) {
        MqttProperties mqttProperties = ApplicationContextHelper.getBean(MqttProperties.class);
        return publish(connectMqttServer(mqttProperties), topic, message, mqttProperties.getQos());
    }

    /**
     * @param topic   主题
     * @param mqttMsg 消息封装bean
     * @return
     */
    public static boolean publish(String topic, MqttMsg mqttMsg) {
        String jsonMessage = JSON.toJSONString(mqttMsg);
        return publish(topic, jsonMessage);
    }

    /**
     * 发布消息
     *
     * @param mqttClient
     * @param topic      主题
     * @param mqttMsg    消息封装bean
     * @return 发布消息执行是否成功(不代表 发布消息成功, 只代表 本次代码执行是否成功)
     */
    public static boolean publish(MqttClient mqttClient, String topic, MqttMsg mqttMsg) {
        String jsonMessage = JSON.toJSONString(mqttMsg);
        return publish(mqttClient, topic, jsonMessage, 1);
    }

    /**
     * 发布消息
     *
     * @param mqttClient
     * @param topic      主题
     * @param message    消息内容
     * @return 发布消息执行是否成功(不代表 发布消息成功, 只代表 本次代码执行是否成功)
     */
    public static boolean publish(MqttClient mqttClient, String topic, String message) {
        return publish(mqttClient, topic, message, 1);
    }

    /**
     * 发布消息
     *
     * @param mqttClient
     * @param topic      主题
     * @param message    消息内容
     * @param qos        传输消息等级
     * @return 发布消息执行是否成功(不代表 发布消息成功, 只代表 本次代码执行是否成功)
     */
    public static boolean publish(MqttClient mqttClient, String topic, String message, int qos) {
        if (mqttClient == null) {
            LOGGER.error("***** MqttClient publish error, because mqttClient is null *****");
            return false;
        }

        if (StringUtils.isBlank(topic)) {
            LOGGER.error("***** MqttClient publish error, because topic is null *****");
            return false;
        }
        String msg = message;
        if (StringUtils.isBlank(message)) {
            msg = "";
        }

        boolean publishResult = false;

        try {
            mqttClient.publish(topic, msg.getBytes("utf-8"), qos, false);
            publishResult = true;
            LOGGER.info("***** MqttClient publish success! topic={}, message={} *****", topic, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return publishResult;
    }

    /**
     * 关闭一个 mqttClient连接
     */
    public void closeMqttClient() {
        MqttProperties mqttProperties = ApplicationContextHelper.getBean(MqttProperties.class);
        closeMqttClient(connectMqttServer(mqttProperties));
    }

    /**
     * 关闭一个 mqttClient连接
     *
     * @param mqttClient
     */
    public void closeMqttClient(MqttClient mqttClient) {
        try {
            if (mqttClient != null) {
                mqttClient.disconnect();
                LOGGER.info("***** closeMqttClient success! *****");
            }
        } catch (MqttException e) {
            LOGGER.error("***** closeMqttClient error, because: {} *****", e.getMessage());
            e.printStackTrace();
        }
    }
}

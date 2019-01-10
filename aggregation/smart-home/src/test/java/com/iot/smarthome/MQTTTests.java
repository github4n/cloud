//package com.lds.iot.smarthome;
//
//import com.alibaba.fastjson.JSON;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.iot.common.util.StringUtil;
//import com.iot.mqttploy.api.MqttPloyApi;
//import com.lds.iot.cloud.helper.DispatcherMessage;
//import com.lds.iot.cloud.helper.DispatcherMessageAck;
//import com.lds.iot.cloud.helper.MQTTCallbackHelper;
//import com.lds.iot.cloud.service.DeviceService;
//import com.lds.iot.device.domain.DeviceRom;
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.eclipse.paho.client.mqttv3.MqttTopic;
//import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//@Ignore
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class MQTTTests {
//
//    private final static Logger LOGGER = LoggerFactory.getLogger(MQTTTests.class);
//
//    private final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    private static MqttClient client;
//    private static MqttConnectOptions options;
//    private final static String host = "tcp://192.168.6.110:1883";
//    private final static String clientId = "client-100512";
//    private final static String username = "client-100512";
//    private final static String password = "123";
//
//    @Autowired
//    private DeviceService devManage;
//
//    @Autowired
//    private MqttPloyApi mqttPloyApi;
//
//    @Before
//    public void setup() {
//        try {
//            if (client == null) {
//                client = new MqttClient(host, clientId, new MemoryPersistence());
//            }
//            if (options == null) {
//                // MQTT的连接设置
//                options = new MqttConnectOptions();
//                options.setUserName(username);
//                options.setPassword(password.toCharArray());
//                // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
//                options.setCleanSession(true);
//                // 设置超时时间 单位为秒
//                options.setConnectionTimeout(10);
//                // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
//                options.setKeepAliveInterval(20);
//                client.connect(options);
//
//                LOGGER.info("host: " + host + ", clientId: " + clientId + ", status: " + client.isConnected());
//
//                if (client.isConnected()) {
//                    // 订阅主题
////                    subscribe();
//                }
//            }
//        } catch (Exception e) {
//            LOGGER.error("***** class: MQTTClientHelper method: init description: create MQTT client error *****");
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 订阅主题
//     */
//    public void subscribe() {
//        try {
//            // 创建MqttClient
//            client.setCallback(new MQTTCallbackHelper());
//            client.subscribe("iot/v1/c/#", 1);
//            LOGGER.info("MQTT connect status: " + client.isConnected());
//        } catch (Exception e) {
//            LOGGER.error("***** class: MQTTClientHelper method: subscribe description: subscribe error *****");
//            e.printStackTrace();
//        }
//    }
//
//    public void publish(DispatcherMessage dispatcherMessage, String publishTopic) {
//        try {
//            String sendMessage = JSON.toJSONString(dispatcherMessage);
//            LOGGER.info("publish topic: " + publishTopic);
//            MqttTopic topic = client.getTopic(publishTopic);
//            MqttMessage message = new MqttMessage(sendMessage.getBytes());
//            message.setQos(1);
//            topic.publish(message);
//            LOGGER.info("MQTT send message: " + sendMessage);
//        } catch (Exception e) {
//            LOGGER.error("***** class: MQTTClientHelper method: publish description: publish error *****");
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testPubSynDevBaseConfigure() {
//        DispatcherMessage synDevBasicConfigMsg = new DispatcherMessage();
//        synDevBasicConfigMsg.setService("device");
//        synDevBasicConfigMsg.setMethod("synDevBaseConfigure");
//        synDevBasicConfigMsg.setSeq("123");
//        synDevBasicConfigMsg.setSrcAddr("000010000000136h70z2382vj");
//        Map<String, Object> payload = Maps.newHashMap();
//        payload.put("unbindFlag", true);
//        payload.put("accounts", Lists.newArrayList());
//        DeviceRom deviceRom = new DeviceRom();
//        deviceRom.setDeviceId("000010000000136h70z2382vj");
//        deviceRom.setModuleCode("moduleCode");
//        deviceRom.setOtaRomInfo("otaRomInfo");
//        deviceRom.setRomType((byte) 1);
//        payload.put("ota", deviceRom);
//        synDevBasicConfigMsg.setPayload(payload);
//
//        String topic = "iot/v1/c/000010000000136h70z2382vj/device/synDevBasicConfig";
//        publish(synDevBasicConfigMsg, topic);
//    }
//
//    @Test
//    public void testPubUpdateDevBaseInfo() {
//        String topic = "iot/v1/s/000010000000136h70z2382vj/device/updateDevBasics";
//        DispatcherMessage message = new DispatcherMessage();
//        message.setService("device");
//        message.setMethod("updateDevBaseInfo");
//        message.setSrcAddr("000010000000136h70z2382vj");
//        message.setSeq("1");
//        Map<String, Object> payload = Maps.newHashMap();
//        payload.put("devId", "000010000000136h70z2382vj");
//        payload.put("resetRandom", new Random().nextLong());
//        payload.put("mac", StringUtil.getRandomString(32));
//        payload.put("version", "v1.00.001");
//        List<Map<String, Object>> attrs = Lists.newArrayList();
//        Map<String, Object> onoffAttr = Maps.newHashMap();
//        onoffAttr.put("onoff", true);
//        attrs.add(onoffAttr);
//        Map<String, Object> dimmingAttr = Maps.newHashMap();
//        dimmingAttr.put("dimming", 60);
//        attrs.add(dimmingAttr);
//        payload.put("attr", attrs);
//        payload.put("timestamp", SIMPLE_DATE_FORMAT.format(new Date()));
//        message.setPayload(payload);
//        publish(message, topic);
//    }
//
//    @Test
//    public void contextLoads() {
//    }
//
//    @Test
//    public void devConnectTest() {
//        String topic = "iot/v1/s/4b0d2377f25748d2899aa328cbc6089e/device/connect";
//        DispatcherMessage message = new DispatcherMessage();
//        message.setService("device");
//        message.setMethod("connect");
//        message.setSrcAddr("4b0d2377f25748d2899aa328cbc6089e");
//        message.setSeq("1");
//        Map<String, Object> payload = Maps.newHashMap();
//        payload.put("timestamp", SIMPLE_DATE_FORMAT.format(new Date()));
//        message.setPayload(payload);
//        publish(message, topic);
//    }
//
//    @Test
//    public void devBindTest() {
//        String topic = "iot/v1/s/aaa/device/devBindReq";
//        DispatcherMessage message = new DispatcherMessage();
//        message.setService("device");
//        message.setMethod("devBindReq");
//        message.setSrcAddr("aaa");
//        message.setSeq("1");
//        Map<String, Object> payload = Maps.newHashMap();
//        payload.put("userId", "uuu");
//        payload.put("timestamp", new Date().toString());
//        message.setPayload(payload);
//        publish(message, topic);
//    }
//
//    @Test
//    public void testDevBindReq() {
//        String topic = "iot/v1/s/4b0d2377f25748d2899aa328cbc6089e/device/devBindReq";
//        DispatcherMessage message = new DispatcherMessage();
//        message.setService("device");
//        message.setMethod("devBindReq");
//        message.setSrcAddr("4b0d2377f25748d2899aa328cbc6089e");
//        message.setSeq("1");
//        Map<String, Object> payload = Maps.newHashMap();
//        payload.put("userId", "402881ea5f8f5106015f8f5205fb0000");
//        payload.put("timestamp", SIMPLE_DATE_FORMAT.format(new Date()));
//        message.setPayload(payload);
//        devManage.devBindReq(message, topic);
//    }
//
//    @Test
//    public void testConnect() {
//        String topic = "iot/v1/s/4b0d2377f25748d2899aa328cbc6089e/device/connect";
//        DispatcherMessage message = new DispatcherMessage();
//        message.setService("device");
//        message.setMethod("connect");
//        message.setSrcAddr("4b0d2377f25748d2899aa328cbc6089e");
//        message.setSeq("1");
//        Map<String, Object> payload = Maps.newHashMap();
//        payload.put("timestamp", SIMPLE_DATE_FORMAT.format(new Date()));
//        message.setPayload(payload);
//        devManage.connect(message, topic);
//    }
//
//    @Test
//    public void testUpdateDevBaseInfo() {
//        String topic = "iot/v1/s/000010000000136h70z2382vj/device/updateDevBasics";
//        DispatcherMessage message = new DispatcherMessage();
//        message.setService("device");
//        message.setMethod("updateDevBaseInfo");
//        message.setSrcAddr("000010000000136h70z2382vj");
//        message.setSeq("1");
//        Map<String, Object> payload = Maps.newHashMap();
//        payload.put("devId", "000010000000136h70z2382vj");
//        payload.put("resetRandom", new Random().nextLong());
//        payload.put("mac", StringUtil.getRandomString(32));
//        payload.put("version", "v1.00.001");
//        List<Map<String, Object>> attrs = Lists.newArrayList();
//        Map<String, Object> onoffAttr = Maps.newHashMap();
//        onoffAttr.put("onoff", true);
//        attrs.add(onoffAttr);
//        Map<String, Object> dimmingAttr = Maps.newHashMap();
//        dimmingAttr.put("dimming", 60);
//        attrs.add(dimmingAttr);
//        payload.put("attr", attrs);
//        payload.put("timestamp", SIMPLE_DATE_FORMAT.format(new Date()));
//        message.setPayload(payload);
//        devManage.updateDevBaseInfo(message, topic);
//    }
//
//    @Test
//    public void testUpdateDevDetails() {
//        String topic = "iot/v1/s/4b0d2377f25748d2899aa328cbc6089e/device/updateDevDetails";
//        DispatcherMessage message = new DispatcherMessage();
//        message.setService("device");
//        message.setMethod("updateDevDetails");
//        message.setSrcAddr("4b0d2377f25748d2899aa328cbc6089e");
//        message.setSeq("1");
//        Map<String, Object> payload = Maps.newHashMap();
//        List<Map<String, Object>> subDevList = Lists.newArrayList();
//        Map<String, Object> subDevMap = Maps.newHashMap();
//        subDevMap.put("devId", "d5093d0742d3f573d328eca8e7ee112e");
//        subDevMap.put("productId", "v1.00.001");
//        List<Map<String, Object>> attrs = Lists.newArrayList();
//        Map<String, Object> onoffAttr = Maps.newHashMap();
//        onoffAttr.put("onoff", true);
//        attrs.add(onoffAttr);
//        Map<String, Object> dimmingAttr = Maps.newHashMap();
//        dimmingAttr.put("dimming", 68);
//        attrs.add(dimmingAttr);
//        subDevMap.put("attr", attrs);
//        subDevList.add(subDevMap);
//        payload.put("subDev", subDevList);
//        payload.put("timestamp", SIMPLE_DATE_FORMAT.format(new Date()));
//        message.setPayload(payload);
//        devManage.updateDevDetails(message, topic);
//    }
//
////    @Test
////    public void testAddDevNotif() {
////        String topic = "iot/v1/cb/4b0d2377f25748d2899aa328cbc6089e/device/addDevNotif";
////        DispatcherMessage message = new DispatcherMessage();
////        message.setService("device");
////        message.setMethod("addDevNotif");
////        message.setSrcAddr("4b0d2377f25748d2899aa328cbc6089e");
////        message.setSeq("1");
////        Map<String, Object> payload = Maps.newHashMap();
////        payload.put("devId", "a03067e0c7045a65af5be6feb369e5c5");
////        payload.put("productId", "8");
////        message.setPayload(payload);
////        message.setAck(DispatcherMessageAck.successAck());
////        devManage.addDevNotif(message, topic);
////    }
//
////    @Test
////    public void testDelDevNotif() {
////        String topic = "iot/v1/cb/4b0d2377f25748d2899aa328cbc6089e/device/delDevNotif";
////        DispatcherMessage message = new DispatcherMessage();
////        message.setService("device");
////        message.setMethod("delDevNotif");
////        message.setSrcAddr("4b0d2377f25748d2899aa328cbc6089e");
////        message.setSeq("1");
////        Map<String, Object> payload = Maps.newHashMap();
////        payload.put("devId", "d5093d0742d3f573d328eca8e7ee112e");
////        message.setPayload(payload);
////        message.setAck(DispatcherMessageAck.successAck());
////        devManage.delDevNotif(message, topic);
////    }
//
//    @Test
//    public void testGetDevListReq()  {
//        String topic = "iot/v1/s/42e1a00c7feb427aaf05c4aeec255c9f/device/getDevListReq";
//        DispatcherMessage message = new DispatcherMessage();
//        message.setService("device");
//        message.setMethod("getDevListReq");
//        message.setSrcAddr("42e1a00c7feb427aaf05c4aeec255c9f");
//        message.setSeq("1");
//        Map<String, Object> payload = Maps.newHashMap();
//        payload.put("homeId", "97cdb56ffc314f5f8a2c6c6510655c14");
//        payload.put("pageSize", 15);
//        payload.put("offset", 0);
//        message.setPayload(payload);
//        message.setAck(DispatcherMessageAck.successAck());
//        devManage.getDevListReq(message, topic);
//        //devManage.addDevNotif(message, topic);
//    }
//
//    @Test
//    public void testDelDevNotif() {
//        String topic = "iot/v1/cb/4b0d2377f25748d2899aa328cbc6089e/device/delDevNotif";
//        DispatcherMessage message = new DispatcherMessage();
//        message.setService("device");
//        message.setMethod("delDevNotif");
//        message.setSrcAddr("4b0d2377f25748d2899aa328cbc6089e");
//        message.setSeq("1");
//        Map<String, Object> payload = Maps.newHashMap();
//        payload.put("devId", "d5093d0742d3f573d328eca8e7ee112e");
//        message.setPayload(payload);
//        message.setAck(DispatcherMessageAck.successAck());
//        //devManage.delDevNotif(message, topic);
//    }
//
//}

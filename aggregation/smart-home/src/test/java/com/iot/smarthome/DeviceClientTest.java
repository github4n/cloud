//package com.lds.iot.smarthome;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.iot.common.helper.ApplicationContextHelper;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.eclipse.paho.client.mqttv3.MqttTopic;
//import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
//import org.junit.Test;
//
//import com.alibaba.fastjson.JSON;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.lds.iot.cloud.helper.DispatcherMessage;
//import com.lds.iot.cloud.helper.DispatcherMessageAck;
//import com.lds.iot.cloud.helper.MQTTCallbackHelper;
//import com.lds.iot.cloud.utils.ToolUtils;
//import org.springframework.core.env.Environment;
//
//public class DeviceClientTest {
//	private static final Log logger = LogFactory.getLog(DeviceClientTest.class);
//	private final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    private static MqttClient client;
//    private static MqttConnectOptions options;
//	private static Environment environment;
//
//    /**
//     * 初始化MQTT客户端
//     */
//    public static void init() {
//        try {
//			environment = ApplicationContextHelper.getBean(Environment.class);
//
//            if (client == null) {
//                client = new MqttClient("tcp://192.168.6.110:1883", "client-10027", new MemoryPersistence());
//            }
//            if (options == null) {
//                // MQTT的连接设置
//                options = new MqttConnectOptions();
//                options.setUserName("client-10027");
//                options.setPassword("123".toCharArray());
//                // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
//                options.setCleanSession(true);
//                // 设置超时时间 单位为秒
//                options.setConnectionTimeout(10);
//                // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
//                options.setKeepAliveInterval(20);
//                client.connect(options);
//
//                logger.info("host: " + "192.168.6.110:1883"+ ", clientId: "+ client.isConnected());
//
//                if (client.isConnected()) {
//                    // 订阅主题
//                	System.out.println("DeviceClient订阅主题");
//                    subscribe();
//                }
//            }
//        } catch (Exception e) {
//            logger.error("***** class: MQTTClientHelper method: init description: create MQTT client error *****");
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 初始化MQTT客户端，针对broker在设备上
//     */
//    public static MqttClient init(String host, String clientId, MqttConnectOptions options) {
//        try {
//            if (client == null) {
//                client = new MqttClient(host, clientId, new MemoryPersistence());
//            }
//            if (options == null) {
//                // MQTT的连接设置
//                options = new MqttConnectOptions();
//                // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
//                options.setCleanSession(true);
//                // 设置超时时间 单位为秒
//                options.setConnectionTimeout(10);
//                // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
//                options.setKeepAliveInterval(20);
//                client.connect(options);
//
//                logger.info("host: " + host + ", clientId: " + clientId + ", status: " + client.isConnected());
//
//                if (client.isConnected()) {
//                    // 订阅主题
//                    subscribe();
//                }
//            }
//            return client;
//        } catch (Exception e) {
//            logger.error("***** class: MQTTClientHelper method: init(host,clientId,options) description: create MQTT client error *****");
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 订阅主题
//     */
//    public static void subscribe() {
//        try {
//            // 创建MqttClient
//            client.setCallback(new MQTTCallbackHelper());
//            client.subscribe("iot/v1/c/123456789", 1);
////            client.subscribe("iot/v1/cb/7db4b9a4eda940089ee36943ebf3ebcb", 1);
//            logger.info("MQTT connect status: " + client.isConnected());
//        } catch (Exception e) {
//            logger.error("***** class: MQTTClientHelper method: subscribe description: subscribe error *****");
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 发布主题
//     */
//    public static void publish(DispatcherMessage dispatcherMessage, String publishTopic) {
//        try {
//            String sendMessage = JSON.toJSONString(dispatcherMessage);
//            logger.info("publish topic: " + publishTopic);
//            MqttTopic topic = client.getTopic(publishTopic);
//            MqttMessage message = new MqttMessage(sendMessage.getBytes());
//            message.setQos(1);
//            topic.publish(message);
//            logger.info("MQTT send message: " + sendMessage);
//        } catch (Exception e) {
//            logger.error("***** class: MQTTClientHelper method: publish description: publish error *****");
//            e.printStackTrace();
//        }
//    }
//
//
//    public static void main(String[] args) {
//    	init();
////    	addSceneRuleResp();
////    	editSceneRuleResp();
////    	delSceneRuleResp();
////    	delSceneResp();
////    	excSceneResp();
//    	logger.info("send success");
//	}
//    /***********************************以下为Scene Device client************************************************/
//    @Test
//	public void addSceneRuleResp() {
//		DispatcherMessage msg=new DispatcherMessage();
//    	msg.setService("scene");
//    	msg.setMethod("addSceneRuleResp");
//    	msg.setSeq("1");
//    	msg.setSrcAddr(ToolUtils.getUUID());
//    	Map<String,Object> payload=new HashMap<String,Object>();
//    	payload.put("sceneId", "fb083b2bc9114b5b81b25908c95db8b9");
//    	payload.put("idx", 1);
//    	payload.put("type", "");
//    	payload.put("devId", "3f77bfd447aff53185682e2319e41cec");
//    	Map<String,Object> attr=new HashMap<String,Object>();
//    	attr.put("OnOff", 1);
//    	attr.put("Dimming", 87);
//    	payload.put("attr", attr);
//    	msg.setPayload(payload);
//    	DispatcherMessageAck ack=DispatcherMessageAck.successAck();
//    	msg.setAck(ack);
//    	publish(msg,"iot/v1/s/123456789");
//	}
//
//    @Test
//	public  void editSceneRuleResp() {
//		DispatcherMessage msg=new DispatcherMessage();
//		msg.setService("scene");
//		msg.setMethod("addSceneRuleResp");
//		msg.setSeq("1");
//		msg.setSrcAddr(ToolUtils.getUUID());
//		Map<String,Object> payload=new HashMap<String,Object>();
//		payload.put("sceneId", "7e3bca2973a84613bd017bfe8fad37f3");
//		payload.put("idx", "1111111111");
//		payload.put("type", "");
//		payload.put("devId", "123456789");
//		Map<String,Object> attr=new HashMap<String,Object>();
//		attr.put("OnOff", 1);
//		attr.put("Dimming", 87);
//		payload.put("attr", attr);
//		msg.setPayload(payload);
//		DispatcherMessageAck ack=DispatcherMessageAck.successAck();
//		msg.setAck(ack);
//		publish(msg,"iot/v1/s/123456789");
//	}
//	@Test
//	public void delSceneRuleResp() {
//		init();
//		DispatcherMessage msg=new DispatcherMessage();
//		msg.setService("scene");
//		msg.setMethod("delSceneRuleResp");
//		msg.setSeq("1");
//		msg.setSrcAddr(ToolUtils.getUUID());
//		Map<String,Object> payload=new HashMap<String,Object>();
//		payload.put("sceneId", "fb083b2bc9114b5b81b25908c95db8b9");
//		payload.put("idx", "1");
//		msg.setPayload(payload);
//		DispatcherMessageAck ack=DispatcherMessageAck.successAck();
//		msg.setAck(ack);
//		publish(msg,"iot/v1/s/123456789");
//	}
//
//	@Test
//	public static void delSceneResp() {
//		DispatcherMessage msg=new DispatcherMessage();
//		msg.setService("scene");
//		msg.setMethod("delSceneResp");
//		msg.setSeq("1");
//		msg.setSrcAddr(ToolUtils.getUUID());
//		Map<String,Object> payload=new HashMap<String,Object>();
//		payload.put("sceneId", "28133331873a4f1da08fbb2e8a4d7c57");
//		List<String> idsList=new ArrayList<String>();
//		idsList.add("1111111111");
//		payload.put("idx", idsList);
//		msg.setPayload(payload);
//		DispatcherMessageAck ack=DispatcherMessageAck.successAck();
//		msg.setAck(ack);
//		publish(msg,"iot/v1/c/7db4b9a4eda940089ee36943ebf3ebcb");
//	}
//
//	@Test
//	public void excSceneResp() {
//		DispatcherMessage msg=new DispatcherMessage();
//		msg.setService("scene");
//		msg.setMethod("excSceneResp");
//		msg.setSeq("1");
//		msg.setSrcAddr(ToolUtils.getUUID());
//		Map<String,Object> payload=new HashMap<String,Object>();
//		payload.put("sceneId", "7e3bca2973a84613bd017bfe8fad37f3");
//		msg.setPayload(payload);
//		DispatcherMessageAck ack=DispatcherMessageAck.successAck();
//		msg.setAck(ack);
//		publish(msg,"iot/v1/s/123456789");
//	}
//
//	/***********************************以下为Space Device client************************************************/
//	public void setDevInfoNotif(){
//		DispatcherMessage message=new DispatcherMessage();
//		message.setService("device");
//		message.setMethod("setDevInfoNotif");
//		message.setSeq("123456");
//		message.setSrcAddr("123456");
//
//		Map<String, Object> map=new HashMap<String, Object>();
//		map.put("devId", "4b0d2377f25748d2899aa328cbc6089e");
//		map.put("userId", "7db4b9a4eda940089ee36943ebf3ebcb");
//		map.put("name", "testHomeName");
//		map.put("icon", "home.png");
//		map.put("homeId", "f73d685cdd1549b3b34570590486c36b");
//		map.put("roomId", "b316371e6bd049e09261129e2b5113e1");
//		message.setPayload(map);
//		DispatcherMessageAck ack = DispatcherMessageAck.successAck();
//		message.setAck(ack);
//		publish(message,"iot/v1/s/123456789");
//	}
//	/***********************************以下为 Device client************************************************/
//
//	@Test  //设备上线
//    public void connect() {
//		init();
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
//	@Test //设备绑定
//    public void devBindReq() {
//		init();
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
//	@Test //4.20	上报设备详细信息
//	public void testUpdateDevDetails() {
//		init();
//        String topic = "iot/v1/s/4b0d2377f25748d2899aa328cbc6089e/device/updateDevDetails";
//	    DispatcherMessage message = new DispatcherMessage();
//	    message.setService("device");
//	    message.setMethod("updateDevDetails");
//	    message.setSrcAddr("4b0d2377f25748d2899aa328cbc6089e");
//	    message.setSeq("1");
//	    Map<String, Object> payload = Maps.newHashMap();
//	    List<Map<String, Object>> subDevList = Lists.newArrayList();
//	    Map<String, Object> subDevMap = Maps.newHashMap();
//	    subDevMap.put("devId", "d5093d0742d3f573d328eca8e7ee112e");
//	    subDevMap.put("productId", "v1.00.001");
//	    List<Map<String, Object>> attrs = Lists.newArrayList();
//	    Map<String, Object> onoffAttr = Maps.newHashMap();
//	    onoffAttr.put("onoff", true);
//	    attrs.add(onoffAttr);
//	    Map<String, Object> dimmingAttr = Maps.newHashMap();
//	    dimmingAttr.put("dimming", 68);
//	    attrs.add(dimmingAttr);
//	    subDevMap.put("attr", attrs);
//	    subDevList.add(subDevMap);
//	    payload.put("subDev", subDevList);
//	    payload.put("timestamp", SIMPLE_DATE_FORMAT.format(new Date()));
//	    message.setPayload(payload);
//	    publish(message, topic);
//	}
//
//	 @Test //添加设备通知
//    public void addDevNotif() {
//		init();
//        String topic = "iot/v1/cb/4b0d2377f25748d2899aa328cbc6089e/device/addDevNotif";
//        DispatcherMessage message = new DispatcherMessage();
//        message.setService("device");
//        message.setMethod("addDevNotif");
//        message.setSrcAddr("4b0d2377f25748d2899aa328cbc6089e");
//        message.setSeq("1");
//        Map<String, Object> payload = Maps.newHashMap();
//        payload.put("devId", "a03067e0c7045a65af5be6feb369e5c5");
//        payload.put("productId", "8");
//        message.setPayload(payload);
//        message.setAck(DispatcherMessageAck.successAck());
//        publish(message, topic);
//    }
//
//    @Test //删除设备通知
//    public void testDelDevNotif() {
//    	init();
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
//        publish(message, topic);
//    }
//
//}

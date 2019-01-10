//package com.lds.iot.smarthome;
//import com.alibaba.fastjson.JSON;
//import com.iot.common.helper.ApplicationContextHelper;
//import com.lds.iot.cloud.helper.DispatcherMessage;
//import com.lds.iot.cloud.helper.DispatcherMessageAck;
//import com.lds.iot.cloud.helper.MQTTCallbackHelper;
//import com.lds.iot.cloud.utils.ToolUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.eclipse.paho.client.mqttv3.MqttTopic;
//import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
//import org.junit.Test;
//import org.springframework.core.env.Environment;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class AppClientTest {
//	private static final Log logger = LogFactory.getLog(AppClientTest.class);
//
//    private static MqttClient client;
//    private static MqttConnectOptions options;
//    private static Environment environment;
//    /**
//     * 初始化MQTT客户端
//     */
//    public static void init() {
//        try {
//			environment = ApplicationContextHelper.getBean(Environment.class);
//
//            if (client == null) {
//                client = new MqttClient("tcp://192.168.6.110:1883", "client-100533", new MemoryPersistence());
//            }
//            if (options == null) {
//                // MQTT的连接设置
//                options = new MqttConnectOptions();
//                options.setUserName("client-100533");
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
//                	System.out.println("App订阅主题");
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
//            client.subscribe("iot/v1/c/21e1ab6a1c204a57a075dd3184ebc60e/#", 1);
////            client.subscribe("iot/v1/cb/123456789/#", 1);
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
////    	addSceneRuleReq();
////    	editSceneRuleReq();
////    	getSceneRuleReq();
////    	delSceneRuleReq();
////    	delSceneReq();
////    	excSceneReq();
//    	logger.info("send success");
//	}
//
//    /***********************************以下为Scene app client************************************************/
//
//    @Test
//    public void addSceneRuleReq() {
//    	init();
//		DispatcherMessage msg=new DispatcherMessage();
//    	msg.setService("scene");
//    	msg.setMethod("addSceneRuleReq");
//    	msg.setSeq("1");
//    	msg.setSrcAddr(ToolUtils.getUUID());
//    	Map<String,Object> payload=new HashMap<String,Object>();
//    	payload.put("sceneId", "7e3bca2973a84613bd017bfe8fad37f3");
//    	payload.put("idx", "1");
//    	payload.put("type", "");
//    	payload.put("devId", "123456789");
//    	Map<String,Object> attr=new HashMap<String,Object>();
//    	attr.put("OnOff", 1);
//    	attr.put("Dimming", 87);
//    	payload.put("attr", attr);
//    	msg.setPayload(payload);
//    	publish(msg,"iot/v1/s/7db4b9a4eda940089ee36943ebf3ebcb");
//	}
//
//    @Test
//    public void editSceneRuleReq() {
//    	init();
//    	DispatcherMessage msg=new DispatcherMessage();
//    	msg.setService("scene");
//    	msg.setMethod("addSceneRuleReq");
//    	msg.setSeq("1");
//    	msg.setSrcAddr(ToolUtils.getUUID());
//    	Map<String,Object> payload=new HashMap<String,Object>();
//    	payload.put("sceneId", "7e3bca2973a84613bd017bfe8fad37f3");
//    	payload.put("idx", "1111111111");
//    	payload.put("type", "");
//    	payload.put("devId", "123456789");
//    	Map<String,Object> attr=new HashMap<String,Object>();
//    	attr.put("OnOff", 20);
//    	attr.put("Dimming", 87);
//    	payload.put("attr", attr);
//    	msg.setPayload(payload);
//    	publish(msg,"iot/v1/s/7db4b9a4eda940089ee36943ebf3ebcb");
//    }
//
//    @Test
//    public void getSceneRuleReq() {
//    	init();
//    	DispatcherMessage msg=new DispatcherMessage();
//    	msg.setService("scene");
//    	msg.setMethod("getSceneRuleReq");
//    	msg.setSeq("1");
//    	msg.setSrcAddr(ToolUtils.getUUID());
//    	Map<String,Object> payload=new HashMap<String,Object>();
//    	payload.put("sceneId", "7e3bca2973a84613bd017bfe8fad37f3");
//    	msg.setPayload(payload);
//    	publish(msg,"iot/v1/s/7db4b9a4eda940089ee36943ebf3ebcb");
//    }
//
//    @Test
//    public void delSceneRuleReq() {
//    	init();
//		DispatcherMessage msg=new DispatcherMessage();
//		msg.setService("scene");
//		msg.setMethod("delSceneRuleReq");
//		msg.setSeq("1");
//		msg.setSrcAddr(ToolUtils.getUUID());
//		Map<String,Object> payload=new HashMap<String,Object>();
//		payload.put("sceneId", "7e3bca2973a84613bd017bfe8fad37f3");
//		payload.put("idx", "1111111111");
//		msg.setPayload(payload);
//		DispatcherMessageAck ack=DispatcherMessageAck.successAck();
//		msg.setAck(ack);
//		publish(msg,"iot/v1/s/7db4b9a4eda940089ee36943ebf3ebcb");
//	}
//
//    @Test
//    public void delSceneReq() {
//    	init();
//    	DispatcherMessage msg=new DispatcherMessage();
//    	msg.setService("scene");
//    	msg.setMethod("delSceneReq");
//    	msg.setSeq("1");
//    	msg.setSrcAddr(ToolUtils.getUUID());
//    	Map<String,Object> payload=new HashMap<String,Object>();
//    	payload.put("sceneId", "7e3bca2973a84613bd017bfe8fad37f3");
//    	msg.setPayload(payload);
//    	DispatcherMessageAck ack=DispatcherMessageAck.successAck();
//    	msg.setAck(ack);
//    	publish(msg,"iot/v1/s/7db4b9a4eda940089ee36943ebf3ebcb");
//    }
//
//    @Test
//    public void excSceneReq() {
//    	init();
//    	DispatcherMessage msg=new DispatcherMessage();
//    	msg.setService("scene");
//    	msg.setMethod("excSceneReq");
//    	msg.setSeq("1");
//    	msg.setSrcAddr(ToolUtils.getUUID());
//    	Map<String,Object> payload=new HashMap<String,Object>();
//    	payload.put("sceneId", "7e3bca2973a84613bd017bfe8fad37f3");
//    	msg.setPayload(payload);
//    	DispatcherMessageAck ack=DispatcherMessageAck.successAck();
//    	msg.setAck(ack);
//    	publish(msg,"iot/v1/s/7db4b9a4eda940089ee36943ebf3ebcb");
//    }
//    /***********************************以下为space app client************************************************/
//    //4.26	获取设备信息请求
//    @Test
//	public void getDevInfoReq(){
//		init();
//    	DispatcherMessage message=new DispatcherMessage();
//		message.setService("device");
//		message.setMethod("getDevInfoReq");
//		message.setSeq("123456");
//		message.setSrcAddr("123456");
//		Map<String, Object> map=new HashMap<String, Object>();
//		map.put("devId", "4b0d2377f25748d2899aa328cbc6089e");
//		message.setPayload(map);
//		DispatcherMessageAck ack = DispatcherMessageAck.successAck();
//		message.setAck(ack);
//		publish(message,"iot/v1/s/7db4b9a4eda940089ee36943ebf3ebcb");
//	}
//
//	//4.28	设置设备信息请求
//    @Test
//	public void setDevInfoReq(){
//		init();
//    	DispatcherMessage message=new DispatcherMessage();
//		message.setService("device");
//		message.setMethod("setDevInfoReq");
//		message.setSeq("123456");
//		message.setSrcAddr("123456");
//
//		Map<String, Object> map=new HashMap<String, Object>();
//		map.put("devId", "4b0d2377f25748d2899aa328cbc6089e");
//		map.put("userId", "7db4b9a4eda940089ee36943ebf3ebcb");
//		map.put("name", "homeName");
//		map.put("icon", "living.png");
//		map.put("homeId", "f73d685cdd1549b3b34570590486c36b");
//		map.put("roomId", "b316371e6bd049e09261129e2b5113e1");
//		message.setPayload(map);
//		publish(message,"iot/v1/s/7db4b9a4eda940089ee36943ebf3ebcb");
//	}
//
//	//4.24	获取房间设备列表请求
//    @Test
//    public void getRoomDevListReq(){
//		DispatcherMessage message=new DispatcherMessage();
//		message.setService("device");
//		message.setMethod("getRoomDevListReq");
//		message.setSeq("123456");
//		message.setSrcAddr("123456");
//
//		Map<String, Object> map=new HashMap<String, Object>();
//		map.put("roomId", "b316371e6bd049e09261129e2b5113e1");
//		map.put("pageSize", 15);
//		map.put("offset", 0);
//		map.put("timestamp", System.currentTimeMillis()+"");
//		message.setPayload(map);
//		publish(message,"iot/v1/s/7db4b9a4eda940089ee36943ebf3ebcb");
//	}
//}

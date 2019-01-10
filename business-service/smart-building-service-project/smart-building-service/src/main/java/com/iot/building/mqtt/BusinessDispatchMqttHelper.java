package com.iot.building.mqtt;

import com.iot.building.listener.MQTTClientListener;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BusinessDispatchMqttHelper {
	
	public final static String SERVICE="mqttService";
	public final static String SRC_ADDRESS="centerControlColud";
	public final static String MOTHOD_DEVICE_STATUS="deviceStatus";
	public final static String MOTHOD_DEVICE_MOUNT="deviceMount";
	public final static String MOTHOD_DEVICE_OTA="deviceOta";
	
    private static MqttSdkService mqttSdkService;
    
    private final static int QOS=1;
    
    //手机端mqtt client 集合
  	public static Set<String> NETWORK_MQTT_CLINET_SET=new HashSet<String>();
    
    private static void publish(MqttMsg msgBody) {
    	String topic=msgBody.getTopic();
    	String clientId=MQTTClientListener.getMqttClientId();
    	if(mqttSdkService==null) {
    		mqttSdkService = ApplicationContextHelper.getBean(MqttSdkService.class);
    	}
    	mqttSdkService.sendMessage(clientId, msgBody,QOS);
    }
    
	/**
	 * 设备状态主题发送
	 * @param msg
	 */
	public static void sendDeviceTopic(Map<String,Object> msg,String topic) {
		MqttMsg msgBody=setMsgBody(MOTHOD_DEVICE_STATUS,msg,topic);
		publish(msgBody);
	}
	
	/**
	 * 设备挂载状态主题发送
	 * @param msg
	 */
	public static void sendDeviceMountTopic(Map<String,Object> msg,String topic) {
		MqttMsg msgBody=setMsgBody(MOTHOD_DEVICE_MOUNT,msg,topic);
		publish(msgBody);
	}
	
	/**
	 * 执行情景主题发送
	 * @param msg
	 */
	public static void sendSceneTopic(MqttMsg mqttMsg,Map<String,Object> msg,String topic) {
		MqttMsg msgBody=setMsgBody(mqttMsg,CommonCallBackHelper.MOTHOD_SCENE_STATUS,msg,topic);
		publish(msgBody);
	}
	/**
	 * 执行情景主题发送
	 * @param msg
	 */
	public static void sendSceneTopic(Map<String,Object> msg,String topic) {
		 sendSceneTopic(null,msg,topic);
	}
	
	/**
	 * 空间状态主题发送
	 * @param msg
	 */
	public static void sendSpaceTopic(Map<String,Object> msg,String topic) {
		MqttMsg msgBody=setMsgBody(CommonCallBackHelper.MOTHOD_SPACE_STATUS,msg,topic);
		publish(msgBody);
	}
	
	/**
	 * 告警状态主题发送
	 * @param msg
	 */
	public static void sendWarningTopic(Map<String,Object> msg,String topic) {
		MqttMsg msgBody=setMsgBody(CommonCallBackHelper.MOTHOD_WARNING_STATUS,msg,topic);
		publish(msgBody);
	}
	
	
	/**
	 * 情景列表主题发送
	 * @param msg
	 */
	public static void sendSceneListResp(MqttMsg mqttMsg,Map<String,Object> msg,String topic) {
		MqttMsg msgBody=setMsgBody(mqttMsg,CommonCallBackHelper.MOTHOD_SCENE_LIST_RESP,msg,topic);
		publish(msgBody);
	}
	
	/**
	 * 设备状态主题发送
	 * @param msg
	 */
	public static void sendDeviceListResp(MqttMsg mqttMsg,Map<String,Object> msg,String topic) {
		MqttMsg msgBody=setMsgBody(mqttMsg,CommonCallBackHelper.MOTHOD_DEVICE_LIST_RESP,msg,topic);
		publish(msgBody);
	}
	
	/**
	 * 设备状态主题发送
	 * @param msg
	 */
	public static void sendMeetingListResp(MqttMsg mqttMsg,Map<String,Object> msg,String topic) {
		MqttMsg msgBody=setMsgBody(mqttMsg,CommonCallBackHelper.MOTHOD_MEETING_LIST_RESP,msg,topic);
		publish(msgBody);
	}
	
	/**
	 * 查询预订发送
	 * @param msg
	 */
	public static void sendBookingList(MqttMsg mqttMsg,Map<String,Object> msg,String topic) {
		MqttMsg msgBody=setMsgBody(mqttMsg,CommonCallBackHelper.MOTHOD_RESERVATION_LIST_RESP,msg,topic);
		publish(msgBody);
	}
	
	/**
	 * 查询详情发送
	 * @param msg
	 */
	public static void sendBookingDetail(MqttMsg mqttMsg,Map<String,Object> msg,String topic) {
		MqttMsg msgBody=setMsgBody(mqttMsg,CommonCallBackHelper.MOTHOD_RESERVATION_DETAIL_RESP,msg,topic);
		publish(msgBody);
	}
	
	/**
	 * 查询详情发送
	 * @param msg
	 */
	public static void sendUserInfoResp(MqttMsg mqttMsg,Map<String,Object> msg,String topic) {
		MqttMsg msgBody=setMsgBody(mqttMsg,CommonCallBackHelper.MOTHOD_USER_INFO_RESP,msg,topic);
		publish(msgBody);
	}
	/**
	 * 查询预订发送
	 * @param msg
	 */
	public static void sendConfirmReservationResp(MqttMsg mqttMsg,Map<String,Object> msg,String topic,int code) {
		MqttMsgAck ack= new MqttMsgAck();
		MqttMsg msgBody=setMsgBody(mqttMsg,CommonCallBackHelper.MOTHOD_CONFIRM_RESERVATION_RESP,msg,topic);
		ack.setCode(code);
		if(code==200) {
			ack.setDesc("success");
		}else {
			ack.setDesc("this meeting not user Reservation");
		}
		msgBody.setAck(ack);
		publish(msgBody);
	}
	/**
	 * 查询预订发送
	 * @param msg
	 */
	public static void sendAddOrUpdateReservationResp(MqttMsg mqttMsg,Map<String,Object> msg,String topic) {
		Boolean flag=Boolean.valueOf(msg.get("ack").toString());
		MqttMsgAck ack= new MqttMsgAck();
		MqttMsg msgBody=setMsgBody(mqttMsg,CommonCallBackHelper.MOTHOD_ADD_OR_UPDATE_RESERVATION_RESP,msg,topic);
		if(flag) {
			ack.setCode(200);
			ack.setDesc("success");
		}else {
			ack.setCode(-1);
			ack.setDesc("time is used");
		}
		msg.remove("ack");
		msgBody.setAck(ack);
		publish(msgBody);
	}
	
	/**
	 * 空间背景图片发送
	 * @param msg
	 */
	public static void sendBackgroundImg(MqttMsg mqttMsg,Map<String,Object> msg,String topic) {
		MqttMsg msgBody=setMsgBody(mqttMsg,CommonCallBackHelper.MOTHOD_SPACE_BACKGROUND_RESP,msg,topic);
		publish(msgBody);
	}
	
	private static MqttMsg setMsgBody(MqttMsg msg,String method,Map<String,Object> payload,String topic) {
		if(msg==null) {
			 msg=new MqttMsg();
		}
		msg.setService(SERVICE);
		msg.setSrcAddr(SRC_ADDRESS);
		msg.setMethod(method);
		msg.setPayload(payload);
		msg.setTopic(topic);
		return msg;
	}
	
	private static MqttMsg setMsgBody(String method,Map<String,Object> payload,String topic) {
		MqttMsg msg=new MqttMsg();
		msg.setService(SERVICE);
		msg.setSrcAddr(SRC_ADDRESS);
		msg.setMethod(method);
		msg.setPayload(payload);
		msg.setTopic(topic);
		return msg;
	}

	/**
	 * 人脸识别主题发送
	 * @param mqttMsg
	 */
	public static void sendPeopleFaceResp(MqttMsg mqttMsg) {
		publish(mqttMsg);
	}
	
	/**
	 * 设备OTA状态主题发送
	 * @param msg
	 */
	public static void sendDeviceOta(Map<String,Object> msg,String topic) {
		MqttMsg msgBody=setMsgBody(MOTHOD_DEVICE_OTA,msg,topic);
		publish(msgBody);
	}
	
	/**
	 * 同步设备主题发送
	 * @param msg
	 */
	public static void sendSynDeviceTopic(Map<String,Object> msg,String topic) {
		MqttMsg msgBody=setMsgBody(CommonCallBackHelper.MOTHOD_SYN_DEVICE,msg,topic);
		publish(msgBody);
	}
}

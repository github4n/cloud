package com.iot.building.mqtt.local;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.building.mqtt.BusinessDispatchMqttHelper;
import com.iot.building.mqtt.CommonCallBackHelper;
import com.iot.building.scene.service.SceneService;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;

@Service("center")
public class MeetingMqttCallBack implements CallBackProcessor {
	
	public static final Logger logger = LoggerFactory.getLogger(MeetingMqttCallBack.class);

	@Autowired
	private CommonCallBackHelper helper;
	@Autowired
	private SceneService sceneService;
	
	@Override
	public void onMessage(MqttMsg mqttMsg) {
		try {
			//会议室获取情景请求
			switchBusiness(mqttMsg);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void switchBusiness(MqttMsg mqttMsg) {
		//会议室获取情景请求
		if(CommonCallBackHelper.MOTHOD_SCENE_LIST_REQ.equals(mqttMsg.getMethod())) {//获取情景列表响应
			sceneListResp(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_SCENE_EXCUTE_REQ.equals(mqttMsg.getMethod())) {//执行情景响应
			excuScenResp(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_DEVICE_LIST_REQ.equals(mqttMsg.getMethod())) {//
			deviceListResp(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_DEVICE_CONTROL_REQ.equals(mqttMsg.getMethod())) {//
			controlDevice(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_MEETING_LIST_REQ.equals(mqttMsg.getMethod())) {//
			meetingListResp(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_ADD_OR_UPDATE_RESERVATION_REQ.equals(mqttMsg.getMethod())) {//更新或添加预约
			addOrUpdateReservation(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_RESERVATION_LIST_REQ.equals(mqttMsg.getMethod())) {//请求预约列表
			getReservationResp(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_DEL_RESERVATION_REQ.equals(mqttMsg.getMethod())) {//
			delReservationReq(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_DETAIL_RESERVATION_REQ.equals(mqttMsg.getMethod())) {//
			detailReservationReq(mqttMsg);
		}
	}


	/**
	 * 设备控制
	 * @param mqttMsg
	 */
	private void controlDevice(MqttMsg mqttMsg) {
		helper.commonControlDevice(mqttMsg);
	}
	
	private void meetingListResp(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		Map<String,Object> backMap=helper.commonMeetingListResp(mqttMsg);
		String uuid=helper.getUUIDByTenantId(params);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),uuid,CommonCallBackHelper.CENTER);
		BusinessDispatchMqttHelper.sendMeetingListResp(mqttMsg,backMap,topic);
	}
	
	private void deviceListResp(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		Map<String,Object> backMap=helper.commonDeviceListResp(mqttMsg);
		String uuid=helper.getUUIDByTenantId(params);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),uuid,CommonCallBackHelper.CENTER);
		BusinessDispatchMqttHelper.sendDeviceListResp(mqttMsg,backMap,topic);
	}

	/**
	 * 响应情景列表的请求
	 * @param mqttMsg
	 */
	private void sceneListResp(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		Map<String,Object> backMap=helper.commonSceneListResp(mqttMsg);
		String uuid=helper.getUUIDByTenantId(params);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),uuid,CommonCallBackHelper.CENTER);
		BusinessDispatchMqttHelper.sendSceneListResp(mqttMsg,backMap,topic);
	}


	/**
	 * 执行情景响应
	 * @param mqttMsg
	 */
	private void excuScenResp(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		String sceneId=params.get("sceneId").toString();
		Long tenantId=Long.parseLong(params.get("tenantId").toString());
		sceneService.sceneExecute(tenantId,Long.valueOf(sceneId));
		String uuid=helper.getUUIDByTenantId(params);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),uuid,CommonCallBackHelper.CENTER);
		BusinessDispatchMqttHelper.sendSceneTopic(params,topic);
	}


	/**
	 * 删除预约请求
	 * @param mqttMsg
	 */
	private void delReservationReq(MqttMsg mqttMsg) {
		helper.commonDelReservationReq(mqttMsg);
	}
	/**
	 * 预约详情请求
	 * @param mqttMsg
	 */
	private void detailReservationReq(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		Map<String,Object> backMap=helper.commonGetReservationDetailResp(mqttMsg);
		String uuid=helper.getUUIDByTenantId(params);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),uuid,CommonCallBackHelper.CENTER);
		BusinessDispatchMqttHelper.sendBookingDetail(mqttMsg,backMap,topic);
	}

	/**
	 * 获取预约信息
	 * @param mqttMsg
	 */
	private void getReservationResp(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		Map<String,Object> backMap=helper.commonGetReservationResp(mqttMsg);
		String uuid=helper.getUUIDByTenantId(params);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),uuid,CommonCallBackHelper.CENTER);
		BusinessDispatchMqttHelper.sendBookingList(mqttMsg,backMap,topic);
	}
	
	/**
	 * 添加或更新预约
	 * @param mqttMsg
	 */
	private void addOrUpdateReservation(MqttMsg mqttMsg) {
		Boolean flag=helper.commonAddOrUpdateReservation(mqttMsg);
		Map<String,Object> backMap=(Map<String,Object>)mqttMsg.getPayload();
		String uuid=helper.getUUIDByTenantId(backMap);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),uuid,CommonCallBackHelper.CENTER);
		Map<String,Object> params=new HashMap<>();params.put("ack", flag);
		BusinessDispatchMqttHelper.sendAddOrUpdateReservationResp(mqttMsg,params,topic);
	}

}

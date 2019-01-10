package com.iot.building.mqtt.network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.iot.building.device.service.DeviceMQTTService;
import com.iot.building.helper.CenterControlDeviceStatus;
import com.iot.building.helper.Constants;
import com.iot.building.mqtt.BusinessDispatchMqttHelper;
import com.iot.building.mqtt.CommonCallBackHelper;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.building.space.vo.SpaceBackgroundImgReq;
import com.iot.building.space.vo.SpaceBackgroundImgResp;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.redis.RedisCacheUtil;
import com.iot.user.api.UserApi;

@Service("network")
public class NetWorkMqttCallBack implements CallBackProcessor {
	
	public static final Logger logger = LoggerFactory.getLogger(NetWorkMqttCallBack.class);
	private final static String NETWORK_BROADCAST_TOPIC="iot/v1/cb/";
	private final static String NETWORK="network";
	private final static String CENTER="center";
	private final static String CONNECT="connect";
	private final static String DISCONNECT="disconnect";
    //手机端mqtt client 集合
    public static Set<String> NETWORK_MQTT_CLINET_SET = new HashSet<String>();
	@Autowired
	private CommonCallBackHelper helper;
	@Autowired
    private Environment environment;
	@Autowired
	private UserApi userApi;
	
	@Resource(name="device")
    private DeviceMQTTService deviceService;
	
	@Autowired
	private DeviceCoreApi deviceCoreApi;
	
	@Autowired
    private IBuildingSpaceService spaceService;
	
    private static String getClientFromTopic(String topic) {
        String[] str = topic.split("/");
        return str[3];
    }

	@Override
	public void onMessage(MqttMsg mqttMsg) {
		try {
			switchBusiness(mqttMsg);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getUUID() {
		return environment.getProperty(Constants.AGENT_MQTT_USERNAME);
	}

	private void switchBusiness(MqttMsg mqttMsg) {
		//会议室获取情景请求
		if(CommonCallBackHelper.MOTHOD_SCENE_LIST_REQ.equals(mqttMsg.getMethod())) { //情景
			sceneListResp(mqttMsg);
        } else if (CommonCallBackHelper.MOTHOD_SCENE_EXCUTE_REQ.equals(mqttMsg.getMethod())) {  //控制设备情景
			excuScenResp(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_MEETING_LIST_REQ.equals(mqttMsg.getMethod())) {//
			meetingListResp(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_ADD_OR_UPDATE_RESERVATION_REQ.equals(mqttMsg.getMethod())) {  //预约请求
			addOrUpdateReservation(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_RESERVATION_LIST_REQ.equals(mqttMsg.getMethod())) {  //
			getReservationResp(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_DEL_RESERVATION_REQ.equals(mqttMsg.getMethod())) {  //
			delReservationReq(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_DETAIL_RESERVATION_REQ.equals(mqttMsg.getMethod())) {  //
			detailReservationReq(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_USER_INFO_RESP.equals(mqttMsg.getMethod())) {  //
			userInfoResp(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_CONFIRM_RESERVATION_REQ.equals(mqttMsg.getMethod())) {  //
			confirmReservationReq(mqttMsg);
//		}else if(mqttMsg.getTopic().contains(NETWORK_BROADCAST_TOPIC)) {  //广播上线下线请求
//			onlineOrOfflineBusiness(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_DEVICE_LIST_REQ.equals(mqttMsg.getMethod())) {//
			deviceListResp(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_DEVICE_CONTROL_REQ.equals(mqttMsg.getMethod())) {//
			controlDevice(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_DEVICE_STATUS.equals(mqttMsg.getMethod())) {//
			//设备属性初始化
			deviceStatusResp(mqttMsg);
		}else if(CommonCallBackHelper.MOTHOD_SPACE_BACKGROUND_REQ.equals(mqttMsg.getMethod())) {
            // 空间背景图片
			spaceBackgroundImgResp(mqttMsg);
		}
	}

//	/**
//	 * @param mqttMsg
//	 * @return
//	 */
//	private  Boolean judgeTenantId(MqttMsg mqttMsg) {
//		String uuid=CommonCallBackHelper.getUuidFromTopic(mqttMsg);
//		String configUUID=environment.getProperty(Constants.AGENT_MQTT_USERNAME);
//		if(uuid.equals(configUUID)) {
//			return true;
//		}
//		FetchUserResp userInfo=userApi.getUserByUuid(uuid);
//		if(userInfo == null ) {
//			return false;
//		}else {
//			return true;
//		}
//	}

    private void deviceStatusResp(MqttMsg mqttMsg) {
        Map<String, Object> backMap = (Map<String, Object>) mqttMsg.getPayload();
        String deviceId = backMap.get("deviceId").toString();
        Map<String, Object> property = (Map<String, Object>) CenterControlDeviceStatus.getDeviceStatus(deviceId);
        GetDeviceInfoRespVo resp = deviceCoreApi.get(deviceId);
//        property.put("productId", resp.getProductId());
        String topic = CommonCallBackHelper.getTopic(mqttMsg.getTopic(), getUUID(), NETWORK);
        BusinessDispatchMqttHelper.sendDeviceTopic(property, topic);
    }

	/**
	 * 设备控制
	 * @param mqttMsg
	 */
	private void controlDevice(MqttMsg mqttMsg) {
		helper.commonControlDevice(mqttMsg);
	}

	private void deviceListResp(MqttMsg mqttMsg) {
		Map<String,Object> backMap=helper.commonDeviceListResp(mqttMsg);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),getUUID(),NETWORK);
		BusinessDispatchMqttHelper.sendDeviceListResp(mqttMsg,backMap,topic);
	}

	private void meetingListResp(MqttMsg mqttMsg) {
		Map<String,Object> backMap=helper.commonMeetingListResp(mqttMsg);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),getUUID(),NETWORK);
		BusinessDispatchMqttHelper.sendMeetingListResp(mqttMsg,backMap,topic);
	}

	/**
	 * 响应情景列表的请求
	 * @param mqttMsg
	 */
	private void sceneListResp(MqttMsg mqttMsg) {
		Map<String,Object> backMap=helper.commonSceneListResp(mqttMsg);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),getUUID(),NETWORK);
		BusinessDispatchMqttHelper.sendSceneListResp(mqttMsg,backMap,topic);
	}

    /**
	 * 执行情景响应
	 * @param mqttMsg
	 */
	private void excuScenResp(MqttMsg mqttMsg) {
		Map<String,Object> backMap=helper.commonExcuScenResp(mqttMsg);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),getUUID(),NETWORK);
		BusinessDispatchMqttHelper.sendSceneTopic(mqttMsg,backMap,topic);
	}
	
	/**
	 * 预约详情请求
	 * @param mqttMsg
	 */
	private void detailReservationReq(MqttMsg mqttMsg) {
		Map<String,Object> backMap=helper.commonGetReservationDetailResp(mqttMsg);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),getUUID(),NETWORK);
		BusinessDispatchMqttHelper.sendBookingDetail(mqttMsg,backMap,topic);
	}

	/**
	 * 预约详情请求
	 * @param mqttMsg
	 */
	private void userInfoResp(MqttMsg mqttMsg) {
		Map<String,Object> backMap=(Map<String,Object>)mqttMsg.getPayload();
		String uuid=helper.getUUIDByTenantId(backMap);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),uuid,CENTER);
		String topicNetwork=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),uuid,NETWORK);
		Map<String,Object> resp=helper.commonGetNearReservation(mqttMsg);
		if(resp!=null) {
			backMap.putAll(resp);
		}
		BusinessDispatchMqttHelper.sendUserInfoResp(mqttMsg,backMap,topic);//pc
		BusinessDispatchMqttHelper.sendUserInfoResp(mqttMsg,backMap,topicNetwork);//phones
	}

	/**
	 * 确认预约 注意主题转发pc
	 * @param mqttMsg
	 */
	private void confirmReservationReq(MqttMsg mqttMsg) {
		Map<String,Object> backMap=(Map<String,Object>)mqttMsg.getPayload();
		int code=helper.commonConfirmReservationResp(mqttMsg);
		String uuid=helper.getUUIDByTenantId(backMap);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),uuid,CENTER);
		String topicNetwork=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),uuid,NETWORK);
		BusinessDispatchMqttHelper.sendConfirmReservationResp(mqttMsg,(Map)mqttMsg.getPayload(),topic,code);//pc
		BusinessDispatchMqttHelper.sendConfirmReservationResp(mqttMsg,(Map)mqttMsg.getPayload(),topicNetwork,code);//phones
	}

	/**
	 * 删除预约请求
	 * @param mqttMsg
	 */
	private void delReservationReq(MqttMsg mqttMsg) {
		helper.commonDelReservationReq(mqttMsg);
	}

    /**
	 * 获取预约信息
	 * @param mqttMsg
	 */
	private void getReservationResp(MqttMsg mqttMsg) {
		Map<String,Object> backMap=helper.commonGetReservationResp(mqttMsg);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),getUUID(),NETWORK);
		BusinessDispatchMqttHelper.sendBookingList(mqttMsg,backMap,topic);
	}

    /**
	 * 添加或更新预约
	 * @param mqttMsg
	 */
	private void addOrUpdateReservation(MqttMsg mqttMsg) {
		Boolean flag=helper.commonAddOrUpdateReservation(mqttMsg);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),getUUID(),CommonCallBackHelper.NETWORK);
		Map<String,Object> params=new HashMap<>();params.put("ack", flag);
		BusinessDispatchMqttHelper.sendAddOrUpdateReservationResp(mqttMsg,params,topic);
	}

    /**
	 * 发送外网设备状态
     * @param property
	 */
	public void deviceStatusResp(Map<String,Object> property) {
//		if(BusinessDispatchMqttHelper.NETWORK_MQTT_CLINET_SET.size()>0) {
			String topic=CommonCallBackHelper.getTopic("iot/v1/c/[userId]/center/device",getUUID(),NETWORK);
			BusinessDispatchMqttHelper.sendDeviceTopic(property,topic);
//		}
	}

    /**
	 * 上线下线业务处理
	 * @param mqttMsg
	 */
	private void onlineOrOfflineBusiness(MqttMsg mqttMsg) {
//		String clientId=getClientFromTopic(mqttMsg.getTopic());
//		if(CONNECT.equals(mqttMsg.getMethod())) {//上线
//			BusinessDispatchMqttHelper.NETWORK_MQTT_CLINET_SET.add(clientId);
//		}else if(DISCONNECT.equals(mqttMsg.getMethod())){//下线 移除标志，size==0 停止推送
//			if(BusinessDispatchMqttHelper.NETWORK_MQTT_CLINET_SET.contains(clientId)) {
//				BusinessDispatchMqttHelper.NETWORK_MQTT_CLINET_SET.remove(clientId);
//
//                // 用户下线将redis中保存在redis的mqtt认证鉴权数据清除
//	            if(deviceService.getTenantType(mqttMsg)==Constants.SMART_BUILD){
//	            	refreshRedis(clientId, mqttMsg.getClientId());
//	            }
//			}
//		}
	}
	
	private void refreshRedis(String userUUID, String clientId){
    	String key = userUUID + ":pwd";
    	 Map<String, String> map = RedisCacheUtil.hashGetAll(key, String.class, false);
    	 if(map!=null&&StringUtils.isNotBlank(clientId)){
    		 String[] clientIdSplit = clientId.split("-");
    		 if(map.containsKey(clientIdSplit[0])){
    			 logger.info(key + " : redis remove mqtt map key : " + clientIdSplit[0]);
    			 RedisCacheUtil.hashRemove(key, clientIdSplit[0]);
    		 }
    	 }
    }

    public void spaceBackgroundImgResp(MqttMsg mqttMsg) {
		Map<String,Object> backMap=(Map<String,Object>)mqttMsg.getPayload();
		Long tenantId = Long.parseLong(backMap.get("tenantId").toString());
		Long spaceId = Long.parseLong(backMap.get("spaceId").toString());
		SpaceBackgroundImgReq req = new SpaceBackgroundImgReq();
		req.setSpaceId(spaceId);
		
		List<SpaceBackgroundImgResp> respList = spaceService.getSpaceBackgroundImg(req);
		if(CollectionUtils.isNotEmpty(respList)){
//			for(SpaceBackgroundImgResp resp : respList){
//				String bgImg = resp.getBgImg();
//				String bgImgUrl = fileApi.getGetUrl(bgImg).getPresignedUrl();
//				resp.setBgImgUrl(bgImgUrl);
//			}
		}
		backMap.clear();
		backMap.put("data", respList);
		String topic=CommonCallBackHelper.getTopic(mqttMsg.getTopic(),getUUID(),NETWORK);
		BusinessDispatchMqttHelper.sendBackgroundImg(mqttMsg, backMap, topic);
	}
	
}

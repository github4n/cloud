package com.iot.building.mqtt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.iot.building.reservation.service.IReservationService;
import com.iot.building.reservation.vo.ReservationReq;
import com.iot.building.reservation.vo.ReservationResp;
import com.iot.building.scene.service.SceneService;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.LoginReq;

@Service("commonCallBackHelper")
public class CommonCallBackHelper {

	public static final String CENTER="center";
	public static final String NETWORK="network";
	//主题部分内容contain
	public final static String SCENE_CONTAIN_TOPIC ="scene";
	public final static String DEVICE_CONTAIN_TOPIC ="device";
	public final static String SPACE_CONTAIN_TOPIC ="space";
	public final static String RESERVATION_CONTAIN_TOPIC ="reservation";
	public final static String MOTHOD_ADD_OR_UPDATE_RESERVATION_REQ="addOrUpdateReservationReq";
	public final static String MOTHOD_ADD_OR_UPDATE_RESERVATION_RESP="addOrUpdateReservationResp";
	public final static String MOTHOD_RESERVATION_LIST_REQ="reservationListReq";
	public final static String MOTHOD_DEL_RESERVATION_REQ="delReservationReq";
	public final static String MOTHOD_DETAIL_RESERVATION_REQ="reservationDetailReq";
	public final static String MOTHOD_SCENE_LIST_REQ="sceneListReq";
	public final static String MOTHOD_SCENE_EXCUTE_REQ="sceneExcuteReq";
	public final static String MOTHOD_DEVICE_LIST_REQ="deviceListReq";
	public final static String MOTHOD_DEVICE_CONTROL_REQ="deviceControlReq";
	public final static String MOTHOD_MEETING_LIST_REQ="meetingListReq";
	//返回
	public final static String MOTHOD_DEVICE_LIST_RESP="deviceListResp";
	public final static String MOTHOD_SPACE_STATUS="spaceStatus";
	public final static String MOTHOD_SCENE_STATUS="sceneStatus";
	public final static String MOTHOD_SCENE_LIST_RESP="sceneListResp";
	public final static String MOTHOD_WARNING_STATUS="warningStatus";
	public final static String MOTHOD_SYN_DEVICE="synDevice";
	public final static String MOTHOD_MEETING_LIST_RESP="meetingListResp";
	public final static String MOTHOD_RESERVATION_LIST_RESP="reservationListResp";
	public final static String MOTHOD_RESERVATION_DETAIL_RESP="reservationDetailResp";
	public final static String MOTHOD_USER_INFO_RESP="userInfoResp";
	public final static String MOTHOD_CONFIRM_RESERVATION_REQ="confirmReservationReq";
	public final static String MOTHOD_CONFIRM_RESERVATION_RESP="confirmReservationResp";
	public final static String MOTHOD_DEVICE_STATUS="deviceStatus";
	public final static String MOTHOD_SPACE_BACKGROUND_REQ="spaceBackgroundImgReq";
	public final static String MOTHOD_SPACE_BACKGROUND_RESP="spaceBackgroundImgResp";
    @Autowired
    private SceneService sceneService;
    @Autowired
    private IBuildingSpaceService spaceService;
    @Autowired
    private IReservationService reservationService;
    @Autowired
    private UserApi userApi;

//    @SuppressWarnings({"unused", "unchecked"})
//    public static <T> List<Map<String, Object>> entityToMap(List<T> list) {
//        List<Map<String, Object>> mapList = new ArrayList<>();
//        if (CollectionUtils.isNotEmpty(list)) {
//            for (T t : list) {
//                Map<String, Object> playload = new HashMap<>();
//                String jsonStr = JSON.toJSONString(t);
//                playload = (Map<String, Object>) JSON.parseObject(jsonStr);
//                mapList.add(playload);
//            }
//        }
//        return mapList;
//    }

    public static String getTopic(String topic, String uuid, String typeStr) {
        String endStr = topic.split("/")[5];
        return "iot/v1/c/" + uuid + "/" + typeStr + "/" + endStr;
    }

	/**
	 * 设备控制
	 * @param mqttMsg
	 */
	public void commonControlDevice(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		String deviceId=params.get("deviceId").toString();
		Map<String,Object> targetValue=(Map<String,Object>)params.get("targetValue");
		spaceService.control(deviceId, targetValue);
	}

	public Map<String,Object> commonDeviceListResp(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		Map<String,Object> backMap=new HashMap<>();
		String spaceId=params.get("spaceId").toString();
		Long orgId=Long.parseLong(backMap.get("orgId").toString());
		Long tenantId=Long.parseLong(backMap.get("tenantId").toString());
		List<Map<String,Object>> mapList=spaceService.findDeviceByRoom(Long.valueOf(spaceId),orgId,tenantId);
		backMap.put("data", mapList);
		return backMap;
	}

	public Map<String,Object> commonMeetingListResp(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		Map<String,Object> backMap=new HashMap<>();
		Long locationId=Long.parseLong(params.get("locationId").toString());
		Long orgId=Long.parseLong(params.get("orgId").toString());
		Long tenantId=Long.parseLong(params.get("tenantId").toString());
		List<Map<String,Object>> mapLsit=spaceService.getMeetingSpaceTree(tenantId,orgId,locationId);
		backMap.put("data", mapLsit);
		return backMap;
	}

    /**
	 * 删除预约请求
	 * @param mqttMsg
	 */
	public void commonDelReservationReq(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		String id=params.get("id").toString();
		reservationService.delReservationById(Long.parseLong(id));
	}

    /**
	 * 获取预约信息
	 * @param mqttMsg
	 */
	public Map<String,Object> commonGetReservationResp(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		Long id=null,spaceId=null;
		String openId=null;
		if(params.get("id") !=null && StringUtils.isNotBlank(params.get("id").toString())) {
			id=Long.parseLong(params.get("id").toString());
		}
		if(params.get("openId") !=null && StringUtils.isNotBlank(params.get("openId").toString())) {
			openId=params.get("openId").toString();
		}
		if(params.get("spaceId") !=null) {
			spaceId=Long.parseLong(params.get("spaceId").toString());
		}
		ReservationReq req=new ReservationReq();
		if(id !=null) {
			req.setId(id);
		}
		if(openId !=null && StringUtils.isNotBlank(openId)) {
			req.setOpenId(openId);
		}
		if(spaceId !=null ) {
			req.setSpaceId(spaceId);
		}

        try {
            if (params.get("start") != null && params.get("end") != null
					&& (params.get("id") ==null || StringUtils.isBlank(params.get("id").toString()))) {
				Long start=Long.parseLong(params.get("start").toString());
				Long end=Long.parseLong(params.get("end").toString());
				req.setStartTime(start);req.setEndTime(end);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<ReservationResp> respList=reservationService.findByReservationList(req);
		Map<String,Object> backMap=new HashMap<>();
		List<Map<String,Object>> mapList=new ArrayList<>();
		mapList=entityToMap(respList);
		backMap.put("data", mapList);
		backMap.put("spaceId", spaceId);
		return backMap;
	}

    /**
	 * 获取单个预约
	 * @param mqttMsg
	 */
	public Map<String,Object> commonGetReservationDetailResp(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		Long id=Long.parseLong(params.get("id").toString());
		ReservationReq req=new ReservationReq();
		req.setId(id);
		Map<String,Object> backMap=new HashMap<>();
		List<ReservationResp> respList=reservationService.findByReservationList(req);
		List<Map<String,Object>> mapList=new ArrayList<>();
		mapList=entityToMap(respList);
		backMap.put("data", mapList);
		return backMap;
	}

    /**
	 * 获取单个预约
	 * @param mqttMsg
	 */
	public Map<String,Object> commonGetNearReservation(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		String openId=params.get("openId").toString();
		ReservationReq req=new ReservationReq();
		req.setOpenId(openId);
//		if(params.get("spaceId") !=null) {
//			Long spaceId=Long.parseLong(params.get("spaceId").toString());
//			req.setSpaceId(spaceId);
//		}
		ReservationResp resp=reservationService.findNearResercationByOpenId(req);
		if(resp==null) {
			return null;
		}
		String josn=JSON.toJSONString(resp);
		Map<String,Object> backMap=JSON.parseObject(josn, Map.class);
		return backMap;
	}

	/**
	 * 确认开启预约
	 * @param mqttMsg
	 */
	public int commonConfirmReservationResp(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		Long id=Long.parseLong(params.get("id").toString());
		String openId=params.get("openId").toString();
		ReservationReq req=new ReservationReq();
		req.setId(id);req.setOpenId(openId);
		List<ReservationResp> respList=reservationService.findByReservationList(req);
		if(respList.size()==0) {
			return -1;
		}
		req.setFlag(1);req.setOpenId(null);
		reservationService.updateReservation(req);
		ReservationResp resp =respList.get(0);
		if(resp.getModel() !=null) {
			new Thread(()-> {
				sceneService.sceneExecute(resp.getTenantId(),resp.getModel());
			}).start();
		}
		return 200;
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	public static <T> List<Map<String,Object>> entityToMap(List<T> list){
		List<Map<String,Object>> mapList=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(list)) {
			for(T t:list) {
				Map<String,Object> playload=new HashMap<>();
				String jsonStr=JSON.toJSONString(t);
				playload=(Map<String, Object>) JSON.parseObject(jsonStr);
				mapList.add(playload);
			}
		}
		return mapList;
	}
	
//	public static <T> List<Map<String,Object>> reservationRespToMap(List<ReservationResp> list){
//		List<Map<String,Object>> mapList=new ArrayList<>();
//		if(CollectionUtils.isNotEmpty(list)) {
//			for(ReservationResp t:list) {
//				Map<String,Object> playload=new HashMap<>();
//				playload.put("id", t.getId());
//				playload.put("createTime", t.getCreateTime());
//				playload.put("flag", t.getFlag());
//				playload.put("model", t.getModel());
//				playload.put("name", t.getName());
//				playload.put("openId", t.getOpenId());
//				playload.put("phone", t.getPhone());
//				playload.put("spaceId", t.getSpaceId());
//				playload.put("status", t.getStatus());
//				playload.put("tenantId", t.getTenantId());
//				playload.put("type", t.getType());
//				playload.put("startTime", t.getStartTime());
//				playload.put("endTime", t.getEndTime());
//				mapList.add(playload);
//			}
//		}
//		return mapList;
//	}

	/**
	 * 添加或更新预约
	 * @param mqttMsg
	 */
	@SuppressWarnings("unchecked")
	public Boolean commonAddOrUpdateReservation(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		try {//0时区转换为date
			Long startTime=Long.parseLong(params.get("startTime").toString());
			Long endTime=Long.parseLong(params.get("endTime").toString());
			params.put("startTime",com.iot.util.ToolUtils.timestampToDate(startTime));
			params.put("endTime",com.iot.util.ToolUtils.timestampToDate(endTime));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String jsonStr=JSON.toJSONString(params);
		ReservationReq req=JSON.parseObject(jsonStr,ReservationReq.class);
		return reservationService.saveReservation(req);
	}

    /**
	 * 响应情景列表的请求
	 * @param mqttMsg
	 */
	public Map<String,Object> commonSceneListResp(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		Map<String,Object> backMap=new HashMap<>();
		List<Map<String,Object>> mapList=new ArrayList<>();
		String spaceId=params.get("spaceId").toString();
		SceneReq sceneReq = new SceneReq();
		sceneReq.setSpaceId(Long.parseLong(spaceId));
		List<SceneResp> sceneList=sceneService.findSceneDetailList(sceneReq);
		 if(!CollectionUtils.sizeIsEmpty(sceneList)) {
			 mapList=CommonCallBackHelper.entityToMap(sceneList);
		 }
		 backMap.put("data", mapList);
		 backMap.put("spaceId", spaceId);
		 return backMap;
	}

    /**
	 * 执行情景响应
	 * @param mqttMsg
	 */
	public Map<String,Object> commonExcuScenResp(MqttMsg mqttMsg) {
		Map<String,Object> params=(Map<String,Object>)mqttMsg.getPayload();
		String sceneId=params.get("sceneId").toString();
		Long tenantId=Long.parseLong(params.get("tenantId").toString());
		sceneService.sceneExecute(tenantId,Long.valueOf(sceneId));
		return params;
	}

    public String getUUIDByTenantId(Map<String, Object> backMap) {
		Long tenantId=Long.parseLong(backMap.get("tenantId").toString());
		LoginReq user=new LoginReq();
		user.setTenantId(tenantId);user.setAdminStatus(-1);
		FetchUserResp resp=userApi.getUserByCondition(user);
		return resp.getUuid();
	}
	
}

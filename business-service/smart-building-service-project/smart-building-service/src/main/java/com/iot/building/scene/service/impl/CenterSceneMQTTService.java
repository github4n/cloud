package com.iot.building.scene.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.iot.building.helper.CenterControlDeviceStatus;
import com.iot.building.helper.Constants;
import com.iot.building.helper.MapCallBack;
import com.iot.building.scene.service.SceneService;
import com.iot.common.enums.APIType;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import com.iot.device.api.CentralControlDeviceApi;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;

@Service("centerScene")
public class CenterSceneMQTTService implements CommonSceneMQTTInterface {

	private static final Logger logger = LoggerFactory.getLogger(SceneMQTTService.class);

    @Autowired
    private DeviceCoreApi deviceApi;
    
    @Autowired
    private CentralControlDeviceApi centralControlDeviceApi;
    
    @Autowired
    private SceneService sceneService;
    
	@Override
	public void addSceneRuleReq(MqttMsg msg, String topic) {
		logger.info("2B添加情景规则请求回调：" + msg.toString());
		
	}
	@Override
	public void editSceneRuleReq(MqttMsg msg, String topic) {
		logger.info("2B编辑情景规则请求回调：" + msg.toString());
		
	}
	@Override
	public void addSceneRuleResp(MqttMsg msg, String topic) {
		logger.info("2B添加情景规则响应回调：" + msg.toString());
		
	}
	@Override
	public void editSceneRuleResp(MqttMsg msg, String topic) {
		logger.info("2B编辑情景规则响应回调：" + msg.toString());
		
	}
	@Override
	public void delSceneReq(MqttMsg message, String topic) {
		logger.info("2B删除情景请求回调：" + message.toString());
		
	}
	@Override
	public void getSceneRuleReq(MqttMsg message, String topic) {
		logger.info("2B获取情景规则请求回调：" + message.toString());
		
	}
	@Override
	public void delSceneRuleReq(MqttMsg message, String topic) {
		logger.info("2B删除情景规则请求回调：" + message.toString());
		
	}
	@Override
	public void delSceneRuleResp(MqttMsg message, String topic) {
		logger.info("2B删除情景规则响应回调：" + message.toString());
		
	}
	@Override
	public void delSceneResp(MqttMsg message, String topic) {
		logger.info("2B删除情景响应回调：" + message.toString());
		
	}
	@Override
	public void excSceneReq(MqttMsg message, String topic) {
		logger.info("2B执行情景请求回调：" + message.toString());
		
	}
	@Override
	public void excSceneResp(MqttMsg message, String topic) {
		commonExceResp(message,Constants.SCENE_ID);
	}
	private void commonExceResp(MqttMsg message,String sceneIdKey) {
		logger.info("2B执行情景响应回调：" + message.toString());
		MqttMsgAck ack = MqttMsgAck.successAck();
		String directDeviceId = message.getSrcAddr().split("\\.")[1];
		Map<String, Object> paramMap = (Map<String, Object>) message.getPayload();
		Long sceneId = Long.parseLong(paramMap.get(sceneIdKey).toString());
		if (message.getAck().getCode() == MqttMsgAck.SUCCESS) {
			//2B更新设备缓存和消息推送
			deviceCacheUpdateAndPushMsg(directDeviceId, sceneId);
		}
	}

	private void deviceCacheUpdateAndPushMsg(String deviceId, Long sceneId) {
		logger.info("2B更新设备缓存和消息推送 sceneId ：" + sceneId);
		GetDeviceInfoRespVo getDeviceInfoRespVo = deviceApi.get(deviceId);
		SceneDetailReq sceneDetailReq = new SceneDetailReq();
		sceneDetailReq.setSceneId(sceneId);
		sceneDetailReq.setTenantId(getDeviceInfoRespVo.getTenantId());
		sceneDetailReq.setLocationId(getDeviceInfoRespVo.getLocationId());
		List<SceneDetailResp> sceneDetailResps = sceneService.findSceneDetailInfo(sceneDetailReq);
		for (SceneDetailResp sceneDetailResp : sceneDetailResps) {
			GetDeviceInfoRespVo deviceInfoRespVo = deviceApi.get(sceneDetailResp.getDeviceId());
			Map<String, Object> targetValue = JSON.parseObject(sceneDetailResp.getTargetValue());
			if (MapUtils.isNotEmpty(targetValue)) {
				CenterControlDeviceStatus.putDeviceStatus(sceneDetailResp.getDeviceId(), targetValue);// 更新内存中
				MapCallBack.mapCallBack(deviceInfoRespVo, targetValue, APIType.MultiProtocolGateway);//消息推送
			}
		}
	}
	
	/**********************************2b情景回调************************************************/
	
	/**
	 * 5.2.9  场景操作回调
	 * @param message
	 * @param topic
	 */
	public void operateResp(MqttMsg message, String topic) {
		commonExceResp(message,Constants.SCENE_THEN_UUID);
	}
	
	/**
	 * 5.2.1 创建场景回调
	 * @param message
	 * @param topic
	 */
	public void createResp(MqttMsg message, String topic) {
		
	}
	
	/**
	 * 5.2.2  删除场景回调
	 * @param message
	 * @param topic
	 */
	public void deleteResp(MqttMsg message, String topic) {
		
	}
	
	/**
	 * 5.2.2  删除场景回调
	 * @param message
	 * @param topic
	 */
	public void updateBaseResp(MqttMsg message, String topic) {
		
	}
	
	/**
	 * 5.2.4 添加场景项回调
	 * @param message
	 * @param topic
	 */
	public void addActionResp(MqttMsg message, String topic) {
		
	}
	
	/**
	 * 5.2.4 添加场景项回调
	 * @param message
	 * @param topic
	 */
	public void delActionResp(MqttMsg message, String topic) {
		
	}
	
	/**
	 * 5.2.6 修改场景项回调
	 * @param message
	 * @param topic
	 */
	public void updateActionResp(MqttMsg message, String topic) {
		
	}
	
	/**
	 * 5.2.7 查询场景项列表回调
	 * @param message
	 * @param topic
	 */
	public void queryThenResp(MqttMsg message, String topic) {
		
	}
	
	/**
	 * 5.2.8 查询场景列表回调
	 * @param message
	 * @param topic
	 */
	public void queryBaseResp(MqttMsg message, String topic) {
		
	}
}
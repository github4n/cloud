package com.iot.building.device.service;

import com.alibaba.fastjson.JSON;
import com.iot.building.helper.CenterControlDeviceStatus;
import com.iot.building.helper.DispatcherRouteHelper;
import com.iot.building.helper.MapCallBack;
import com.iot.building.ifttt.mapper.TriggerTobMapper;
import com.iot.building.ifttt.vo.TriggerVo;
import com.iot.building.mqtt.BusinessDispatchMqttHelper;
import com.iot.building.scene.service.SceneService;
import com.iot.building.utils.MQTTUtils;
import com.iot.common.enums.APIType;
import com.iot.common.util.StringUtil;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service("ifttt")
public class IFTTTMQTTService implements CallBackProcessor {

    private final static Logger LOGGER = LoggerFactory.getLogger(IFTTTMQTTService.class);

    public static final int QOS = 1;

    @Autowired
    private TriggerTobMapper triggerTobMapper;

    @Autowired
    private DeviceCoreApi deviceCoreApi;
    @Autowired
    private SceneService sceneService;
    @Override
    public void onMessage(MqttMsg mqttMsg) {
        //DispatcherRouteHelper.dispatch(this, mqttMsg);
        LOGGER.info("=====ifttt===onMessage==");
        iftttStateNotify(mqttMsg,mqttMsg.getTopic());
    }


    public void iftttStateNotify(MqttMsg mqttMsg, String reqTopic) {
        LOGGER.info("=====iftttStateNotify=====", mqttMsg, reqTopic);
        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        String clientId = MQTTUtils.parseReqTopic(reqTopic);
        LOGGER.info("=====iftttStateNotify===clientId=="+clientId);
        String uuid = payload.get("uuid").toString();
        LOGGER.info("=====iftttStateNotify===uuid=="+uuid);
        GetDeviceInfoRespVo vo = deviceCoreApi.get(clientId);
        LOGGER.info("=====iftttStateNotify===GetDeviceInfoRespVo=="+vo);
        List<TriggerVo> list =  triggerTobMapper.selectByRuleId(Long.valueOf(uuid),vo.getTenantId());
        LOGGER.info("=====iftttStateNotify===list=="+list.size());
        if(CollectionUtils.isNotEmpty(list)){
            for(TriggerVo triggerVo : list){
                LOGGER.info("=====iftttStateNotify===list==start====");
                if(StringUtil.isNotEmpty(triggerVo.getActuctorDeviceId())){
                    LOGGER.info("=====iftttStateNotify===list==process====");
                    deviceCacheUpdateAndPushMsg(clientId,Long.valueOf(triggerVo.getActuctorDeviceId()));
                    LOGGER.info("=====iftttStateNotify===success==");
                }
            }
        }
    }

    private void deviceCacheUpdateAndPushMsg(String deviceId, Long sceneId) {
        logger.info("2B更新设备缓存和消息推送 sceneId ：" + sceneId);
        GetDeviceInfoRespVo getDeviceInfoRespVo = deviceCoreApi.get(deviceId);
        SceneDetailReq sceneDetailReq = new SceneDetailReq();
        sceneDetailReq.setSceneId(sceneId);
        sceneDetailReq.setTenantId(getDeviceInfoRespVo.getTenantId());
        sceneDetailReq.setLocationId(getDeviceInfoRespVo.getLocationId());
        List<SceneDetailResp> sceneDetailResps = sceneService.findSceneDetailInfo(sceneDetailReq);
        LOGGER.info("=====deviceCacheUpdateAndPushMsg===sceneDetailResps=="+sceneDetailResps.size());
        for (SceneDetailResp sceneDetailResp : sceneDetailResps) {
            GetDeviceInfoRespVo deviceInfoRespVo = deviceCoreApi.get(sceneDetailResp.getDeviceId());
            Map<String, Object> targetValue = JSON.parseObject(sceneDetailResp.getTargetValue());
            if (MapUtils.isNotEmpty(targetValue)) {
                LOGGER.info("=====deviceCacheUpdateAndPushMsg===更新内存中==");
                CenterControlDeviceStatus.putDeviceStatus(sceneDetailResp.getDeviceId(), targetValue);// 更新内存中
                MapCallBack.mapCallBack(deviceInfoRespVo, targetValue, APIType.MultiProtocolGateway);//消息推送
            }
        }
    }



    /*@Override
    public void onMessage(MqttMsg mqttMsg) {
    	try {
            if (mqttMsg == null) {
                return;
            }
            DispatcherRouteHelper.dispatch(centerDeviceMQTTService, mqttMsg);
    	}catch (Exception e) {
			e.printStackTrace();
		}

    }*/
}
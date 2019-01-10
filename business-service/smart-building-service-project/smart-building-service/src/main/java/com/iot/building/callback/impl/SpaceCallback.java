package com.iot.building.callback.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import com.iot.building.callback.ListenerCallback;
import com.iot.building.helper.Constants;
import com.iot.building.mqtt.BusinessDispatchMqttHelper;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.common.beans.BeanUtil;
import com.iot.common.enums.APIType;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;

public class SpaceCallback implements ListenerCallback {

    private static final Logger log = LoggerFactory.getLogger(SpaceCallback.class);

    private IBuildingSpaceService service;
    private SpaceApi commonSpaceServie =ApplicationContextHelper.getBean(SpaceApi.class);;

    private Environment dnvironment = ApplicationContextHelper.getBean(Environment.class);

    @Override
    public void callback(GetDeviceInfoRespVo device, Map<String, Object> map, APIType apiType) {
        try {
            log.info("............进入空间回调。。。。。。。。。。。。。" + device.getUuid() + "=====" + map.toString());
            service = ApplicationContextHelper.getBean(IBuildingSpaceService.class);
            if (map.containsKey("Energy") || map.containsKey("Temp") || map.containsKey("Tamper")
                    || map.containsKey("PowerLow")) {
                return;
            }
            // TODO 发送主题
            Map<String, Object> callBackMap = getSpaceId(device);
            if (callBackMap != null && device != null) {
                Long spaceId = (Long) callBackMap.get("spaceId");
                String status = map.get("Alarm") != null ? String.valueOf(map.get("Alarm"))
                        : String.valueOf(map.get("Smoke"));// 设备告警状态
                if (status != null && status.equals("1")
                        && Constants.warningTypeMap().containsKey(device.getDeviceTypeId().toString())) {
                    callBackMap.put("status", 2);
                } else {
                    SpaceResp spaceResp = commonSpaceServie.findSpaceInfoBySpaceId(device.getTenantId(),spaceId);
                    SpaceReq space = new SpaceReq();
                    BeanUtil.copyProperties(spaceResp, space);
//                    List<String> deviceIds = service.getDeviceIdBySpaceId(space.getId());
                    int flag = service.setSpaceStatus(spaceId,device.getOrgId(),device.getTenantId());
                    log.info("空间sapceId=" + spaceId + "的状态=" + flag + "====================");
                    callBackMap.put("status", flag);
                }
                String uuid = dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
                String topic = "iot/v1/c/" + uuid + "/center/space";
                map.put("tenantId", device.getTenantId());
                BusinessDispatchMqttHelper.sendSpaceTopic(callBackMap, topic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置该设备归属的空间
     *
     * @param deviceId
     */
    private Map<String, Object> getSpaceId(GetDeviceInfoRespVo device) {
        SpaceDeviceResp spaceDevice = service.findSpaceIdByDeviceId(device.getUuid(),device.getOrgId(),device.getTenantId());
        Map<String, Object> callBackMap = new HashMap<>();
        if (spaceDevice != null) {
            callBackMap.put("spaceId", spaceDevice.getSpaceId());
            return callBackMap;
        }
        return null;
    }
}

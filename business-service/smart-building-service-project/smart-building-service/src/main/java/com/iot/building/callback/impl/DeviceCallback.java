package com.iot.building.callback.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import com.iot.building.callback.ListenerCallback;
import com.iot.building.helper.CenterControlDeviceStatus;
import com.iot.building.helper.Constants;
import com.iot.building.mqtt.BusinessDispatchMqttHelper;
import com.iot.building.mqtt.network.NetWorkMqttCallBack;
import com.iot.common.enums.APIType;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;

public class DeviceCallback implements ListenerCallback {

	private static final Logger log = LoggerFactory.getLogger(DeviceCallback.class);
	
	private NetWorkMqttCallBack netWorkMqttCallBack=ApplicationContextHelper.getBean(NetWorkMqttCallBack.class);
	private Environment dnvironment=ApplicationContextHelper.getBean(Environment.class);
	
	@Override
	public void callback(GetDeviceInfoRespVo device, Map<String, Object> map, APIType apiType) {
		log.info("............进入设备回调。。。。。。。。。。。。。");
		try {
			if(!filterPlugProperty(map, device)) {
				return;
			}
			String uuid=dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
			String topic="iot/v1/c/"+uuid+"/center/device";
			//发送设备主题
			Map<String, Object> deviceStates = (Map<String, Object>) CenterControlDeviceStatus.getDeviceStatus(device.getUuid());
			Map<String, Object> payload = new HashMap<>();
			payload.putAll(deviceStates);
			payload.put("productId", device.getProductId());
			payload.put("deviceId", device.getUuid());
			payload.put("tenantId", device.getTenantId());
			payload.put("deviceTypeId", device.getDeviceTypeId());
			BusinessDispatchMqttHelper.sendDeviceTopic(payload,topic);
			//发送外网状态
			netWorkMqttCallBack.deviceStatusResp(payload);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Boolean filterPlugProperty(Map<String,Object> map,GetDeviceInfoRespVo device) {
		if (device == null) {
			return false;
		}
		if(device.getDeviceTypeId() !=null 
				&& Constants.getPlugTypeMap().get(device.getDeviceTypeId().toString()) !=null) {
            return map.containsKey("OnOffStatus");
		}
		return true;
	}
}


package com.iot.building.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.vo.req.DeviceStateInfoReq;
import com.iot.device.vo.req.device.AddCommDeviceStateInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceStateReq;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;


public class CenterControlDeviceStatus {

	static DeviceStateCoreApi deviceStateServiceApi=ApplicationContextHelper.getBean(DeviceStateCoreApi.class);;
	static DeviceCoreApi deviceApi=ApplicationContextHelper.getBean(DeviceCoreApi.class);;
	
    public static void putDeviceStatus(String deviceId,Map<String,Object> attr){
    	GetDeviceInfoRespVo deviceResp=deviceApi.get(deviceId);
    	UpdateDeviceStateReq req=new UpdateDeviceStateReq();
    	List<AddCommDeviceStateInfoReq> deviceStateInfoReqList=new ArrayList<>();
    	Object value;
    	Map<String, Object> propertyMap= getDeviceStatus(deviceId);
    	for (String key : attr.keySet()) {
    		value=attr.get(key);
    		AddCommDeviceStateInfoReq stateReq=new AddCommDeviceStateInfoReq();
    		if (key.equals("RGBW") && (value instanceof Number)) {
				value = ToolUtils.convertToRGB(Integer.valueOf(value.toString()) / 256);
			}
    		stateReq.setPropertyName(key);
    		stateReq.setPropertyValue(value.toString());
			deviceStateInfoReqList.add(stateReq);
			
		}
    	req.setDeviceId(deviceId);
    	req.setStateList(deviceStateInfoReqList);
    	req.setTenantId(deviceResp.getTenantId());
    	deviceStateServiceApi.saveOrUpdate(req);
    }

	private static DeviceStateInfoReq getInfoReq(String deviceId, String key,Object value) {
		DeviceStateInfoReq inReq=new DeviceStateInfoReq();
		inReq.setDeviceId(deviceId);
		inReq.setPropertyName(key);
		inReq.setPropertyValue(value.toString());
		return inReq;
	}
    
    public static Map<String,Object> getDeviceStatus(String deviceId){
    	GetDeviceInfoRespVo deviceResp=deviceApi.get(deviceId);
    	return deviceStateServiceApi.get(deviceResp.getTenantId(),deviceId);
    }
    
}

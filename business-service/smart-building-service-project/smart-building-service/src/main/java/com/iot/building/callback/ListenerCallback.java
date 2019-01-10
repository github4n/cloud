package com.iot.building.callback;

import java.util.Map;

import com.iot.common.enums.APIType;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;


public interface ListenerCallback {
	public void callback(GetDeviceInfoRespVo device, Map<String, Object> map, APIType apiType);
}

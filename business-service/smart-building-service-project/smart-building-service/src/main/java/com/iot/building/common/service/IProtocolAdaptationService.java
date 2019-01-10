package com.iot.building.common.service;

import com.iot.building.common.vo.ProtocolParamVo;

public interface IProtocolAdaptationService {

	public String controlProtocolAdaptation(ProtocolParamVo protocolParamVo);
	
	public String queryDeviceStatus(ProtocolParamVo protocolParamVo);
	
}

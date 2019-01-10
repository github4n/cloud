package com.iot.control.ifttt.vo.req;

import java.io.Serializable;
import java.util.Map;

import com.iot.common.enums.APIType;
import com.iot.control.ifttt.vo.SensorVo;
import com.iot.control.ifttt.vo.res.RuleResp;

public class ExecIftttReq implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4001266994999950991L;
	
	private RuleResp ruleResp;
	private SensorVo sensor;
	private Map<String, Object> callbackMap;
	private APIType apiType;
	
	public RuleResp getRuleResp() {
		return ruleResp;
	}
	public void setRuleResp(RuleResp ruleResp) {
		this.ruleResp = ruleResp;
	}
	public SensorVo getSensor() {
		return sensor;
	}
	public void setSensor(SensorVo sensor) {
		this.sensor = sensor;
	}
	public Map<String, Object> getCallbackMap() {
		return callbackMap;
	}
	public void setCallbackMap(Map<String, Object> callbackMap) {
		this.callbackMap = callbackMap;
	}
	public APIType getApiType() {
		return apiType;
	}
	public void setApiType(APIType apiType) {
		this.apiType = apiType;
	}

}

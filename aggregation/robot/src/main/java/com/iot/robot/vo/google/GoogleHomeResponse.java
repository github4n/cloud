package com.iot.robot.vo.google;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iot.robot.vo.Endpoint;
import com.iot.robot.vo.ResponsePost;
/**
 * 创建人：chenxiaolin
 * 创建时间：2018年4月26日 下午5:11:08
 * 修改人： chenxiaolin
 * 修改时间：2018年4月26日 下午5:11:08
 */
public class GoogleHomeResponse extends AbstractGoogleRes implements ResponsePost{
	
	private List<GoogleEndpoint> devices;
	
	public GoogleHomeResponse() {
		devices = new ArrayList<GoogleEndpoint>();
		payload = new HashMap<String, Object>();
		payload.put("devices", devices);
	}
	public Map<String, Object> getPayload() {
		return payload;
	}
	public void setPayload(Map<String, Object> payload) {
		this.payload = payload;
	}
	public void setAgentUserId(String agentUserId) {
	    payload.put("agentUserId", agentUserId);
	}
	@Override
	public void addEndpoint(Endpoint e) {
		devices.add((GoogleEndpoint) e);
	}

	public List<GoogleEndpoint> returnDevices() {
		return devices;
	}
}

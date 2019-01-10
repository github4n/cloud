package com.iot.robot.vo.alexa;

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
public class AlexaDiscoveryResponse implements ResponsePost {

	private Map<String, List<AlexaEndpoint>> payload = new HashMap<String, List<AlexaEndpoint>>();
	
	private List<AlexaEndpoint> endpoints = new ArrayList<AlexaEndpoint>();
	
	private Map<String, String> header = new HashMap<String, String>();

	public AlexaDiscoveryResponse() {
		payload.put("endpoints", endpoints);
		
	}
	public Map<String, List<AlexaEndpoint>> getPayload() {
		return payload;
	}

	public Map<String, String> getHeader() {
		return header;
	}
	
	public void addEndpoint(Endpoint e) {
		endpoints.add((AlexaEndpoint) e);
	}
	public void build(String messageId) {
		header.put("namespace", "Alexa.Discovery");
		header.put("name", "Discover.Response");
		header.put("payloadVersion", "3");
		header.put("messageId", messageId);
	}
}

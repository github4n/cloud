package com.iot.robot.vo.google;

import java.util.HashMap;
import java.util.Map;

public class AbstractGoogleRes {

	protected String requestId;

	protected Map<String,Object> payload = new HashMap<String, Object>();

	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
}

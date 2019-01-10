package com.iot.robot.vo.google;


import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 创建人：chenxiaolin
 * 创建时间：2018年4月26日 下午5:11:08
 * 修改人： chenxiaolin
 * 修改时间：2018年4月26日 下午5:11:08
 */
public class GoogleHomeModel implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4338091672834770695L;
	
	private String requestId;
	private List<Map<String,Object>> inputs;
	private Map<String,Object> payload;
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public List<Map<String, Object>> getInputs() {
		return inputs;
	}
	public void setInputs(List<Map<String, Object>> inputs) {
		this.inputs = inputs;
	}
	public Map<String, Object> getPayload() {
		return payload;
	}
	public void setPayload(Map<String, Object> payload) {
		this.payload = payload;
	} 
	
	public static void main(String[] args) {
		GoogleHomeModel m = new GoogleHomeModel();
		m.setRequestId("dfd");
		Object json = JSONObject.toJSON(m);
		System.out.println(json);
	}
}

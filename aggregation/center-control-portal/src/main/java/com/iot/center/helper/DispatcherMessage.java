package com.iot.center.helper;

import java.io.Serializable;

/**
 * MQTT 消息体 
 * @author fenglijian
 * @version 1.0
 */
public class DispatcherMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4486381356962783923L;
	
	private String service;
	
	private String method;
	
	private String seq;
	
	private String srcAddr;
	
	private Object payload;

	public String toString() {
		return "service:"+service+";method:"+method+";seq:"+seq+";srcAddr:"+srcAddr+";payload:"+payload;
	}
	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getSrcAddr() {
		return srcAddr;
	}

	public void setSrcAddr(String srcAddr) {
		this.srcAddr = srcAddr;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
}

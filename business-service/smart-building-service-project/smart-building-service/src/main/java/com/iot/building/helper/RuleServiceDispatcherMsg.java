package com.iot.building.helper;

import java.util.Map;

public class RuleServiceDispatcherMsg {

	/**
	 * 
	 */
	private String SERVICE; 
	private String METHOD; 
	private Map<String,Object> MSG_BODY;
	private String REQUEST_ID;
	private String CODE = "200";
	private String ERR_MSG;
	private String REQUEST_CLIENT_ID;
	private String REQUEST_TOPIC;
	
	
	public String getREQUEST_TOPIC() {
		return REQUEST_TOPIC;
	}
	public void setREQUEST_TOPIC(String rEQUEST_TOPIC) {
		REQUEST_TOPIC = rEQUEST_TOPIC;
	}
	public String getREQUEST_CLIENT_ID() {
		return REQUEST_CLIENT_ID;
	}
	public void setREQUEST_CLIENT_ID(String rEQUEST_CLIENT_ID) {
		REQUEST_CLIENT_ID = rEQUEST_CLIENT_ID;
	}
	public String getSERVICE() {
		return SERVICE;
	}
	public void setSERVICE(String sERVICE) {
		SERVICE = sERVICE;
	}
	public String getMETHOD() {
		return METHOD;
	}
	public void setMETHOD(String mETHOD) {
		METHOD = mETHOD;
	}
	public Map<String,Object> getMSG_BODY() {
		return MSG_BODY;
	}
	public void setMSG_BODY(Map<String,Object> mSG_BODY) {
		MSG_BODY = mSG_BODY;
	}
	public String getREQUEST_ID() {
		return REQUEST_ID;
	}
	public void setREQUEST_ID(String rEQUEST_ID) {
		REQUEST_ID = rEQUEST_ID;
	}
	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	public String getERR_MSG() {
		return ERR_MSG;
	}
	public void setERR_MSG(String eRR_MSG) {
		ERR_MSG = eRR_MSG;
	}
	@Override
	public String toString() {
		return "RuleServiceDispatcherMsg [SERVICE=" + SERVICE + ", METHOD="
				+ METHOD + ", MSG_BODY=" + MSG_BODY + ", REQUEST_ID="
				+ REQUEST_ID + ", CODE=" + CODE + ", ERR_MSG=" + ERR_MSG
				+ ", REQUEST_USER_ID=" + REQUEST_CLIENT_ID + "]";
	}
	
}

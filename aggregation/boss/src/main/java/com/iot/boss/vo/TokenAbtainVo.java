package com.iot.boss.vo;

public class TokenAbtainVo {

	private String code;
	private String client_id;
	private String grant_type;
	private String refresh_token;
	public String getCode() {
		return code;
	}
	public void setCode(String code1) {
		this.code = code1;
	}
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getGrant_type() {
		return grant_type;
	}
	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
}

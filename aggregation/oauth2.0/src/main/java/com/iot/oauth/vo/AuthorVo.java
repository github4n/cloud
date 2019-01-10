package com.iot.oauth.vo;

import java.io.Serializable;

public class AuthorVo implements Serializable {

	private static final long serialVersionUID = 5557001108617955042L;

	private String client_id;
	private String redirect_uri;
	private String response_type;
	private String domain;
	private String access_type;
	private String approval_prompt;
	private String state;
	private String code;
	private String userUuid;
	// oauth端 生成的临时 uuid
	private String uuid;

	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getRedirect_uri() {
		return redirect_uri;
	}
	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}
	public String getResponse_type() {
		return response_type;
	}
	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getAccess_type() {
		return access_type;
	}
	public void setAccess_type(String access_type) {
		this.access_type = access_type;
	}
	public String getApproval_prompt() {
		return approval_prompt;
	}
	public void setApproval_prompt(String approval_prompt) {
		this.approval_prompt = approval_prompt;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}

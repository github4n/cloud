package com.iot.user.vo;

public class LoginRobotReq extends LoginReq {

	/**
	 * 1、alexa
	 * 2、googlehome
     * 3、ifttt
	 */
    private Integer client;
    
    private String clientId;

    private Long thirdPartyInfoId;

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Long getThirdPartyInfoId() {
        return thirdPartyInfoId;
    }

    public void setThirdPartyInfoId(Long thirdPartyInfoId) {
        this.thirdPartyInfoId = thirdPartyInfoId;
    }
}

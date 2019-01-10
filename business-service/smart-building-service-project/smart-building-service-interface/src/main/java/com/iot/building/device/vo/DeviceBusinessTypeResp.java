package com.iot.building.device.vo;

public class DeviceBusinessTypeResp extends DeviceBusinessTypeReq{
	
	/**
     * 业务代码
	 * code
     */
	private Long code;
	
	/**
     * 名称
	 * name
     */
	private String name;
	
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
package com.iot.robot.vo.alexa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iot.robot.abilty.alexa.Alexa;
import com.iot.robot.vo.Endpoint;
/**
 * 创建人：chenxiaolin
 * 创建时间：2018年4月26日 下午5:11:08
 * 修改人： chenxiaolin
 * 修改时间：2018年4月26日 下午5:11:08
 */
public class AlexaEndpoint implements Endpoint {
	private String endpointId;
	private String friendlyName;
	private String description = "";
	private String manufacturerName = "Commercial Electric";
	private List<String> displayCategories = new ArrayList<String>();
	private List<Alexa> capabilities = new ArrayList<>(); 
	private Map<String,String> cookie = new HashMap<>();
	public String getEndpointId() {
		return endpointId;
	}
	public void setEndpointId(String endpointId) {
		this.endpointId = endpointId;
	}
	public String getFriendlyName() {
		return friendlyName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public List<Alexa> getCapabilities() {
		return capabilities;
	}
	public Map getCookie() {
		return cookie;
	}
	public void setCookie(Map cookie) {
		this.cookie = cookie;
	}
	public List<String> getDisplayCategories() {
		return displayCategories;
	}
	public void addDisplayCategories(String displayCategory) {
		this.displayCategories.add(displayCategory);
	}

	@Override
	public void addDescription(String description) {
		this.description = description;
	}

	@Override
	public void addManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	@Override
	public void setEndpointName(String endpointName) {
		this.friendlyName = endpointName;
	}
	@SuppressWarnings("hiding")
	public void addCapabilities(Object capability) {
		this.capabilities.add((Alexa) capability);
	}
	@Override
	public int capSize() {
		return capabilities.size();
	}

	@Override
	public void addAttributes(String key, Object val) {

	}
}

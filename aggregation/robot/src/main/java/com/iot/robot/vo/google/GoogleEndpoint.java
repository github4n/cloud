package com.iot.robot.vo.google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iot.common.util.StringUtil;
import com.iot.robot.vo.Endpoint;
/**
 * 创建人：chenxiaolin
 * 创建时间：2018年4月26日 下午5:11:08
 * 修改人： chenxiaolin
 * 修改时间：2018年4月26日 下午5:11:08
 */
public class GoogleEndpoint implements Endpoint {

	private String id;
	
	private String type = "";
	
	private List<String> traits = new ArrayList<>();
	
	private Map<String,Object> name = new HashMap<String, Object>();
	
	private boolean willReportState = false;
	
	private Map<String,Object> deviceInfo = new HashMap<String, Object>();
	
	private Map<String,Object> customData = new HashMap<String, Object>();

	private Map<String,Object> attributes = new HashMap<String, Object>();

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}
	public List<String> getTraits() {
		return traits;
	}
	public Map<String, Object> getName() {
		return name;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public boolean isWillReportState() {
		return willReportState;
	}

	public Map<String, Object> getDeviceInfo() {
		return deviceInfo;
	}

	public Map<String, Object> getCustomData() {
		return customData;
	}

	@Override
	public void setEndpointId(String endpointId) {
		this.id = endpointId;
	}

	@Override
	public void setEndpointName(String endpointName) {
		this.name.put("name", endpointName);
	}

	@Override
	public void addCapabilities(Object capability) {
		this.traits.add((String) capability);
	}

	@Override
	public void addDisplayCategories(String displayCategory) {
		this.type = displayCategory;
	}

	@Override
	public void addDescription(String description) {

	}

	@Override
	public void addManufacturerName(String manufacturerName) {

	}

	@Override
	public void addAttributes(String key, Object val) {
		if (StringUtil.isBlank(key) || val == null) {
			return ;
		}

		this.attributes.put(key, val);
	}

	@Override
	public int capSize() {
		return traits.size();
	}
	
}

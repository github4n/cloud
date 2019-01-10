package com.iot.robot.vo;


public interface Endpoint {
	public void setEndpointId(String endpointId);
	public void setEndpointName(String endpointName);
	public void addCapabilities(Object capability);
	public void addDisplayCategories(String displayCategory);
	public void addDescription(String description);
	public void addManufacturerName(String manufacturerName);
	public void addAttributes(String key, Object val);
	public int capSize();
}

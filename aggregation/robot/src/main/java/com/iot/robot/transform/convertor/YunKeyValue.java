package com.iot.robot.transform.convertor;

public class YunKeyValue<T> {

	private String endpointId;
	private String key;
	private T value;

	public String getEndpointId() {
		return endpointId;
	}

	public void setEndpointId(String endpointId) {
		this.endpointId = endpointId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
	    return key.equals(obj.toString());
	}
}

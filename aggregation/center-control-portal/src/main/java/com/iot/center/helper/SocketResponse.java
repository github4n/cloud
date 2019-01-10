package com.iot.center.helper;

public class SocketResponse {

	private String type;//类型
	
	private Object value;//属性
	
	public SocketResponse(String type,Object value){
		this.type=type;
		this.value=value;
	}
	
	public SocketResponse(){
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
}

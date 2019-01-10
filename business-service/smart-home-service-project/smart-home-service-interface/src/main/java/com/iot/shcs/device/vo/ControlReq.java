package com.iot.shcs.device.vo;

import java.util.HashMap;
import java.util.Map;

public class ControlReq extends RobotReq {
    
    private boolean multiC = false;

    private Map<String, Object> kvs = new HashMap<>();

	private String key;
	private Object value;
    
    public Map<String, Object> getKvs() {
        return kvs;
    }

    public void setKvs(String key, Object value) {
        kvs.put(key, value);
        this.key = key;
        this.value = value;
        multiC = true;
    }
    
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
		multiC = false;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public boolean isMultiC() {
        return multiC;
    }
}

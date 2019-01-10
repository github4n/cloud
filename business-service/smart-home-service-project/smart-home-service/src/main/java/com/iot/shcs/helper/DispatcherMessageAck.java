package com.iot.shcs.helper;

import java.io.Serializable;

public class DispatcherMessageAck implements Serializable {
    private int code;
    private String desc;

    public static final int SUCCESS=200;

    public static final int BUSINESS_ERROR=400;

    public static final int ERROR=500;

    public DispatcherMessageAck() {

    }
    
    public DispatcherMessageAck(int code,String desc) {
    	this.code=code;
    	this.desc=desc;
    }
    
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public static com.iot.control.helper.DispatcherMessageAck successAck() {
    	return failureAck(SUCCESS, "success");
    }

    public static com.iot.control.helper.DispatcherMessageAck failureAck(int code, String desc) {
    	return new com.iot.control.helper.DispatcherMessageAck(code, desc);
    }
}

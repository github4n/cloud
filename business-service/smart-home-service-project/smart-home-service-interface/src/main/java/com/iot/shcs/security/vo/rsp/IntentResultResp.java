package com.iot.shcs.security.vo.rsp;

public class IntentResultResp {

    public static final Integer OFF  = 0;
    public static final Integer STAY = 1;
    public static final Integer AWAY = 3;

    public static final Integer STATUS_OFF  = 0;
    public static final Integer STATUS_STAY = 1;
    public static final Integer STATUS_AWAY = 3;
    public static final Integer STATUS_RUNNING_AWAY = 4;
    public static final Integer STATUS_RUNNING_STAY = 5;
    public static final Integer STATUS_UNREADY = 6;

    // Password error, please confirm the password
    public static final Integer PWERROR = 4;

    // Some security devices are not ready. Please check your security status.
    public static final Integer UNREADY = 5;

    // You are not authorized
    public static final Integer INVALIDUSER = 99;

    // There is a problem. Please try again later.
    public static final Integer ERROR = 100;

    public static final Integer SUCCESS = 200;

    private String errorMsg;
    
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

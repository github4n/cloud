package com.iot.smarthome.exception;


import java.io.Serializable;

/**
 * SmartHome异常
 */
public class SmartHomeException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -2684365557861576910L;

    private String errorCode;

    private String message;

    public SmartHomeException() {

    }

    public SmartHomeException(String errorCode) {
        this.errorCode = errorCode;
    }

    public SmartHomeException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
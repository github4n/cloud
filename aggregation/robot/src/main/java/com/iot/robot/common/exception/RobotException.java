package com.iot.robot.common.exception;


import java.io.Serializable;

/**
 * robot异常
 */
public class RobotException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -2684365557861576910L;

    private String errorCode;

    private String message;

    public RobotException() {

    }

    public RobotException(String errorCode) {
        this.errorCode = errorCode;
    }

    public RobotException(String errorCode, String message) {
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
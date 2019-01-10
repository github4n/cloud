package com.iot.robot.utils.google;

import com.iot.robot.utils.ErrorCodeKeys;
import com.iot.robot.utils.alexa.AlexaErrorCode;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/8/29 15:40
 * @Modify by:
 */
public enum GoogleErrorCode {
    /*TIMEOUT("timeout"),
    UN_KNOW_ERROR("unknownError"),
    DEVICE_OFFLINE("deviceOffline"),
    // 设备开关状态为 关
    DEVICE_TURNED_OFF("deviceTurnedOff"),
    DEVICE_NOT_FOUND("deviceNotFound"),
    NOT_SUPPORTED("notSupported"),
    ;*/

    TIMEOUT("timeout", ErrorCodeKeys.TIMEOUT),
    UN_KNOW_ERROR("unknownError", ErrorCodeKeys.UN_KNOW_ERROR),
    DEVICE_OFFLINE("deviceOffline", ErrorCodeKeys.DEVICE_OFFLINE),
    // 设备开关状态为 关
    DEVICE_TURNED_OFF("deviceTurnedOff", ErrorCodeKeys.DEVICE_TURNED_OFF),
    DEVICE_NOT_FOUND("deviceNotFound", ErrorCodeKeys.DEVICE_NOT_FOUND),
    NOT_SUPPORTED("notSupported", ErrorCodeKeys.NOT_SUPPORTED);

    private String googleCode;
    private String yunCode;

    GoogleErrorCode(String googleCode, String yunCode) {
        this.googleCode = googleCode;
        this.yunCode = yunCode;
    }

    public static String getErrorCodeByYunCode(String yunCode) {
        for (GoogleErrorCode errorCode : values()) {
            if (errorCode.getYunCode().equals(yunCode)) {
                return errorCode.getGoogleCode();
            }
        }
        return UN_KNOW_ERROR.getGoogleCode();
    }

    public String getGoogleCode() {
        return googleCode;
    }

    public String getYunCode() {
        return yunCode;
    }
}

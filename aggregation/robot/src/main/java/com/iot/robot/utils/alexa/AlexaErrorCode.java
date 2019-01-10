package com.iot.robot.utils.alexa;

import com.alibaba.fastjson.JSON;
import com.iot.robot.utils.ErrorCodeKeys;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/9/11 15:55
 * @Modify by:
 */
public enum AlexaErrorCode {

    ENDPOINT_UNREACHABLE("ENDPOINT_UNREACHABLE", ErrorCodeKeys.DEVICE_OFFLINE, "Unable to reach endpoint because it appears to be offline."),
    // 设备开关状态为 关
    BRIDGE_UNREACHABLE("BRIDGE_UNREACHABLE", ErrorCodeKeys.DEVICE_TURNED_OFF, "Endpoint is off right now, so can't make any adjustments."),
    NO_SUCH_ENDPOINT("NO_SUCH_ENDPOINT", ErrorCodeKeys.DEVICE_NOT_FOUND, "the target endpoint does not exist or no longer exists."),
    INTERNAL_ERROR("INTERNAL_ERROR", ErrorCodeKeys.UN_KNOW_ERROR, "some thing was wrong, please operate later."),
    NOT_SUPPORTED("INTERNAL_ERROR", ErrorCodeKeys.NOT_SUPPORTED, "notSupported."),
    TIMEOUT("INTERNAL_ERROR", ErrorCodeKeys.TIMEOUT, "timeout.");

    private String alexaCode;
    private String yunCode;
    private String desc;


    AlexaErrorCode(String alexaCode, String yunCode, String desc) {
        this.alexaCode = alexaCode;
        this.yunCode = yunCode;
        this.desc = desc;
    }

    public static AlexaErrorCode getAlexaErrorCodeByYunCode(String yunCode) {
        for (AlexaErrorCode errorCode : values()) {
            if (errorCode.getYunCode().equals(yunCode)) {
                return errorCode;
            }
        }
        return INTERNAL_ERROR;
    }

    public String getAlexaCode() {
        return alexaCode;
    }

    public String getYunCode() {
        return yunCode;
    }

    public String getDesc() {
        return desc;
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(AlexaErrorCode.INTERNAL_ERROR));
        System.out.println(AlexaErrorCode.INTERNAL_ERROR.getYunCode());
    }
}

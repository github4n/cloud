package com.iot.smarthome.constant;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/9/11 15:55
 * @Modify by:
 */
public enum SmartHomeErrorCode {

    TIMEOUT("TIMEOUT", "request timeout."),

    // 设备离线
    DEVICE_OFFLINE("DEVICE_OFFLINE", "unable to reach device because it appears to be offline."),

    // 设备开关状态为 关
    DEVICE_TURNED_OFF("DEVICE_TURNED_OFF", "device is off right now, so can't make any adjustments."),

    DEVICE_NOT_FOUND("DEVICE_NOT_FOUND", "the target device does not exist or no longer exists."),

    // 内部报错
    INTERNAL_ERROR("INTERNAL_ERROR", "some thing was wrong, please operate later."),

    // 不支持 协议/参数
    NOT_SUPPORTED("NOT_SUPPORTED", "notSupported."),

    // 参数错误
    PARAMETER_ERROR("PARAMETER_ERROR", "parameter error."),

    // token过期
    EXPIRED_TOKEN("EXPIRED_TOKEN", "accessToken have expired."),

    // token验证失败
    INVALID_TOKEN("INVALID_TOKEN", "general failure to authenticate."),

    // 未知错误
    UN_KNOWN_ERROR("UN_KNOWN_ERROR", "unknown error.");

    private String code;
    private String desc;


    SmartHomeErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}

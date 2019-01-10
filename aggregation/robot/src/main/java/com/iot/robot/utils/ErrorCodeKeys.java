package com.iot.robot.utils;

/**
 * @Descrpiton: 智能音箱对接 错误码(云端公共错误码)
 * @Author: yuChangXing
 *
 * @Date: 2018/8/29 15:40
 * @Modify by:
 */
public class ErrorCodeKeys {
    public static final String TIMEOUT = "timeout";
    public static final String UN_KNOW_ERROR = "unknownError";
    public static final String DEVICE_OFFLINE = "deviceOffline";
    // 设备开关状态为 关
    public static final String DEVICE_TURNED_OFF = "deviceTurnedOff";
    public static final String DEVICE_NOT_FOUND = "deviceNotFound";
    public static final String NOT_SUPPORTED = "notSupported";

    // ***** customSkill
    // ***** 安防
    // 无效用户
    public static final String INVALID_USER = "invalid_user";
    // 密码有误
    public static final String PWD_ERROR = "pwd_error";
    // 未绑定网关 -- You haven't add any security devices.
    public static final String UNBOUND_GATEWAY = "unbound_gateway";
}

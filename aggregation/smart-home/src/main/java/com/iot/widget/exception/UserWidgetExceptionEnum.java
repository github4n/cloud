package com.iot.widget.exception;

import com.iot.common.exception.IBusinessException;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/7 10:51
 * 修改人:
 * 修改时间：
 */
public enum UserWidgetExceptionEnum implements IBusinessException {
    // ***** 设备相关
    // 设备离线
    DEVICE_OFFLINE(10101, "DEVICE_OFFLINE"),
    // 设备开关状态为 关
    DEVICE_TURNED_OFF(10102, "DEVICE_TURNED_OFF"),
    DEVICE_NOT_FOUND(10103, "DEVICE_NOT_FOUND"),

    // ***** scene
    SCENE_NOT_FOUND(10201, "SCENE_NOT_FOUND"),
    // scene内容为空
    SCENE_EMPTY(10202, "SCENE_EMPTY"),

    // ***** 安防
    UNBOUND_GATEWAY(10301, "UNBOUND_GATEWAY"),


    // ***** 其它

    // 不支持 协议/参数
    NOT_SUPPORTED(30601, "NOT_SUPPORTED"),
    // 参数错误
    PARAMETER_ERROR(30602, "PARAMETER_ERROR"),
    // request timeout.
    TIMEOUT(30603, "TIMEOUT"),
    // 内部报错
    INTERNAL_ERROR(30604, "INTERNAL_ERROR"),
    // 未知错误
    UN_KNOWN_ERROR(30615, "UN_KNOWN_ERROR");


    private int code;
    private String messageKey;

    UserWidgetExceptionEnum(String messageKey) {
        this.code = 400;
        this.messageKey = messageKey;
    }

    UserWidgetExceptionEnum(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }
}

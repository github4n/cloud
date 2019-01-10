package com.iot.ifttt.common;

/**
 * 描述：ifttt服务枚举
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/28 19:28
 */
public enum IftttServiceEnum {

    TIMER("timer"),  //定时服务
    ASTRONOMICAL("astronomical"), //天文定时服务
    TIME_RANGE("timeRange"), //时间范围服务
    DEV_STATUS("devStatus"), //设备状态服务
    DEV_EVENT("devEvent"), //设备事件服务
    MQ("MQ"), //MQ消息服务
    EMAIL("email"), //邮件服务
    SMS("sms"); //短信服务

    private String code;

    IftttServiceEnum(String code) {
        this.code = code;
    }

    public static IftttServiceEnum getEnum(String code) {
        for (IftttServiceEnum vo : IftttServiceEnum.values()) {
            if (vo.code.equals(code)) {
                return vo;
            }
        }
        return null;
    }

    public String getCode() {
        return this.code;
    }
}

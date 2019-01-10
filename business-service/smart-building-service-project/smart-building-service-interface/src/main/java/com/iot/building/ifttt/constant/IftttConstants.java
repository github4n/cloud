package com.iot.building.ifttt.constant;

import com.iot.common.constant.SystemConstants;

/**
 * 描述：常量类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/25 11:29
 */
public class IftttConstants extends SystemConstants {
    /**
     * 运行状态（0-停止，1-运行）
     */
    public static final Byte STOP = 0;
    public static final Byte RUNNING = 1;

    // IFTTT 触发器类型
    public final static String IFTTT_TRIGGER_TIMER = "timer";
    public final static String IFTTT_TRIGGER_TIMER_2B = "timer_2B";
    public final static String IFTTT_TRIGGER_DEVICE = "dev";
    public final static String IFTTT_TRIGGER_SUNRISE = "sunrise";
    public final static String IFTTT_TRIGGER_SUNSET = "sunset";

    //ifttt属性
    public final static String TRIGGER_TYPE_TIMIMG = "timing";
    public final static String TRIGGER_TYPE_WEATHER = "weather";
    public final static String TRIGGER_TYPE_PEOPLE = "people";

    //IFTTT 模板标识
    public final static Byte IFTTT_TEMPLATE_FALSE = 0;
    public final static Byte IFTTT_TEMPLATE_TRUE = 1;

    /**
     * IFTTT 规则类型
     * (0=普通, 1=安防)
     */
    public final static Byte IFTTT_RULE_TYPE_NORMAL = 0;
    public final static Byte IFTTT_RULE_TYPE_SECURITY = 1;

    //安防类型 securityType
    public final static String IFTTT_SECURITY_STAY = "stay";
    public final static String IFTTT_SECURITY_AWAY = "away";

    //安防类型 securityType
    public final static Byte IFTTT_SECURITY_TYPE_STAY = 1;
    public final static Byte IFTTT_SECURITY_TYPE_AWAY = 2;
    public final static Byte IFTTT_SECURITY_TYPE_OFF = 3;

    //是否跨网关
    public final static Byte IFTTT_MULTI_FALSE = 0;
    public final static Byte IFTTT_MULTI_TRUE = 1;

    public final static String IFTTT_2B_FLAG = "_2B";
}

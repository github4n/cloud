package com.iot.shcs.security.constant;

import com.iot.common.constant.SystemConstants;

/*安防常量类*/
public class SecurityConstants extends SystemConstants {
    /**
     * 运行状态（0-停止，1-运行）
     */
    public static final Integer STOP = 0;
    public static final Integer RUNNING = 1;


    /**
     * IFTTT 规则类型
     * (0=普通, 1=安防)
     */
    public final static Byte IFTTT_RULE_TYPE_NORMAL = 0;
    public final static Byte IFTTT_RULE_TYPE_SECURITY = 1;

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


    /**
     * IFTTT 规则类型
     * (0=普通, 1=安防)
     */
    public final static Byte SECURITY_RULE_TYPE_NORMAL = 0;
    public final static Byte SECURITY_RULE_TYPE_SECURITY = 1;

    //安防类型 securityType
    public final static String SECURITY_STAY = "stay";
    public final static String SECURITY_AWAY = "away";
    public final static String SECURITY_OFF="off";
    public final static String SECURITY_PANIC="panic";


    //安防类型 securityType
    public final static Integer SECURITY_TYPE_STAY = 1;
    public final static Integer SECURITY_TYPE_AWAY = 2;
    public final static Integer SECURITY_TYPE_OFF = 3;


}

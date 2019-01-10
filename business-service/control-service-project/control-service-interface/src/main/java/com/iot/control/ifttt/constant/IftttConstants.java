package com.iot.control.ifttt.constant;

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
    public final static String IFTTT_TRIGGER_DEVICE = "dev";
    public final static String IFTTT_TRIGGER_SUNRISE = "sunrise";
    public final static String IFTTT_TRIGGER_SUNSET = "sunset";

    //ifttt属性
    public final static String TRIGGER_TYPE_TIMIMG = "timing";


}

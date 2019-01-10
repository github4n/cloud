package com.iot.shcs.ifttt.constant;

import com.iot.common.constant.SystemConstants;

/**
 * 描述：常量类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/25 11:29
 */
public class AutoConstants extends SystemConstants {
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

    //是否跨网关
    public final static Byte IFTTT_MULTI_FALSE = 0;
    public final static Byte IFTTT_MULTI_TRUE = 1;

    // IFTTT 执行器类型
    public final static String IFTTT_THEN_DEVICE = "dev";
    public final static String IFTTT_THEN_SCENE = "scene";

    // 是否直连设备标识
    public final static String DEVICE_DIRECT_YES = "1";
    public final static String DEVICE_DIRECT_NO = "0";

    // 删除错误码
    public final static int NOT_EXIST = -10992;
    public final static int INVALID_IFTTT_ID = -33810;//无效的iftttId

}

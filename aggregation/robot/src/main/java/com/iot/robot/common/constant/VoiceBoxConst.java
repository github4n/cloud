package com.iot.robot.common.constant;

/**
 * @Descrpiton: 音箱常量
 * @Author: yuChangXing
 * @Date: 2018/10/8 14:15
 * @Modify by:
 */
public class VoiceBoxConst {

    // 音箱mqtt消息的seq前缀
    public static final String SEQ_PREFIX = "r_";

    // 报警
    public static final String SECURITY_COMMAND_PANIC = "panic";
    // 取消报警(不是把安防 设置为off模式)
    public static final String SECURITY_COMMAND_UN_PANIC = "unPanic";
    // 把安防 设置为off模式
    public static final String SECURITY_COMMAND_DISARM = "disarm";
    // 布防
    public static final String SECURITY_COMMAND_ARM = "arm";

    // 操作失败 默认提示语
    public static final String DEFAULT_ERROR_TIP_MESSAGE = "There is a problem, please try again later.";
}

package com.iot.widget.utils;

import com.iot.common.util.StringUtil;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/7 14:36
 * 修改人:
 * 修改时间：
 */
public class WidgetUtil {
    // user widget控制的mqtt消息seq前缀
    public static final String SEQ_PREFIX = "w_";

    /**
     *  生成带"r_" 前缀的seq
     *
     * @return
     */
    public static String generateRobotSeq() {
        return SEQ_PREFIX + StringUtil.getRandomString(8);
    }
}

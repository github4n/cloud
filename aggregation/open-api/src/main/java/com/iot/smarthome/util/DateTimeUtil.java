package com.iot.smarthome.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/19 10:44
 * @Modify by:
 */
public class DateTimeUtil {
    /**
     * 日期格式yyyy-MM-dd HH:mm:ss
     */
    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期格式yyyy-MM-dd
     */
    public static final String YYYYMMDD = "yyyy-MM-dd";


    /**
     * 描述：获取当前时间
     *
     * @return yyyy-MM-dd HH:mm:ss格式字符串
     * @since
     */
    public static String getNowDateTime() {
        return DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS).format(LocalDateTime.now());
    }

    /**
     * 描述：获取当前日期
     *
     * @return yyyy-MM-dd格式字符串
     * @since
     */
    public static String getNowDate() {
        return DateTimeFormatter.ofPattern(YYYYMMDD).format(LocalDateTime.now());
    }

    /**
     * 描述：将Date格式化成字符串
     *
     * @param date    时间
     * @param pattern 时间格式
     * @return 自定义格式字符串
     * @since
     */
    public static String format(Date date, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    /**
     * 描述：将 long时间戳 格式化成字符串
     *
     * @param time 时间戳
     * @since
     */
    public static String formatDateTime(long time) {
        return DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS).format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
    }



    public static void main(String[] args) {
        System.out.println(DateTimeUtil.getNowDateTime());
        System.out.println(DateTimeUtil.getNowDate());
        System.out.println(DateTimeUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }
}

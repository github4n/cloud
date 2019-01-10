package com.iot.airswitch.constant;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/10/15
 * @Description: *
 */
public class Constants {

    public static final String CMD_STATUS_REPORT = "A0";
    public static final String CMD_QUERY_STATUS = "A1";
    public static final String CMD_ACM_COUNT = "A2";
    public static final String CMD_ACM_QUERY = "A3";
    public static final String CMD_PUSH_EVENT = "A4";
    public static final String CMD_HEART_BEAT = "AA";
    public static final String CMD_REGISTER = "B0";
    public static final String CMD_GET_NET_CONFIG = "B1";
    public static final String CMD_PUSH_NET_CONFIG = "B2";
    public static final String CMD_SET_NET_CONFIG = "B3";
    public static final String CMD_PUSH_GW_CONFIG = "B4";
    public static final String CMD_GET_GW_CONFIG = "B5";
    public static final String CMD_SET_GW_CONFIG = "B7";
    public static final String CMD_USER_REGISTER = "B9";
    public static final String CMD_USER_MANAGER = "BA";

    /**
     * UID 默认
     */
    public static final String UID_NMM = "FFFFFF00";

    public static final String CMD_PVER = "F1";
    public static final String CMD_VER = "00";
    public static final String CMD_ACK = "00";

    // 直连设备标识
    public final static int IS_DIRECT_DEVICE = 1;// 直连设备
    public final static int IS_NOT_DIRECT_DEVICE = 0;// 非直连设备

    public final static Long AIR_SWITCH_PRODUCT_ID = 1090210993L;
    public final static Long AIR_SWITCH_DEVICE_TYPE_ID = -4000L;

    public final static Long AIR_SWITCH_NODE_PRODUCT_ID = 1090210994L;
    public final static Long AIR_SWITCH_NODE_DEVICE_TYPE_ID = -4001L;

    public final static String PROPERTY_CODE_DHCP = "AirDHCP";
    public final static String PROPERTY_CODE_DNS = "AirDNS";
    public final static String PROPERTY_CODE_HEART_BEAT = "AirHeartBeat";

    public final static String DATE_FORMAT_YYYY_MM_DD_HH = "yyyy-MM-dd HH";
    public final static String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public final static String DATE_FORMAT_YYYY_MM = "yyyy-MM";
    public final static String DATE_FORMAT_HH = "HH";

    public final static Integer STATISTICS_MIN = 60;
    public final static Integer STATISTICS_HOUR = 3600;

    public final static String ELECTRICITY_REDIS_PREFIX = "airswitch:electricity:";
    public final static String ELECTRICITY_REDIS_DATE = "yyyy-MM-dd-HH";

    public final static String AIR_SWITCH_HEART_BEAT_REDIS_PREFIX = "airswitch:heartbeat:";

    public final static String TOTAL_ELECTRICITY_COUNT_REDIS = "airswitch:totalCount";
    public final static String BUSINESS_ELECTRICITY_COUNT_REDIS = "airswitch:businessCount";

    public final static String SPILT_SIGN = "0x00x0";

    public final static Integer AIR_SWITCH_PORT = 5918;

    public final static List<Integer> EarlyWarningList = Lists.newArrayList(9, 12, 13, 14, 15);
    public final static List<Integer> AlarmList = Lists.newArrayList(0, 1, 2, 3, 4, 5, 6, 8, 10, 11, 22, 23);
}

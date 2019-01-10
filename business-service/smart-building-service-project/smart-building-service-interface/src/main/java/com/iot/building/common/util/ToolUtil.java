package com.iot.building.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ToolUtil {

	public static Long localToUTC() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Date localDate= new Date();
        long localTimeInMillis=localDate.getTime();
        /** long时间转换成Calendar */
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(localTimeInMillis);
        /** 取得时间偏移量 */
        int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
        /** 取得夏令时差 */
        int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
        /** 从本地时间里扣除这些差量，即可以取得UTC时间*/
        calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        /** 取得的时间就是UTC标准时间 */
        Long millis=calendar.getTimeInMillis();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        return millis;
    }
}

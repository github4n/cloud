package com.iot.ifttt.util;

import com.google.common.collect.Maps;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * 描述：时间工具类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/10/18 19:42
 */
public class TimeUtil {

    public static long getTimeGap(String area) {
        ZoneId zoneId = getZoneId(area);

        // 指定地区当前时间
        ZonedDateTime zdt = ZonedDateTime.now(zoneId);
        // 偏移的秒数
        long totalSeconds = zdt.getOffset().getTotalSeconds();
        return totalSeconds / 60;
    }

    /**
     * 获取时区对象
     *
     * @param area 地区,如 Asia/Shanghai、America/Los_Angeles
     * @return ZoneId
     * @author yuChangXing
     * @created 2018/07/26 16:49
     */
    public static ZoneId getZoneId(String area) {
        ZoneId zoneId = null;
        try {
            zoneId = ZoneId.of(area);
        } catch (Exception e) {
            zoneId = ZoneId.systemDefault();
        }
        return zoneId;
    }

    public static String getUTCAt(String at, String area) {
        Long timeGap = getTimeGap(area);
        int gap = timeGap.intValue();

        String[] strArray = at.split(":");
        String h = strArray[0];
        String m = strArray[1];

        /*时区处理start*/
        Map<String, Integer> map = getUTCTimeMap(h, m, gap);
        /*时区处理end*/
        return map.get("h") + ":" + map.get("m");
    }

    /**
     * 获取0时区cron
     *
     * @param cron
     * @param area
     * @return
     */
    public static String getUTCCron(String cron, String area) {
        Long timeGap = getTimeGap(area);
        int gap = timeGap.intValue();
        String[] strArray = cron.split(" ");
        String m = strArray[1];
        String h = strArray[2];

        /*时区处理start*/
        Map<String, Integer> map = getUTCTimeMap(h, m, gap);
        /*时区处理end*/

        int int_h = map.get("h");
        int int_m = map.get("m");
        strArray[1] = int_m + "";
        strArray[2] = int_h + "";
        String res = "";
        for (String v : strArray) {
            res += v + " ";
        }
        res = res.substring(0, res.length() - 1);

        return res;
    }

    private static Map<String, Integer> getUTCTimeMap(String h, String m, int gap) {
        int int_h = Integer.parseInt(h);
        int int_m = Integer.parseInt(m);
        /*时区处理start*/
        if (gap > 0) {
            int gap_h = gap / 60;
            int gap_m = gap % 60;

            //减偏移
            //分
            int_m -= gap_m;
            if (int_m < 0) {
                int_m += 60;
                int_h -= 1;
            }
            //小时
            int_h -= gap_h;
            if (int_h < 0) {
                int_h += 24;
            }
        } else if (gap < 0) {
            gap = -gap;

            int gap_h = gap / 60;
            int gap_m = gap % 60;

            //加偏移
            //分钟
            int_m += gap_m;
            if (int_m > 60) {
                int_m -= 60;
                int_h += 1;
            }
            //小时
            int_h += gap_h;
            if (int_h > 24) {
                int_h -= 24;
            }
        }
        /*时区处理end*/
        Map<String, Integer> map = Maps.newHashMap();
        map.put("h", int_h);
        map.put("m", int_m);
        return map;
    }
}

package com.iot.building.ifttt.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

/**
 * 描述：时间格式工具类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/2 9:30
 */
public class TimeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeUtil.class);

    /**
     * 获取定时任务时间表达式
     *
     * @param properties
     * @return
     */
    public static String getCron(String properties) {
        Map<Object, Object> propMap = JSON.parseObject(properties, Map.class);

        String at = (String) propMap.get("at");
        List<Integer> repeats = (List<Integer>) propMap.get("repeat");
        return convertCron(at, repeats);
    }

    /**
     * 定时任务，时间格式转换
     *
     * @param at
     * @param repeats
     * @return
     */
    public static String convertCron(String at, List<Integer> repeats) {
        String cron = "";
        //格式转换 0 0 12 ? * ?
        String[] timeArray = at.split(":");
        String m = timeArray[1];
        String h = timeArray[0];
        if (timeArray[1].equals("00")) {
            m = "0";
        }
        if (timeArray[0].equals("00")) {
            h = "0";
        }

        cron += "0 " + m + " " + h;

        //周几
        String week = "";
        int index = 1;
        int len = repeats.size();
        for (Integer day : repeats) {
            day++;
            if (index != len) {
                week += day + ",";
            } else {
                week += day;
            }
            index++;
        }

        //?：匹配该域的任意值。月份的天河周的天互相冲突，必须将其中一个设置为?
        if (len == 7) { // 周一到周天都选了，用?代替
            week = "?";
            cron += " * * ";
        }else{
            cron += " ? * ";
        }
        cron += week;
        return cron;
    }

    public static String getUTCCron(String cron, int gap) {
        String[] strArray = cron.split(" ");
        String m = strArray[1];
        String h = strArray[2];

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
        } else if(gap < 0) {
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
        strArray[1] = int_m + "";
        strArray[2] = int_h + "";
        String res = "";
        for (String v : strArray) {
            res += v + " ";
        }
        res = res.substring(0, res.length() - 1);

        return res;
    }

    /**
     * 获取时间格式
     *
     * @param time
     * @return
     */
    public static String getTime(String time) {
        String[] times = time.split(":");
        int h = Integer.parseInt(times[0]);
        int m = Integer.parseInt(times[1]);
        boolean pm_flag = false;
        if (h > 12) {
            pm_flag = true;
            h = h - 12;
        }
        String h_str = h + "";
        if (h < 10) {
            h_str = "0" + h;
        }
        String m_str = m + "";
        if (m < 10) {
            m_str = "0" + m;
        }

        String newTime = h_str + ":" + m_str;
        if (pm_flag) {
            newTime += " PM";
        } else {
            newTime += " AM";
        }
        return newTime;
    }

    /**
     * 获取周格式
     *
     * @param week
     * @return
     */
    public static String getWeeks(List<Integer> week) {
        String weeks = "";
        if (week.size() == 7) {
            weeks = "Every day";
            return weeks;
        }

        for (Integer w : week) {
            switch (w) {
                case 0:
                    weeks += "Sun,";
                    break;
                case 1:
                    weeks += "Mon,";
                    break;
                case 2:
                    weeks += "Tue,";
                    break;
                case 3:
                    weeks += "Wed,";
                    break;
                case 4:
                    weeks += "Thu,";
                    break;
                case 5:
                    weeks += "Fri,";
                    break;
                case 6:
                    weeks += "Sat,";
                    break;
                default:
                    break;
            }
        }
        if (!"".equals(weeks)) {
            weeks = weeks.substring(0, weeks.length() - 1);
        }
        return weeks;
    }

    /**
     *  获取时区对象
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
            LOGGER.info("getZoneId(), get zoneId={} error! will use system default zoneId", area);
            zoneId = ZoneId.systemDefault();
        }
        return zoneId;
    }
}

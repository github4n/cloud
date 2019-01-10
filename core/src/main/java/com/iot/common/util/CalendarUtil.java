package com.iot.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 项目名称：IOT云平台
 * 模块名称：常用工具
 * 功能描述：日期工具类
 * 创建人： mao2080@sina.com
 * 创建时间： 2017年3月20日 上午11:13:00
 * 修改人： mao2080@sina.com
 * 修改时间： 2017年3月20日 上午11:13:00
 */
public class CalendarUtil {

    /**
     * 日期格式yyyy-MM-dd HH:mm:ss
     */
    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式yyyy-MM-dd HH:mm
     */
    public static final String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";

    /**
     * 日期格式yyyy-MM-dd
     */
    public static final String YYYYMMDD = "yyyy-MM-dd";

    /**
     * 描述：获取当前日期
     *
     * @return yyyy-MM-dd格式字符串
     * @author mao2080@sina.com
     * @created 2017年3月20日 下午2:19:33
     * @since
     */
    public static String getNowDate() {
        return format(Calendar.getInstance(), YYYYMMDD);
    }

    /**
     * 描述：获取当前时间
     *
     * @return yyyy-MM-dd HH:mm:ss格式字符串
     * @author mao2080@sina.com
     * @created 2017年3月20日 下午2:20:03
     * @since
     */
    public static String getNowTime() {
        return format(Calendar.getInstance(), YYYYMMDDHHMMSS);
    }

    /**
     * 描述：获取当前时间
     *
     * @param pattern 时间格式
     * @return 自定义格式字符串
     * @author mao2080@sina.com
     * @created 2017年3月20日 下午2:26:35
     * @since
     */
    public static String getNowTime(String pattern) {
        return format(Calendar.getInstance(), CommonUtil.isEmpty(pattern) ? YYYYMMDDHHMMSS : pattern);
    }

    /**
     * 描述：获取当前时间（Calendar，清空时分秒毫秒）
     *
     * @return Calendar
     * @author mao2080@sina.com
     * @created 2017年3月20日 下午2:26:35
     * @since
     */
    public static Calendar getNowCalendar() {
        Calendar cal = Calendar.getInstance();
        clearHMSM(cal);
        return cal;
    }

    /**
     * 描述：将Calendar格式化成字符串
     *
     * @param cal Calendar时间
     * @return yyyy-MM-dd格式字符串
     * @author mao2080@sina.com
     * @created 2017年3月20日 下午2:20:03
     * @since
     */
    public static String format(Calendar cal) {
        return format(cal, YYYYMMDD);
    }

    /**
     * 描述：将Calendar格式化成字符串
     *
     * @param cal     Calendar时间
     * @param pattern 时间格式
     * @return 自定义格式字符串
     * @author mao2080@sina.com
     * @created 2017年3月20日 下午2:20:03
     * @since
     */
    public static String format(Calendar cal, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(CommonUtil.isEmpty(pattern) ? YYYYMMDD : pattern);
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 描述：将字符串（yyyy-MM-dd）转Calendar
     *
     * @param strCal 字符串
     * @return Calendar
     * @author mao2080@sina.com
     * @created 2017年3月20日 下午2:28:40
     * @since
     */
    public static Calendar format(String strCal) {
        return format(strCal, YYYYMMDD);
    }

    /**
     * 描述：将字符串（指定格式）转Calendar
     *
     * @param strCal  字符串
     * @param pattern 指定格式
     * @return Calendar
     * @author mao2080@sina.com
     * @created 2017年3月20日 下午2:28:40
     * @since
     */
    public static Calendar format(String strCal, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(CommonUtil.isEmpty(pattern) ? YYYYMMDD : pattern);
            Date date = sdf.parse(strCal);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 描述: 解析
     * @author  nongchongwei
     * @param strCal pattern
     * @return  String
     * @exception
     * @date     2019/1/5 14:10
     */
    public static Date parseDate(String strCal, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(CommonUtil.isEmpty(pattern) ? YYYYMMDD : pattern);
            Date date = sdf.parse(strCal);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 描述: 获取昨天
     * @author  nongchongwei
     * @param pattern
     * @return  String
     * @exception
     * @date     2019/1/5 14:10
     */
    public static String getYesterdayByCalendar(String pattern){
        Calendar calendar = Calendar.getInstance();
        System.out.println(Calendar.DATE);
        calendar.add(Calendar.DATE,-1);
        Date time = calendar.getTime();
        String yesterday = format(time,pattern);
        return yesterday;
    }

    /**
     * 描述：将Date格式化成字符串
     *
     * @param date     时间
     * @param pattern 时间格式
     * @return 自定义格式字符串
     * @author 490485964@qq.com
     * @created 2017年3月20日 下午2:20:03
     * @since
     */
    public static String format(Date date, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(CommonUtil.isEmpty(pattern) ? YYYYMMDD : pattern);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 描述：清空时分秒毫秒
     *
     * @param cal Calendar
     * @author mao2080@sina.com
     * @created 2017年3月20日 下午2:34:04
     * @since
     */
    public static void clearHMSM(Calendar cal) {
        if (cal != null) {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        }
    }

    /**
     * 描述：清空分秒毫秒
     *
     * @param cal Calendar
     * @author mao2080@sina.com
     * @created 2017年3月20日 下午2:34:04
     * @since
     */
    public static void clearMSM(Calendar cal) {
        if (cal != null) {
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        }
    }

    /**
     * 描述：获取几天前或者几天后日期
     *
     * @param days        （负数代表过去多少天，正数代表未来多少天）
     * @param isClearHMSM 是否清空时分秒
     * @return
     * @author mao2080@sina.com
     * @created 2017年4月13日 下午3:41:04
     * @since
     */
    public static Calendar getBeforeOrAfterDay(int days, boolean isClearHMSM) {
        Calendar cal = Calendar.getInstance();
        if (isClearHMSM) {
            CalendarUtil.clearHMSM(cal);
        }
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal;
    }

    /**
     * 描述：获取几天前或者几天后日期
     *
     * @param days        （负数代表过去多少天，正数代表未来多少天）
     * @param isClearMSM 是否清空时分秒
     * @return
     * @author 490485964@qq.com
     * @created 2017年4月13日 下午3:41:04
     * @since
     */
    public static Calendar getAfterOrBeforeDay(int days, boolean isClearMSM) {
        Calendar cal = Calendar.getInstance();
        if (isClearMSM) {
            CalendarUtil.clearMSM(cal);
        }
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal;
    }
    /**
     * 描述：取一个时间的前后几天
     * @author 490485964@qq.com
     * @date 2018/6/14 14:47
     * @param
     * @return
     */
    public static Date getDateBeforeOrAfter(Date date,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.DAY_OF_MONTH,day);
        return now.getTime();
    }
    /**
     * 描述：取一个时间的前后几小时
     * @author 490485964@qq.com
     * @date 2018/6/25 14:43
     * @param
     * @return
     */
    public static Date getDateBeforeOrAfterHour(Date date,int hour){
        Calendar now =Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.HOUR_OF_DAY,hour);
        return now.getTime();
    }
    /**
     * 描述：获取小时前或者小时后日期
     *
     * @param days （负数代表过去多小时，正数代表未来多小时）
     * @return
     * @author mao2080@sina.com
     * @created 2017年4月13日 下午3:41:04
     * @since
     */
    public static Calendar getBeforeOrAfterHour(int hour) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, hour);
        return cal;
    }

    /**
     * 描述：获取某月天数
     *
     * @param date
     * @return
     * @author wujianlong
     * @created 2017年10月12日 下午2:42:49
     * @since
     */
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 描述：java获取当前月的天数
     *
     * @return
     * @author 李帅
     * @created 2017年11月29日 上午11:03:19
     * @since
     */
    public static int getDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getActualMaximum(Calendar.DATE);
    }

    /**
     * 两个时间之间相差距离多少天
     *
     * @param str1 时间参数 1：
     * @param str2 时间参数 2：
     * @return 相差天数
     * @author wujianlong
     */
    public static long getDistanceDays(String str1, String str2) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }


    /**
     * 两个时间之间相差距离多少小时
     *
     * @param str1 时间参数 1：
     * @param str2 时间参数 2：
     * @return 相差天数
     */
    public static long getDistanceHours(String str1, String str2) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 两个时间之间相差距离多少分钟
     *
     * @param str1 时间参数 1：
     * @param str2 时间参数 2：
     * @return 相差天数
     */
    public static long getDistanceMinute(String str1, String str2) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     *@description 返回日期格式为yyyy-MM-dd
     *@author wucheng
     *@params [dateTime]
     *@create 2019/1/7 11:17
     *@return java.util.Date
     */
    public static Date getFormatTime(Date dateTime) {
        Date dateFormatTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String todayStr = sdf.format(dateTime);
            Long time = sdf.parse(todayStr).getTime();
            dateFormatTime = new Date(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatTime;
    }

    /**
     *@description 获取时间格式为2019-01-08 23:59:59格式的日期字符串
     *@author wucheng
     *@params [dateTime] 传入时间
     *@create 2019/1/8 11:31
     *@return java.lang.String
     */
    public static String getBeginFormatTime(Date dateTime) {
        String todayStr = CalendarUtil.format(dateTime, "yyyy-MM-dd") + " 00:00:00";
        return todayStr;
    }
    /**
     *@description 获取时间格式为2019-01-08 23:59:59格式的日期字符串
     *@author wucheng
     *@params [dateTime] 传入时间
     *@create 2019/1/8 11:31
     *@return java.lang.String
     */
    public static String getEndFormatTime(Date dateTime) {
        String todayStr = CalendarUtil.format(dateTime, "yyyy-MM-dd") + " 23:59:59";
        return todayStr;
    }

    public static void main(String[] args) {
        System.out.println(format(Calendar.getInstance(), YYYYMMDDHHMMSS));
        System.out.println(format(Calendar.getInstance(), YYYYMMDDHHMM));
        System.out.println(format(Calendar.getInstance(), null));
        System.out.println(getNowDate());
        System.out.println(getNowTime());
        System.out.println(getNowTime(YYYYMMDDHHMMSS));
        Calendar cl = Calendar.getInstance();
        clearHMSM(cl);
        System.out.println(format(cl, YYYYMMDDHHMMSS));
    }

}


package com.iot.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateUtil {
    public static final long MILLI_SECOND_PER_DAY = 86400000L;
    public static final long MILLI_SECOND_PER_HOUR = 3600000L;
    public static final long MILLI_SECOND_PER_MIN = 60000L;
    public static final long MIN_NS = 1000L;
    public static final int DAY = 1;
    public static final int HOUR = 2;
    public static final int MIN = 3;
    public static final int SECOND = 4;
    public static final int DAY_OF_WEEK = 7;
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;
    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";
    public static SimpleDateFormat sdf;
    public static SimpleDateFormat sdfymdhds;

    static {
        sdf = new SimpleDateFormat(DateUtil.DateStyle.YYYY_MM_DD.value);
        sdfymdhds = new SimpleDateFormat(DateUtil.DateStyle.YYYY_MM_DD_HH_MM_SS.value);
    }

    public DateUtil() {
    }

    private static SimpleDateFormat getDateFormat(String parttern) throws RuntimeException {
        return new SimpleDateFormat(parttern);
    }

    public static String getFormatTime(Date date, String partern) {
        return getDateFormat(partern).format(date);
    }

    private static int getInteger(Date date, int dateType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(dateType);
    }

    private static String addInteger(String date, int dateType, int amount) {
        String dateString = null;
        DateUtil.DateStyle dateStyle = getDateStyle(date);
        if (dateStyle != null) {
            Date myDate = StringToDate(date, dateStyle);
            myDate = addInteger(myDate, dateType, amount);
            dateString = DateToString(myDate, dateStyle);
        }

        return dateString;
    }

    private static Date addInteger(Date date, int dateType, int amount) {
        Date myDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(dateType, amount);
            myDate = calendar.getTime();
        }

        return myDate;
    }

    private static Date getAccurateDate(List<Long> timestamps) {
        Date date = null;
        long timestamp = 0L;
        Map<Long, long[]> map = new HashMap();
        List<Long> absoluteValues = new ArrayList();
        if (timestamps != null && timestamps.size() > 0) {
            if (timestamps.size() > 1) {
                for (int i = 0; i < timestamps.size(); ++i) {
                    for (int j = i + 1; j < timestamps.size(); ++j) {
                        long absoluteValue = Math.abs((Long) timestamps.get(i) - (Long) timestamps.get(j));
                        absoluteValues.add(absoluteValue);
                        long[] timestampTmp = new long[]{(Long) timestamps.get(i), (Long) timestamps.get(j)};
                        map.put(absoluteValue, timestampTmp);
                    }
                }

                long minAbsoluteValue = -1L;
                if (!absoluteValues.isEmpty()) {
                    minAbsoluteValue = (Long) absoluteValues.get(0);
                }

                for (int i = 0; i < absoluteValues.size(); ++i) {
                    for (int j = i + 1; j < absoluteValues.size(); ++j) {
                        if ((Long) absoluteValues.get(i) > (Long) absoluteValues.get(j)) {
                            minAbsoluteValue = (Long) absoluteValues.get(j);
                        } else {
                            minAbsoluteValue = (Long) absoluteValues.get(i);
                        }
                    }
                }

                if (minAbsoluteValue != -1L) {
                    long[] timestampsLastTmp = (long[]) map.get(minAbsoluteValue);
                    if (absoluteValues.size() > 1) {
                        timestamp = Math.max(timestampsLastTmp[0], timestampsLastTmp[1]);
                    } else if (absoluteValues.size() == 1) {
                        long dateOne = timestampsLastTmp[0];
                        long dateTwo = timestampsLastTmp[1];
                        if (Math.abs(dateOne - dateTwo) < 100000000000L) {
                            timestamp = Math.max(timestampsLastTmp[0], timestampsLastTmp[1]);
                        } else {
                            long now = (new Date()).getTime();
                            if (Math.abs(dateOne - now) <= Math.abs(dateTwo - now)) {
                                timestamp = dateOne;
                            } else {
                                timestamp = dateTwo;
                            }
                        }
                    }
                }
            } else {
                timestamp = (Long) timestamps.get(0);
            }
        }

        if (timestamp != 0L) {
            date = new Date(timestamp);
        }

        return date;
    }

    public static boolean isDate(String date) {
        boolean isDate = false;
        if (date != null && StringToDate(date) != null) {
            isDate = true;
        }

        return isDate;
    }

    public static DateUtil.DateStyle getDateStyle(String date) {
        DateUtil.DateStyle dateStyle = null;
        Map<Long, DateStyle> map = new HashMap();
        List<Long> timestamps = new ArrayList();
        DateUtil.DateStyle[] var4 = DateUtil.DateStyle.values();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            DateUtil.DateStyle style = var4[var6];
            Date dateTmp = StringToDate(date, style.getValue());
            if (dateTmp != null) {
                timestamps.add(dateTmp.getTime());
                map.put(dateTmp.getTime(), style);
            }
        }

        dateStyle = (DateUtil.DateStyle) map.get(getAccurateDate(timestamps).getTime());
        return dateStyle;
    }

    public static Date StringToDate(String date) {
        DateUtil.DateStyle dateStyle = null;
        return StringToDate(date, (DateUtil.DateStyle) dateStyle);
    }

    public static Date StringToDate(String date, String parttern) {
        Date myDate = null;
        if (date != null) {
            try {
                myDate = getDateFormat(parttern).parse(date);
            } catch (Exception var4) {
                return myDate;
            }
        }

        return myDate;
    }

    public static Date StringToDate(String date, DateUtil.DateStyle dateStyle) {
        Date myDate = null;
        if (dateStyle == null) {
            List<Long> timestamps = new ArrayList();
            DateUtil.DateStyle[] var4 = DateUtil.DateStyle.values();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                DateUtil.DateStyle style = var4[var6];
                Date dateTmp = StringToDate(date, style.getValue());
                if (dateTmp != null) {
                    timestamps.add(dateTmp.getTime());
                }
            }

            myDate = getAccurateDate(timestamps);
        } else {
            myDate = StringToDate(date, dateStyle.getValue());
        }

        return myDate;
    }

    public static String DateToString(Date date, String parttern) {
        String dateString = null;
        if (date != null) {
            try {
                dateString = getDateFormat(parttern).format(date);
            } catch (Exception var4) {
                ;
            }
        }

        return dateString;
    }

    public static String DateToString(Date date, DateUtil.DateStyle dateStyle) {
        String dateString = null;
        if (dateStyle != null) {
            dateString = DateToString(date, dateStyle.getValue());
        }

        return dateString;
    }

    public static String StringToString(String date, String parttern) {
        return StringToString(date, (String) null, (String) parttern);
    }

    public static String StringToString(String date, DateUtil.DateStyle dateStyle) {
        return StringToString(date, (DateUtil.DateStyle) null, (DateUtil.DateStyle) dateStyle);
    }

    public static String StringToString(String date, String olddParttern, String newParttern) {
        String dateString = null;
        if (olddParttern == null) {
            DateUtil.DateStyle style = getDateStyle(date);
            if (style != null) {
                Date myDate = StringToDate(date, style.getValue());
                dateString = DateToString(myDate, newParttern);
            }
        } else {
            Date myDate = StringToDate(date, olddParttern);
            dateString = DateToString(myDate, newParttern);
        }

        return dateString;
    }

    public static String StringToString(String date, DateUtil.DateStyle olddDteStyle, DateUtil.DateStyle newDateStyle) {
        String dateString = null;
        if (olddDteStyle == null) {
            DateUtil.DateStyle style = getDateStyle(date);
            dateString = StringToString(date, style.getValue(), newDateStyle.getValue());
        } else {
            dateString = StringToString(date, olddDteStyle.getValue(), newDateStyle.getValue());
        }

        return dateString;
    }

    public static String addYear(String date, int yearAmount) {
        return addInteger((String) date, 1, yearAmount);
    }

    public static Date addYear(Date date, int yearAmount) {
        return addInteger((Date) date, 1, yearAmount);
    }

    public static String addMonth(String date, int yearAmount) {
        return addInteger((String) date, 2, yearAmount);
    }

    public static Date addMonth(Date date, int yearAmount) {
        return addInteger((Date) date, 2, yearAmount);
    }

    public static String addDay(String date, int dayAmount) {
        return addInteger((String) date, 5, dayAmount);
    }

    public static Date addDay(Date date, int dayAmount) {
        return addInteger((Date) date, 5, dayAmount);
    }

    public static String addWeek(String date, int hourAmount) {
        return addInteger((String) date, 7, hourAmount);
    }

    public static Date addWeek(Date date, int dayAmount) {
        return addInteger((Date) date, 7, dayAmount);
    }

    public static String addHour(String date, int hourAmount) {
        return addInteger((String) date, 11, hourAmount);
    }

    public static Date addHour(Date date, int hourAmount) {
        return addInteger((Date) date, 11, hourAmount);
    }

    public static String addMinute(String date, int hourAmount) {
        return addInteger((String) date, 12, hourAmount);
    }

    public static Date addMinute(Date date, int hourAmount) {
        return addInteger((Date) date, 12, hourAmount);
    }

    public static String addSecond(String date, int hourAmount) {
        return addInteger((String) date, 13, hourAmount);
    }

    public static Date addSecond(Date date, int hourAmount) {
        return addInteger((Date) date, 13, hourAmount);
    }

    public static int getYear(String date) {
        return getYear(StringToDate(date));
    }

    public static int getYear(Date date) {
        return getInteger(date, 1);
    }

    public static int getMonth(String date) {
        return getMonth(StringToDate(date));
    }

    public static int getMonth(Date date) {
        return getInteger(date, 2);
    }

    public static int getDay(String date) {
        return getDay(StringToDate(date));
    }

    public static int getDay(Date date) {
        return getInteger(date, 5);
    }

    public static int getHour(String date) {
        return getHour(StringToDate(date));
    }

    public static int getHour(Date date) {
        return getInteger(date, 11);
    }

    public static int getMinute(String date) {
        return getMinute(StringToDate(date));
    }

    public static int getMinute(Date date) {
        return getInteger(date, 12);
    }

    public static int getSecond(String date) {
        return getSecond(StringToDate(date));
    }

    public static int getSecond(Date date) {
        return getInteger(date, 13);
    }

    public static String getDate(String date) {
        return StringToString(date, DateUtil.DateStyle.YYYY_MM_DD);
    }

    public static String getDate(Date date) {
        return DateToString(date, DateUtil.DateStyle.YYYY_MM_DD);
    }

    public static String getTime(String date) {
        return StringToString(date, DateUtil.DateStyle.HH_MM_SS);
    }

    public static String getTime(Date date) {
        return DateToString(date, DateUtil.DateStyle.HH_MM_SS);
    }

    public static DateUtil.Week getWeek(String date) {
        DateUtil.Week week = null;
        DateUtil.DateStyle dateStyle = getDateStyle(date);
        if (dateStyle != null) {
            Date myDate = StringToDate(date, dateStyle);
            week = getWeek(myDate);
        }

        return week;
    }

    public static DateUtil.Week getWeek(Date date) {
        DateUtil.Week week = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekNumber = calendar.get(7) - 1;
        switch (weekNumber) {
            case 0:
                week = DateUtil.Week.SUNDAY;
                break;
            case 1:
                week = DateUtil.Week.MONDAY;
                break;
            case 2:
                week = DateUtil.Week.TUESDAY;
                break;
            case 3:
                week = DateUtil.Week.WEDNESDAY;
                break;
            case 4:
                week = DateUtil.Week.THURSDAY;
                break;
            case 5:
                week = DateUtil.Week.FRIDAY;
                break;
            case 6:
                week = DateUtil.Week.SATURDAY;
        }

        return week;
    }

    public static int getIntervalDays(String date, String otherDate) {
        return getIntervalDays(StringToDate(date), StringToDate(otherDate));
    }

    public static int getIntervalDays(Date date, Date otherDate) {
        date = StringToDate(getDate(date));
        long time = Math.abs(date.getTime() - otherDate.getTime());
        return (int) time / 86400000;
    }

    public static int getIntervalMinis(Date date, Date otherDate) {
        date = StringToDate(getDate(date));
        long time = Math.abs(date.getTime() - otherDate.getTime());
        return (int) time / 86400;
    }

    public static String getThisMonthBeginString(Date now) {
        return getDateFormat(DateUtil.DateStyle.YYYY_MM_DD.getValue()).format(getMonthBeginDate(now));
    }

    public static Date getDateStart(Date date) {
        if (null == date) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            return calendar.getTime();
        }
    }

    public static Date getDateEnd(Date date) {
        if (null == date) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(11, 23);
            calendar.set(12, 59);
            calendar.set(13, 59);
            return calendar.getTime();
        }
    }

    public static String getThisMonthEndString(Date now) {
        return getDateFormat(DateUtil.DateStyle.YYYY_MM_DD.getValue()).format(getMonthEndDate(now));
    }

    public static Date getMonthBeginDate(Date now) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(5, 1);
        return cal.getTime();
    }

    public static Date getMonthEndDate(Date now) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(5, cal.getMaximum(5));
        return cal.getTime();
    }

    public static String getWeekBeginString(Date now) {
        return getDateFormat(DateUtil.DateStyle.YYYY_MM_DD.getValue()).format(getWeekBeginDate(now));
    }

    public static Date getWeekBeginDate(Date now) {
        Calendar cal = Calendar.getInstance();
        int dayofweek = cal.get(7) - 1;
        if (dayofweek == 0) {
            dayofweek = 7;
        }

        cal.add(5, -dayofweek + 1);
        return cal.getTime();
    }

    public static int getGSMTime(Date now) {
        long gsmTime = -28800000L;
        long nowTime = now.getTime();
        return Integer.valueOf((nowTime - gsmTime) / 1000L + "");
    }

    public static Date getGSMDate(int gsm) {
        long gsmTime = -28800000L;
        long time = (long) gsm * 1000L + gsmTime;
        return new Date(time);
    }

    public static final int toUnix(Date date) {
        return (int) Math.floor((double) (date.getTime() / 1000L));
    }

    public static String longToDate(long lo, DateUtil.DateStyle dateStyle) {
        String dateString = null;
        if (dateStyle != null) {
            Date date = new Date(lo);
            dateString = DateToString(date, dateStyle.getValue());
        }

        return dateString;
    }

    public static String longToDate(String lo, DateUtil.DateStyle dateStyle) {
        String dateString = null;
        Long DateMinus = Long.parseLong(lo);
        if (dateStyle != null) {
            Date date = new Date(DateMinus);
            dateString = DateToString(date, dateStyle.getValue());
        }

        return dateString;
    }

    public static String getInterval(Date createAt) {
        String interval = null;
        long millisecond = (new Date()).getTime() - createAt.getTime();
        long second = millisecond / 1000L;
        if (second <= 0L) {
            second = 0L;
        }

        if (second == 0L) {
            interval = "刚刚";
        } else if (second < 30L) {
            interval = second + "秒以前";
        } else if (second >= 30L && second < 60L) {
            interval = "半分钟前";
        } else {
            long day;
            if (second >= 60L && second < 3600L) {
                day = second / 60L;
                interval = day + "分钟前";
            } else if (second >= 3600L && second < 86400L) {
                day = second / 60L / 60L;
                if (day <= 24L) {
                    interval = day + "小时前";
                } else {
                    interval = "今天" + getFormatTime(createAt, DateUtil.DateStyle.HH_MM.getValue());
                }
            } else if (second >= 86400L && second <= 172800L) {
                interval = "昨天" + getFormatTime(createAt, DateUtil.DateStyle.HH_MM.getValue());
            } else if (second >= 172800L && second <= 604800L) {
                day = second / 60L / 60L / 24L;
                interval = day + "天前";
            } else if (second <= 31536000L && second >= 604800L) {
                interval = getFormatTime(createAt, DateUtil.DateStyle.MM_DD_HH_MM.getValue());
            } else if (second >= 31536000L) {
                interval = getFormatTime(createAt, DateUtil.DateStyle.YYYY_MM_DD_HH_MM.getValue());
            } else {
                interval = "0";
            }
        }

        return interval;
    }

    public static String getActivityEndDateTimeString(String activityEndDateTime) {
        long day = 0L;
        long hour = 0L;
        long min = 0L;
        long sec = 0L;

        long diff;
        try {
            diff = StringToDate(activityEndDateTime).getTime() - (new Date()).getTime();
            if (diff < 0L) {
                return null;
            }
        } catch (Exception var12) {
            return null;
        }

        day = diff / 86400000L;
        hour = diff % 86400000L / 3600000L + day * 24L;
        min = diff % 86400000L % 3600000L / 60000L + day * 24L * 60L;
        sec = diff % 86400000L % 3600000L % 60000L / 1000L;
        StringBuilder buff = new StringBuilder();
        if (day > 0L) {
            buff.append(day).append("天");
        }

        if (hour - day * 24L > 0L) {
            buff.append(hour - day * 24L).append("小时");
        }

        if (min - day * 24L * 60L > 0L) {
            buff.append(min - day * 24L * 60L).append("分钟");
        }

        if (sec > 0L) {
            buff.append(sec).append("秒");
        }

        return buff.toString();
    }

    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    private static Date plusOrMinusDatetime(Date dateTime, long count, int type) {
        long interval = 1000L;
        if (type == 1) {
            interval = 86400000L;
        } else if (type == 2) {
            interval = 3600000L;
        } else if (type == 3) {
            interval = 60000L;
        }

        try {
            Date temp = new Date(dateTime.getTime());
            temp.setTime(temp.getTime() + count * interval);
            return temp;
        } catch (Exception var7) {
            return null;
        }
    }

    public static Date plusOrMinusDatetime(String dateTime, String parttern, long count, int type) {
        try {
            return plusOrMinusDatetime(getDateFormat(parttern).parse(dateTime), count, type);
        } catch (Exception var6) {
            return null;
        }
    }

    public static int getQuarter(Date currentDate) {
        int quarter = 0;
        int month = getMonth(currentDate) + 1;
        if (month != 1 && month != 2 && month != 3) {
            if (month != 4 && month != 5 && month != 6) {
                if (month != 7 && month != 8 && month != 9) {
                    if (month == 10 || month == 11 || month == 12) {
                        quarter = 4;
                    }
                } else {
                    quarter = 3;
                }
            } else {
                quarter = 2;
            }
        } else {
            quarter = 1;
        }

        return quarter;
    }

    public static String getAppointDateStr(Date date, int day) {
        Calendar instance = Calendar.getInstance();
        if (null != date) {
            instance.setTime(date);
        }

        instance.add(5, day);
        return sdf.format(instance.getTime());
    }

    public static String getAppointDateStrHMS(Date date, int day) {
        Calendar instance = Calendar.getInstance();
        if (null != date) {
            instance.setTime(date);
        }

        instance.add(5, day);
        return sdfymdhds.format(instance.getTime());
    }

    public static DateUtil.WeekOfDay getTimeInterval(Date date, DateUtil.DateStyle dateStyle) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayWeek = cal.get(7);
        if (1 == dayWeek) {
            cal.add(5, -1);
        }

        cal.setFirstDayOfWeek(1);
        int day = cal.get(7);
        cal.add(5, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = null;
        Date imtimeBeginDate = cal.getTime();
        imptimeBegin = DateToString(imtimeBeginDate, dateStyle);
        cal.add(5, 6);
        String imptimeEnd = null;
        Date imtimeEndDate = cal.getTime();
        imptimeEnd = DateToString(imtimeEndDate, dateStyle);
        DateUtil.WeekOfDay weekOfDay = new DateUtil.WeekOfDay(imtimeBeginDate, imtimeEndDate, imptimeBegin + "-" + imptimeEnd);
        return weekOfDay;
    }

    public static DateUtil.WeekOfDay getChineseTimeInterval(Date date, DateUtil.DateStyle dateStyle) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayWeek = cal.get(7);
        if (1 == dayWeek) {
            cal.add(5, -1);
        }

        cal.setFirstDayOfWeek(2);
        int day = cal.get(7);
        cal.add(5, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = null;
        Date imtimeBeginDate = cal.getTime();
        imptimeBegin = DateToString(imtimeBeginDate, dateStyle);
        cal.add(5, 6);
        String imptimeEnd = null;
        Date imtimeEndDate = cal.getTime();
        imptimeEnd = DateToString(imtimeEndDate, dateStyle);
        DateUtil.WeekOfDay weekOfDay = new DateUtil.WeekOfDay(imtimeBeginDate, imtimeEndDate, imptimeBegin + "-" + imptimeEnd);
        return weekOfDay;
    }

    public static String getLastTimeInterval() {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(7) - 1;
        int offset1 = 1 - dayOfWeek;
        int offset2 = 7 - dayOfWeek;
        calendar1.add(5, offset1 - 7);
        calendar2.add(5, offset2 - 7);
        String lastBeginDate = sdf.format(calendar1.getTime());
        String lastEndDate = sdf.format(calendar2.getTime());
        return lastBeginDate + "," + lastEndDate;
    }

    public static String format(Date date) {
        long delta = (new Date()).getTime() - date.getTime();
        long years;
        if (delta < 60000L) {
            years = toSeconds(delta);
            return (years <= 0L ? 1L : years) + "秒前";
        } else if (delta < 2700000L) {
            years = toMinutes(delta);
            return (years <= 0L ? 1L : years) + "分钟前";
        } else if (delta < 86400000L) {
            years = toHours(delta);
            return (years <= 0L ? 1L : years) + "小时前";
        } else if (delta < 172800000L) {
            return "昨天" + getFormatTime(date, DateUtil.DateStyle.HH_MM.getValue());
        } else if (delta < 2592000000L) {
            years = toDays(delta);
            return (years <= 0L ? 1L : years) + "天前";
        } else if (delta < 29030400000L) {
            years = toMonths(delta);
            return (years <= 0L ? 1L : years) + "月前";
        } else {
            years = toYears(delta);
            return (years <= 0L ? 1L : years) + "年前";
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }


    public static enum Week {
        SUNDAY("星期日", "Sunday", "Sun.", 7),
        MONDAY("星期一", "Monday", "Mon.", 1),
        TUESDAY("星期二", "Tuesday", "Tues.", 2),
        WEDNESDAY("星期三", "Wednesday", "Wed.", 3),
        THURSDAY("星期四", "Thursday", "Thur.", 4),
        FRIDAY("星期五", "Friday", "Fri.", 5),
        SATURDAY("星期六", "Saturday", "Sat.", 6);

        String name_cn;
        String name_en;
        String name_enShort;
        int number;

        private Week(String name_cn, String name_en, String name_enShort, int number) {
            this.name_cn = name_cn;
            this.name_en = name_en;
            this.name_enShort = name_enShort;
            this.number = number;
        }

        public String getChineseName() {
            return this.name_cn;
        }

        public String getName() {
            return this.name_en;
        }

        public String getShortName() {
            return this.name_enShort;
        }

        public int getNumber() {
            return this.number;
        }
    }

    public static enum DateStyle {
        YYYY_EN("yyyy"),
        YYYY_CN("yyyy年"),
        YYYYMMDDHHMMSS("yyyyMMddHHmmss"),
        MM_DD("MM-dd"),
        YYYY_MM("yyyy-MM"),
        YYYY_MM_DD("yyyy-MM-dd"),
        MM_DD_HH_MM("MM-dd HH:mm"),
        MM_DD_HH_MM_SS("MM-dd HH:mm:ss"),
        YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
        YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
        MM_DD_EN("MM/dd"),
        YYYY_MM_EN("yyyy/MM"),
        YYYY_MM_DD_EN("yyyy/MM/dd"),
        MM_DD_HH_MM_EN("MM/dd HH:mm"),
        MM_DD_HH_MM_SS_EN("MM/dd HH:mm:ss"),
        YYYY_MM_DD_HH_MM_EN("yyyy/MM/dd HH:mm"),
        YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss"),
        MM_DD_CN("MM月dd日"),
        YYYY_MM_CN("yyyy年MM月"),
        YYYY_MM_DD_CN("yyyy年MM月dd日"),
        MM_DD_HH_MM_CN("MM月dd日 HH:mm"),
        MM_DD_HH_MM_SS_CN("MM月dd日 HH:mm:ss"),
        YYYY_MM_DD_HH_MM_CN("yyyy年MM月dd日 HH:mm"),
        YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss"),
        HH_MM("HH:mm"),
        HH_MM_SS("HH:mm:ss");

        private String value;

        private DateStyle(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public static class WeekOfDay {
        public Date firstWeekOfDay;
        public Date endWeekOfDay;
        public String intervalTime;

        public WeekOfDay(Date firstWeekOfDay, Date endWeekOfDay, String intervalTime) {
            this.firstWeekOfDay = firstWeekOfDay;
            this.endWeekOfDay = endWeekOfDay;
            this.intervalTime = intervalTime;
        }
    }
}

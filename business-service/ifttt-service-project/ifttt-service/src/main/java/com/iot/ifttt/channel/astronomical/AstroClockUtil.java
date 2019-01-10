package com.iot.ifttt.channel.astronomical;

import com.google.common.collect.Maps;
import com.iot.util.AssertUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Author: chq
 * @Descrpiton:天文定时计算
 * @Date: 10:22 2018/5/12
 * @Modify by:
 */

public class AstroClockUtil {

    private static Integer[] months = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private static double mathMod(double a, double b) {
        int c = (int) (a / b);
        return a - (c * b);
    }

    private static double degToRad(double anomSun) {
        return Math.PI * anomSun / 180.0;
    }

    private static double radToDeg(double d) {
        return 180.0 * d / Math.PI;
    }

    private static Boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    // 当前年月日距1900.1.1的天数
    private static int getNumOfDays(int year, int month, int day) {

        int date = 0;
        date = (year - 1900) * 365 + (year - 1900) / 4 + 1;
        for (int i = 0; i < month - 1; i++) {
            date += months[i % 12];
        }
        if (isLeapYear(year) && month >= 2) {
            date += 1;
        }
        date += day;
        return date;
    }

    public static Map<String, Object> calculateNoaaSunTime(AreaTime area) {
        checkArea(area);
        int date = getNumOfDays(area.getYear(), area.getMonth(), area.getDay());
        double julianDay = date + 2415018.5 - area.getTimeZone() / 24 + (area.getHour() + area.getMinute().floatValue() / 60 + area.getSecond().floatValue() / 3600) / 24;
        double julianCentury = (julianDay - 2451545.0) / 36525.0;
        double longSun = mathMod(280.46646 + julianCentury * (36000.76983 + julianCentury * 0.0003032), 360.0);
        double anomSun = 357.52911 + julianCentury * (35999.05029 - 0.0001537 * julianCentury);
        double eccentEarthOrbit = 0.016708634 - julianCentury * (0.000042037 + 0.0000001267 * julianCentury);
        double sunEqOfCtr = Math.sin(degToRad(anomSun) * (1.914602 - julianCentury * (0.004817 + 0.000014 * julianCentury))
                + Math.sin(degToRad(2 * anomSun)) * (0.019993 - 0.000101 * julianCentury) + Math.sin(degToRad(3 * anomSun)) * 0.000289);
        double sunTrueLong = longSun + sunEqOfCtr;
        double sunAppLong = sunTrueLong - 0.00569 - 0.00478 * Math.sin(degToRad(125.04 - 1934.136 * julianCentury));
        double meanObliqEcliptic = 23 + (26 + (21.448 - julianCentury * (46.815 + julianCentury * (0.00059 - julianCentury * 0.001813))) / 60) / 60;
        double obliqCorr = meanObliqEcliptic + 0.00256 * Math.cos(degToRad(125.04 - 1934.136 * julianCentury));
        double sunDeclin = radToDeg(Math.asin(Math.sin(degToRad(obliqCorr)) * Math.sin(degToRad(sunAppLong))));
        double varY = Math.tan(degToRad(obliqCorr / 2)) * Math.tan(degToRad(obliqCorr / 2));
        double eqOfTime = 4 * radToDeg(varY * Math.sin(2 * degToRad(longSun)) - 2 * eccentEarthOrbit * Math.sin(degToRad(anomSun)) + 4 * eccentEarthOrbit * varY * Math.sin(degToRad(anomSun)) * Math.cos(2 * degToRad(longSun))
                - 0.5 * varY * varY * Math.sin(4 * degToRad(longSun)) - 1.25 * eccentEarthOrbit * eccentEarthOrbit * Math.sin(2 * degToRad(anomSun)));
        double haSunrise = radToDeg(Math.acos(Math.cos(degToRad(90.833)) / (Math.cos(degToRad(area.getLatitude()))
                * Math.cos(degToRad(sunDeclin))) - Math.tan(degToRad(area.getLatitude())) * Math.tan(degToRad(sunDeclin))));
        double solarNoon = (720 - 4 * area.getLongitude() - eqOfTime + area.getTimeZone() * 60) / 1440;
        double sunriseTime = solarNoon - haSunrise * 4 / 1440;
        double sunsetTime = solarNoon + haSunrise * 4 / 1440;
        return getAreaTimeMap(sunriseTime, sunsetTime, area);
    }

    private static Map<String, Object> getAreaTimeMap(double sunriseTime, double sunsetTime, AreaTime area) {
        Integer sunriseHour = (int) (sunriseTime * 1440 / 60);
        Integer sunriseMinute = (int) ((sunriseTime * 1440) % 60);
        Integer sunriseSecond = (int) (((sunriseTime * 1440) - (int) (sunriseTime * 1440)) * 60);
        Integer sunsetHour = (int) (sunsetTime * 1440 / 60);
        Integer sunsetMinute = (int) ((sunsetTime * 1440) % 60);
        Integer sunsetSecond = (int) (((sunsetTime * 1440) - (int) (sunsetTime * 1440)) * 60);
        Date sunriseTimeDate = changeInt2Date(area, sunriseHour, sunriseMinute, sunriseSecond);
        Date sunsetTimeDate = changeInt2Date(area, sunsetHour, sunsetMinute, sunsetSecond);
        Map<String, Object> areaTimeMap = Maps.newHashMap();
        areaTimeMap.put("sunriseTime", sunriseTimeDate);
        areaTimeMap.put("sunsetTime", sunsetTimeDate);
        return areaTimeMap;
    }

    private static void checkArea(AreaTime area) {
        AssertUtils.notEmpty(area, "area is null");
        AssertUtils.notEmpty(area.getLatitude(), "Latitude is null");
        AssertUtils.notEmpty(area.getLongitude(), "Longitude is null");
        AssertUtils.notEmpty(area.getTimeZone(), "TimeZone is null");
        AssertUtils.notEmpty(area.getYear(), "Year is null");
        AssertUtils.notEmpty(area.getMonth(), "month is null");
        AssertUtils.notEmpty(area.getDay(), "day is null");
    }

    private static Date changeInt2Date(AreaTime area, Integer hour, Integer minute, Integer second) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String timeStr = area.getYear() + "-" + area.getMonth() + "-" + area.getDay() + " " + hour + ":" + minute + ":"
                + second;
        Date time = null;
        try {
            time = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static void main(String[] args) {
        AreaTime area = new AreaTime();
        area.setLatitude((float) 26.1029299400);
        area.setLongitude((float) 119.2917778100);
        area.setTimeZone((float) 8);
        area.setYear(2020);
        area.setMonth(5);
        area.setDay(11);
        area.setHour(0);
        Map<String, Object> areaTimeMap = calculateNoaaSunTime(area);
        System.out.println(areaTimeMap.get("sunriseTime"));
        System.out.println(areaTimeMap.get("sunsetTime"));
    }

}

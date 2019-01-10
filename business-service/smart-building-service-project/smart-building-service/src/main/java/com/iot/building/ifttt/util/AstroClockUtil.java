package com.iot.building.ifttt.util;

import com.google.common.collect.Maps;
import com.iot.building.ifttt.entity.AreaTime;
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

	private static Integer[] months = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	private static double mathMod(double a, double b) {
		int c = (int) (a / b);
		return (a - (c * b));
	}

	private static double degToRad(double anom_sun) {
		double b = (Math.PI * anom_sun / 180.0);
		return b;
	}

	private static double radToDeg(double d) {
		double b = (180.0 * d / Math.PI);
		return b;
	}

	private static Boolean isLeapYear(int year) {
		return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
	}

	// 当前年月日距1900.1.1的天数
	private static int getNumOfDays(int year, int month, int day) {

		int date = 0;
		date = (year - 1900) * 365 + (year - 1900) / 4 + 1;
		for (int i = 0; i < month - 1; i++) {
			date += months[(i) % 12];
		}
		if (isLeapYear(year) && month >= 2) {
			date += 1;
		}
		date += day;
		return date;
	}

	public static Map<String, Object> calculateNoaaSunTime(AreaTime area) {
		AssertUtils.notEmpty(area, "area is null");
		AssertUtils.notEmpty(area.getLatitude(), "Latitude is null");
		AssertUtils.notEmpty(area.getLongitude(), "Longitude is null");
		AssertUtils.notEmpty(area.getTimeZone(), "TimeZone is null");
		AssertUtils.notEmpty(area.getYear(), "Year is null");
		AssertUtils.notEmpty(area.getMonth(), "month is null");
		AssertUtils.notEmpty(area.getDay(), "day is null");
		double julian_day;
		double julian_century, long_sun, anom_sun, eccent_earth_orbit, sun_eq_of_ctr, sun_true_long, sun_true_anom,
				sun_rad_vector, sun_app_long, mean_obliq_ecliptic, obliq_corr, sun_rt_ascen, sun_declin, var_y,
				eq_of_time, HA_sunrise, solar_noon, sunrise_time, sunset_time;

		// 当前年月日距1900.1.1的天数
		int date = getNumOfDays(area.getYear(), area.getMonth(), area.getDay());

		julian_day = date + 2415018.5 - (area.getTimeZone() / 24)
				+ (area.getHour() + area.getMinute() / 60 + area.getSecond() / 3600) / 24;

		julian_century = ((julian_day - 2451545.0) / 36525.0);

		long_sun = mathMod((280.46646 + julian_century * (36000.76983 + julian_century * 0.0003032)), 360.0);

		anom_sun = (357.52911 + julian_century * (35999.05029 - 0.0001537 * julian_century));

		eccent_earth_orbit = (0.016708634 - julian_century * (0.000042037 + 0.0000001267 * julian_century));

		sun_eq_of_ctr = (Math.sin(degToRad(anom_sun))
				* (1.914602 - julian_century * (0.004817 + 0.000014 * julian_century))
				+ Math.sin(degToRad(2 * anom_sun)) * (0.019993 - 0.000101 * julian_century)
				+ Math.sin(degToRad(3 * anom_sun)) * 0.000289);

		sun_true_long = long_sun + sun_eq_of_ctr;

		sun_true_anom = anom_sun + sun_eq_of_ctr;

		// sun_rad_vector = ((1.000001018 * (1 - eccent_earth_orbit *
		// eccent_earth_orbit))
		// / (1 + eccent_earth_orbit * Math.cos(degToRad(sun_true_anom))));

		sun_app_long = (sun_true_long - 0.00569 - 0.00478 * Math.sin(degToRad((125.04 - 1934.136 * julian_century))));

		mean_obliq_ecliptic = (23 + (26
				+ ((21.448 - julian_century * (46.815 + julian_century * (0.00059 - julian_century * 0.001813)))) / 60)
				/ 60);
		obliq_corr = (mean_obliq_ecliptic + 0.00256 * Math.cos(degToRad((125.04 - 1934.136 * julian_century))));

		// sun_rt_ascen = radToDeg(Math.atan2(Math.cos(degToRad(sun_app_long)),
		// Math.cos(degToRad(obliq_corr)) * Math.sin(degToRad(sun_app_long))));

		sun_declin = radToDeg(Math.asin(Math.sin(degToRad(obliq_corr)) * Math.sin(degToRad(sun_app_long))));

		var_y = (Math.tan(degToRad(obliq_corr / 2)) * Math.tan(degToRad(obliq_corr / 2)));

		eq_of_time = 4 * radToDeg((var_y * Math.sin(2 * degToRad(long_sun))
				- 2 * eccent_earth_orbit * Math.sin(degToRad(anom_sun))
				+ 4 * eccent_earth_orbit * var_y * Math.sin(degToRad(anom_sun)) * Math.cos(2 * degToRad(long_sun))
				- 0.5 * var_y * var_y * Math.sin(4 * degToRad(long_sun))
				- 1.25 * eccent_earth_orbit * eccent_earth_orbit * Math.sin(2 * degToRad(anom_sun))));

		HA_sunrise = radToDeg(Math.acos(
				Math.cos(degToRad(90.833)) / (Math.cos(degToRad(area.getLatitude())) * Math.cos(degToRad(sun_declin)))
						- Math.tan(degToRad(area.getLatitude())) * Math.tan(degToRad(sun_declin))));

		solar_noon = ((720 - 4 * area.getLongitude() - eq_of_time + area.getTimeZone() * 60) / 1440);

		sunrise_time = solar_noon - HA_sunrise * 4 / 1440;

		sunset_time = solar_noon + HA_sunrise * 4 / 1440;

		Map<String, Object> areaTimeMap = Maps.newHashMap();

		Integer sunriseHour = (int) (sunrise_time * 1440 / 60);
		Integer sunriseMinute = (int) ((sunrise_time * 1440) % 60);
		Integer sunriseSecond = (int) (((sunrise_time * 1440) - (int) (sunrise_time * 1440)) * 60);

		Integer sunsetHour = (int) (sunset_time * 1440 / 60);
		Integer sunsetMinute = (int) ((sunset_time * 1440) % 60);
		Integer sunsetSecond = (int) (((sunset_time * 1440) - (int) (sunset_time * 1440)) * 60);

		Date sunriseTime = changeInt2Date(area, sunriseHour, sunriseMinute, sunriseSecond);
		Date sunsetTime = changeInt2Date(area, sunsetHour, sunsetMinute, sunsetSecond);

		areaTimeMap.put("sunriseTime", sunriseTime);
		areaTimeMap.put("sunsetTime", sunsetTime);
//		areaTimeMap.put("sunriseHour", sunriseHour);
//		areaTimeMap.put("sunriseMinute", sunriseMinute);
//		areaTimeMap.put("sunriseSecond", sunriseSecond);
//		
//		areaTimeMap.put("sunsetHour", sunsetHour);
//		areaTimeMap.put("sunsetMinute", sunsetMinute);
//		areaTimeMap.put("sunsetSecond", sunsetSecond);
		

		return areaTimeMap;
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

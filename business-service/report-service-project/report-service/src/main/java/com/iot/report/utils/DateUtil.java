package com.iot.report.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *@description 日期工具类
 *@author wucheng
 *@create 2019/1/4 15:03
 */
@Slf4j
public class DateUtil {

    /**
     *@description 获取档期日期的前一天的日期
     *@author wucheng
     *@params []
     *@create 2019/1/4 14:25
     *@return java.lang.String
     */
	public static String getBeforeDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String todayStr = sdf.format(new Date());
		try {
			Long time = sdf.parse(todayStr).getTime() - 24 * 60 * 60 * 1000;
			Date yesterday = new Date(time);
			return sdf.format(yesterday);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
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
		if (dateTime == null) {
			log.error("date is null");
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String todayStr = sdf.format(dateTime);
			try {
				Long time = sdf.parse(todayStr).getTime();
				dateFormatTime = new Date(time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return dateFormatTime;
	}
}

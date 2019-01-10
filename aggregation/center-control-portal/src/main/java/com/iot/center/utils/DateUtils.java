package com.iot.center.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

/**
 * 时间日期工具类
 * @author fenglijian
 *
 */
public class DateUtils {
	
	//获取某年某月的第一天日期
	public static Date getFirstDayOfMonth(int year, int month) 
	{	
		Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
 
	//获取某年某月的最后一天日期
	public static Date getLastDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(year, month - 1, day);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
	
	public static Long getFirstDayOfMonthMillis(int year, int month) {
//		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		Calendar calendar = Calendar.getInstance();
//		java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(year, month - 1, 1);
//        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
 		calendar.set(Calendar.MILLISECOND, 0);
 		Date date=calendar.getTime();
        return calendar.getTimeInMillis();
    }
 
	//获取某年某月的最后一天日期
	public static Long getLastDayOfMonthMillis(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(year, month - 1, day);
        calendar.set(Calendar.HOUR_OF_DAY, 23);//设置时为0点
	    calendar.set(Calendar.MINUTE, 59);//设置分钟为0分
	    calendar.set(Calendar.SECOND, 59);//设置秒为0秒
	    calendar.set(Calendar.MILLISECOND, 000);//设置毫秒为000
        return calendar.getTimeInMillis();
    }
		   
    public static String dateFormat(String formatter, Date date){
		if(StringUtils.isBlank(formatter)){
			formatter = "yyyy-MM-dd HH:mm";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		return sdf.format(date);
	}
	
	public static Date dateParse(String formatter, String dateStr){
		if(StringUtils.isBlank(formatter)){
			formatter = "yyyy-MM-dd HH:mm";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 
     * get Calendar of given year 
     * @param year 
     * @return 
     */  
    private static Calendar getCalendarFormYear(int year){  
        Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);        
        cal.set(Calendar.YEAR, year);  
        return cal;  
    }
	
	/** 
     * get start date of given week no of a year 
     * @param year 
     * @param weekNo 
     * @return 
     */  
    public static Date getFirstDayOfWeek(int year,int weekNo){  
        Calendar cal = getCalendarFormYear(year);  
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);  
        return cal.getTime();      
          
    }  
      
    /** 
     * get the end day of given week no of a year. 
     * @param year 
     * @param weekNo 
     * @return 
     */  
    public static Date getLastDayOfWeek(int year,int weekNo){  
        Calendar cal = getCalendarFormYear(year);  
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);  
        cal.add(Calendar.DAY_OF_WEEK, 6);  
        return cal.getTime();      
    }
    
    /** 
     * get start date of given week no of a year 
     * @param year 
     * @param weekNo 
     * @return 
     */  
    public static Long getFirstDayOfWeekMillis(int year,int weekNo){  
        Calendar cal = getCalendarFormYear(year);  
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);  
        return cal.getTimeInMillis();      
          
    }  
      
    /** 
     * get the end day of given week no of a year. 
     * @param year 
     * @param weekNo 
     * @return 
     */  
    public static Long getLastDayOfWeekMillis(int year,int weekNo){  
        Calendar cal = getCalendarFormYear(year);  
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);  
        cal.add(Calendar.DAY_OF_WEEK, 6);  
        return cal.getTimeInMillis();      
    }
    
    /**
     * 时间戳转字符串时间
     * @param timeStamp 时间戳
     * @return
     */
    public static String getDateTime(Long timeStamp){
    	return getDateTime("yyyy-MM-dd HH:mm", timeStamp);
    }
    
    /**
     * 时间戳转字符串时间
     * @param formatter
     * @param timeStamp
     * @return
     */
    public static String getDateTime(String formatter, Long timeStamp){
    	SimpleDateFormat sdf=new SimpleDateFormat(formatter);
    	String dateTime = sdf.format(new Date(timeStamp));
    	return dateTime;
    }
    
    public static void main(String[] args) {
		System.out.println(DateUtils.getFirstDayOfMonth(2018, 11).getTime());
		System.out.println(DateUtils.getLastDayOfMonth(2018, 11).getTime());
	}

}

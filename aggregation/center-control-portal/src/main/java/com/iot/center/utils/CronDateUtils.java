package com.iot.center.utils;
import java.text.ParseException;  
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.iot.center.enums.LoopEnum;  
  
/** 
 * 该类提供Quartz的cron表达式与Date之间的转换 
 * Created by zhangzh on 2016/8/2. 
 */  
public class CronDateUtils {  
    private static final String CRON_DATE_FORMAT = "ss mm HH * * ?";  
  
    /*** 
     * 
     * @param date 时间 
     * @return  cron类型的日期 
     */  
    public static String getCron(final Date date){  
        SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);  
        String formatTimeStr = "";  
        if (date != null) {  
            formatTimeStr = sdf.format(date);  
        }  
        return formatTimeStr;  
    }  
  
    /*** 
     * 
     * @param cron Quartz cron的类型的日期 
     * @return  Date日期 
     */  
  
    public static Date getDate(final String cron) {  
  
  
        if(cron == null) {  
            return null;  
        }  
  
        SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);  
        Date date = null;  
        try {  
            date = sdf.parse(cron);  
        } catch (ParseException e) {  
            return null;// 此处缺少异常处理,自己根据需要添加  
        }  
        return date;  
    }  
    
    public static String[] splitDateTime(String dateTime){
    	String[] dateTimes = dateTime.split(" ");
    	String[] dates = dateTimes[0].split("-");
    	String[] times = dateTimes[1].split(":");
    	
    	List list = new ArrayList(Arrays.asList(dates));
    	list.addAll(Arrays.asList(times));
    	String[] str = new String[list.size()];
    	list.toArray(str);
    	return str;
    }
    
    public static String[] splitTime(String time){
    	return time.split(":");
    }
    
    public static Map<String, String> generateCron(String startTime, String endTime, String loopType, String week, String weekStartTime, String weekEndTime){
    	Map<String, String> cronMap = Maps.newHashMap();
    	String startCron = "";
    	String endCron = "";
    	if(LoopEnum.WEEK.getCode().equals(loopType)){
    		String[] startTimes = splitTime(weekStartTime);
    		String[] endTimes = splitTime(weekEndTime);
    		startCron = "0 "+startTimes[1]+" "+startTimes[0]+" ? * "+week+" *";
    		endCron = "0 "+endTimes[1]+" "+endTimes[0]+" ? * "+week+" *";
    	}else{
    		String[] startTimes = splitDateTime(startTime);
    		String[] endTimes = splitDateTime(endTime);
    		String startYear = startTimes[0];
    		String endYear = endTimes[0];
    		// 0 1 1 2 2 ? 2013-2014
    		startCron = "0 "+startTimes[4]+" "+startTimes[3]+" "+startTimes[2]+" "+startTimes[1]+" ? "+startYear+"-"+endYear;
    		endCron = "0 "+endTimes[4]+" "+endTimes[3]+" "+endTimes[2]+" "+endTimes[1]+" ? "+startYear+"-"+endYear;
    	}
    	
    	cronMap.put("startCron", startCron);
    	cronMap.put("endCron", endCron);
    	
    	return cronMap;
    }
    
    public static String generateCron(String startTime, String endTime, String loopType, String week){
    	String corn = "",day="",month="",year="";
    	String[] startTimes = splitDateTime(startTime);
		String[] endTimes = splitDateTime(endTime);
		String startYear = startTimes[0];
		String endYear = endTimes[0];
		String startMonth=startTimes[1];
		String endMonth=endTimes[1];
		String startDay=startTimes[2];
		String endDay=endTimes[2];
		String hour=startTimes[3];
		String minute=startTimes[4];
		if(LoopEnum.SINGLEWEEK.getCode().equals(loopType)) {//单次 0
			week="?";
    		day=startDay;
    		month=startMonth;
    		year=startYear;
    	}else if(LoopEnum.WEEK.getCode().equals(loopType)){//循环 1按星期
    		day="?";
    		month=startMonth.equals(endMonth)==true?startMonth:startMonth+"-"+endMonth;
    		year=startYear.equals(endYear)==true?startYear:startYear+"-"+endYear;
    	}
    	corn = "0 "+minute+" "+hour+" "+day+" "+month+" "+week+" "+year;
    	return corn;
    }
    
    public static void main(String[] args) {
		String datetime = "2018-05-11 18:10";
		String time = "18:13";
		
		String[] dts = splitDateTime(datetime);
		String[] ts = splitTime(time);
		
		for(String dt : dts){
			System.out.println(dt);
		}
		
		for(String t : ts){
			System.out.println(t);
		}
	}
}  

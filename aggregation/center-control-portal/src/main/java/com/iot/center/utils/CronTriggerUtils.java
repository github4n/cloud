package com.iot.center.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;

import com.google.common.collect.Lists;

/**
 * 根据cron表达式生成执行时间
 * @author fenglijian
 *
 */
public class CronTriggerUtils {
	
	public static List<String> trigger(String cron, Date start , Date end){
		try {
			List<String> result = Lists.newArrayList();
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();

            cronTriggerImpl.setCronExpression(cron);

            // 根据cron表达式计算具体值
            List<Date> dates = TriggerUtils.computeFireTimesBetween(cronTriggerImpl, null, start, end);
            System.out.println(dates.size());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dates.forEach(date -> {
                System.out.println(dateFormat.format(date));
                result.add(dateFormat.format(date));
            });
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	/*public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        
        trigger("0 1 15 16 5 ? 2017-2018", now, calendar.getTime());
	}*/

}

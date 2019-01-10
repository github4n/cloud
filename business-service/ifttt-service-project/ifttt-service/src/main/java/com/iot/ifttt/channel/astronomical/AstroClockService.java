package com.iot.ifttt.channel.astronomical;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.ifttt.channel.timer.TimerService;
import com.iot.ifttt.common.IftttServiceEnum;
import com.iot.ifttt.entity.AppletItem;
import com.iot.ifttt.entity.AppletThis;
import com.iot.ifttt.service.IAppletItemService;
import com.iot.ifttt.service.IAppletThisService;
import com.iot.ifttt.util.TimeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述：天文定时业务逻辑
 * 创建人： LaiGuiMing
 * 创建时间： 2018/6/11 16:16
 */
@Service
public class AstroClockService {
    private final Logger logger = LoggerFactory.getLogger(AstroClockService.class);

    @Autowired
    private IAppletThisService appletThisService;
    @Autowired
    private IAppletItemService appletItemService;
    @Autowired
    private TimerService timerService;

    private static final String SUNRISE = "sunrise";

    private static final String SUNSET = "sunset";

    private static final int NO_DELAY = 0;

    private static final int BEFORE = 1;

    /**
     * 统计天文定时任务
     */
    public void countAstroClockJob() {
        //取出天文定时this
        Map<String, Object> params = Maps.newHashMap();
        params.put("service_code", IftttServiceEnum.ASTRONOMICAL.getCode());
        List<AppletThis> thisList = appletThisService.selectByMap(params);
        //取出对应的item
        List<AppletItem> items = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(thisList)) {
            for (AppletThis vo : thisList) {
                Map<String, Object> params1 = Maps.newHashMap();
                params1.put("event_id", vo.getId());
                List<AppletItem> list = appletItemService.selectByMap(params1);
                items.addAll(list);
            }
        }
        //循环计算天文定时
        if (CollectionUtils.isNotEmpty(items)) {
            for (AppletItem vo : items) {
                addSubJob(vo, 1);
            }
        }
    }

    /**
     * 计算并添加定时任务
     *
     * @param item
     * @param dateNum 偏移天数 0当天 1第二天
     */
    public void addSubJob(AppletItem item, Integer dateNum) {
        /*{
            "trigType": "sunrise",
                "longitude": "113.211",
                “latitude”:”40.14924”,
                “timeZone”:” GMT”,
                “intervalType”:0, //0：无延迟，1：Before，2：After,
                “intervalTime”:”3600”, // 以秒为单位
            "repeat": [ 0, 1, 2, 3, 4, 5, 6 ]
        }*/
        String json = item.getJson();
        Map<Object, Object> propMap = JSON.parseObject(json, Map.class);

        //第一步 判断repeat
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.DATE, dateNum); //加天数
        Integer dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        List<Integer> repeatlist = (List<Integer>) propMap.get("repeat");
        Boolean flag = false;
        if(CollectionUtils.isNotEmpty(repeatlist)){
            for (Integer repeat : repeatlist) {
                if (dayOfWeek.equals(repeat)) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            logger.warn("天文定时,repeat判断，当天不执行，不做处理！" + repeatlist.toString());
            return;
        }

        //第二步 判断计算时间
        AreaTime areaTime = getAreaTime(propMap, calendar);
        Map<String, Object> areaTimeMap = AstroClockUtil.calculateNoaaSunTime(areaTime);
        //第三步 封装cron
        String trigType = (String) propMap.get("trigType");
        int intervalType = Integer.valueOf(String.valueOf(propMap.get("intervalType"))); //0：无延迟，1：Before，2：After
        String intervalTimeStr = (String) propMap.get("intervalTime"); // 以秒为单位
        Integer intervalTime = Integer.valueOf(intervalTimeStr);

        Date time;
        if (SUNRISE.equals(trigType)) {
            time = (Date) areaTimeMap.get("sunriseTime");
        } else if (SUNSET.equals(trigType)) {
            time = (Date) areaTimeMap.get("sunsetTime");
        } else {
            //类型不对，不做处理
            logger.warn("天文定时类型不匹配，不做处理！" + trigType);
            return;
        }

        //前后
        if (intervalType == NO_DELAY) {
            intervalTime = 0;
        } else if (intervalType == BEFORE) {
            intervalTime = -intervalTime;
        }
        String cron = calcCron(time, intervalTime);

        //转成0时区
        String area = (String) propMap.get("area");
        cron = TimeUtil.getUTCCron(cron,area);

        //第四步 添加任务
        timerService.updateJob(item.getId(), cron);
    }

    /**
     * 获取区域实际
     *
     * @param propMap
     * @param calendar
     * @return
     */
    private AreaTime getAreaTime(Map<Object, Object> propMap, Calendar calendar) {
        AreaTime areaTime = new AreaTime();
        String latitude = (String) propMap.get("latitude");
        float latitudef = Float.parseFloat(latitude);
        areaTime.setLatitude(latitudef);

        String longitude = (String) propMap.get("longitude");
        float longitudef = Float.parseFloat(longitude);
        areaTime.setLongitude(longitudef);

        String timeZoneStr = (String) propMap.get("timeZone");
        String[] gmt = timeZoneStr.split("GMT");
        float timeZone = Float.parseFloat(gmt[1]);
        areaTime.setTimeZone(timeZone);

        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 1;
        Integer datetime = calendar.get(Calendar.DATE);

        areaTime.setYear(year);
        areaTime.setMonth(month);
        areaTime.setDay(datetime);
        return areaTime;
    }

    /**
     * 计算时间表达式
     *
     * @param astroClock
     * @param intervalTime
     * @return
     */
    private String calcCron(Date astroClock, Integer intervalTime) {
        int time = intervalTime;
        int addType = 1;
        //负数处理
        if (time < 0) {
            addType = -1;
            time = -time;
        }

        Integer intervalHour = time / 3600;
        Integer intervalMinute = (time - intervalHour * 3600) / 60;
        Integer intervalSecond = time % 60;

        Calendar cronTime = Calendar.getInstance();

        cronTime.setTime(astroClock);
        cronTime.add(Calendar.HOUR, addType * intervalHour);
        cronTime.add(Calendar.MINUTE, addType * intervalMinute);
        cronTime.add(Calendar.SECOND, addType * intervalSecond);

        Integer cronYear = cronTime.get(Calendar.YEAR);
        //Integer cronMonth = cronTime.get(Calendar.MONTH) + 1;
        //Integer cronDatetime = cronTime.get(Calendar.DATE);
        Integer cronSecond = cronTime.get(Calendar.SECOND);
        Integer cronMinute = cronTime.get(Calendar.MINUTE);
        Integer cronHour = cronTime.get(Calendar.HOUR_OF_DAY);

        return cronSecond + " " + cronMinute + " " + cronHour + " * * ? " + cronYear;
    }

}

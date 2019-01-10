package com.iot.building.ifttt.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.iot.building.device.service.impl.DeviceService;
import com.iot.building.ifttt.entity.AreaTime;
import com.iot.building.ifttt.entity.Rule;
import com.iot.building.ifttt.util.AstroClockUtil;
import com.iot.building.ifttt.util.TimeUtil;
import com.iot.building.ifttt.vo.SensorVo;
import com.iot.building.ifttt.vo.req.AreaTimeReq;
import com.iot.building.ifttt.vo.req.RuleReq;
import com.iot.common.exception.BusinessException;
import com.iot.schedule.api.ScheduleApi;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.schedule.vo.AddJobReq;
import com.iot.util.AssertUtils;

/**
 * 描述：天文定时业务逻辑
 * 创建人： LaiGuiMing
 * 创建时间： 2018/6/11 16:16
 */
@Service
public class AstroClockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleService.class);

    @Autowired
    private RuleService ruleService;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ScheduleApi scheduleApi;

    private final static String SUNRISE = "sunrise";

    private final static String SUNSET = "sunset";

    private final static Integer NO_DELAYED = 0;

    private final static Integer BEFORE = 1;

    private final static Integer AFTER = 2;

    /**
     * 添加定时任务
     *
     * @param sensor
     * @param cron
     * @param trigType
     *//*
    public void addCronJob(SensorVo sensor, String cron, String trigType) {
        LOGGER.info("addCronJob({}, {}, {}", sensor, cron, trigType);
        AssertUtils.notEmpty(cron, "cron is null");
        Long ruleId = sensor.getRuleId();
        Rule rule = ruleService.getCache(ruleId);
        if (rule != null && rule.getIsMulti() == 1) {
            AddJobReq addJobReq = new AddJobReq();

            // 获取地区
            String area = deviceService.getAreaByUserId(rule.getUserId());
            ZoneId zoneId = TimeUtil.getZoneId(area);
            LOGGER.info("addCronJob(), final use zoneId={}", zoneId.getId());

            // 指定地区当前时间
            ZonedDateTime zdt = ZonedDateTime.now(zoneId);
            // 偏移的秒数
            long totalSeconds = zdt.getOffset().getTotalSeconds();
            long totalMins = (totalSeconds)/(60);
            cron = TimeUtil.getUTCCron(cron, Integer.parseInt(String.valueOf(totalMins)));

            addJobReq.setCron(cron);
            addJobReq.setJobName(sensor.getId().toString());
            addJobReq.setJobClass(ScheduleConstants.TFTTT_JOB);
            Map<String, Object> data = Maps.newHashMap();
            data.put("ruleId", sensor.getRuleId());
            data.put("type", trigType);
            data.put("tenantId", rule.getTenantId());
            addJobReq.setData(data);
            try {
                scheduleApi.delJob(sensor.getId().toString());
            } catch (BusinessException e) {
                LOGGER.warn("del job error: ", e);
            }
            scheduleApi.addJob(addJobReq);
        }
    }

    *//**
     * 添加子任务
     *
     * @param calendar
     * @param dayOfWeek
     * @param jsStr
     * @param repeatlist
     * @param sensor
     *//*
    public void checkAndAddSubJob(Calendar calendar, Integer dayOfWeek, JSONObject jsStr, List<Integer> repeatlist,
                                  SensorVo sensor) {
        LOGGER.info("checkAndAddSubJob({}, {}, {}, {}, {})", calendar, dayOfWeek, jsStr, repeatlist, sensor);
        for (Integer repeat : repeatlist) {
            if (dayOfWeek == repeat) {
                AreaTimeReq areaTime = new AreaTimeReq();

                String latitude = (String) jsStr.get("latitude");
                float latitudef = Float.parseFloat(latitude);
                areaTime.setLatitude(latitudef);

                String longitude = (String) jsStr.get("longitude");
                float longitudef = Float.parseFloat(longitude);
                areaTime.setLongitude(longitudef);

                String timeZoneStr = (String) jsStr.get("timeZone");
                String[] GMT = timeZoneStr.split("GMT");
                float timeZone = Float.parseFloat(GMT[1]);
                areaTime.setTimeZone(timeZone);

                Integer year = calendar.get(Calendar.YEAR);
                Integer month = calendar.get(Calendar.MONTH) + 1;
                Integer datetime = calendar.get(Calendar.DATE);

                areaTime.setYear(year);
                areaTime.setMonth(month);
                areaTime.setDay(datetime);
                Map<String, Object> areaTimeMap = calculateNoaaSunTime(areaTime);

                Integer intervalType = (Integer) jsStr.get("intervalType");
                String trigType = (String) jsStr.get("trigType");
                String intervalTimeStr = (String) jsStr.get("intervalTime");
                Integer intervalTime = Integer.valueOf(intervalTimeStr);

                Date sunriseTime = (Date) areaTimeMap.get("sunriseTime");
                Date sunsetTime = (Date) areaTimeMap.get("sunsetTime");

                // 添加subjob
                if (NO_DELAYED == intervalType) {
                    String cron = "";
                    if (SUNRISE.equals(trigType)) {

                        cron = calcCron(0, sunriseTime, 1);

                    } else if (SUNSET.equals(trigType)) {

                        cron = calcCron(0, sunsetTime, 1);
                    }

                    addCronJob(sensor, cron, trigType);
                }
                if (BEFORE == intervalType) {// before
                    String cron = "";
                    if (SUNRISE.equals(trigType)) {

                        cron = calcCron(intervalTime, sunriseTime, -1);

                    } else if (SUNSET.equals(trigType)) {

                        cron = calcCron(intervalTime, sunsetTime, -1);
                    }
                    addCronJob(sensor, cron, trigType);
                }
                if (AFTER == intervalType) {// after
                    String cron = "";
                    if (SUNRISE.equals(trigType)) {

                        cron = calcCron(intervalTime, sunriseTime, 1);

                    } else if (SUNSET.equals(trigType)) {

                        cron = calcCron(intervalTime, sunsetTime, 1);
                    }
                    addCronJob(sensor, cron, trigType);
                }
                break;
            }
        }
    }*/

    /**
     * 生成时间格式
     *
     * @param intervalTime
     * @param astroClock
     * @param addType
     * @return
     */
    private String calcCron(Integer intervalTime, Date astroClock, Integer addType) {
        LOGGER.info("calcCron({}, {}, {}", intervalTime, astroClock, addType);
        Integer intervalHour = (int) (intervalTime / 3600);
        Integer intervalMinute = (int) ((intervalTime - intervalHour * 3600) / 60);
        Integer intervalSecond = (int) (intervalTime % 60);

        Calendar cronTime = Calendar.getInstance();

        cronTime.setTime(astroClock);
        cronTime.add(Calendar.HOUR, addType * intervalHour);
        cronTime.add(Calendar.MINUTE, addType * intervalMinute);
        cronTime.add(Calendar.SECOND, addType * intervalSecond);

        Integer cronYear = cronTime.get(Calendar.YEAR);
        Integer cronMonth = cronTime.get(Calendar.MONTH) + 1;
        Integer cronDatetime = cronTime.get(Calendar.DATE);
        Integer cronSecond = cronTime.get(Calendar.SECOND);
        Integer cronMinute = cronTime.get(Calendar.MINUTE);
        Integer cronHour = cronTime.get(Calendar.HOUR_OF_DAY);

        String cron = cronSecond + " " + cronMinute + " " + cronHour + " * * ? "
                + cronYear;
        return cron;
    }

    /**
     * 获取天文定时数据
     *
     * @param areaTime
     * @return
     */
    public Map<String, Object> calculateNoaaSunTime(AreaTimeReq areaTime) {
        AreaTime area = new AreaTime();
        area.setLatitude(areaTime.getLatitude());
        area.setLongitude(areaTime.getLongitude());
        area.setTimeZone(areaTime.getTimeZone());
        area.setYear(areaTime.getYear());
        area.setMonth(areaTime.getMonth());
        area.setDay(areaTime.getDay());
        area.setHour(areaTime.getHour());
        area.setMinute(areaTime.getMinute());
        area.setSecond(areaTime.getSecond());
        return AstroClockUtil.calculateNoaaSunTime(area);
    }

    /**
     * 添加天文定时子任务
     *
     * @param rule
     *//*
    public void addSubAstroClockJob(RuleReq rule) {
        if (rule != null && rule.getId() != null) {
            Long ruleId = rule.getId();
            Calendar calendar = rule.getCalendar();
            Integer dayOfWeek = rule.getDayOfWeek();
            List<SensorVo> sensorList = sensorService.selectByRuleId(ruleId);

            if (calendar == null && dayOfWeek == null) {
                Date date = new Date();
                calendar = Calendar.getInstance();
                calendar.setTime(date);
                dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            }

            if (CollectionUtils.isNotEmpty(sensorList)) {
                for (SensorVo sensor : sensorList) {
                    String properties = sensor.getProperties();
                    AssertUtils.notEmpty(properties, "properties is null");
                    JSONObject jsStr = JSONObject.parseObject(properties);
                    List<Integer> repeatlist = (List<Integer>) jsStr.get("repeat");
                    if (CollectionUtils.isNotEmpty(repeatlist)) {
                        // 3.遍历propertieslist 将所需字段取出，条件判断repeat是否执行即创建subjob；
                        checkAndAddSubJob(calendar, dayOfWeek, jsStr, repeatlist, sensor);
                    }
                }
            }
        }
    }*/

   /* *//**
     * 检测添加天文定时任务
     *//*
    public void addAstroClockJob(Long tenantId) {
        // 1.查找ifttt_rule中type为sunrise、sunset
        List<Rule> ruleList = findRuleList();
        if (CollectionUtils.isNotEmpty(ruleList)) {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(calendar.DATE, 1);
            Integer dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            // 2.遍历rule_id在ifttt_sensor表，查找properties
            for (Rule rule : ruleList) {
                // 3.遍历propertieslist 将所需字段取出，条件判断repeat是否执行即创建subjob；
                RuleReq ruleReq = new RuleReq();
                ruleReq.setTenantId(tenantId);
                ruleReq.setId(rule.getId());
                ruleReq.setCalendar(calendar);
                ruleReq.setDayOfWeek(dayOfWeek);
                addSubAstroClockJob(ruleReq);
            }
        }
    }*/

    private List<Rule> findRuleList() {
        List<Rule> ruleList = new ArrayList<>();
        List<Rule> sunsetList = ruleService.selectByType(SUNSET);
        List<Rule> sunriseList = ruleService.selectByType(SUNRISE);
        if (CollectionUtils.isNotEmpty(sunriseList)) {
            ruleList.addAll(sunriseList);
        }
        if (CollectionUtils.isNotEmpty(sunsetList)) {
            ruleList.addAll(sunsetList);
        }
        return ruleList;
    }

    public static void main(String[] args) {
        String properties = "{" + "	\"idx\": 2," + "	\"trigType\": \"sunset\", " + "	\"longitude\": \"113.211\", "
                + "	\"latitude\": \"40.14924\", " + "	\"timeZone\": \"GMT-8\", " + "	\"intervalType\": 0, "
                + "	\"intervalTime\": \"3600\", " + "	\"repeat\": [0, 1, 2, 3, 4, 5, 6] " + "}";

        JSONObject jsStr = JSONObject.parseObject(properties);
        System.out.println(jsStr);
        AreaTimeReq areaTime = new AreaTimeReq();
        List<Integer> repeatlist = (List<Integer>) jsStr.get("repeat");
        String latitude = (String) jsStr.get("latitude");
        float latitudef = Float.parseFloat(latitude);
        areaTime.setLatitude(latitudef);

        String timeZoneStr = (String) jsStr.get("timeZone");
        String[] GMT = timeZoneStr.split("GMT");
        float timeZone = Float.parseFloat(GMT[1]);
        System.out.println(timeZone);

        System.out.println(repeatlist);
        System.out.println(areaTime.getLatitude());
        for (Integer repeat : repeatlist) {
            if (1 == repeat) {
                System.out.println(repeat);
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(calendar.DATE, 1);
                System.out.println(calendar.getTime());
                Integer dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                System.out.println(dayOfWeek);

                Integer year = calendar.get(Calendar.YEAR);
                Integer month = calendar.get(Calendar.MONTH) + 1;
                Integer datetime = calendar.get(Calendar.DATE);
                System.out.println("year " + year);
                System.out.println("month " + month);
                System.out.println("datetime " + datetime);
                Integer intervalTime = 3665;
                Integer intervalHour = (int) (intervalTime / 3600);
                Integer intervalMinute = (int) ((intervalTime - intervalHour * 3600) / 60);
                Integer intervalSecond = (int) (intervalTime % 60);
                System.out.println("intervalHour:" + intervalHour + " intervalMinute:" + intervalMinute
                        + " intervalSecond:" + intervalSecond);
                calendar.add(Calendar.HOUR, 24);
                calendar.add(Calendar.MINUTE, intervalMinute);
                calendar.add(Calendar.SECOND, intervalSecond);

                Integer cronSecond = calendar.get(Calendar.SECOND);
                Integer cronMinute = calendar.get(Calendar.MINUTE);
                Integer cronHour = calendar.get(Calendar.HOUR);
                System.out.println(calendar.getTime());
                System.out.println("cronSecond:" + cronSecond + " cronMinute:" + cronMinute + " cronHour:" + cronHour);

                String isOpenCenterControl = "1";
                Boolean.valueOf("1");
                Boolean isOpen = Boolean.valueOf(isOpenCenterControl);
                System.out.println(Boolean.valueOf("true"));

            }

        }
    }
}

package com.iot.airswitch.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.iot.airswitch.api.AirSwitchStatisticsApi;
import com.iot.airswitch.constant.Constants;
import com.iot.airswitch.constant.QueryDateType;
import com.iot.airswitch.util.TransformUtil;
import com.iot.airswitch.vo.req.SwitchElectricityStatisticsReq;
import com.iot.airswitch.vo.resp.SwitchElectricityStatisticsHeadResp;
import com.iot.airswitch.vo.resp.SwitchElectricityStatisticsTopResp;
import com.iot.airswitch.vo.resp.SwtichElectricityStatisticsChartResp;
import com.iot.device.api.AirSwitchEventApi;
import com.iot.device.api.ElectricityStatisticsApi;
import com.iot.device.vo.req.*;
import com.iot.device.vo.rsp.*;
import com.iot.redis.RedisCacheUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @Author: Xieby
 * @Date: 2018/11/8
 * @Description: *
 */
@Service
public class AirSwitchStatisticsService {

    private Logger log = LoggerFactory.getLogger(AirSwitchStatisticsService.class);

    @Autowired
    private AirSwitchService airSwitchService;
    @Autowired
    private AirSwitchEventApi airSwitchEventApi;
    @Autowired
    private AirSwitchStatisticsApi airSwitchStatisticsApi;
    @Autowired
    private ElectricityStatisticsApi electricityStatisticsApi;

    /**
     * 获取天，月，年电量统计
     */
    public SwitchElectricityStatisticsHeadResp getStatisticsHeadInfo(SwitchElectricityStatisticsReq req) {
        log.info("start getStatisticsHeadInfo...");
        SwitchElectricityStatisticsHeadResp resp = new SwitchElectricityStatisticsHeadResp();
        String key = Constants.TOTAL_ELECTRICITY_COUNT_REDIS + req.getDate();
        // 从redis获取
        if (req.getSpaceId().equals(-1L) && RedisCacheUtil.hasKey(key)) {
            String topInfo = RedisCacheUtil.valueGet(key);
            resp = JSON.parseObject(topInfo, SwitchElectricityStatisticsHeadResp.class);
        } else {
            resp = getStatisticsHeadInfoFromDb(req);
            if (req.getSpaceId().equals(-1L) && !RedisCacheUtil.hasKey(key)) {
                RedisCacheUtil.valueSet(key, JSON.toJSONString(resp), 60*60*24*7L);
            }
        }
        log.info("end getStatisticsHeadInfo = {}.", JSON.toJSONString(resp));
        return resp;
    }

    /**
     * 获取天，月，年电量统计
     */
    public SwitchElectricityStatisticsHeadResp getStatisticsHeadInfoFromDb(SwitchElectricityStatisticsReq req) {
        log.info("start getStatisticsHeadInfoFromDb...");
        SwitchElectricityStatisticsHeadResp result = new SwitchElectricityStatisticsHeadResp();
        if (CollectionUtils.isEmpty(req.getDeviceIds())) {
            return result;
        }
        try {
            Date queryDate = DateUtils.parseDate(req.getDate(), Constants.DATE_FORMAT_YYYY_MM_DD);
            ElectricityStatisticsReq minReq = new ElectricityStatisticsReq();
            minReq.setTimeStr(req.getDate());
            minReq.setTenantId(req.getTenantId());
            minReq.setDeviceIds(req.getDeviceIds());
            minReq.setStep(Constants.STATISTICS_HOUR);
            log.info("min req = {}.", JSON.toJSONString(minReq));
            // 获取当天小时电量列表
            List<ElectricityStatisticsRsp> minList = electricityStatisticsApi.getMinListByReq(minReq);
            log.info("min list = {}.", JSON.toJSONString(minList));
            // 获取昨天小时电量列表
            Date lastDate = DateUtils.addDays(queryDate, -1);
            minReq.setTimeStr(DateFormatUtils.format(lastDate, Constants.DATE_FORMAT_YYYY_MM_DD));
            List<ElectricityStatisticsRsp> minLastList = electricityStatisticsApi.getMinListByReq(minReq);

            DailyElectricityStatisticsReq dayReq = new DailyElectricityStatisticsReq();
            dayReq.setMonthPrefix(TransformUtil.getMonthPrefix(queryDate));
            dayReq.setTenantId(req.getTenantId());
            dayReq.setDeviceIds(req.getDeviceIds());
            // 获取当月天电量列表
            List<DailyElectricityStatisticsResp> dayList = electricityStatisticsApi.getDailyListByReq(dayReq);
            // 获取上个月天电量列表
            Date lastMonth = DateUtils.addMonths(queryDate, -1);
            dayReq.setMonthPrefix(TransformUtil.getMonthPrefix(lastMonth));
            List<DailyElectricityStatisticsResp> dayLastList = electricityStatisticsApi.getDailyListByReq(dayReq);

            MonthlyElectricityStatisticsReq monthReq = new MonthlyElectricityStatisticsReq();
            monthReq.setTenantId(req.getTenantId());
            monthReq.setYear(queryDate.getYear()+1900);
            monthReq.setDeviceIds(req.getDeviceIds());
            // 获取当年月电量列表
            List<MonthlyElectricityStatisticsResp> monthList = electricityStatisticsApi.getMonthlyListByReq(monthReq);
            // 获取去年月电量列表
            int lastYear = (queryDate.getYear() + 1900) - 1;
            monthReq.setYear(lastYear);
            List<MonthlyElectricityStatisticsResp> monthLastList = electricityStatisticsApi.getMonthlyListByReq(monthReq);

            Double dayTotal = 0D, dayLastTotal = 0D,
                    monthTotal = 0D, monthLastTotal = 0D,
                    yearTotal = 0D, yearLastTotal = 0D;

            dayTotal = minList.stream().mapToDouble(ElectricityStatisticsRsp::getElectricValue).sum();
            dayLastTotal = minLastList.stream().mapToDouble(ElectricityStatisticsRsp::getElectricValue).sum();
            monthTotal = dayList.stream().mapToDouble(DailyElectricityStatisticsResp::getElectricValue).sum();
            monthLastTotal = dayLastList.stream().mapToDouble(DailyElectricityStatisticsResp::getElectricValue).sum();
            yearTotal = monthList.stream().mapToDouble(MonthlyElectricityStatisticsResp::getElectricValue).sum();
            yearLastTotal = monthLastList.stream().mapToDouble(MonthlyElectricityStatisticsResp::getElectricValue).sum();

            result.setDayStatistics(dayTotal.longValue());
            result.setDayRate(TransformUtil.getRate(dayTotal, dayLastTotal));
            result.setMonthStatistics(monthTotal.longValue());
            result.setMonthRate(TransformUtil.getRate(monthTotal, monthLastTotal));
            result.setYearStatistics(yearTotal.longValue());
            result.setYearRate(TransformUtil.getRate(yearTotal, yearLastTotal));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("end getStatisticsHeadInfoFromDb = {}.", JSON.toJSONString(result));
        return result;
    }

    /**
     * 获取图表列表
     */
    public SwtichElectricityStatisticsChartResp getStatisticsChartInfo(SwitchElectricityStatisticsReq req) {
        log.info("start getStatisticsChartInfo...");
        SwtichElectricityStatisticsChartResp result = new SwtichElectricityStatisticsChartResp();
        TreeMap<Integer, Double> resultMap = Maps.newTreeMap();
        if (QueryDateType.DAY.code.equals(req.getType())) {
            for (int i=0; i<24; i++) {
                resultMap.put(i, 0D);
            }
            if (CollectionUtils.isNotEmpty(req.getDeviceIds())) {
                List<ElectricityStatisticsRsp> minList = getHourElectricityList(req);
                minList.forEach(m-> {
                    Integer dateIndex = Integer.parseInt(m.getArea());
                    Double num = m.getElectricValue();
                    resultMap.put(dateIndex, resultMap.containsKey(dateIndex) ? (resultMap.get(dateIndex) + num) : num);
                });
            }
        } else if (QueryDateType.MONTH.code.equals(req.getType())) {

            int monthSize = LocalDate.now().lengthOfMonth();
            for (int i=1; i<(monthSize+1); i++) {
                resultMap.put(i, 0D);
            }
            if (CollectionUtils.isNotEmpty(req.getDeviceIds())) {
                List<DailyElectricityStatisticsResp> dailyList = getDailyElectricityList(req);
                dailyList.forEach(d-> {
                    Integer dateIndex = Integer.parseInt(d.getDay().substring(8, 10));
                    Double num = d.getElectricValue();
                    resultMap.put(dateIndex, resultMap.containsKey(dateIndex) ? (resultMap.get(dateIndex) + num) : num);
                });
            }
        } else if (QueryDateType.YEAR.code.equals(req.getType())) {
            for (int i=1; i<13; i++) {
                resultMap.put(i, 0D);
            }
            if (CollectionUtils.isNotEmpty(req.getDeviceIds())) {
                List<MonthlyElectricityStatisticsResp> monthlyList = getMonthlyElectricityList(req);
                monthlyList.forEach(m-> {
                    Integer dateIndex = m.getYear();
                    Double num = m.getElectricValue();
                    resultMap.put(dateIndex, resultMap.containsKey(dateIndex) ? (resultMap.get(dateIndex) + num) : num);
                });
            }
        }

        // 返回图表信息 (x轴 y轴)
        List<Integer> xDataList = Lists.newArrayList();
        List<Long> yDataList = Lists.newArrayList();
        for (Integer date : resultMap.keySet()) {
            xDataList.add(date);
            yDataList.add(resultMap.get(date).longValue());
        }
        result.setxList(xDataList);
        result.setyList(yDataList);
        log.info("end getStatisticsChartInfo = {}.", JSON.toJSONString(result));
        return result;
    }

    /**
     * 获取用电量Top10的设备
     */
    public SwitchElectricityStatisticsTopResp getStatisticsTopInfo(SwitchElectricityStatisticsReq req) {
        log.info("start getStatisticsTopInfo....");
        SwitchElectricityStatisticsTopResp result = new SwitchElectricityStatisticsTopResp();
        if (CollectionUtils.isEmpty(req.getDeviceIds())) {
            return result;
        }
        Map<String, Double> resultMap = Maps.newHashMap();
        if (QueryDateType.DAY.code.equals(req.getType())) {
            List<ElectricityStatisticsRsp> minList = getHourElectricityList(req);
            minList.forEach(m-> {
                String deviceId = m.getDeviceId();
                resultMap.put(deviceId, resultMap.containsKey(deviceId) ? (resultMap.get(deviceId) + m.getElectricValue()) : m.getElectricValue());
            });
        } else if (QueryDateType.MONTH.code.equals(req.getType())) {
            List<DailyElectricityStatisticsResp> dailyList = getDailyElectricityList(req);
            dailyList.forEach(d-> {
                String deviceId = d.getDeviceId();
                resultMap.put(deviceId, resultMap.containsKey(deviceId) ? (resultMap.get(deviceId) + d.getElectricValue()) : d.getElectricValue());
            });
        } else if (QueryDateType.YEAR.code.equals(req.getType())) {
            List<MonthlyElectricityStatisticsResp> monthlyList = getMonthlyElectricityList(req);
            monthlyList.forEach(m-> {
                String deviceId = m.getDeviceId();
                resultMap.put(deviceId, resultMap.containsKey(deviceId) ? (resultMap.get(deviceId) + m.getElectricValue()) : m.getElectricValue());
            });
        }

        // 按照电量降序
        List<Map.Entry<String, Double>> list = Lists.newArrayList(resultMap.entrySet());
        list.sort(new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        if (CollectionUtils.isEmpty(list)) {
            return result;
        }

        // 获取list
        int listSize = list.size() > req.getTop() ? req.getTop() : list.size();
        list = list.subList(0, listSize);

        List<SwitchElectricityStatisticsTopResp.TopData> dataList = Lists.newArrayList();
        list.forEach(m-> {
            SwitchElectricityStatisticsTopResp.TopData topData = new SwitchElectricityStatisticsTopResp.TopData();
            topData.setDeviceId(m.getKey());
            topData.setCount(m.getValue().longValue());
            dataList.add(topData);
        });

        result.setList(dataList);

        log.info("end getStatisticsTopInfo = {}.", JSON.toJSONString(result));
        return result;
    }

    /**
     * 获取当天
     * @param req
     * @return
     */
    public List<DailyElectricityStatisticsResp> getBusinessElectricityInfo(SwitchElectricityStatisticsReq req) {
        log.info("start getBusinessElectricityInfo req = {}.", JSON.toJSONString(req));
        List<DailyElectricityStatisticsResp> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(req.getDeviceIds())) {
            return list;
        }
        try {
            DailyElectricityStatisticsReq dayReq = new DailyElectricityStatisticsReq();
//            dayReq.setMonthPrefix(StringUtils.substring(req.getDate(), 0, 7));
            dayReq.setTenantId(req.getTenantId());
            dayReq.setDeviceIds(req.getDeviceIds());
            dayReq.setDayStr(req.getDate());
            // 获取当月天电量列表
            list = electricityStatisticsApi.getDailyListByReq(dayReq);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("end getBusinessElectricityInfo = {}.", JSON.toJSONString(list));
        return list;
    }

    /**
     * 根据条件查询月电量
     */
    private List<MonthlyElectricityStatisticsResp> getMonthlyElectricityList(SwitchElectricityStatisticsReq req) {
        try {
            MonthlyElectricityStatisticsReq monthReq = new MonthlyElectricityStatisticsReq();
            monthReq.setTenantId(req.getTenantId());
            monthReq.setYear(Integer.parseInt(req.getDate().substring(0, 4)));
            monthReq.setDeviceIds(req.getDeviceIds());
            List<MonthlyElectricityStatisticsResp> monthLastList = electricityStatisticsApi.getMonthlyListByReq(monthReq);
            return monthLastList;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Lists.newArrayList();
    }

    /**
     * 根据条件查询天电量
     */
    private List<DailyElectricityStatisticsResp> getDailyElectricityList(SwitchElectricityStatisticsReq req) {
        try {
            DailyElectricityStatisticsReq dailyReq = new DailyElectricityStatisticsReq();
            dailyReq.setTenantId(req.getTenantId());
            dailyReq.setDeviceIds(req.getDeviceIds());
            dailyReq.setMonthPrefix(req.getDate().substring(0, 7));
            List<DailyElectricityStatisticsResp> dayList = electricityStatisticsApi.getDailyListByReq(dailyReq);
            return dayList;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Lists.newArrayList();
    }

    /**
     * 根据条件查询小时电量
     */
    private List<ElectricityStatisticsRsp> getHourElectricityList(SwitchElectricityStatisticsReq req) {
        try {
            ElectricityStatisticsReq minReq = new ElectricityStatisticsReq();
            minReq.setTimeStr(req.getDate());
            minReq.setTenantId(req.getTenantId());
            minReq.setDeviceIds(req.getDeviceIds());
            minReq.setStep(Constants.STATISTICS_HOUR);
            List<ElectricityStatisticsRsp> minList = electricityStatisticsApi.getMinListByReq(minReq);
            return minList;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Lists.newArrayList();
    }

    /**
     * 获取事件列表
     */
    public List<AirSwitchEventRsp> getEventList(AirSwitchEventReq req) {
        return airSwitchEventApi.queryList(req);
    }

    /**
     * 获取事件统计的图表
     */
    public SwtichElectricityStatisticsChartResp getEventStatisticsInfo(SwitchElectricityStatisticsReq req) {
        log.info("start getEventStatisticsInfo.....");
        SwtichElectricityStatisticsChartResp result = new SwtichElectricityStatisticsChartResp();
        TreeMap<Integer, Integer> resultMap = Maps.newTreeMap();
        if (QueryDateType.DAY.code.equals(req.getType())) {
            for (int i=0; i<24; i++) {
                resultMap.put(i, 0);
            }
            if (CollectionUtils.isNotEmpty(req.getDeviceIds())) {
                List<AirSwitchHourEventStatisticsResp> hourList = getHourEventList(req);
                log.info("return info = {}.", JSON.toJSONString(hourList));
                hourList.forEach(h-> {
                    Integer dateIndex = h.getHour();
                    if (dateIndex != null) {
                        resultMap.put(dateIndex, resultMap.containsKey(dateIndex) ? (resultMap.get(dateIndex) + h.getCount()) : h.getCount());
                    }
                });
            }
        } else if (QueryDateType.MONTH.code.equals(req.getType())) {

            int monthSize = LocalDate.now().lengthOfMonth();
            for (int i=1; i<(monthSize+1); i++) {
                resultMap.put(i, 0);
            }
            if (CollectionUtils.isNotEmpty(req.getDeviceIds())) {
                List<AirSwitchDailyEventStatisticsResp> dailyList = getDailyEventList(req);
                dailyList.forEach(d-> {
                    Integer dateIndex = Integer.parseInt(d.getDay().substring(8, 10));
                    resultMap.put(dateIndex, resultMap.containsKey(dateIndex) ? (resultMap.get(dateIndex) + d.getCount()) : d.getCount());
                });
            }
        } else if (QueryDateType.YEAR.code.equals(req.getType())) {
            for (int i=1; i<13; i++) {
                resultMap.put(i, 0);
            }
            if (CollectionUtils.isNotEmpty(req.getDeviceIds())) {
                List<AirSwitchMonthEventStatisticsResp> monthList = getMonthEventList(req);
                monthList.forEach(m-> {
                    Integer dateIndex = Integer.parseInt(m.getMonth());
                    resultMap.put(dateIndex, resultMap.containsKey(dateIndex) ? (resultMap.get(dateIndex) + m.getCount()) : m.getCount());
                });
            }
        }

        // 返回图表信息 (x轴 y轴)
        List<Integer> xDataList = Lists.newArrayList();
        List<Long> yDataList = Lists.newArrayList();
        for (Integer date : resultMap.keySet()) {
            xDataList.add(date);
            yDataList.add(resultMap.get(date).longValue());
        }
        result.setxList(xDataList);
        result.setyList(yDataList);
        log.info("end getEventStatisticsInfo = {}.", JSON.toJSONString(result));
        return result;
    }

    /**
     * 获取月的事件列表
     */
    private List<AirSwitchMonthEventStatisticsResp> getMonthEventList(SwitchElectricityStatisticsReq req) {
        try {
            AirSwitchMonthEventStatisticsReq r = new AirSwitchMonthEventStatisticsReq();
            r.setAlarmType(req.getAlarmType());
            r.setTenantId(req.getTenantId());
            r.setDeviceIds(req.getDeviceIds());
            r.setYear(Integer.parseInt(req.getDate().substring(0, 4)));
            List<AirSwitchMonthEventStatisticsResp> monthList = airSwitchEventApi.queryMonthEventList(r);
            return monthList;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Lists.newArrayList();
    }

    /**
     * 获取天的事件列表
     */
    private List<AirSwitchDailyEventStatisticsResp> getDailyEventList(SwitchElectricityStatisticsReq req) {
        try {
            AirSwitchDailyEventStatisticsReq r= new AirSwitchDailyEventStatisticsReq();
            r.setAlarmType(req.getAlarmType());
            r.setDeviceIds(req.getDeviceIds());
            r.setTenantId(req.getTenantId());
            r.setYearMonth(req.getDate().substring(0, 7));
            List<AirSwitchDailyEventStatisticsResp> list = airSwitchEventApi.queryDailyEventList(r);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Lists.newArrayList();
    }

    /**
     * 获取小时的事件列表
     */
    private List<AirSwitchHourEventStatisticsResp> getHourEventList(SwitchElectricityStatisticsReq req) {
        try {
            AirSwitchHourEventStatisticsReq r = new AirSwitchHourEventStatisticsReq();
            r.setDay(req.getDate());
            r.setAlarmType(req.getAlarmType());
            r.setTenantId(req.getTenantId());
            r.setDeviceIds(req.getDeviceIds());
            List<AirSwitchHourEventStatisticsResp> list = airSwitchEventApi.queryHourEventList(r);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Lists.newArrayList();
    }

    /**
     * 查询最新10条告警消息
     */
    public SwitchElectricityStatisticsTopResp getEventTopInfo(SwitchElectricityStatisticsReq req) {
        log.info("start getEventTopInfo..... list = {}", JSON.toJSONString(req) );
        SwitchElectricityStatisticsTopResp result = new SwitchElectricityStatisticsTopResp();
        if (CollectionUtils.isEmpty(req.getDeviceIds())) {
            return result;
        }
        AirSwitchEventReq eventReq = new AirSwitchEventReq();
        eventReq.setTenantId(req.getTenantId());
        eventReq.setDeviceIds(req.getDeviceIds());
        eventReq.setAlarmType(req.getAlarmType());
        List<AirSwitchEventRsp> list = airSwitchEventApi.queryList(eventReq);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        // 降序排列
        list.sort(new Comparator<AirSwitchEventRsp>() {
            @Override
            public int compare(AirSwitchEventRsp o1, AirSwitchEventRsp o2) {
                return o2.getTime().compareTo(o1.getTime());
            }
        });
        log.info("size json = " + JSON.toJSONString(req));
        log.info("size json2 = " + JSON.toJSONString(list));
        int size = req.getTop() > list.size() ? list.size() : req.getTop();
        List<AirSwitchEventRsp> resultList = list.subList(0, size);
        List<SwitchElectricityStatisticsTopResp.TopData> dataList = Lists.newArrayList();
        resultList.forEach(r-> {
            SwitchElectricityStatisticsTopResp.TopData topData = new SwitchElectricityStatisticsTopResp.TopData();
            topData.setDeviceId(r.getDeviceId());
            topData.setReason(r.getAlarmDesc());
            Date d = new Date();
            d.setTime(r.getTime());
            topData.setDate(d);

            dataList.add(topData);
        });

        result.setList(dataList);
        log.info("end getEventTopInfo = {}.", JSON.toJSONString(result));
        return result;
    }

    public void countAirSwitchEvent(Long tenantId) {
        new Thread( () -> {
            String lastDate = LocalDateTime.now().plus(-1, ChronoUnit.HOURS).format(DateTimeFormatter.ofPattern(Constants.ELECTRICITY_REDIS_DATE));
//        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.ELECTRICITY_REDIS_DATE));
            countHourEvent(tenantId, lastDate);
            int currentHour = LocalDateTime.now().getHour();
            // 统计天电量
            if (currentHour == 0) {
                countDayEvent(tenantId, LocalDateTime.now().plus(-1, ChronoUnit.DAYS).format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD)));
            } else {
                countDayEvent(tenantId, LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD)));
            }
            // 统计月电量
            LocalDateTime currentTime = LocalDateTime.now();
            int currentMonth = currentTime.getDayOfMonth();
            if (currentMonth == 1) {
                LocalDateTime dateTime = LocalDateTime.now().plus(-1, ChronoUnit.MONTHS);
                countMonthEvent(tenantId, dateTime.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM)), dateTime.getYear(), dateTime.getMonth().getValue());
            } else {
                countMonthEvent(tenantId, currentTime.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM)), currentTime.getYear(), currentTime.getMonth().getValue());
            }
        }).start();
    }

    /**
     * 统计事件数量（按小时）
     * lastDate : 2018-01-01-00
     */
    public void countHourEvent(Long tenantId, String lastDate) {
        log.info("start count hour event >>>>>>");
        try {

            AirSwitchEventReq minReq = new AirSwitchEventReq();
            minReq.setTenantId(tenantId);
            minReq.setDay(lastDate.substring(0, 10));
            minReq.setHour(lastDate.substring(11, 13));
            List<AirSwitchEventRsp> list = airSwitchStatisticsApi.getEventList(minReq);

            if (CollectionUtils.isEmpty(list)) {
                return;
            }

            List<AirSwitchHourEventStatisticsReq> eventList = Lists.newArrayList();
            Map<String, Integer> resultMap = Maps.newHashMap();
            list.forEach(e-> {
                String key = e.getDeviceId() + Constants.SPILT_SIGN + e.getHour() + Constants.SPILT_SIGN + e.getAlarmType() + Constants.SPILT_SIGN + e.getType();
                resultMap.put(key, resultMap.containsKey(key) ? (resultMap.get(key) + 1) : 1);
            });
            for (String key : resultMap.keySet()) {
                log.info("hour event key = " + key);
                String[] strings = StringUtils.splitByWholeSeparator(key, Constants.SPILT_SIGN);
                String deviceId = strings[0];
                String hourDate = strings[1];
                Integer alarmType = Integer.parseInt(strings[2]);
                String type = strings[3];

                AirSwitchHourEventStatisticsReq data = new AirSwitchHourEventStatisticsReq();
                data.setType(type);
                data.setAlarmType(alarmType);
                data.setDeviceId(deviceId);
                data.setTenantId(tenantId);
                data.setHour((Strings.isNullOrEmpty(hourDate) || "null".equals(hourDate)) ? null : Integer.parseInt(hourDate));
                data.setDay(lastDate.substring(0, 10));
                data.setCount(resultMap.get(key));
                data.setTime(System.currentTimeMillis());
                eventList.add(data);
            }
            log.info("hour event list = {}.", JSON.toJSONString(eventList));
            if (CollectionUtils.isNotEmpty(eventList)) {
                airSwitchEventApi.saveAndUpdateHourEventBatch(eventList);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        log.info("end count hour event >>>>>>");
    }

    /**
     * 统计事件数量（按天）
     */
    public void countDayEvent(Long tenantId, String date) {
        log.info("start count day event >>>>>> " + date);

        try {
            AirSwitchHourEventStatisticsReq req = new AirSwitchHourEventStatisticsReq();
            req.setTenantId(tenantId);
            req.setDay(date);
            List<AirSwitchHourEventStatisticsResp> list = airSwitchEventApi.queryHourEventList(req);
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
            Multimap<String, Integer> multimap = ArrayListMultimap.create();
            list.forEach(e-> {
                String key = e.getDeviceId() + Constants.SPILT_SIGN + e.getAlarmType();
                multimap.put(key, e.getCount());
            });

            List<AirSwitchDailyEventStatisticsReq> dataList = Lists.newArrayList();
            for (String key : multimap.keySet()) {
                Integer total = multimap.get(key).stream().mapToInt(Integer::intValue).sum();
                String[] strs = StringUtils.splitByWholeSeparator(key, Constants.SPILT_SIGN);
                AirSwitchDailyEventStatisticsReq dailyReq = createDailyEventStatistics(tenantId, strs[0], Integer.parseInt(strs[1]), total, date);
                dataList.add(dailyReq);
            }

            log.info("day event list = {}.", JSON.toJSONString(dataList));

            if (CollectionUtils.isNotEmpty(dataList)) {
                airSwitchEventApi.saveAndUpdateDailyEventBatch(dataList);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        log.info("end count day event >>>>>>");
    }

    /**
     * 统计事件数量（按月）
     */
    private void countMonthEvent(Long tenantId, String yearMonth, int year, int month) {
        log.info("start count month event >>>>>> " + yearMonth);
        try {
            AirSwitchDailyEventStatisticsReq req = new AirSwitchDailyEventStatisticsReq();
            req.setTenantId(tenantId);
            req.setYearMonth(yearMonth);
            List<AirSwitchDailyEventStatisticsResp> list = airSwitchEventApi.queryDailyEventList(req);
            log.info("daily event list = {}.", JSON.toJSONString(list));
            if (CollectionUtils.isEmpty(list)) {
                return;
            }

            Multimap<String, Integer> multimap = ArrayListMultimap.create();
            list.forEach(e-> {
                String key = e.getDeviceId() + Constants.SPILT_SIGN + e.getAlarmType();
                multimap.put(key, e.getCount());
            });

            List<AirSwitchMonthEventStatisticsReq> dataList = Lists.newArrayList();
            for (String key : multimap.keySet()) {
                Integer total = multimap.get(key).stream().mapToInt(Integer::intValue).sum();
                String[] strs = StringUtils.splitByWholeSeparator(key, Constants.SPILT_SIGN);
                AirSwitchMonthEventStatisticsReq monthReq = createMonthEventStatistics(tenantId, strs[0], Integer.parseInt(strs[1]), total, year, month);
                dataList.add(monthReq);
            }
            log.info("month event list = {}.", JSON.toJSONString(dataList));
            if (CollectionUtils.isNotEmpty(dataList)) {
                airSwitchEventApi.saveAndUpdateMonthEventBatch(dataList);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("end count month event >>>>>>");
    }

    private AirSwitchMonthEventStatisticsReq createMonthEventStatistics(Long tenantId, String deviceId, Integer alarmType, Integer total, int year, int month) {
        AirSwitchMonthEventStatisticsReq r = new AirSwitchMonthEventStatisticsReq();
        r.setTenantId(tenantId);
        r.setYear(year);
        r.setMonth(String.valueOf(month));
        r.setDeviceId(deviceId);
        r.setCount(total);
        r.setAlarmType(alarmType);
        r.setTime(System.currentTimeMillis());
        return r;
    }

    private AirSwitchDailyEventStatisticsReq createDailyEventStatistics(Long tenantId, String deviceId, Integer alarmType, Integer total, String date) {
        AirSwitchDailyEventStatisticsReq req = new AirSwitchDailyEventStatisticsReq();
        req.setTenantId(tenantId);
        req.setDay(date);
        req.setYearMonth(date.substring(0, 7));
        req.setDeviceId(deviceId);
        req.setAlarmType(alarmType);
        req.setCount(total);
        req.setTime(System.currentTimeMillis());
        return req;
    }

    /**
     * 获取首页电量统计
     */
    public List<Map> getTotalElectricityList(SwitchElectricityStatisticsReq req) {
        log.info("start getTotalElectricityList req = {}.", JSON.toJSONString(req));
        List<Map> list = Lists.newArrayList();
        String totalKey = Constants.BUSINESS_ELECTRICITY_COUNT_REDIS + req.getDate();
        if (req.getSpaceId().equals(-1L) && RedisCacheUtil.hasKey(totalKey)) {
            String info = RedisCacheUtil.valueGet(totalKey);
            list = JSONArray.parseArray(info, Map.class);
        } else {
            list = airSwitchService.getBusinessElectricityInfoFromDb(req);
            if (req.getSpaceId().equals(-1L) && !RedisCacheUtil.hasKey(totalKey)) {
                RedisCacheUtil.valueSet(totalKey, JSON.toJSONString(list), 60*60*24*7L);
            }
        }
        log.info("end getTotalElectricityList = {}.", JSON.toJSONString(list));
        return list;
    }

    /**
     * 定时统计电量
     */
    public void countElectricity(Long tenantId) {
        new Thread(() -> {
            log.info("count electricity start >>>>>>>");
            String lastDate = LocalDateTime.now().plus(-2, ChronoUnit.HOURS).format(DateTimeFormatter.ofPattern(Constants.ELECTRICITY_REDIS_DATE));
            String currentDate = LocalDateTime.now().plus(-1, ChronoUnit.HOURS).format(DateTimeFormatter.ofPattern(Constants.ELECTRICITY_REDIS_DATE));
            airSwitchService.countHourElectricity(tenantId, lastDate, currentDate);
            int currentHour = LocalDateTime.now().getHour();
            // 统计天电量
            if (currentHour == 0) {
                airSwitchService.countDayElectricity(tenantId, LocalDateTime.now().plus(-1, ChronoUnit.DAYS).format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD)));
            } else {
                airSwitchService.countDayElectricity(tenantId, LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD)));
            }
            // 统计月电量
            LocalDateTime currentTime = LocalDateTime.now();
            int currentMonth = currentTime.getDayOfMonth();
            if (currentMonth == 1) {
                LocalDateTime dateTime = LocalDateTime.now().plus(-1, ChronoUnit.MONTHS);
                airSwitchService.countMonthElectricity(tenantId, dateTime.getYear(), dateTime.getMonth().getValue());
            } else {
                airSwitchService.countMonthElectricity(tenantId, currentTime.getYear(), currentTime.getMonth().getValue());
            }

            // 统计首页电量
            String curDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD));
            airSwitchService.countHomePageElectricity(tenantId, curDate);

            log.info("count electricity end >>>>>>>");
        }).start();
    }

    public void syncElectricity(String date, Long tenantId) {
        log.info("start sync electricity >>>>>>> {}.", date);

        try {
            Date cDate = DateUtils.parseDate(date, Constants.DATE_FORMAT_YYYY_MM_DD);
            // 统计小时电量
            for (int i = 0; i < 24; i++) {
                cDate.setHours(i);
                if (cDate.before(new Date())) {
                    String currentDate = DateFormatUtils.format(cDate, Constants.ELECTRICITY_REDIS_DATE);
                    Date lDate = DateUtils.addHours(cDate, -1);
                    String lastDate = DateFormatUtils.format(lDate, Constants.ELECTRICITY_REDIS_DATE);
                    airSwitchService.countHourElectricity(tenantId, lastDate, currentDate);
                }
            }

            airSwitchService.countDayElectricity(tenantId, date);

            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD));
            airSwitchService.countMonthElectricity(tenantId, localDate.getYear(), cDate.getMonth());

            // 统计首页电量
            airSwitchService.countHomePageElectricity(tenantId, date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.info("end sync electricity >>>>>>>");
    }

    public static void main(String[] args) {
        String d = "2018-12-19";
        try {
            Date cDate = DateUtils.parseDate(d, Constants.DATE_FORMAT_YYYY_MM_DD);
            System.out.println(cDate.getYear());
            System.out.println(cDate.getMonth());
            Instant instant = cDate.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();

            // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
            LocalDate localDate = instant.atZone(zoneId).toLocalDate();
            System.out.println("Date = " + cDate);
            System.out.println("LocalDate = " + localDate);

            LocalDate ld = LocalDate.parse(d, DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD));
            System.out.println("LocalDate = " + ld);


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

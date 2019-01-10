package com.iot.airswitch.timer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.iot.airswitch.api.AirSwitchStatisticsApi;
import com.iot.airswitch.constant.Constants;
import com.iot.airswitch.service.AirSwitchService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/11/5
 * @Description: *
 */
@Component
public class ElectricityTimer {

    private Logger log = LoggerFactory.getLogger(ElectricityTimer.class);

    @Autowired
    private AirSwitchService airSwitchService;
    @Autowired
    private AirSwitchStatisticsApi airSwitchStatisticsApi;

//    /**
//     * 定时统计小时电量
//     */
//    @Scheduled(cron = "0 8 * * * ?")
//    public void countHourElectricity() {
//        log.info("count hour electricity ............");
//        Long tenantId = 11L;
//        String lastDate = LocalDateTime.now().plus(-1, ChronoUnit.HOURS).format(DateTimeFormatter.ofPattern(Constants.ELECTRICITY_REDIS_DATE));
//        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.ELECTRICITY_REDIS_DATE));
////        LocalDateTime localDateTime = LocalDateTime.now().plus(-1, ChronoUnit.HOURS);
////        String lastHour = localDateTime.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_HH));
////        String lastDate = localDateTime.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD));
//        airSwitchService.countHourElectricity(tenantId, lastDate, currentDate);
//        int currentHour = LocalDateTime.now().getHour();
//        // 统计
//        if (currentHour == 0) {
//            airSwitchService.countDayElectricity(tenantId, LocalDateTime.now().plus(-1, ChronoUnit.DAYS).format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD)));
//        } else {
//            airSwitchService.countDayElectricity(tenantId, LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD)));
//        }
//        LocalDateTime currentTime = LocalDateTime.now();
//        int currentMonth = currentTime.getDayOfMonth();
//        if (currentMonth == 1) {
//            LocalDateTime dateTime = LocalDateTime.now().plus(-1, ChronoUnit.MONTHS);
//            airSwitchService.countMonthElectricity(tenantId, dateTime.getYear(), dateTime.getMonth().getValue());
//        } else {
//            airSwitchService.countMonthElectricity(tenantId, currentTime.getYear(), currentTime.getMonth().getValue());
//        }
//   }
//
//    /**
//     * 定时统计每天电量
//     */
//    @Scheduled(cron = "0 20 0 * * *")
//    public void countDayElectricity() {
//        log.info("count day electricity ............");
//        LocalDateTime localDateTime = LocalDateTime.now().plus(-1, ChronoUnit.DAYS);
//        String lastDay = localDateTime.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD));
//        airSwitchService.countDayElectricity(11L, lastDay);
//    }
//
//    /**
//     * 定时统计每月电量
//     */
//    @Scheduled(cron = "0 30 0 1 * *")
//    public void countMonthElectricity() {
//        log.info("count month electricity ............");
//        LocalDateTime localDateTime = LocalDateTime.now().plus(-1, ChronoUnit.MONTHS);
//        int year = localDateTime.getYear();
//        int month = localDateTime.getMonth().getValue();
//        airSwitchService.countMonthElectricity(11L, year, month);
//    }
//
//    /**
//     * 统计事件小时数量
//     */
//    @Scheduled(cron = "0 4 * * * ?")
//    public void countHourEvent() {
//        log.info("count hour event ............");
//        LocalDateTime localDateTime = LocalDateTime.now().plus(-1, ChronoUnit.HOURS);
//        String lastHour = localDateTime.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_HH));
//        String lastDate = localDateTime.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD));
//        airSwitchStatisticsApi.countHourEvent(11L, lastDate, lastHour);
//    }
//
//
//    public static void main(String[] args) throws ParseException {
//        Date date = new Date();
//
//        System.out.println(DateFormatUtils.format(date, Constants.DATE_FORMAT_YYYY_MM_DD));
//        System.out.println(Integer.parseInt(DateFormatUtils.format(date, Constants.DATE_FORMAT_HH)));
//
//        LocalDateTime localDateTime = LocalDateTime.now().plus(-1, ChronoUnit.HOURS);
//        System.out.println(localDateTime.getHour());
//        System.out.println(localDateTime.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYYY_MM_DD)));
//
//
//        String d = "2018-10-01 11";
//        Date date1 = DateUtils.parseDate("2018-10-01" + " " + "12", "yyyy-MM-dd HH");
//        System.out.println(date1);
//
//        Multimap<String, Double> m = ArrayListMultimap.create();
//        m.put("1", 10.1d);
//        m.put("1", 11.2d);
//        m.put("1", 12d);
//
//        System.out.println(m.get("1").stream().mapToDouble(Double::doubleValue).sum());
//
//        List<Double> dList = Lists.newArrayList(1.2D, 2.2D, 3.3D);
//        Double d1 = 0D;
//        for (Double dou : dList) {
//            d1 += dou;
//        }
//
//        System.out.println(d1.longValue());
//
//        System.out.println(LocalDateTime.now().plus(-1, ChronoUnit.HOURS).format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh")));
//
//        System.out.println(LocalDate.now().lengthOfMonth());
//    }

}

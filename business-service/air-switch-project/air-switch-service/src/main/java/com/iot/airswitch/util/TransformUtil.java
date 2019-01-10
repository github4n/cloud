package com.iot.airswitch.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.airswitch.constant.Constants;
import com.iot.device.vo.rsp.ElectricityStatisticsRsp;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Xieby
 * @Date: 2018/10/30
 * @Description: *
 */
public class TransformUtil {

    /**
     * 将电量转化为°C
     */
    public static double parseElectricity(Integer num, Integer size) {
        Double d = new Double(num);
        BigDecimal bigDecimal = new BigDecimal(d/1000);
        return bigDecimal.setScale(size, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将16进制转化为Date
     */
    public static Date hexToDate(String hex) {
        Long time = Long.parseLong(hex, 16)*1000;
        Date date = new Date();
        date.setTime(time);
        return date;
    }

    /**
     * 计算比率
     */
    public static Double getRate(Double double1, Double double2) {
        if (double1.intValue() == 0) {
            return 0D;
        }
        if (double2 == 0) {
            return 100D;
        }
        double v = ((double1-double2)/double2)*100;
        BigDecimal bigDecimal = new BigDecimal(v);
        int newScale = v > 0 ? 1 : 2;
        return bigDecimal.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String getMonthPrefix(Date date) {
        int year = date.getYear()+1900;
        int month = date.getMonth() + 1;
        return year + "-" + (month > 9 ? month : "0"+month);
    }

    public static void main(String[] args) {
        System.out.println(getRate(0D, 100D));
        System.out.println(getRate(10D, 0D));
        System.out.println(getRate(10D, 100D));
        System.out.println(getRate(100D, 10D));
    }
}

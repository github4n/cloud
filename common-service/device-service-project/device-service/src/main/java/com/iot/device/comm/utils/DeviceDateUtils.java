package com.iot.device.comm.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:01 2018/4/25
 * @Modify by:
 */
public class DeviceDateUtils {


    public static List<String> completeDay(Calendar cal, int loop, List<String> completeDates) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        //System.out.println(sdf.format(cal.getTime()));
        completeDates.add(sdf.format(cal.getTime()));
        for (int i = 0; i < loop; i++) {
            if (i == 0) {
                continue;
            }
            cal.add(Calendar.HOUR_OF_DAY, -1);
            //System.out.println(sdf.format(cal.getTime()));
            completeDates.add(sdf.format(cal.getTime()));
        }

        return completeDates;
    }

    public static List<String> completeWeek(Calendar cal, int loop, List<String> completeDates) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //System.out.println(sdf.format(cal.getTime()));
        completeDates.add(sdf.format(cal.getTime()));
        for (int i = 0; i < loop; i++) {
            if (i == 0) {
                continue;
            }
            cal.add(Calendar.DAY_OF_WEEK, -1);
            //System.out.println(sdf.format(cal.getTime()));
            completeDates.add(sdf.format(cal.getTime()));
        }

        return completeDates;
    }

    public static List<String> completeMonth(Calendar cal, int loop, List<String> completeDates) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //System.out.println(sdf.format(cal.getTime()));
        completeDates.add(sdf.format(cal.getTime()));
        for (int i = 0; i < loop; i++) {
            if (i == 0) {
                continue;
            }
            cal.add(Calendar.DAY_OF_MONTH, -1);
            //System.out.println(sdf.format(cal.getTime()));
            completeDates.add(sdf.format(cal.getTime()));
        }

        return completeDates;
    }
}

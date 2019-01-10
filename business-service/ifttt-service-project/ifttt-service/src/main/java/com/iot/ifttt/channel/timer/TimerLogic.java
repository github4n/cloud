package com.iot.ifttt.channel.timer;

import com.alibaba.fastjson.JSON;
import com.iot.ifttt.channel.base.ILogic;
import com.iot.ifttt.entity.AppletItem;
import com.iot.ifttt.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 描述：定时业务逻辑
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/27 18:39
 */

/**
 * 说明：
 * 1、添加时，添加定时任务
 * 2、删除时，删除定时任务
 */
@Service
public class TimerLogic implements ILogic {

    @Autowired
    private TimerService timerService;

    @Override
    public void add(AppletItem item) {
        //添加定时任务 0时区
        /*{
            at:13:30
            repeat:[1,2,3,4,5,6,7]
        }*/
        //时间表达式
        String cron = getCron(item.getJson());
        timerService.addJob(item.getId(), cron);
    }

    @Override
    public void delete(AppletItem item) {
        timerService.delJob(item.getId());
    }

    /************************************* 子方法 ************************************************/


    private String getCron(String properties) {
        Map<Object, Object> propMap = JSON.parseObject(properties, Map.class);
        String at = (String) propMap.get("at");
        String area = String.valueOf(propMap.get("area"));
        if (!StringUtils.isEmpty(area) && !"null".equals(area)) {
            at = TimeUtil.getUTCAt(at, area);
        }

        List<Integer> repeats = (List<Integer>) propMap.get("repeat");
        return convertCron(at, repeats);
    }

    /**
     * 定时任务，时间格式转换
     *
     * @param at
     * @param repeats
     * @return
     */
    private String convertCron(String at, List<Integer> repeats) {
        String cron = "";
        //格式转换 0 0 12 ? * ?
        String[] timeArray = at.split(":");
        String m = timeArray[1];
        String h = timeArray[0];
        if (timeArray[1].equals("00")) {
            m = "0";
        }
        if (timeArray[0].equals("00")) {
            h = "0";
        }

        cron += "0 " + m + " " + h;

        //周几
        StringBuffer week = new StringBuffer();
        int index = 1;
        int len = repeats.size();
        for (Integer day : repeats) {
            day++;
            if (index != len) {
                week.append(day);
                week.append(',');
            } else {
                week.append(day);
            }
            index++;
        }

        String weekStr = week.toString();
        //?：匹配该域的任意值。月份的天河周的天互相冲突，必须将其中一个设置为?
        if (len == 7) { // 周一到周天都选了，用?代替
            weekStr = "?";
            cron += " * * ";
        } else {
            cron += " ? * ";
        }
        cron += weekStr;
        return cron;
    }
}

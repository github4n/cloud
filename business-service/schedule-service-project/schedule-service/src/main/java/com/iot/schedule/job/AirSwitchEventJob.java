package com.iot.schedule.job;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.iot.airswitch.api.AirSwitchStatisticsApi;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.schedule.common.ScheduleConstants;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;
import java.util.Map;

/**
 * @Author: Xieby
 * @Date: 2018/11/15
 * @Description: *
 */
public class AirSwitchEventJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("========================= air switch event job start ======================");
        try {

            Map<String, Object> data =(Map<String, Object>) context.getMergedJobDataMap().get(ScheduleConstants.JOB_DATA_KEY);
            String ids = data.get("tenantIds") == null ? null : data.get("tenantIds").toString();
            if (Strings.isNullOrEmpty(ids)) {
                return;
            }
            List<Long> idList = JSON.parseArray(ids, Long.class);
            System.out.println("tenant ids = " + JSON.toJSONString(idList));
            AirSwitchStatisticsApi statisticsApi = ApplicationContextHelper.getBean(AirSwitchStatisticsApi.class);
            for (Long tenantId : idList) {
                statisticsApi.countAirSwitchEvent(tenantId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("========================= air switch event job end ======================");
    }
}

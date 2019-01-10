package com.iot.schedule.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iot.building.device.api.DeviceBusinessTypeApi;
import com.iot.common.helper.ApplicationContextHelper;

/**
 * @Author: Xieby
 * @Date: 2018/9/4
 * @Description: 下发配置的定时任务
 */
public class BusinessTypeStatisticJob implements Job {

	public static final Logger LOGGER = LoggerFactory.getLogger(BusinessTypeStatisticJob.class);
	DeviceBusinessTypeApi deviceBusinessTypeApi = ApplicationContextHelper.getBean(DeviceBusinessTypeApi.class);
	
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	LOGGER.info("========================= statistic job start ======================");
        try {
            // execute issue
        	deviceBusinessTypeApi.initStatistic();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("========================= statistic job end ======================");
    }
}

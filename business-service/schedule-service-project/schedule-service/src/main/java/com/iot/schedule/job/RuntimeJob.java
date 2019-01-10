package com.iot.schedule.job;

import java.util.Map;

import com.iot.common.helper.ApplicationContextHelper;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.device.api.ElectricityStatisticsApi;
import com.iot.schedule.common.ScheduleConstants;

@Service
public class RuntimeJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(RuntimeJob.class);

    private ElectricityStatisticsApi electricityStatisticsApi = ApplicationContextHelper.getBean(ElectricityStatisticsApi.class);


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("*************** 更新设备运行时间 start ***************");

        electricityStatisticsApi.insertOrUpdateRuntime();

        logger.info("*************** 更新设备运行时间  end ***************");


    }

}

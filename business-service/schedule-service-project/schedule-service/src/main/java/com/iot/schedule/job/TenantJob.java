package com.iot.schedule.job;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.tenant.api.AppApi;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TenantJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(TenantJob.class);

    private AppApi appApi = ApplicationContextHelper.getBean(AppApi.class);


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("*************** 更新扫描 start ***************");

        appApi.updateAppStatusByTime();

        logger.info("*************** 更新扫描  end ***************");


    }

}

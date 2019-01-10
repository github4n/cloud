package com.iot.schedule.job;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.api.ElectricityStatisticsApi;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ElectricityStatisticsJob implements Job{

	private static Logger logger = LoggerFactory.getLogger(ElectricityStatisticsJob.class);
	private ElectricityStatisticsApi electricityStatisticsApi = ApplicationContextHelper.getBean(ElectricityStatisticsApi.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("*************** 更新设备上报电量 start ***************");
//	     Map<String, Object> data =(Map<String, Object>) context.getMergedJobDataMap().get(ScheduleConstants.JOB_DATA_KEY);
//	     Long tenantId = Long.valueOf(data.get("tenantId")+"");
//	     logger.info("执行电量统计任务：" + data.toString());
//	     if(tenantId!=null){
//	    	 electricityStatisticsApi.insertOrUpdateElectricityStatistics();
//	     }

		try {
			electricityStatisticsApi.insertOrUpdateElectricityStatistics();
		} catch (Exception e) {
			e.printStackTrace();
		}

	     logger.info("*************** 更新设备上报电量 end ***************");
		
	}

}

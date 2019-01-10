package com.iot.schedule.job;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.file.api.VideoFileApi;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 项目名称：cloud
 * 功能描述：处理未上报文件信息的文件
 * 创建人： yeshiyuan
 * 创建时间：2018/8/2 10:24
 * 修改人： yeshiyuan
 * 修改时间：2018/8/2 10:24
 * 修改描述：
 */
public class DealUnUploadFileInfoScheduleJob implements Job {

	private final static Logger logger = LoggerFactory.getLogger(DealUnUploadFileInfoScheduleJob.class);

	private VideoFileApi videoFileApi = ApplicationContextHelper.getBean(VideoFileApi.class);
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try {
			//根据当前时间得到7*24小时之前的时间
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.HOUR,-24);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd:HH");
			String dateStr = simpleDateFormat.format(calendar.getTime());
			logger.info("DealUnUploadFileInfoScheduleJob strat -> data{}", dateStr);
			videoFileApi.dealUnUploadFileInfoTask(dateStr);
			logger.info("DealUnUploadFileInfoScheduleJob end ");
		} catch (Exception e) {
			logger.error("DealUnUploadFileInfoScheduleJob error : ",e);
		}
	}
	
}

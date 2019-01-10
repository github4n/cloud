package com.iot.video.timer.listener;

import com.iot.common.util.JsonUtil;
import com.iot.video.api.VideoManageApi;
import com.iot.video.api.VideoTimerApi;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * 模块名称：视频模块
 * 功能描述：更新全时录影任务（全时录影溢出处理）
 * 创建人： 李帅
 * 创建时间：2017年11月1日 下午2:28:20
 * 修改人：李帅
 * 修改时间：2017年11月1日 下午2:28:20
 */
public class UpdateHourScheduleJob implements Job {

	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(getClass());

	VideoTimerApi videoTimerApi;

	VideoManageApi videoManageApi;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		videoTimerApi = (VideoTimerApi)(jobExecutionContext.getMergedJobDataMap().get("videoTimerApi"));
		videoManageApi = (VideoManageApi)(jobExecutionContext.getMergedJobDataMap().get("videoManageApi"));
		//根据当前时间得到7*24小时之前的时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR,-168);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd:HH");
		String dateStr = simpleDateFormat.format(calendar.getTime());
		logger.info("UpdateHourScheduleJob scan time : {}", dateStr);
		try{
			//查询这个时间段里已经有录影的全时计划
			Set<String> planIdSet = videoTimerApi.getHadVideoOfPlanFromRedis(dateStr);
			if (!planIdSet.isEmpty()){
				logger.info("UpdateHourScheduleJob start -> planIdSet:{}", JsonUtil.toJson(planIdSet));
				for (String planId : planIdSet  ) {
					//把视频相关数据置为失效，并生成一个待处理的定时任务由DealUntieFileScheduleJob处理
					try{
						videoManageApi.dealPlanAllTimeOver(planId,dateStr);
					}catch (Exception e){
						logger.error("全时录影处理溢出数据报错：",e);
						throw new Exception("全时录影处理溢出数据报错");
					}
				}
				videoTimerApi.deletePlanAllTimeOfRedis(dateStr);
				logger.info("UpdateHourScheduleJob run success ... ");
			}
		}catch (Exception e){
			logger.error("UpdateHourScheduleJob error：",e);
		}

	}


}

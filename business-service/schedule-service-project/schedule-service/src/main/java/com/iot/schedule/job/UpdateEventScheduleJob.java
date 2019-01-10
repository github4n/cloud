package com.iot.schedule.job;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.util.JsonUtil;
import com.iot.video.api.VideoManageApi;
import com.iot.video.api.VideoTimerApi;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：视频模块
 * 功能描述：更新事件任务（事件溢出数据清理）
 * 创建人： zhouzongwei
 * 创建时间：2017年10月31日 下午6:24:56
 * 修改人： zhouzongwei
 * 修改时间：2017年10月31日 下午6:24:56
 */
public class UpdateEventScheduleJob implements Job {
	
	private static final Logger LOGER = LoggerFactory.getLogger(UpdateEventScheduleJob.class);
    
    VideoTimerApi videoTimerApi = ApplicationContextHelper.getBean(VideoTimerApi.class);

	VideoManageApi videoManageApi = ApplicationContextHelper.getBean(VideoManageApi.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			//从redis中获取事件录影溢出的计划
			Set<String> planIdSet = videoTimerApi.getPlanEventOverFromRedis();
			if (!planIdSet.isEmpty()){
				LOGER.info("UpdateEventScheduleJob start -> planIdSet:{}", JsonUtil.toJson(planIdSet));
				for (String planId : planIdSet  ) {
					//把视频相关数据置为失效，并生成一个待处理的定时任务由DealUntieFileScheduleJob处理
					try{
						videoManageApi.dealPlanEventOver(planId);
					}catch (Exception e){
						LOGER.error("事件录影处理溢出数据报错：",e);
						throw new Exception("事件录影处理溢出数据报错");
					}
				}
				LOGER.info("UpdateEventScheduleJob run success ...");
			}
		} catch (Exception e) {
			LOGER.error("UpdateEventScheduleJob error:",e);
		}
	}
    
}

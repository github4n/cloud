package com.iot.schedule.job;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.StringUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.video.api.VideoTimerApi;
import com.iot.video.vo.UntieTaskVo;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：视频模块
 * 功能描述：处理视频无效数据任务
 * 创建人： 李帅
 * 创建时间：2017年11月1日 下午2:28:20
 * 修改人：李帅
 * 修改时间：2017年11月1日 下午2:28:20
 */
public class DealInvalidVideoDataScheduleJob implements Job {

	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(DealInvalidVideoDataScheduleJob.class);

	VideoTimerApi videoTimerApi = ApplicationContextHelper.getBean(VideoTimerApi.class);
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try {
			//获取解绑任务信息
			Map<String,String> planIdMap = videoTimerApi.getUntieTaskInfo();
			if(!planIdMap.isEmpty()) {
				logger.info("DealInvalidVideoDataScheduleJob start -> parms:{}", JsonUtil.toJson(planIdMap));
				for (Map.Entry<String,String> entry : planIdMap.entrySet()) {  
					String planId = entry.getValue();
					String redisKey = entry.getKey();
					try {
						String taskKey = redisKey.replace(ScheduleConstants.IPC_UNBANDING_TASK_KEY, "");
						if (!StringUtil.isBlank(planId) && !StringUtil.isBlank(taskKey)){
							videoTimerApi.deleteLapseDatas(planId, taskKey);
						}
					} catch (Exception e) {
						//记录失败原因
						UntieTaskVo untieTaskVo = RedisCacheUtil.valueObjGet(redisKey, UntieTaskVo.class);
						untieTaskVo.setFailedDesc(JsonUtil.toJson(e));
						RedisCacheUtil.valueObjSet(redisKey,untieTaskVo);
						logger.info("job params : {} ",JsonUtil.toJson(untieTaskVo));
						logger.error("deleteLapseDatas error : ",e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("DealInvalidVideoDataScheduleJob error : ",e);
		}
	}
	
}

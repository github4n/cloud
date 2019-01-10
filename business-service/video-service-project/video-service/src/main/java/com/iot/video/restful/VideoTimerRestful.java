package com.iot.video.restful;

import com.iot.file.api.VideoFileApi;
import com.iot.redis.RedisCacheUtil;
import com.iot.video.api.VideoTimerApi;
import com.iot.video.contants.ModuleConstants;
import com.iot.video.dto.LapseIdDto;
import com.iot.video.dto.SchedulePlanDto;
import com.iot.video.entity.VideoPlanBeyond;
import com.iot.video.manager.VideoTimerManager;
import com.iot.video.mongo.entity.VideoFileEntity;
import com.iot.video.vo.EventVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/** 
 * 
 * 项目名称：IOT云平台 
 * 模块名称：
 * 功能描述：
 * 创建人：
 * 创建时间：2018/3/19 10:47
 * 修改人：
 * 修改时间：2018/3/19 10:47
 * 修改描述：
 */
@RestController
@RequestMapping("/api/videoTimer/service")
public class VideoTimerRestful implements VideoTimerApi {

	@Autowired
    private VideoTimerManager videoTimerManager;

	@Autowired
    private VideoFileApi videoFileApi;
	
	/**
	 * 
	 * 描述：获取过期的文件ID和事件ID
	 * @author 李帅
	 * @created 2018年3月21日 下午5:28:19
	 * @since 
	 * @return
	 */
	public LapseIdDto getLapseFileIdAndEventIdList(){
		return videoTimerManager.getLapseFileIdAndEventIdList();
	}
	
	/**
	 * 
	 * 描述：删除全时录影失效文件数据
	 * @author 李帅
	 * @created 2018年3月21日 下午5:28:27
	 * @since 
	 * @param lapseFileId
	 */
	public void deleteLapseFile(@RequestBody List<String> lapseFileId){
		videoTimerManager.deleteLapseFile(lapseFileId);
	}
	
	/**
	 * 
	 * 描述：删除全时录影失效事件数据
	 * @author 李帅
	 * @created 2018年3月21日 下午5:28:34
	 * @since 
	 * @param lapseEventId
	 */
	public void deleteLapseEvent(@RequestBody List<Long> lapseEventId){
		videoTimerManager.deleteLapseEvent(lapseEventId);
	}
	
	/**
	 * 
	 * 描述：获取计划信息
	 * @author 李帅
	 * @created 2018年3月21日 下午5:28:43
	 * @since 
	 * @param planQueryState
	 * @return
	 */
	public List<SchedulePlanDto> getSchedulePlanInfoList(String planQueryState){
		return videoTimerManager.getSchedulePlanInfoList(planQueryState);
	}
	
	/**
	 * 
	 * 描述：批量更新计划提醒状态
	 * @author 李帅
	 * @created 2018年3月21日 下午5:28:51
	 * @since 
	 * @param planIdList
	 * @param comedueRemindState
	 */
	public void batchUpdatePlanRemindState(@RequestBody List<String> planIdList, String comedueRemindState){
		videoTimerManager.batchUpdatePlanRemindState(planIdList, comedueRemindState);
	}
	
	/**
	 * 
	 * 描述：批量更新计划执行状态
	 * @author 李帅
	 * @created 2018年3月21日 下午5:28:59
	 * @since 
	 * @param planIdList
	 * @param planExecStatus
	 */
	public void batchUpdatePlanState(@RequestBody List<String> planIdList, String planExecStatus){
		videoTimerManager.batchUpdatePlanState(planIdList, planExecStatus);
	}
	
	/**
	 *
	 * 描述：获取失效计划的文件ID
	 * @author 李帅
	 * @created 2018年3月21日 下午5:29:07
	 * @since
	 * @param planId
	 * @return
	 *//*
	public List<String> getOutDatePlanFildID(@RequestParam("planId") String planId){
		return videoTimerManager.getOutDatePlanFildID(planId);
	}*/
	
	/**
	 * 
	 * 描述：删除计划文件数据
	 * @author 李帅
	 * @created 2018年3月21日 下午5:29:16
	 * @since 
	 * @param planIdList
	 */
	/*public void deleteLapseFileByPlanId(@RequestBody List<String> planIdList){
		videoTimerManager.deleteLapseFileByPlanId(planIdList);
	}*/
	
	/**
	 * 
	 * 描述：删除计划事件数据
	 * @author 李帅
	 * @created 2018年3月21日 下午5:29:23
	 * @since 
	 * @param planIdList
	 */
	public void deleteLapseEventByPlanId(@RequestBody List<String> planIdList){
		videoTimerManager.deleteLapseEventByPlanId(planIdList);
	}
	
	/**
	 * 
	 * 描述：获取计划ID和计划套餐量
	 * @author 李帅
	 * @created 2018年3月21日 下午5:29:31
	 * @since 
	 * @return
	 */
    public List<VideoPlanBeyond> getAllVenderPlanList(){
        return videoTimerManager.getAllVenderPlanList();
    }

	/**
	 * 
	 * 描述：排序获取前num条事件信息
	 * @author 李帅
	 * @created 2018年3月21日 下午5:29:46
	 * @since 
	 * @param planId
	 * @param batchValue
	 * @return
	 */
	public List<EventVo> getSortVideoEventInfo(String planId, int batchValue){
		return videoTimerManager.getSortVideoEventInfo(planId, batchValue);
	}
	
	/**
	 * 
	 * 描述：获取事件关联的ts文件信息
	 * @author 李帅
	 * @created 2018年3月21日 下午5:29:54
	 * @since 
	 * @param eventVo
	 * @return
	 */
	public List<String> getEventTsFileInfo(@RequestBody EventVo eventVo){
		return videoTimerManager.getEventTsFileInfo(eventVo.getPlanId(), eventVo.getEventOddurTime());
	}
	
	/**
	 * 
	 * 描述：获取解绑任务信息
	 * @author 李帅
	 * @created 2018年5月24日 上午11:52:21
	 * @since 
	 * @return
	 */
	public Map<String,String> getUntieTaskInfo(){
		return videoTimerManager.getUntieTaskInfo();
	}
	
	/**
	 * 
	 * 描述：删除解绑任务信息
	 * @author 李帅
	 * @created 2018年5月30日 下午6:53:27
	 * @since z
	 * @param redisKey
	 */
	public void deleteRedisObject(String redisKey){
		videoTimerManager.deleteRedisObject(redisKey);
	}
	
	/**
	 * 
	 * 描述：删除失效数据
	 * @author 李帅
	 * @created 2018年5月24日 上午11:52:21
	 * @since 
	 * @return
	 */
	public void deleteLapseDatas(@RequestParam("planId") String planId, @RequestParam("taskKey") String taskKey){
		Integer queryNum = 500;
		String fileTaskKey = ModuleConstants.IPC_FILE_DELETE_TASK_KEY.concat(taskKey);
		// 分批获取计划的失效文件信息
		List<String> videoFiles = videoTimerManager.findInvalidVideoFilePath(planId);
		if (videoFiles != null && videoFiles.size() > 0){
			Set<String> set = new HashSet<>(videoFiles);
			RedisCacheUtil.setAdd(fileTaskKey, set, false);
		}
		// 删除S3上的文件
		videoFileApi.deleteFileByRedisTaskId(fileTaskKey);
		//删除失效文件信息
		videoTimerManager.deleteInvalidVideoFileData(planId);
		// 标志删除文件数据完成
		String videoFileTaskKey = ModuleConstants.IPC_VIDEO_FILE_DELETE_TASK_KEY.concat(taskKey);
		RedisCacheUtil.valueSet(videoFileTaskKey, ModuleConstants.COMPLETED);
		// 删除计划事件失效数据
		videoTimerManager.deleteInvalidVideoEventData(planId);
		// 标志删除事件数据完成
		String eventTaskKey = ModuleConstants.IPC_EVENT_DELETE_TASK_KEY.concat(taskKey);
		RedisCacheUtil.valueSet(eventTaskKey, ModuleConstants.COMPLETED);
	}


	/**
	 * @despriction：从redis中获取某一小时里有录影的全时计划
	 * @author  yeshiyuan
	 * @created 2018/6/12 15:49
	 * @param dateStr 时间（格式为2018-06-11:11）
	 * @return
	 */
	@Override
	public Set<String> getHadVideoOfPlanFromRedis(@RequestParam("dateStr") String dateStr) {
		return RedisCacheUtil.setGetAll(ModuleConstants.VIDEO_PLAN_ALL_TIME + dateStr,String.class,false);
	}

	/**
	 * @despriction：删除redis中某一小时里有录影的全时计划
	 * @author  yeshiyuan
	 * @created 2018/6/12 15:49
	 * @param dateStr 时间（格式为2018-06-11:11）
	 * @return
	 */
	@Override
	public void deletePlanAllTimeOfRedis(@RequestParam("dateStr") String dateStr) {
		RedisCacheUtil.delete(ModuleConstants.VIDEO_PLAN_ALL_TIME + dateStr);
	}

	/**
	 * @despriction：从redis中获取事件录影溢出的计划
	 * @author  yeshiyuan
	 * @created 2018/6/12 15:49
	 * @return
	 */
	@Override
	public Set<String> getPlanEventOverFromRedis() {
		return RedisCacheUtil.setGetAll(ModuleConstants.VIDEO_PLAN_EVENT_OVER,String.class,false);
	}



	/**
	 * @despriction：批量处理过期计划
	 * @author  yeshiyuan
	 * @created 2018/6/14 15:04
	 * @param planIds 计划集合
	 * @param opType 操作类型(1:计划过期5天之内；2：计划过期5天之后)
	 * @return
	 */
	@Override
	public int bacthDealPlanExpire(@RequestBody List<String> planIds, String opType) {
		return videoTimerManager.bacthDealPlanExpire(planIds,opType);
	}
}
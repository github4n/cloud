package com.iot.video.manager;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.CalendarUtil;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.NumberUtil;
import com.iot.common.util.StringUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.video.contants.ModuleConstants;
import com.iot.video.dto.LapseIdDto;
import com.iot.video.dto.SchedulePlanDto;
import com.iot.video.entity.VideoPlanBeyond;
import com.iot.video.enums.PlanExpireFlagEnum;
import com.iot.video.enums.TaskStatusEnum;
import com.iot.video.exception.BusinessExceptionEnum;
import com.iot.video.mongo.service.VideoEventMongoService;
import com.iot.video.mongo.service.VideoFileMongoService;
import com.iot.video.service.VideoTimerService;
import com.iot.video.vo.EventVo;
import com.iot.video.vo.UntieTaskVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VideoTimerManager {

	private final int DeleteNum = 900;
	
	private static final Logger logger  = LoggerFactory.getLogger(VideoTimerManager.class);
	
	@Autowired
	private VideoTimerService videoTimerService;

	@Autowired
	private VideoFileMongoService videoFileMongoService;

	@Autowired
	private VideoEventMongoService videoEventMongoService;
	
	@Value("${unbind.task.timeout}")
	private Integer unbindTaskTimeout;
    
	/**
	 * 
	 * 描述：获取计划最后一帧图片
	 * @author 李帅
	 * @created 2018年3月22日 上午10:44:04
	 * @since 
	 * @return
	 */
	public LapseIdDto getLapseFileIdAndEventIdList(){
		LapseIdDto lapseId = new LapseIdDto();

		List<String> lapseFileId = new ArrayList<String>();
		List<Long> lapseEventId = new ArrayList<Long>();
		try {
			// 获取过期的计划ID和套餐天数
			List<Map<String, String>> planToEventOrFulltimeAmountList = this.videoTimerService.getLapsePlanIdList();
			String planIdKey = null;
			Integer eventOrFulltimeAmountValue = 0;

			List<String> planVideoDateList = null;
			String dueTime = null;
			for (Map<String, String> planToEventOrFulltimeAmountMap : planToEventOrFulltimeAmountList) {

				for (Map.Entry<String, String> planToEventOrFulltimeAmount : planToEventOrFulltimeAmountMap
						.entrySet()) {
					if ("planId".equals(planToEventOrFulltimeAmount.getKey())) {
						planIdKey = planToEventOrFulltimeAmount.getValue();
					}
					if ("eventOrFulltimeAmount".equals(planToEventOrFulltimeAmount.getKey())) {
						eventOrFulltimeAmountValue = NumberUtil.toInteger(planToEventOrFulltimeAmount.getValue());
					}
				}
				if (planIdKey != null && !"".equals(planIdKey) && eventOrFulltimeAmountValue != null) {
					// 获取计划录影日期
					planVideoDateList = videoTimerService.getPlanVideoDates(planIdKey);
					dueTime = planVideoDateList.get(eventOrFulltimeAmountValue - 1);

					// 获取全时录影失效文件ID
					lapseFileId.addAll(videoTimerService.getLapseFileIdList(planIdKey, dueTime));
					// 获取全时录影失效事件ID
					lapseEventId.addAll(videoTimerService.getLapseEventIdList(planIdKey, dueTime));
				}
			}

		} catch (Exception e) {
			logger.error(BusinessExceptionEnum.GETLAPSEFILEIDANDEVENTIDLIST_FAILED.getMessageKey(), e);
			throw new BusinessException(BusinessExceptionEnum.GETLAPSEFILEIDANDEVENTIDLIST_FAILED);
		}
		lapseId.setLapseEventId(lapseEventId);
		lapseId.setLapseFileId(lapseFileId);
		return lapseId;
	}
	
	/**
	 * 
	 * 描述：删除全时录影失效文件数据
	 * @author 李帅
	 * @created 2018年3月21日 下午5:28:27
	 * @since 
	 * @param lapseFileId
	 */
	public void deleteLapseFile(List<String> lapseFileId){
		try {
			List<List<String>> lapseFileIdListList = null;
			if (lapseFileId.size() > DeleteNum) {
				lapseFileIdListList =  CommonUtil.dealBySubList(lapseFileId, DeleteNum);
				for (List<String> lapseFileIdStringList : lapseFileIdListList) {
					videoTimerService.deleteLapseFile(lapseFileIdStringList);
				}
			} else {
				videoTimerService.deleteLapseFile(lapseFileId);
			}
		} catch (Exception e) {
			logger.error(BusinessExceptionEnum.DELETELAPSEFILE_FAILED.getMessageKey(), e);
			throw new BusinessException(BusinessExceptionEnum.DELETELAPSEFILE_FAILED);
		}
	}
	
	/**
	 * 
	 * 描述：删除全时录影失效事件数据
	 * @author 李帅
	 * @created 2018年3月22日 上午11:19:18
	 * @since 
	 * @param lapseEventId
	 * @return
	 */
	public void deleteLapseEvent(List<Long> lapseEventId){
		try {
			List<List<Long>> lapseEventIdListList = null;
			if (lapseEventId.size() > DeleteNum) {
				lapseEventIdListList = dealBySubLongList(lapseEventId, DeleteNum);
				for (List<Long> lapseEventIdStringList : lapseEventIdListList) {
					videoTimerService.deleteLapseEvent(lapseEventIdStringList);
				}
			} else {
				videoTimerService.deleteLapseEvent(lapseEventId);
			}
		} catch (Exception e) {
			logger.error(BusinessExceptionEnum.DELETELAPSEEVENT_FAILED.getMessageKey(), e);
			throw new BusinessException(BusinessExceptionEnum.DELETELAPSEEVENT_FAILED);
		}
	}

	
	/**
	 * 
	 * 描述：通过list的 subList(int fromIndex, int toIndex)方法实现
	 * 
	 * @author 李帅
	 * @created 2017年11月2日 上午10:44:35
	 * @since
	 * @param sourList
	 * @param batchCount
	 */
	public List<List<Long>> dealBySubLongList(List<Long> sourList, int batchCount) {
		int sourListSize = sourList.size();
		int subCount = sourListSize % batchCount == 0 ? sourListSize / batchCount : sourListSize / batchCount + 1;
		int startIndext = 0;
		int stopIndext = 0;
		int endIndext = sourListSize % batchCount == 0 ? batchCount : sourListSize % batchCount;
		List<List<Long>> tempListList = new ArrayList<List<Long>>();
		List<Long> tempList = null;
		for (int i = 0; i < subCount; i++) {
			stopIndext = (i == subCount - 1) ? stopIndext + endIndext : stopIndext + batchCount;
			tempList = new ArrayList<Long>(sourList.subList(startIndext, stopIndext));
			tempListList.add(tempList);
			startIndext = stopIndext;
		}
		return tempListList;
	}
	
	/**
	 * 
	 * 描述：获取计划信息
	 * @author 李帅
	 * @created 2018年3月22日 上午11:38:17
	 * @since 
	 * @param planQueryState
	 * @return
	 */
	public List<SchedulePlanDto> getSchedulePlanInfoList(String planQueryState){
		List<SchedulePlanDto> planInfo = new ArrayList<SchedulePlanDto>();
		try {
//			Map<String, String> paramMap = new HashMap<String, String>();
//			if ("0".equals(planQueryState)) {
//				paramMap.put("comedueState0", planQueryState);
//			} else if ("1".equals(planQueryState)) {
//				paramMap.put("comedueState1", planQueryState);
//			} else if ("2".equals(planQueryState)) {
//				paramMap.put("comedueState2", planQueryState);
//			} else if ("20".equals(planQueryState)) {
//				paramMap.put("comedueState21", planQueryState);
//			} else if ("21".equals(planQueryState)) {
//				paramMap.put("comedueState22", planQueryState);
//			} else if ("22".equals(planQueryState)) {
//				paramMap.put("comedueState23", planQueryState);
//			} else if ("23".equals(planQueryState)) {
//				paramMap.put("comedueState24", planQueryState);
//			}

			planInfo = videoTimerService.getSchedulePlanInfoList(planQueryState);
		} catch (Exception e) {
			logger.error(BusinessExceptionEnum.GETSCHEDULEPLANINFOLIST_FAILED.getMessageKey(), e);
			throw new BusinessException(BusinessExceptionEnum.GETSCHEDULEPLANINFOLIST_FAILED);
		}
		return planInfo;
	}
	
	/**
	 * 
	 * 描述：批量更新计划提醒状态
	 * @author 李帅
	 * @created 2018年3月22日 上午11:54:12
	 * @since 
	 * @param planIdList
	 * @param comedueRemindState
	 * @return
	 */
	public void batchUpdatePlanRemindState(List<String> planIdList, String comedueRemindState){
		try {
			List<List<String>> planIdListList = null;
			if (planIdList.size() > DeleteNum) {
				planIdListList = CommonUtil.dealBySubList(planIdList, DeleteNum);
				for (List<String> planIdStringList : planIdListList) {
					videoTimerService.batchUpdatePlanRemindState(planIdStringList, comedueRemindState);
				}
			} else {
				videoTimerService.batchUpdatePlanRemindState(planIdList, comedueRemindState);
			}
		} catch (Exception e) {
			logger.error(BusinessExceptionEnum.BATCHUPDATEPLANREMINDSTATE_FAILED.getMessageKey(), e);
			throw new BusinessException(BusinessExceptionEnum.BATCHUPDATEPLANREMINDSTATE_FAILED);
		}
	}
	
	/**
	 * 
	 * 描述：批量更新计划执行状态
	 * @author 李帅
	 * @created 2018年3月22日 下午1:44:25
	 * @since 
	 * @param planIdList
	 * @param planExecStatus
	 * @return
	 */
	public void batchUpdatePlanState(List<String> planIdList, String planExecStatus){
		try {
			List<List<String>> planIdListList = null;
			if (planIdList.size() > DeleteNum) {
				planIdListList = CommonUtil.dealBySubList(planIdList, DeleteNum);
				for (List<String> planIdStringList : planIdListList) {
					videoTimerService.batchUpdatePlanState(planIdStringList, planExecStatus);
					videoTimerService.batchUpdatePayRecordPlanState(planIdStringList, planExecStatus);
				}
			} else {
				videoTimerService.batchUpdatePlanState(planIdList, planExecStatus);
				videoTimerService.batchUpdatePayRecordPlanState(planIdList, planExecStatus);
			}
		} catch (Exception e) {
			logger.error(BusinessExceptionEnum.BATCHUPDATEPLANREMINDSTATE_FAILED.getMessageKey(), e);
			throw new BusinessException(BusinessExceptionEnum.BATCHUPDATEPLANREMINDSTATE_FAILED);
		}
	}
	
	/**
	 * 
	 * 描述：获取计划的失效文件路径
	 * @author 李帅
	 * @created 2018年3月22日 下午2:03:16
	 * @since 
	 * @param planId
	 * @return
	 */
	public List<String> findInvalidVideoFilePath(String planId){
		return videoFileMongoService.findInvalidVideoFilePath(planId);
	}
	
	/**
	 * 
	 * 描述：删除计划文件数据
	 * @author 李帅
	 * @created 2018年3月22日 下午2:25:49
	 * @since 
	 * @param planIdList
	 * @return
	 */
	/*public void deleteLapseFileByPlanId(List<String> planIdList){
		try {
			List<List<String>> planIdListList = null;
			if (planIdList.size() > DeleteNum) {
				planIdListList =  CommonUtil.dealBySubList(planIdList, DeleteNum);
				for (List<String> planIdStringList : planIdListList) {
					videoTimerService.deleteLapseFileByPlanId(planIdStringList);
				}
			} else {
				videoTimerService.deleteLapseFileByPlanId(planIdList);
			}
		} catch (Exception e) {
			logger.error(BusinessExceptionEnum.BATCHUPDATEPLANREMINDSTATE_FAILED.getMessageKey(), e);
			throw new BusinessException(BusinessExceptionEnum.BATCHUPDATEPLANREMINDSTATE_FAILED);
		}
	}*/
	
	/**
	 * 
	 * 描述：删除计划事件数据
	 * @author 李帅
	 * @created 2018年3月22日 下午2:28:38
	 * @since 
	 * @param planIdList
	 * @return
	 */
	public void deleteLapseEventByPlanId(List<String> planIdList){
		try {
			List<List<String>> planIdListList = null;
			if (planIdList.size() > DeleteNum) {
				planIdListList =  CommonUtil.dealBySubList(planIdList, DeleteNum);
				for (List<String> planIdStringList : planIdListList) {
					videoTimerService.deleteLapseEventByPlanId(planIdStringList);
				}
			} else {
				videoTimerService.deleteLapseEventByPlanId(planIdList);
			}
		} catch (Exception e) {
			logger.error(BusinessExceptionEnum.BATCHUPDATEPLANREMINDSTATE_FAILED.getMessageKey(), e);
			throw new BusinessException(BusinessExceptionEnum.BATCHUPDATEPLANREMINDSTATE_FAILED);
		}
	}
	
	/**
	 * 
	 * 描述：获取计划ID和计划套餐量
	 * @author 李帅
	 * @created 2018年3月22日 下午2:37:31
	 * @since 
	 * @return
	 */
	public List<VideoPlanBeyond> getAllVenderPlanList(){
		List<VideoPlanBeyond> videoPlans = null;
		try {
			// 查询所有客户
			videoPlans = videoTimerService.getAllVenderPlanList();
		} catch (Exception e) {
			logger.error(BusinessExceptionEnum.GET_ALLVENDER_PLANLIST_FAILED.getMessageKey(), e);
			throw new BusinessException(BusinessExceptionEnum.GET_ALLVENDER_PLANLIST_FAILED);
		}
		return videoPlans;
	}
	
	/**
	 * 
	 * 描述：排序获取前num条事件信息
	 * @author 李帅
	 * @created 2018年3月22日 下午2:55:00
	 * @since 
	 * @param planId
	 * @param batchValue
	 * @return
	 */
	public List<EventVo> getSortVideoEventInfo(String planId, int batchValue){
		List<EventVo> eventList = null;
		try {
			eventList = videoTimerService.getSortVideoEventInfo(planId, batchValue);
		} catch (Exception e) {
			logger.error(BusinessExceptionEnum.GET_SORT_VIDEO_EVENTINFO_FAILED.getMessageKey(), e);
			throw new BusinessException(BusinessExceptionEnum.GET_SORT_VIDEO_EVENTINFO_FAILED);
		}
		return eventList;
	}
	
	/**
	 * 
	 * 描述：获取事件关联的ts文件信息
	 * @author 李帅
	 * @created 2018年3月22日 下午3:01:51
	 * @since 
	 * @param planId
	 * @param eventOddurTime
	 * @return
	 */
	public List<String> getEventTsFileInfo(String planId, Date eventOddurTime){
		List<String> videoFileList = null;
		try {
			videoFileList = videoTimerService.getEventTsFileInfo(planId, eventOddurTime);
		} catch (Exception e) {
			logger.error(BusinessExceptionEnum.GET_EVENT_TS_FILEINFO_FAILED.getMessageKey(), e);
			throw new BusinessException(BusinessExceptionEnum.GET_EVENT_TS_FILEINFO_FAILED);
		}
		return videoFileList;
	}
	
	/**
	 * 
	 * 描述：获取解绑任务信息
	 * @author 李帅
	 * @created 2018年5月24日 上午11:52:29
	 * @since 
	 * @return
	 */
	public Map<String,String> getUntieTaskInfo() {
		Map<String,String> planIdMap = null;
		try {
			//查询待删除无效数据的任务队列
			List<String> taskUuidList = RedisCacheUtil.listGetAll(ModuleConstants.IPC_UNBANDING_TASK_KEY_QUEUE);
			Iterator it = taskUuidList.iterator();
			planIdMap = new HashMap<>();
			UntieTaskVo untieTaskVo = null;
			while (it.hasNext()) {
				String key = ModuleConstants.IPC_UNBANDING_TASK_KEY + it.next();
				untieTaskVo = RedisCacheUtil.valueObjGet(key, UntieTaskVo.class);
				if(null != untieTaskVo && untieTaskVo.getStatus() == TaskStatusEnum.NOT_STARTED.getCode()) {
					untieTaskVo.setStatus(TaskStatusEnum.STARTED.getCode());
					untieTaskVo.setExecTime(new Date());
					RedisCacheUtil.valueObjSet(key, untieTaskVo);
					planIdMap.put(key, untieTaskVo.getPlanId());
				}else if(null != untieTaskVo && untieTaskVo.getStatus() == TaskStatusEnum.STARTED.getCode()){
					if(!this.isComplete(key,untieTaskVo.getPlanId())){
						String nowTime = CalendarUtil.getNowTime(CalendarUtil.YYYYMMDDHHMMSS);
						SimpleDateFormat formatter = new SimpleDateFormat(CalendarUtil.YYYYMMDDHHMMSS);
						String dateString = formatter.format(untieTaskVo.getExecTime());
						long hours = CalendarUtil.getDistanceHours(dateString, nowTime);
						if(hours > unbindTaskTimeout) {
							untieTaskVo.setExecTime(new Date());
							untieTaskVo.setRetry(untieTaskVo.getRetry()==null ? 1:  untieTaskVo.getRetry() + 1);
							RedisCacheUtil.valueObjSet(key, untieTaskVo);
							planIdMap.put(key,untieTaskVo.getPlanId());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(BusinessExceptionEnum.GET_UNTIE_TASK_INFO_FAILED.getMessageKey(), e);
			throw new BusinessException(BusinessExceptionEnum.GET_UNTIE_TASK_INFO_FAILED);
		}
		return planIdMap;
	}

	/**
	 * 描述：判断任务是否完成
	 * @author mao2080@sina.com
	 * @created 2018/6/6 11:04
	 * @param taskKey 任务key
	 * @param planId 计划uuid
	 * @return boolean
	 */
	private boolean isComplete(String taskKey,String planId){
		String uuidKey = taskKey.replace(ModuleConstants.IPC_UNBANDING_TASK_KEY, "");
		String fileTaskKey = ModuleConstants.IPC_FILE_DELETE_TASK_KEY.concat(uuidKey);
		String eventTaskKey = ModuleConstants.IPC_EVENT_DELETE_TASK_KEY.concat(uuidKey);
		String videoFileTaskKey = ModuleConstants.IPC_VIDEO_FILE_DELETE_TASK_KEY.concat(uuidKey);
		boolean addEventTaskKey = false,addVideoFileTaskKey = false; //插入redis标志
		if (StringUtil.isBlank(eventTaskKey)){
			//补偿机制，直接查看数据库是否有垃圾数据
			int count = videoTimerService.countInvalidVideoEvent(planId);
			if (count>0){
				RedisCacheUtil.valueSet(eventTaskKey,ModuleConstants.UN_COMPLETED);
				addEventTaskKey = true;
			}
		}else{
			if(!ModuleConstants.COMPLETED.equals(RedisCacheUtil.valueGet(eventTaskKey))){
				return false;
			}
		}
		if (StringUtil.isBlank(videoFileTaskKey)){
			//补偿机制，为空时直接查看数据库是否有垃圾数据
			int count = videoTimerService.countInvalidVideoFile(planId);
			if (count>0){
				RedisCacheUtil.valueSet(videoFileTaskKey,ModuleConstants.UN_COMPLETED);
				addVideoFileTaskKey = true;
			}
		}else {
			if(!ModuleConstants.COMPLETED.equals(RedisCacheUtil.valueGet(videoFileTaskKey))){
				return false;
			}
		}
		if (addEventTaskKey || addVideoFileTaskKey){
			return false;
		}
		if(RedisCacheUtil.hasKey(fileTaskKey)){
			return false;
		}
		RedisCacheUtil.delete(eventTaskKey);
		RedisCacheUtil.delete(videoFileTaskKey);
		RedisCacheUtil.delete(taskKey);
		RedisCacheUtil.listRemove(ModuleConstants.IPC_UNBANDING_TASK_KEY_QUEUE, uuidKey, 1);
		return true;
	}
	
	/**
	 * 
	 * 描述：删除解绑任务信息
	 * @author 李帅
	 * @created 2018年5月30日 下午6:53:39
	 * @since 
	 * @param redisKey
	 */
	public void deleteRedisObject(String redisKey) {
		RedisCacheUtil.delete(redisKey);
	}


	/**
	 * @despriction：批量处理过期计划
	 * @author  yeshiyuan
	 * @created 2018/6/14 15:04
	 * @param planIds 计划集合
	 * @param opType 操作类型(1:计划过期5天之内；2：计划过期5天之后)
	 * @return
	 */
	public int bacthDealPlanExpire(List<String> planIds,String opType) {
		if (planIds==null || planIds.isEmpty()){
			return 0;
		}
		int i = 0;
		//操作类型(1:计划过期5天之内；2：计划过期5天之后)
		if ("1".equals(opType)){
			for (String planId : planIds) {
				//redis标志计划过期，防止ipc还继续上报事件、上报文件
				RedisCacheUtil.hashPut(ModuleConstants.VIDEO_PLAN_INFO+planId,ModuleConstants.VIDEO_PLAN_INFO_KEY_EEPIREFLAG, PlanExpireFlagEnum.expire.getCode(),false);
			}
		}
		List<List<String>> planIdListList = new ArrayList<>();
		if (planIds.size() > DeleteNum) {
			planIdListList = CommonUtil.dealBySubList(planIds, DeleteNum);
		} else {
			planIdListList.add(planIds);
		}
		for (List<String> planIdList:planIdListList) {
			//操作类型(1:计划过期5天之内；2：计划过期5天之后)
			if ("1".equals(opType)){
				int count = videoTimerService.bacthDealPlanExpireWithInFiveDay(planIdList);
				i = i+count;
			}else{
				int count = videoTimerService.bacthDealPlanExpireAfterFiveDay(planIdList);
				i = i+count;
			}
		}
		return i;
	}

	/**
	 * @despriction：删除失效视频事件数据
	 * @author  yeshiyuan
	 * @created 2018/8/7 11:28
	 * @param planId 计划id
	 * @return
	 */
	public int deleteInvalidVideoEventData(String planId) {
		return videoEventMongoService.deleteInvalidVideoEventData(planId);
	}

	/**
	  * @despriction：删除失效视频文件数据
	  * @author  yeshiyuan
	  * @created 2018/8/15 11:48
	  * @param planId 计划id
	  * @return
	  */
	public int deleteInvalidVideoFileData(String planId) {
		return videoFileMongoService.deleteInvalidDataByPlanId(planId);
	}
}

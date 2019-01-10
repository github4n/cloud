package com.iot.video.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.video.dao.VideoTimerMapper;
import com.iot.video.dto.SchedulePlanDto;
import com.iot.video.entity.VideoPlanBeyond;
import com.iot.video.service.VideoTimerService;
import com.iot.video.vo.EventVo;

@Service("videoTimerService")
public class VideoTimerServiceImpl implements VideoTimerService {

    @SuppressWarnings("unused")
	private static final Logger logger  = LoggerFactory.getLogger(VideoTimerServiceImpl.class);

    @Autowired
    private VideoTimerMapper videoTimerMapper;

    /**
     * 
     * 描述：获取过期的计划ID和套餐天数
     * @author 李帅
     * @created 2018年3月22日 上午11:00:39
     * @since 
     * @return
     */
    @Override
    public List<Map<String, String>> getLapsePlanIdList(){
    	List<Map<String, String>> planToEventOrFulltimeAmountList = new ArrayList<Map<String, String>>();
        planToEventOrFulltimeAmountList = videoTimerMapper.getLapsePlanIdList();
        return planToEventOrFulltimeAmountList;
    }
    
    /**
     * 
     * 描述：获取计划录影日期
     * @author 李帅
     * @created 2018年3月22日 上午11:01:20
     * @since 
     * @param planIdKey
     * @return
     */
    @Override
    public List<String> getPlanVideoDates(String planIdKey){
    	List<String> planVideoDateList = new ArrayList<String>();
       	planVideoDateList = videoTimerMapper.getPlanVideoDates(planIdKey);
        return planVideoDateList;
    }
    
    /**
     * 
     * 描述：获取全时录影失效文件ID
     * @author 李帅
     * @created 2018年3月22日 上午11:01:55
     * @since 
     * @param planIdKey
     * @param dueTime
     * @return
     */
    @Override
    public List<String> getLapseFileIdList(String planIdKey, String dueTime){
    	List<String> lapseFileId = new ArrayList<String>();
       	lapseFileId = videoTimerMapper.getLapseFileIdList(planIdKey, dueTime);
        return lapseFileId;
    }
    
    /**
     * 
     * 描述：获取全时录影失效事件ID
     * @author 李帅
     * @created 2018年3月22日 上午11:02:25
     * @since 
     * @param planIdKey
     * @param dueTime
     * @return
     */
    @Override
    public List<Long> getLapseEventIdList(String planIdKey, String dueTime){
    	List<Long> lapseEventId = new ArrayList<Long>();
       	lapseEventId = videoTimerMapper.getLapseEventIdList(planIdKey, dueTime);
        return lapseEventId;
    }
    
    /**
     * 
     * 描述：删除全时录影失效文件数据
     * @author 李帅
     * @created 2018年3月22日 上午11:17:38
     * @since 
     * @param lapseFileId
     */
    @Override
    public void deleteLapseFile(List<String> lapseFileId) {
		videoTimerMapper.deleteLapseFile(lapseFileId);
    }
    
    /**
     * 
     * 描述：删除全时录影失效事件数据
     * @author 李帅
     * @created 2018年3月22日 上午11:19:37
     * @since 
     * @param lapseEventId
     */
    @Override
    public void deleteLapseEvent(List<Long> lapseEventId) {
		videoTimerMapper.deleteLapseEvent(lapseEventId);
    }
    
    /**
     * 
     * 描述：获取计划信息
     * @author 李帅
     * @created 2018年3月23日 下午3:11:50
     * @since 
     * @param planQueryState
     * @return
     */
    @Override
    public List<SchedulePlanDto> getSchedulePlanInfoList(String planQueryState) {
    	List<SchedulePlanDto> planInfo = new ArrayList<SchedulePlanDto>();
   		planInfo = videoTimerMapper.getSchedulePlanInfoList(planQueryState);
    	return planInfo;
    }
    
    /**
     * 
     * 描述：批量更新计划提醒状态
     * @author 李帅
     * @created 2018年3月22日 上午11:54:28
     * @since 
     * @param planIdList
     * @param comedueRemindState
     */
    @Override
    public void batchUpdatePlanRemindState(List<String> planIdList, String comedueRemindState){
		videoTimerMapper.batchUpdatePlanRemindState(planIdList, comedueRemindState);
    }
    
    /**
     * 
     * 描述：批量更新计划执行状态
     * @author 李帅
     * @created 2018年3月22日 下午1:44:42
     * @since 
     * @param planIdList
     * @param planExecStatus
     */
    @Override
    public void batchUpdatePlanState(List<String> planIdList, String planExecStatus){
		videoTimerMapper.batchUpdatePlanState(planIdList, planExecStatus);
    }
    
    /**
     * 
     * 描述：批量更新订单计划状态
     * @author 李帅
     * @created 2018年3月23日 下午3:25:35
     * @since 
     * @param planIdList
     * @param planExecStatus
     */
    @Override
    public void batchUpdatePayRecordPlanState(List<String> planIdList, String planExecStatus){
		videoTimerMapper.batchUpdatePayRecordPlanState(planIdList, planExecStatus);
    }

    /**
     * 
     * 描述：删除计划事件数据
     * @author 李帅
     * @created 2018年3月22日 下午2:29:26
     * @since 
     * @param planIdList
     */
    @Override
    public void deleteLapseEventByPlanId(List<String> planIdList){
		videoTimerMapper.deleteLapseEventByPlanId(planIdList);
    }
    
    /**
     * 
     * 描述：获取计划ID和计划套餐量
     * @author 李帅
     * @created 2018年3月22日 下午2:37:46
     * @since 
     * @return
     */
    @Override
    public List<VideoPlanBeyond> getAllVenderPlanList(){
    	List<VideoPlanBeyond> videoPlans = new ArrayList<VideoPlanBeyond>();
       	videoPlans = videoTimerMapper.getAllVenderPlanList();
        return videoPlans;
    }
    
    /**
     * 
     * 描述：排序获取前num条事件信息
     * @author 李帅
     * @created 2018年3月22日 下午2:55:50
     * @since 
     * @param planId
     * @param batchValue
     * @return
     */
    @Override
    public List<EventVo> getSortVideoEventInfo(String planId, int batchValue){
    	List<EventVo> eventList = null;
       	eventList = videoTimerMapper.getSortVideoEventInfo(planId, batchValue);
        return eventList;
    }
    
    /**
     * 
     * 描述：获取事件关联的ts文件信息
     * @author 李帅
     * @created 2018年3月22日 下午3:02:06
     * @since 
     * @param planId
     * @param eventOddurTime
     * @return
     */
    @Override
    public List<String> getEventTsFileInfo(String planId, Date eventOddurTime){
    	List<String> videoFileList = null;
       	videoFileList = videoTimerMapper.getEventTsFileInfo(planId, eventOddurTime);
        return videoFileList;
    }

    /**
     * @despriction：批量处理计划过期5天之内的数据
     * @author  yeshiyuan
     * @created 2018/6/14 15:04
     * @param planIds 计划集合
     * @return
     */
    @Override
    public int bacthDealPlanExpireWithInFiveDay(List<String> planIds) {
        return videoTimerMapper.bacthDealPlanExpireWithInFiveDay(planIds);
    }

    /**
     * @despriction：批量处理计划过期5天之后的数据
     * @author  yeshiyuan
     * @created 2018/6/14 15:04
     * @param planIds 计划集合
     * @return
     */
    @Override
    public int bacthDealPlanExpireAfterFiveDay(List<String> planIdList) {
        return videoTimerMapper.bacthDealPlanExpireAfterFiveDay(planIdList);
    }

    /**
     * @despriction：统计失效文件数据
     * @author  yeshiyuan
     * @created 2018/6/26 15:26
     * @param planId 计划uuid
     * @return
     */
    @Override
    public int countInvalidVideoFile(String planId) {
        return videoTimerMapper.countInvalidVideoFile(planId);
    }

    /**
     * @despriction：统计失效事件数据
     * @author  yeshiyuan
     * @created 2018/6/26 15:26
     * @param planId 计划uuid
     * @return
     */
    @Override
    public int countInvalidVideoEvent(String planId) {
        return videoTimerMapper.countInvalidVideoEvent(planId);
    }
}

package com.iot.video.service;

import com.iot.video.dto.SchedulePlanDto;
import com.iot.video.entity.VideoPlanBeyond;
import com.iot.video.vo.EventVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人：
 * 创建时间：2018/3/19 10:56
 * 修改人：
 * 修改时间：2018/3/19 10:56
 * 修改描述：
 */
public interface VideoTimerService {

    /**
     * 
     * 描述：获取过期的计划ID和套餐天数
     * @author 李帅
     * @created 2018年3月22日 上午11:00:30
     * @since 
     * @return
     */
    List<Map<String, String>> getLapsePlanIdList();

    /**
     * 
     * 描述：获取计划录影日期
     * @author 李帅
     * @created 2018年3月22日 上午11:01:10
     * @since 
     * @param planIdKey
     * @return
     */
    List<String> getPlanVideoDates(String planIdKey);
    
    /**
     * 
     * 描述：获取全时录影失效文件ID
     * @author 李帅
     * @created 2018年3月22日 上午11:01:45
     * @since 
     * @param planIdKey
     * @param dueTime
     * @return
     */
    List<String> getLapseFileIdList(String planIdKey, String dueTime);
    
    /**
     * 
     * 描述：获取全时录影失效事件ID
     * @author 李帅
     * @created 2018年3月22日 上午11:02:16
     * @since 
     * @param planIdKey
     * @param dueTime
     * @return
     */
    List<Long> getLapseEventIdList(String planIdKey, String dueTime);
    
    /**
     * 
     * 描述：删除全时录影失效文件数据
     * @author 李帅
     * @created 2018年3月22日 上午11:17:28
     * @since 
     * @param lapseFileId
     */
    void deleteLapseFile(List<String> lapseFileId);
    
    /**
     * 
     * 描述：删除全时录影失效事件数据
     * @author 李帅
     * @created 2018年3月22日 上午11:19:29
     * @since 
     * @param lapseEventId
     */
    void deleteLapseEvent(List<Long> lapseEventId);
    
    /**
     * 
     * 描述：获取计划信息
     * @author 李帅
     * @created 2018年3月23日 下午3:11:42
     * @since 
     * @param planQueryState
     * @return
     */
    List<SchedulePlanDto> getSchedulePlanInfoList(String planQueryState);
    
    /**
     * 
     * 描述：批量更新计划提醒状态
     * @author 李帅
     * @created 2018年3月22日 上午11:54:19
     * @since 
     * @param planIdList
     * @param comedueRemindState
     */
    void batchUpdatePlanRemindState(List<String> planIdList, String comedueRemindState);
    
    /**
     * 
     * 描述：批量更新计划执行状态
     * @author 李帅
     * @created 2018年3月22日 下午1:44:34
     * @since 
     * @param planIdList
     * @param planExecStatus
     */
    void batchUpdatePlanState(List<String> planIdList, String planExecStatus);
    
    /**
     * 
     * 描述：批量更新订单计划状态
     * @author 李帅
     * @created 2018年3月22日 下午1:44:34
     * @since 
     * @param planIdList
     * @param planExecStatus
     */
    void batchUpdatePayRecordPlanState(List<String> planIdList, String planExecStatus);
    
    /**
     * 
     * 描述：删除计划事件数据
     * @author 李帅
     * @created 2018年3月22日 下午2:29:18
     * @since 
     * @param planIdList
     */
    void deleteLapseEventByPlanId(List<String> planIdList);
    
    /**
     * 
     * 描述：获取所有厂商的所有计划列表
     * @author 李帅
     * @created 2018年3月22日 下午2:37:39
     * @since 
     * @return
     */
    List<VideoPlanBeyond> getAllVenderPlanList();
    
    /**
     * 
     * 描述：排序获取前num条事件信息
     * @author 李帅
     * @created 2018年3月22日 下午2:55:42
     * @since 
     * @param planId
     * @param batchValue
     * @return
     */
    List<EventVo> getSortVideoEventInfo(String planId, int batchValue);
    
    /**
     * 
     * 描述：获取事件关联的ts文件信息
     * @author 李帅
     * @created 2018年3月22日 下午3:01:58
     * @since 
     * @param planId
     * @param eventOddurTime
     * @return
     */
    List<String> getEventTsFileInfo(String planId, Date eventOddurTime);

    /**
     * @despriction：批量处理计划过期5天之内的数据
     * @author  yeshiyuan
     * @created 2018/6/14 15:04
     * @param planIds 计划集合
     * @return
     */
    int bacthDealPlanExpireWithInFiveDay(List<String> planIds) ;

    /**
     * @despriction：批量处理计划过期5天之后的数据
     * @author  yeshiyuan
     * @created 2018/6/14 15:04
     * @param planIds 计划集合
     * @return
     */
    int bacthDealPlanExpireAfterFiveDay(List<String> planIdList);

    /**
     * @despriction：统计失效文件数据
     * @author  yeshiyuan
     * @created 2018/6/26 15:26
     * @param planId 计划uuid
     * @return
     */
    int countInvalidVideoFile(String planId);

    /**
     * @despriction：统计失效事件数据
     * @author  yeshiyuan
     * @created 2018/6/26 15:26
     * @param planId 计划uuid
     * @return
     */
    int countInvalidVideoEvent(String planId);
}

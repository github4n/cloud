package com.iot.video.service;

import com.github.pagehelper.Page;
import com.iot.video.dto.*;
import com.iot.video.entity.VideoFile;
import com.iot.video.entity.VideoPayRecord;
import com.iot.video.entity.VideoPlan;
import com.iot.video.entity.VideoTask;
import com.iot.video.vo.*;

import java.util.List;

/**
 *
 * 项目名称：IOT云平台 模块名称： 功能描述： 创建人： 创建时间：2018/3/19 10:56 修改人： 修改时间：2018/3/19
 * 10:56 修改描述：
 */
public interface VideoManageService {


	/**
	 * 
	 * 描述：更新录影计划状态
	 * @author wujianlong
	 * @created 2018年3月22日 上午9:34:28
	 * @since 
	 * @param tenantId
	 * @param userId
	 * @param planId
	 * @param planExecStatus
	 * @return
	 */
	void updatePlanExecStatus(Long tenantId, String userId, String planId, Integer planExecStatus);
	
	List<VideoTaskVo> getVideoTaskList(Long tenantId, String userId, String planId);

	void updatePlanCycle( Long tenantId, String userId, String planId,  String planCycle, int planStatus);
	
	void deleteTaskByPlanId(Long tenantId, String userId, String planId);
	
	void createVideoTask(VideoTask videoTask);
	
	Page<VideoPlanVo> getVideoPlanList( long tenantId, String userId);

	List<PlanTaskDto> getDayIndexByPlanId(List<String> planIds);

	List<PlanEventDto> getEventUseQuantityByPlanId(List<String> planIds);

	PlanTaskDto getDayIndexByPlanIdExec(String planId);

	PlanEventDto getEventUseQuantityByPlanIdExec(String planId);

	VideoPlanInfoDto getPlanInfoByDevId(long tenantId, String userId, String deviceId);
	
	Page<VideoPackageVo> getVideoPackageList(String deviceType);

	void createPlan(VideoPlan plan);

	void updatePlanName(long tenantId, String userId, String planId, String planName);

	Page<PayRecordVo> getBuyHisRecordList(Long tenantId, String userId, String planId, String orderId);

	Page<PayRecordVo> getBuyRecordList(Long tenantId, String userId);

	List<AllRecordVo> getAllBuyRecordList(AllRecordSearchParam searchParam);
	
	AppPayDto getPackagePriceById(Long packageId);

	int renewPlan(VideoPlan plan);

	VideoPlanVo getPlanEndTime(Long tenantId, String userId, String planId);

	void insertPayRecord(PayRecordVo recordVo, long tenantId, String userId);

	int getPlanLeftDays(String tenantId, String userId, String planId);

	void planBandingDevice(Long tenantId, String userId, String planId, String deviceId);

	/*void deleteVideoEvent(Long tenantId, String userId, String planId, Long eventId);*/
	

	
	String getDeviceId(String planId);

	String getPlanId(String deviceId);

	List<VideoPlanTaskDto> getSyncTaskInfo(String planId, String nowTime);

	void createVideoFile(VideoFile videoFile);

	Long getTenantId(String planId);



	int countDeviceBandingPlan(String deviceId);

	List<String> selectBandedPlanDeviceIds(List<String> deviceIdList);

	int countPlanById(String planId);




	
	/**
	 * 
	 * 描述：获取视频事件相关联的ts和jpg文件ID
	 * @author 李帅
	 * @created 2018年6月11日 上午11:25:37
	 * @since 
	 * @param tenantId
	 * @param userId
	 * @param planId
	 * @param eventUUID
	 * @return
	 */
	List<String> getEventFileId(Long tenantId, String userId, String planId, String eventUUID);
	


	/**
	  * @despriction：统计设备绑定的计划
	  * @author  yeshiyuan
	  * @created 2018/5/17 14:10
	  * @param deviceId 设备id
	  * @return
	  */
	int countDeviceHasBindPlan(String deviceId);
	/**
	 * 描述：获取最新的支付成功的订单
	 * @author 490485964@qq.com
	 * @date 2018/5/21 16:54
	 * @param tenantId
	 * @param planId
	 * @param planStatus
	 * @return
	 */
	VideoPayRecordDto getLatestOrderByPlanIdAndStatus(Long tenantId, String planId, Integer planStatus);
	/**
	 * 描述：根据日期查询视频开始时间小时
	 * @author 490485964@qq.com
	 * @date 2018/5/21 19:25
	 * @param
	 * @return
	 *//*List<Integer> getVideoStartTimeByDate(Date searchDate,String planId);*/


	/**
	 * @despriction：查询video_pay_record
	 * @author  yeshiyuan
	 * @created 2018/5/22 10:18
	 * @param planId 计划id
	 * @param orderId 订单id
	 * @param tenantId 租户id
	 * @return
	 */
	VideoPayRecord getVideoPayRecord(String planId, String orderId, Long tenantId);

	/**
	 * @despriction：视频购买记录失效
	 * @author  yeshiyuan
	 * @created 2018/5/22 14:11
	 * @return
	 */
	int updateVideoPayRecordOfPlanStatus(String planId,String orderId,Long tenantId,Integer planStatus);

	/**
	 * @despriction：获取计划某一订单的时间差
	 * @author  yeshiyuan
	 * @created 2018/5/22 14:46
	 * @return
	 */
	int getPayRecordDateDiff(String planId,String orderId,Long tenantId);

	/**
	 * @despriction：修改计划结束时间
	 * @author  yeshiyuan
	 * @created 2018/5/22 15:03
	 * @param planId 计划id
	 * @param dateDiff 时间差
	 * @return
	 */
	int updatePlanEndTime(String planId,Integer dateDiff);

	/**
	 * @despriction：查询计划的其他购买记录
	 * @author  yeshiyuanPlan
	 * @created 2018/5/22 13:47
	 * @param planId 计划id
	 * @param orderId 订单id
	 * @return
	 */
	List<VideoPlanOrderDto> queryPlanOtherPayRecord(String orderId, String planId);

	/**
	 * 描述：查询设备最后绑定的计划
	 * @author mao2080@sina.com
	 * @created 2018/5/24 9:57
	 * @param tenantId 租户id
	 * @param deviceId 设备id
	 * @return void
	 */
	String getBandingDevicePlanId(Long tenantId, String deviceId);

	/**
	 * 描述：计划解绑设备
	 * @author mao2080@sina.com
	 * @created 2018/5/24 9:57
	 * @param tenantId 租户id
	 * @param deviceId 设备id
	 * @return void
	 */
	int planUnBandingDevice(Long tenantId, String planId);







	/**
	 * 描述：查询套餐信息
	 * @author mao2080@sina.com
	 * @created 2018/6/13 10:05
	 * @param tenantId 租户id
	 * @param planId 计划id
	 * @return com.iot.video.dto.VideoPackageDto
	 */
	Long getPackageInfo(Long tenantId, String planId);





	/**
	  * @despriction：通过事件uuid批量修改video_file
	  * @author  yeshiyuan
	  * @created 2018/6/12 19:24
	  * @param eventUuids
	  * @return
	  *//*
	int batchUpdateVideoFileByEventUuid(List<String> eventUuids);*/

	/**
	 * @despriction：某一时间前的事件置为失效
	 * @author  yeshiyuan
	 * @created 2018/6/12 18:48
	 * @param tenantId 租户id
	 * @param planId 计划id
	 * @return
	 *//*
	int setVideoEventDataInvalid(Long tenantId, String planId, String eventTime);*/








}

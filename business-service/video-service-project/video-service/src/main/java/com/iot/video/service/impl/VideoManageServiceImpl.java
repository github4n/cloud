package com.iot.video.service.impl;

import com.github.pagehelper.Page;
import com.iot.common.util.CalendarUtil;
import com.iot.common.util.StringUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.video.contants.ModuleConstants;
import com.iot.video.dao.VideoManageMapper;
import com.iot.video.dto.*;
import com.iot.video.entity.VideoFile;
import com.iot.video.entity.VideoPayRecord;
import com.iot.video.entity.VideoPlan;
import com.iot.video.entity.VideoTask;
import com.iot.video.service.VideoManageService;
import com.iot.video.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * 项目名称：IOT云平台 模块名称： 功能描述： 创建人： 创建时间：2018/3/19 11:07 修改人： 修改时间：2018/3/19
 * 11:07 修改描述：
 */
@Service("videoManageService")
public class VideoManageServiceImpl implements VideoManageService {

//	private static final Logger logger = LoggerFactory.getLogger(VideoManageServiceImpl.class);

	@Autowired
	private VideoManageMapper videoManageMapper;


	@Override
	public void updatePlanExecStatus(Long tenantId, String userId, String planId, Integer planExecStatus) {
		videoManageMapper.updatePlanExecStatus(tenantId, userId, planId, planExecStatus);
	}

	@Override
	public List<VideoTaskVo> getVideoTaskList(Long tenantId, String userId, String planId) {
		return videoManageMapper.getVideoTaskList(tenantId, userId, planId);
	}

	@Override
	public void updatePlanCycle(Long tenantId, String userId, String planId, String planCycle, int planStatus) {
		videoManageMapper.updatePlanCycle(tenantId, userId, planId, planCycle, planStatus);
	}

	@Override
	public void deleteTaskByPlanId(Long tenantId, String userId, String planId) {
		videoManageMapper.deleteTaskByPlanId(tenantId, userId, planId);
	}

	@Override
	public void createVideoTask(VideoTask videoTask) {
		videoManageMapper.createVideoTask(videoTask);
	}

	@Override
	public Page<VideoPlanVo> getVideoPlanList(long tenantId, String userId) {
		Calendar cal = CalendarUtil.getBeforeOrAfterDay(-5,false);
		return videoManageMapper.getVideoPlanList(tenantId, userId,CalendarUtil.format(cal,CalendarUtil.YYYYMMDDHHMMSS));
	}

	@Override
	public List<PlanTaskDto> getDayIndexByPlanId(List<String> planIds) {
		return videoManageMapper.getDayIndexByPlanId(planIds);
	}

	@Override
	public List<PlanEventDto> getEventUseQuantityByPlanId(List<String> planIds) {
		return videoManageMapper.getEventUseQuantityByPlanId(planIds);
	}

	@Override
	public PlanTaskDto getDayIndexByPlanIdExec(String planId) {
		return videoManageMapper.getDayIndexByPlanIdExec(planId);
	}

	@Override
	public PlanEventDto getEventUseQuantityByPlanIdExec(String planId) {
		return videoManageMapper.getEventUseQuantityByPlanIdExec(planId);
	}

	@Override
	public VideoPlanInfoDto getPlanInfoByDevId(long tenantId, String userId, String deviceId) {
		Calendar cal = CalendarUtil.getBeforeOrAfterDay(-5,false);
		return videoManageMapper.getPlanInfoByDevId(tenantId,userId,deviceId,CalendarUtil.format(cal,CalendarUtil.YYYYMMDDHHMMSS));
	}

	@Override
	public Page<VideoPackageVo> getVideoPackageList(String deviceType) {
		return videoManageMapper.getVideoPackageList(deviceType);
	}

	@Override
	public void createPlan(VideoPlan plan) {
		this.videoManageMapper.createPlan(plan);
	}

	@Override
	public void updatePlanName(long tenantId, String userId, String planId, String planName) {
		this.videoManageMapper.updatePlanName(tenantId, userId, planId, planName);
	}

	@Override
	public Page<PayRecordVo> getBuyHisRecordList(Long tenantId, String userId, String planId, String orderId){
		return this.videoManageMapper.getBuyHisRecordList(tenantId, userId, planId, orderId);
	}

	@Override
	public Page<PayRecordVo> getBuyRecordList(Long tenantId, String userId){
		return this.videoManageMapper.getBuyRecordList(tenantId, userId);
	}

	@Override
	public AppPayDto getPackagePriceById(Long packageId) {
		return this.videoManageMapper.getPackagePriceById(packageId);
	}

	@Override
	public int renewPlan(VideoPlan plan) {
		return this.videoManageMapper.renewPlan(plan);
	}

	@Override
	public VideoPlanVo getPlanEndTime(Long tenantId, String userId, String planId){
		return this.videoManageMapper.getPlanEndTime(tenantId, userId, planId);
	}

	@Override
	public void insertPayRecord(PayRecordVo recordVo, long tenantId, String userId) {
		this.videoManageMapper.insertPayRecord(recordVo, tenantId, userId);
	}

	@Override
	public int getPlanLeftDays(String tenantId, String userId, String planId){
		return this.videoManageMapper.getPlanLeftDays(tenantId, userId, planId);
	}

	@Override
	public void planBandingDevice(Long tenantId, String userId, String planId, String deviceId) {
		this.videoManageMapper.planBandingDevice(tenantId, userId, planId, deviceId);
	}

	/*@Override
	public void deleteVideoEvent(Long tenantId, String userId, String planId, Long eventId) {
		this.videoManageMapper.deleteVideoEvent(tenantId, userId, planId, eventId);
	}*/
	
	/*@Override
	public void deleteVideoEventFile(Long tenantId, String userId, String planId, String eventUUID) {
		this.videoManageMapper.deleteVideoEventFile(tenantId, userId, planId, eventUUID);
	}*/
	
	@Override
	public String getDeviceId(String planId) {
		String deviceId = RedisCacheUtil.hashGetString(ModuleConstants.VIDEO_PLAN_INFO + planId,ModuleConstants.VIDEO_PLAN_INFO_KEY_DEVICEID);
		if (StringUtil.isBlank(deviceId)){
			deviceId = this.videoManageMapper.getDeviceId(planId);
			RedisCacheUtil.hashPut(ModuleConstants.VIDEO_PLAN_INFO + planId, ModuleConstants.VIDEO_PLAN_INFO_KEY_DEVICEID,deviceId, false);
		}
		return deviceId;
	}

	@Override
	public String getPlanId(String deviceId) {
		return this.videoManageMapper.getPlanId(deviceId);
	}

	@Override
	public List<VideoPlanTaskDto> getSyncTaskInfo(String planId, String nowTime) {
		return this.videoManageMapper.getSyncTaskInfo(planId,nowTime);
	}

	@Override
	public void createVideoFile(VideoFile videoFile) {
		this.videoManageMapper.createVideoFile(videoFile);
	}

	@Override
	public Long getTenantId(String planId) {
		String tenantIdStr = RedisCacheUtil.hashGet(ModuleConstants.VIDEO_PLAN_INFO + planId,ModuleConstants.VIDEO_PLAN_INFO_KEY_TENANTID,String.class);
		Long tenantId = null;
		if (StringUtil.isBlank(tenantIdStr)){
			tenantId = this.videoManageMapper.getTenantId(planId);
		}else{
			tenantId = Long.valueOf(tenantIdStr);
			RedisCacheUtil.hashPut(ModuleConstants.VIDEO_PLAN_INFO + planId,ModuleConstants.VIDEO_PLAN_INFO_KEY_TENANTID,tenantId,false);
		}
		return tenantId;
	}



	@Override
	public int countDeviceBandingPlan(String deviceId) {
		return this.videoManageMapper.countDeviceBandingPlan(deviceId);
	}

	@Override
	public List<String> selectBandedPlanDeviceIds(List<String> deviceIdList) {
		return this.videoManageMapper.selectBandedPlanDeviceIds(deviceIdList);
	}

	@Override
	public int countPlanById(String planId) {
		return this.videoManageMapper.countPlanById(planId);
	}


	/*@Override
	public VideoEvent findByPlanIdAndEventId(Long tenantId, String userId, String planId, Long eventId) {
		return this.videoManageMapper.findByPlanIdAndEventId(tenantId, planId,eventId);
	}*/
	
	@Override
	public List<String> getEventFileId(Long tenantId, String userId, String planId, String eventUUID) {
		return this.videoManageMapper.getEventFileId(tenantId, planId, eventUUID);
	}
	


	@Override
	public int countDeviceHasBindPlan(String deviceId) {
		return videoManageMapper.countDeviceHasBindPlan(deviceId);
	}

	@Override
	public VideoPayRecordDto getLatestOrderByPlanIdAndStatus(Long tenantId, String planId, Integer planStatus) {
		return videoManageMapper.getLatestOrderByPlanIdAndStatus(tenantId,planId,planStatus);
	}

	/*@Override
	public List<Integer> getVideoStartTimeByDate(Date searchDate,String planId) {
		return videoManageMapper.getVideoStartTimeByDate(searchDate,planId);
	}*/

	@Override
	public VideoPayRecord getVideoPayRecord(String planId, String orderId, Long tenantId) {
		return videoManageMapper.getVideoPayRecord(planId, orderId, tenantId);
	}

	@Override
	public int updateVideoPayRecordOfPlanStatus(String planId, String orderId, Long tenantId, Integer planStatus) {
		return videoManageMapper.updateVideoPayRecordOfPlanStatus(planId, orderId, tenantId, planStatus);
	}

	@Override
	public int getPayRecordDateDiff(String planId, String orderId, Long tenantId) {
		return videoManageMapper.getPayRecordDateDiff(planId,orderId,tenantId);
	}

	@Override
	public int updatePlanEndTime(String planId, Integer dateDiff) {
		return videoManageMapper.updatePlanEndTime(planId,dateDiff);
	}
	
	@Override
	public List<AllRecordVo> getAllBuyRecordList(AllRecordSearchParam searchParam){
		return this.videoManageMapper.getAllBuyRecordList(searchParam);
	}

	@Override
	public List<VideoPlanOrderDto> queryPlanOtherPayRecord(String orderId, String planId) {
		return this.videoManageMapper.queryPlanOtherPayRecord(orderId,planId,new Date());
	}

	/**
	 * 描述：查询设备最后绑定的计划
	 * @author mao2080@sina.com
	 * @created 2018/5/24 9:57
	 * @param tenantId 租户id
	 * @param deviceId 设备id
	 * @return void
	 */
	@Override
	public String getBandingDevicePlanId(Long tenantId, String deviceId){
		return this.videoManageMapper.getBandingDevicePlanId(tenantId, deviceId);
	}

	/**
	 * 描述：计划解绑设备
	 * @author mao2080@sina.com
	 * @created 2018/5/24 9:57
	 * @param tenantId 租户id
	 * @param deviceId 设备id
	 * @return void
	 */
	@Override
	public int planUnBandingDevice(Long tenantId, String planId){
		return this.videoManageMapper.planUnBandingDevice(tenantId, planId);
	}






	/**
	 * 描述：查询套餐信息
	 * @author mao2080@sina.com
	 * @created 2018/6/13 10:05
	 * @param tenantId 租户id
	 * @param planId 计划id
	 * @return com.iot.video.dto.VideoPackageDto
	 */
	@Override
	public Long getPackageInfo(Long tenantId, String planId){
		return this.videoManageMapper.getPackageId(tenantId, planId);
	}





	/**
	 * @despriction：通过事件uuid批量修改video_file
	 * @author  yeshiyuan
	 * @created 2018/6/12 19:24
	 * @param null
	 * @return
	 */
	/*@Override
	public int batchUpdateVideoFileByEventUuid(List<String> eventUuids) {
		return this.videoManageMapper.batchUpdateVideoFileDataStatus(eventUuids);
	}*/

	/**
	 * @despriction：某一时间前的事件置为失效
	 * @author  yeshiyuan
	 * @created 2018/6/12 18:48
	 * @param tenantId 租户id
	 * @param planId 计划id
	 * @return
	 *//*
	@Override
	public int setVideoEventDataInvalid(Long tenantId, String planId, String eventTime) {
		return this.videoManageMapper.setVideoEventDataInvalidByTime(tenantId, planId, eventTime);
	}*/


	/**
	 * @despriction：通过事件id获取事件对应的视频文件
	 * @author  yeshiyuan
	 * @created 2018/6/22 16:44
	 * @param null
	 * @return
	 */
	/*@Override
	public List<VideoTsFileDto> getVideoFileListByEventUuid(String planId, String eventUuid,String fileType) {
		return this.videoManageMapper.getVideoFileListByEventUuid(planId, eventUuid,fileType);
	}*/



	/**
	 * @despriction：事件录影日期统计
	 * @author  yeshiyuan
	 * @created 2018/7/26 11:38
	 * @return
	 */
	/*@Override
	public List<String> selectHasEventVideoDay(String planId, Date startDate, Date endDate) {
		return this.videoManageMapper.selectHasEventVideoDay(planId, startDate, endDate);
	}*/
}

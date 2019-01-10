package com.iot.video.restful;

import com.github.pagehelper.PageInfo;
import com.iot.common.exception.BusinessException;
import com.iot.video.api.VideoManageApi;
import com.iot.video.dto.*;
import com.iot.video.entity.VideoEvent;
import com.iot.video.entity.VideoFile;
import com.iot.video.entity.VideoPayRecord;
import com.iot.video.exception.BusinessExceptionEnum;
import com.iot.video.manager.VideoManageServiceManager;
import com.iot.video.vo.req.CountVideoDateReq;
import com.iot.video.vo.req.GetEventVideoFileReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 
 * 项目名称：IOT云平台 模块名称： 功能描述： 创建人： 创建时间：2018/3/19 10:47 修改人： 修改时间：2018/3/19
 * 10:47 修改描述：
 */
@RestController
public class VideoManageRestful implements VideoManageApi {

	@Autowired
    private VideoManageServiceManager videoManageServiceManager;

	@Override
	public PageInfo<VideoPlanDto> getPlanList(@RequestBody PlanSearchParam planSearchParam) {
		return videoManageServiceManager.getPlanList(planSearchParam);
	}

	@Override
	public VideoPlanInfoDto getPlanInfoByDevId(@RequestBody PlanSearchParam planSearchParam) {
		return videoManageServiceManager.getPlanInfoByDevId(planSearchParam);
	}

	public void updatePlanExecStatus(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") String userId, @RequestParam("planId") String planId, 
			@RequestParam("planExecStatus") Integer planExecStatus) {
		videoManageServiceManager.updatePlanExecStatus(tenantId, userId, planId, planExecStatus);
	}

	public void updatePlanName(@RequestBody PlanNameParam planNameParam) {
		videoManageServiceManager.updatePlanName(planNameParam.getTenantId(), planNameParam.getUserId(), planNameParam.getPlanId(), planNameParam.getPlanName());
	}

	public List<VideoTaskDto> getVideoTaskList(@RequestParam("tenantId") Long tenantId,@RequestParam("userId") String userId,
			@RequestParam("planId") String planId) {
		return videoManageServiceManager.getVideoTaskList(tenantId,userId,planId);
	}

	public void updatePlanTask(@RequestBody List<TaskPlanParam> taskPlanList) {
		videoManageServiceManager.updatePlanTask(taskPlanList);
	}

	public PageInfo<VideoPackageDto> getVideoPackageList(@RequestBody VideoPackageSearchParam videoPackageSearchParam) {
		return videoManageServiceManager.getVideoPackageList(videoPackageSearchParam);
	}
	public String createPlan(@RequestBody PlanParam plan) {
		return videoManageServiceManager.createPlan(plan);
	}

	public PageInfo<PayRecordDto> getBuyHisRecordList(@RequestBody HisRecordSearchParam hisRecordSearchParam) {
		return videoManageServiceManager.getBuyHisRecordList(hisRecordSearchParam);
	}

	public PageInfo<PayRecordDto> getBuyRecordList(@RequestBody RecordSearchParam searchParam) {
		return videoManageServiceManager.getBuyRecordList(searchParam);
	}

	public List<ALLRecordDto> getAllBuyRecordList(@RequestBody AllRecordSearchParam searchParam) {
		return videoManageServiceManager.getAllBuyRecordList(searchParam);
	}
	
	@Override
	public void saveWebPlan(@RequestBody WebPlanDto webPlan) {
		videoManageServiceManager.saveWebPlan(webPlan,webPlan.getOrderId());
	}

	@Override
	public WebPlanDto getWebPlan(@RequestParam("orderId") String orderId) {
		return videoManageServiceManager.getWebPlan(orderId);
	}

	@Override
	public AppPayDto getPackagePriceById(@RequestParam("packageId") String packageId) {
		return videoManageServiceManager.getPackagePriceById(packageId);
	}

	@Override
	public void saveAppPay(String payId, String planId) {
		videoManageServiceManager.saveAppPay(payId,planId);
	}

	@Override
	public String getAppPay(String payId) {
		return videoManageServiceManager.getAppPay(payId);
	}

	@Override
	public int renewPlan(@RequestParam("tenantId") Long tenantId,@RequestParam("userId") String userId,
						 @RequestParam("planId") String planId, @RequestParam("counts") int counts,@RequestParam("orderId") String orderId){
		return videoManageServiceManager.renewPlan(tenantId, userId, planId, counts,orderId);
	}

	@Override
	public void planBandingDevice(Long tenantId, String userId, String planId, String deviceId) {
		videoManageServiceManager.planBandingDevice(tenantId, userId, planId, deviceId);
	}

	@Override
	public void deleteVideoEvent(Long tenantId, String userId, String planId, String eventId) {
		videoManageServiceManager.deleteVideoEvent(tenantId, userId, planId, eventId);
	}
	
	@Override
	public String getDeviceId(String planId) {
		return videoManageServiceManager.getDeviceId(planId);
	}

	@Override
	public String getPlanId(String deviceId) {
		return videoManageServiceManager.getPlanId(deviceId);
	}

	@Override
	public List<VideoPlanTaskDto> getSyncTaskInfo(String planId) {
		return videoManageServiceManager.getSyncTaskInfo(planId);
	}

	@Override
	public void createVideoFile(@RequestBody VideoFile videoFile) {
		videoManageServiceManager.createVideoFile(videoFile);
	}

	@Override
	public List<String> judgeDeviceBandPlan(@RequestBody List<String> deviceIdList) {
		return videoManageServiceManager.judgeDeviceBandPlan(deviceIdList);
	}


	@Override
	public void insertVideoEvent(@RequestBody VideoEvent videoEvent) {
		videoManageServiceManager.insertVideoEvent(videoEvent);
	}

	@Override
	public void deletePlanOrder(String orderId) {
		videoManageServiceManager.deletePlanOrder(orderId);
	}

	@Override
	public boolean judgePlanExist(String planId) {
		return videoManageServiceManager.countPlanById(planId);
	}


	@Override
	public boolean checkDeviceHasBindPlan(@RequestParam(value = "deviceId") String deviceId) {
		return videoManageServiceManager.checkDeviceHasBindPlan(deviceId);
	}

	@Override
	public VideoPayRecordDto getLatestOrderByPlanIdAndStatus(@RequestParam("tenantId") Long tenantId,
															 @RequestParam("planId") String planId, @RequestParam("planStatus") Integer planStatus) {
		return videoManageServiceManager.getLatestOrderByPlanIdAndStatus(tenantId,planId,planStatus);
	}

	@Override
	public List<Integer> getVideoStartTimeHourByDate(String searchDate,String planId) {
		return videoManageServiceManager.getVideoStartTimeHourByDate(searchDate,planId);
	}

	@Override
	public VideoPayRecord getVideoPayRecord(@RequestParam("tenantId") Long tenantId, @RequestParam("planId") String planId,@RequestParam("orderId") String orderId) {
		return videoManageServiceManager.getVideoPayRecord(planId, orderId, tenantId);
	}

	@Override
	public void refundOneOrderOfPlan(@RequestParam("planId") String planId,@RequestParam("orderId") String orderId,@RequestParam("tenantId") Long tenantId) {
		videoManageServiceManager.refundOneOrderOfPlan(planId, orderId, tenantId);
	}

	@Override
	public List<VideoPlanOrderDto> queryPlanOtherPayRecord(String orderId, String planId) {
		return videoManageServiceManager.queryPlanOtherPayRecord(orderId, planId);
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
	public void planUnBandingDevice(@RequestParam("tenantId")Long tenantId, @RequestParam("deviceId")String deviceId){
		this.videoManageServiceManager.planUnBandingDevice(tenantId, deviceId);
	}

	/**
	 * @despriction：处理某一时间前的全时录影溢出数据（video_file,video_event）置为无效
	 * @author  yeshiyuan
	 * @created 2018/6/12 17:31
	 * @param planId 计划id
	 * @return
	 */
	@Override
	public void dealPlanAllTimeOver(@RequestParam("planId") String planId,@RequestParam("videoStartTime")String videoStartTime) {
		this.videoManageServiceManager.dealPlanAllTimeOver(planId,videoStartTime);
	}

	/**
	 * @despriction：清除计划事件录影溢出数据（video_file,video_event）
	 * @author  yeshiyuan
	 * @created 2018/6/13 17:25
	 * @param planId 计划id
	 * @return
	 */
	@Override
	public void dealPlanEventOver(String planId) {
		this.videoManageServiceManager.dealPlanEventOver(planId);
	}

	/**
	 * @despriction：处理过期计划的相关数据（video_file,video_event，redis缓存信息、解绑设备）
	 * @author  yeshiyuan
	 * @created 2018/6/15 13:41
	 * @param null
	 * @return
	 */
	@Override
	public void dealPlanExpireRelateData(String planId) {
		this.videoManageServiceManager.dealPlanExpireRelateData(planId);
	}

	/**
	 * @despriction：通过事件id获取事件对应的视频文件
	 * @author  yeshiyuan
	 * @created 2018/6/22 16:44
	 * @param null
	 * @return
	 */
	@Override
	public List<VideoTsFileDto> getVideoFileListByEventUuid(@RequestBody GetEventVideoFileReq req) {
		return videoManageServiceManager.getVideoFileListByEventUuid(req.getPlanId(), req.getEventId(), req.getFileType(), req.getEventOddurTime());
	}

	/**
	 * @despriction：统计IPC录影日期
	 * @author  yeshiyuan
	 * @created 2018/7/26 10:56
	 * @return
	 */
	@Override
	public List<String> countVideoDate(@RequestBody CountVideoDateReq req) {
		if (req == null){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "params is null");
		}
		return videoManageServiceManager.countVideoDate(req.getPlanId(), req.getStartDate(), req.getEndDate());
	}
}
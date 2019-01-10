package com.iot.video.manager;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.CalendarUtil;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.SecurityUtil;
import com.iot.common.util.StringUtil;
import com.iot.enums.DataStatusEnum;
import com.iot.file.api.VideoFileApi;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.api.OrderApi;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.vo.goods.req.VideoPackageReq;
import com.iot.payment.vo.order.resp.VideoPlanTypeResp;
import com.iot.redis.RedisCacheUtil;
import com.iot.util.ToolUtils;
import com.iot.video.contants.ModuleConstants;
import com.iot.video.dto.*;
import com.iot.video.entity.*;
import com.iot.video.enums.*;
import com.iot.video.exception.BusinessExceptionEnum;
import com.iot.video.mongo.entity.VideoEventEntity;
import com.iot.video.mongo.entity.VideoFileEntity;
import com.iot.video.mongo.service.VideoEventMongoService;
import com.iot.video.mongo.service.VideoFileMongoService;
import com.iot.video.queue.VideoEventQueue;
import com.iot.video.queue.VideoFileQueue;
import com.iot.video.service.VideoManageService;
import com.iot.video.service.VideoPlanService;
import com.iot.video.service.impl.VideoManageServiceImpl;
import com.iot.video.util.CommonUtil;
import com.iot.video.util.UUIDUtil;
import com.iot.video.vo.AllRecordVo;
import com.iot.video.vo.PayRecordVo;
import com.iot.video.vo.VideoPlanVo;
import com.iot.video.vo.VideoTaskVo;
import com.iot.video.vo.redis.RedisVideoPlanInfoVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class VideoManageServiceManager {

	private static final Logger logger  = LoggerFactory.getLogger(VideoManageServiceImpl.class);

    @Autowired
	private VideoManageService videoManageService;

    @Autowired
	private VideoPlanService videoPlanService;

    @Autowired
	private VideoPlanManager videoPlanManager;

	@Autowired
	private GoodsServiceApi goodsServiceApi;

	@Autowired
	private OrderApi orderApi;

	@Autowired
	private VideoEventMongoService videoEventMongoService;

	@Autowired
	private VideoFileMongoService videoFileMongoService;

	@Autowired
	private VideoFileApi videoFileApi;

	/**
	 *
	 * 描述：更新执行计划
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:55:17
	 * @since
	 * @param tenantId
	 * @param userId
	 * @param planId
	 * @param planExecStatus
	 */
	public void updatePlanExecStatus(Long tenantId, String userId, String planId, Integer planExecStatus) {
		verifyPlanExecStatus(tenantId,userId,planId,planExecStatus);

		try {
			//如果是开启的话需要校验计划是否过期
			if (PlanExecStatusEnum.OPEN.getCode().equals(planExecStatus)){
				boolean isExpire = videoPlanManager.checkPlanIsExpire(planId);
				if (isExpire) {
					throw new BusinessException(BusinessExceptionEnum.PLAN_EXPIRE);
				}
			}
			videoManageService.updatePlanExecStatus(tenantId,userId,planId,planExecStatus);
			RedisCacheUtil.hashPut( ModuleConstants.VIDEO_PLAN_INFO + planId, ModuleConstants.VIDEO_PLAN_INFO_KEY_PLANEXECSTATUS, planExecStatus, false);
		} catch(Exception e) {
			logger.error("", e);
			throw new BusinessException(BusinessExceptionEnum.UPDATE_PLANEXECSTATUS_FAIL);
		}
	}

	/**
	 *
	 * 描述：校验参数
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:55:27
	 * @since
	 * @param tenantId
	 * @param userId
	 * @param planId
	 * @param planExecStatus
	 */
	private void verifyPlanExecStatus(Long tenantId, String userId, String planId, Integer planExecStatus) {
		if(tenantId == null) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_TENANTID, "tenant id is null or blank");
		}

		if(StringUtils.isBlank(userId)) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_USERID, "user id is null or blank");
		}

		if(StringUtils.isBlank(planId)) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_PLANID, "plan id is null or blank");
		}

		if(planExecStatus == null) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_PLANEXECSTATUS, "plan exec status is null");
		}
	}

	/**
	 *
	 * 描述：修改录影计划任务
	 * @author wujianlong
	 * @created 2018年3月22日 上午10:47:14
	 * @since
	 * @param taskPlanList
	 * @return
	 */
	public void updatePlanTask(List<TaskPlanParam> taskPlanList) {
		verifyUpdatePlan(taskPlanList);

		String planCycle = taskPlanList.get(0).getPlanCycle();
		if (!PlanCycleEnum.WEEK.getCode().equals(planCycle) && !PlanCycleEnum.DAY.getCode().equals(planCycle)) {
			throw new BusinessException(BusinessExceptionEnum.PLANCYCLE_IS_NOT_WEEK_OR_DAY);
		}

		// 校验执行状态
		int planStatus = taskPlanList.get(0).getPlanStatus();
		if (PlanCycleEnum.DAY.getCode().equals(planCycle) && PlanStatusEnum.START.getCode() == planStatus) {
			planCycle = PlanCycleEnum.WEEK.getCode();
		}

		String planId = taskPlanList.get(0).getPlanId();
		Long tenantId = taskPlanList.get(0).getTenantId();
		String userId = taskPlanList.get(0).getUserId();
		String deviceId = videoManageService.getDeviceId(planId);
		if(StringUtils.isBlank(deviceId)) {
			throw new BusinessException(BusinessExceptionEnum.PLAN_NOT_BOUND_DEVICE, "Plan has not yet bound the device");
		}
		if(planStatus == 0) {
			videoManageService.updatePlanCycle(tenantId, userId, planId, "D", planStatus);
			videoManageService.deleteTaskByPlanId(tenantId, userId, planId);
		}else {
			try {
				videoManageService.updatePlanCycle(tenantId, userId, planId, planCycle, planStatus);
				videoManageService.deleteTaskByPlanId(tenantId, userId, planId);

				VideoTask videoTask = null;
				if (!PlanCycleEnum.DAY.getCode().equals(planCycle)) {
					for (TaskPlanParam task : taskPlanList) {
						videoTask = new VideoTask();
						BeanUtils.copyProperties(task, videoTask);
//						videoTask.setTaskId(RedisCacheUtil.incr(ModuleConstants.DB_TABLE_VIDEO_TASK, 0L));
						videoManageService.createVideoTask(videoTask);
					}
				}
			} catch(Exception e) {
				logger.error("", e);
				throw new BusinessException(BusinessExceptionEnum.UPDATE_PLAN_FAIL);
			}
		}
	}

	/**
	 *
	 * 描述：校验参数
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:55:40
	 * @since
	 * @param taskPlanList
	 */
	private void verifyUpdatePlan(List<TaskPlanParam> taskPlanList) {
		if(CollectionUtils.isEmpty(taskPlanList)) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_TASKPLANLIST);
		}
	}

	/**
	 *
	 * 描述：获取计划列表
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:55:51
	 * @since
	 * @param planSearchParam
	 * @return
	 */
	public PageInfo<VideoPlanDto> getPlanList(PlanSearchParam planSearchParam) {
		Long tenantId = planSearchParam.getTenantId();
		String userId = planSearchParam.getUserId();
		if(tenantId == null) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_TENANTID, "tenant id is null or blank");
		}
		if(StringUtils.isBlank(userId)) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_USERID, "user id is null or blank");
		}
		Integer pageNum = planSearchParam.getPageNum();
		Integer pageSize = planSearchParam.getPageSize();
		if (null == pageNum) {
			pageNum = 0;
		}
		if (null == pageSize || 0 == pageSize) {
			pageSize=10;
		}
		PageHelper.startPage(pageNum,pageSize,true);
		Page<VideoPlanDto> videoPlanPage= new Page<VideoPlanDto>();
		Page<VideoPlanVo> videoPlanVoList= null;
		try {
			videoPlanVoList = videoManageService.getVideoPlanList(tenantId,userId);
			if(null == videoPlanVoList || videoPlanVoList.size()==0){
				videoPlanPage.setTotal(videoPlanVoList.getTotal());
				videoPlanPage.setPageNum(pageNum);
				videoPlanPage.setPageSize(pageSize);
				PageInfo<VideoPlanDto> pageInfo = new PageInfo<VideoPlanDto>(videoPlanPage);
				return  pageInfo;
			}
			List<String> planList = new ArrayList<>();
			for(VideoPlanVo videoPlanVo : videoPlanVoList){
				planList.add(videoPlanVo.getPlanId());
				//查套餐信息
				GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(videoPlanVo.getPackageId());
				if(null != goodsInfo){
					videoPlanVo.setPackageName(goodsInfo.getGoodsName());
					videoPlanVo.setPackagePrice(goodsInfo.getPrice() == null ? 0.00d : goodsInfo.getPrice().doubleValue());
					videoPlanVo.setPackageType(goodsInfo.getTypeId() == null ? "" :goodsInfo.getTypeId().toString());
					videoPlanVo.setEventOrFulltimeAmount(StringUtil.isBlank(goodsInfo.getStandard()) ? 0 : Integer.parseInt(goodsInfo.getStandard()));
				}
			}
			Map<String,String> planTaskMap = new HashMap<>();
			List<PlanTaskDto> planTaskDtoList = videoManageService.getDayIndexByPlanId(planList);
			for(PlanTaskDto planTaskDto : planTaskDtoList){
				planTaskMap.put(planTaskDto.getPlanId(),planTaskDto.getTaskDateStr());
			}
			for(VideoPlanVo videoPlanVo : videoPlanVoList) {
				String taskDateStr = planTaskMap.get(videoPlanVo.getPlanId());
				List<String> result = new ArrayList<>();
				if(null != taskDateStr && !"".equals(taskDateStr)){
					result = Arrays.asList(taskDateStr.split(","));
				}
				videoPlanVo.setDayIndex(result);
				Integer packageType = Integer.valueOf(videoPlanVo.getPackageType());
				if (VideoPlanTypeEnum.EVENT.getCode().compareTo(packageType)==0){
					//如果是事件类型，需要统计已使用事件数量
					Integer usedEventNum = videoPlanManager.getUsedEventNum(videoPlanVo.getPlanId());
					if (videoPlanVo.getEventOrFulltimeAmount().compareTo(usedEventNum) == -1) {
						//超过已使用数量的话则已可使用数量为临界值
						videoPlanVo.setUseQuantity(videoPlanVo.getEventOrFulltimeAmount());
					}else {
						videoPlanVo.setUseQuantity(usedEventNum);
					}
				}
				//因为sql查出来的一定是可以续费的，所以根据协议，这里写死1：可续费
				videoPlanVo.setRenewMark("1");

				//1、提醒续费；0：不提醒
				//计划结束时间
				Date planEndTime = videoPlanVo.getPlanEndTime();
				//计划结束时间后5天
				Date planEndTimeAddFive = CalendarUtil.getDateBeforeOrAfter(planEndTime,5);
				//计划结束时间前5天
				Date planEndTimeMinusFive = CalendarUtil.getDateBeforeOrAfter(planEndTime,-5);
				Date now = new Date();
				if(now.before(planEndTimeAddFive) && now.after(planEndTimeMinusFive)){
					videoPlanVo.setRenewRemindFlag(1);
				}else{
					videoPlanVo.setRenewRemindFlag(0);
				}
				VideoPlanDto videoPlanDto = new VideoPlanDto();
				BeanUtils.copyProperties(videoPlanVo, videoPlanDto);
				videoPlanDto.setPackageId(SecurityUtil.EncryptByAES(videoPlanVo.getPackageId().toString(), ModuleConstants.AES_KEY));
				videoPlanPage.add(videoPlanDto);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new BusinessException(BusinessExceptionEnum.GET_PLANLIST_FAIL);
		}
		videoPlanPage.setTotal(videoPlanVoList.getTotal());
		videoPlanPage.setPageNum(pageNum);
		videoPlanPage.setPageSize(pageSize);
		PageInfo<VideoPlanDto> pageInfo = new PageInfo<VideoPlanDto>(videoPlanPage);
		return pageInfo;
	}


	public VideoPlanInfoDto getPlanInfoByDevId(PlanSearchParam planSearchParam) {
		Long tenantId = planSearchParam.getTenantId();
		String userId = planSearchParam.getUserId();
		String deviceId = planSearchParam.getDeviceId();
		if(tenantId == null) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_TENANTID, "tenant id is null or blank");
		}
		if(StringUtils.isBlank(userId)) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_USERID, "user id is null or blank");
		}
		if(StringUtils.isBlank(deviceId)) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_DEVICE_ID, "device id is null or blank");
		}
		VideoPlanInfoDto videoPlanInfoDto = null;
		try {
			videoPlanInfoDto = videoManageService.getPlanInfoByDevId(tenantId,userId,deviceId);
			if (null == videoPlanInfoDto) return videoPlanInfoDto;

			//查套餐信息
			GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(videoPlanInfoDto.getPackageId());
			if(null != goodsInfo){
				videoPlanInfoDto.setEventOrFulltimeAmount(StringUtil.isBlank(goodsInfo.getStandard()) ? 0 : Integer.parseInt(goodsInfo.getStandard()));
				videoPlanInfoDto.setPackageName(goodsInfo.getGoodsName());
				videoPlanInfoDto.setPackagePrice(goodsInfo.getPrice() == null ? 0.00d : goodsInfo.getPrice().doubleValue());
				videoPlanInfoDto.setPackageType(goodsInfo.getTypeId() == null ? "" :goodsInfo.getTypeId().toString());
			}

			PlanTaskDto planTaskDto = videoManageService.getDayIndexByPlanIdExec(videoPlanInfoDto.getPlanId());
			if(null != planTaskDto){
				String taskDateStr = planTaskDto.getTaskDateStr();
				List<String> result = new ArrayList<>();
				if(null != taskDateStr && !"".equals(taskDateStr)){
					result = Arrays.asList(taskDateStr.split(","));
				}
				videoPlanInfoDto.setDayIndex(result);
			}
			if (VideoPlanTypeEnum.EVENT.getCode().equals(Integer.valueOf(videoPlanInfoDto.getPackageType()))){
				//如果是事件类型，需要统计已使用事件数量
				Integer usedEventNum = videoPlanManager.getUsedEventNum(videoPlanInfoDto.getPlanId());
				if (videoPlanInfoDto.getEventOrFulltimeAmount().compareTo(usedEventNum) == -1) {
					//超过已使用数量的话则已可使用数量为临界值
					videoPlanInfoDto.setUseQuantity(videoPlanInfoDto.getEventOrFulltimeAmount());
				}else {
					videoPlanInfoDto.setUseQuantity(usedEventNum);
				}
			}

			//因为sql查出来的一定是可以续费的，所以根据协议，这里写死1：可续费
			videoPlanInfoDto.setRenewMark("1");

			//1、提醒续费；0：不提醒
			//计划结束时间
			Date planEndTime = videoPlanInfoDto.getPlanEndTime();
			//计划结束时间后5天
			Date planEndTimeAddFive = CalendarUtil.getDateBeforeOrAfter(planEndTime,5);
			//计划结束时间前5天
			Date planEndTimeMinusFive = CalendarUtil.getDateBeforeOrAfter(planEndTime,-5);
			Date now = new Date();
			if(now.before(planEndTimeAddFive) && now.after(planEndTimeMinusFive)){
				videoPlanInfoDto.setRenewRemindFlag(1);
			}else{
				videoPlanInfoDto.setRenewRemindFlag(0);
			}

		} catch (Exception e) {
			logger.error("", e);
			throw new BusinessException(BusinessExceptionEnum.GET_PLANINFO_FAIL,e);
		}
		return videoPlanInfoDto;
	}

//	/**
//	 * 
//	 * 描述：校验参数
//	 * @author wujianlong
//	 * @created 2018年3月31日 上午10:56:09
//	 * @since 
//	 * @param planSearchParam
//	 */
//	private void verifyPlanSearchParam(PlanSearchParam planSearchParam) {
//		if(planSearchParam == null) {
//			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_PLANSEARCHPARAM);
//		}
//		
//		verifyPlanExecStatus(planSearchParam.getTenantId(),planSearchParam.getUserId(),planSearchParam.getPlanId(),0);
//	}

	/**
	 *
	 * 描述：获取任务列表
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:56:22
	 * @since
	 * @param tenantId
	 * @param userId
	 * @param planId
	 * @return
	 */
	public List<VideoTaskDto> getVideoTaskList(Long tenantId, String userId, String planId) {
		verifyVideoTaskList(tenantId,userId,planId);

		List<VideoTaskDto> taskList = new ArrayList<VideoTaskDto>();
		List<VideoTaskVo> taskVoList = null;
		VideoTaskDto videoTaskDto = null;
		try {
			taskVoList = videoManageService.getVideoTaskList(tenantId, userId, planId);
			for(VideoTaskVo videoTaskVo : taskVoList) {
				videoTaskDto = new VideoTaskDto();
				BeanUtils.copyProperties(videoTaskVo, videoTaskDto);
				videoTaskDto.setTaskId(CommonUtil.encryptId(videoTaskVo.getTaskId()));
				taskList.add(videoTaskDto);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new BusinessException(BusinessExceptionEnum.GET_VIDEOTASKLIST_FAIL);
		}

		return taskList;
	}

	/**
	 *
	 * 描述：校验参数
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:56:33
	 * @since
	 * @param tenantId
	 * @param userId
	 * @param planId
	 */
	private void verifyVideoTaskList(Long tenantId, String userId, String planId) {
		verifyPlanExecStatus(tenantId,userId,planId,0);
	}

	/**
	 *
	 * 描述：获取视频套餐列表
	 * @author wujianlong
	 * @created 2018年3月31日 上午10:56:44
	 * @since
	 * @param videoPackageSearchParam
	 * @return
	 */
	public PageInfo<VideoPackageDto> getVideoPackageList(VideoPackageSearchParam videoPackageSearchParam) {
		verifyVideoPackageList(videoPackageSearchParam);

		Integer pageNum = videoPackageSearchParam.getPageNum();
		Integer pageSize = videoPackageSearchParam.getPageSize();

		if (null == pageNum) {
            pageNum = 0;
        }
        if (null == pageSize || 0 == pageSize) {
            pageSize=10;
        }

        Page<VideoPackageDto> videoPlanPage= new Page<VideoPackageDto>();
        VideoPackageDto videoPackageDto = null;
		PageInfo goodsPageInfo = new PageInfo();
		try {
        	List<Integer> typeIdList = new ArrayList<>();
			typeIdList.add(VideoPlanTypeEnum.ALL_TIME.getCode());
			typeIdList.add(VideoPlanTypeEnum.EVENT.getCode());
			VideoPackageReq videoPackageReq = new VideoPackageReq();
			videoPackageReq.setPackageTypeList(typeIdList);
			videoPackageReq.setPageNum(pageNum);
			videoPackageReq.setPageSize(pageSize);
			goodsPageInfo = goodsServiceApi.getVideoPackageList(videoPackageReq);
			List<GoodsInfo> goodsInfoList = goodsPageInfo.getList();
			for(GoodsInfo goodsInfo : goodsInfoList){
				videoPackageDto = new VideoPackageDto();
				videoPackageDto.setPackageId(CommonUtil.encryptId(goodsInfo.getId()));
				videoPackageDto.setCurrency(goodsInfo.getCurrency());
				videoPackageDto.setEventOrFulltimeAmount(Integer.parseInt(goodsInfo.getStandard()));
				videoPackageDto.setPackageName(goodsInfo.getGoodsName());
				videoPackageDto.setPackagePrice(goodsInfo.getPrice() == null ? "0.00" : goodsInfo.getPrice().toString());
				videoPackageDto.setPackageType(goodsInfo.getTypeId() == null ? "" :goodsInfo.getTypeId().toString());
				videoPlanPage.add(videoPackageDto);
			}
        } catch (Exception e) {
        	logger.error("", e);
        	throw new BusinessException(BusinessExceptionEnum.GET_VIDEOPACKAGELIST_FAIL,e);
        }
		videoPlanPage.setPages(goodsPageInfo.getPages());
        videoPlanPage.setTotal(goodsPageInfo.getTotal());
        videoPlanPage.setPageNum(pageNum);
        videoPlanPage.setPageSize(pageSize);
        PageInfo<VideoPackageDto> pageInfo = new PageInfo<VideoPackageDto>(videoPlanPage);
        return pageInfo;
	}

	/**
	 *
	 * 描述：参数校验
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:02:05
	 * @since
	 * @param videoPackageSearchParam
	 */
	private void verifyVideoPackageList(VideoPackageSearchParam videoPackageSearchParam) {
		if(StringUtils.isBlank(videoPackageSearchParam.getDeviceType())) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_DEVICETYPE);
		}
	}

	/**
	 *
	 * 描述：创建计划
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:02:22
	 * @since
	 * @param plan
	 * @return
	 */
	@Transactional
	public String createPlan(PlanParam plan)
	{
		if(null == plan){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan object is null");
		}
		if(plan.getTenantId() == null){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan tenant id is null or blank");
		}
		if(StringUtil.isBlank(plan.getUserId())){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan user id is null or blank");
		}
		if(plan.getPackageId()==null || plan.getPackageId()==0){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan package id is null or blank");
		}
		if(StringUtil.isBlank(plan.getPackageName())) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan package name is null or blank");
		}
		if(plan.getCounts() <= 0){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan count <= 0");
		}

		VideoPlan sqlPlan = new VideoPlan();
//		long id = RedisCacheUtil.incr(ModuleConstants.DB_TABLE_VIDEO_PLAN, 0L);
		String planId = UUIDUtil.getUUID();
		int planOrder = getPlanOrder(plan.getUserId());
		Date now = new Date();
		long endTimeInSec = plan.getCounts() * ModuleConstants.PLAN_DURATION_30_IN_SEC;
		Date endTime = new Date(now.getTime() + endTimeInSec);

//		sqlPlan.setId(id);
		sqlPlan.setPlanId(planId);
		BeanUtil.copyProperties(plan, sqlPlan);
		sqlPlan.setPlanStartTime(now);
		sqlPlan.setPlanEndTime(endTime);
		sqlPlan.setPlanExecStatus(PlanStatusEnum.START.getCode());
		sqlPlan.setPlanCycle(PlanCycleEnum.WEEK.getCode());
		sqlPlan.setPlanStatus(PlanStatusEnum.START.getCode());
		sqlPlan.setCreateTime(now);
		sqlPlan.setPlanOrder(planOrder);
		String planName = new StringBuilder().append(plan.getPackageName()).append("_").append(planOrder).toString();
		sqlPlan.setPlanName(planName);
		sqlPlan.setTenantId(plan.getTenantId());
		sqlPlan.setPackageId(plan.getPackageId());

		try{
			videoManageService.createPlan(sqlPlan);

			//把计划相关信息存储至redis中（video:plan-info:planId ）
			Map<String,Object> map = new HashMap<>();
			map.put(ModuleConstants.VIDEO_PLAN_INFO_KEY_TENANTID,sqlPlan.getTenantId());
			GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(plan.getPackageId());
			if(null == goodsInfo){
				throw new BusinessException(BusinessExceptionEnum.PACKAGEINFO_NOT_EXISTS);
			}else{
				map.put(ModuleConstants.VIDEO_PLAN_INFO_KEY_PLANTYPE, goodsInfo.getTypeId().toString());
				map.put(ModuleConstants.VIDEO_PLAN_INFO_KEY_PACKAGE_EVENTNUM_FULLHOUR, goodsInfo.getStandard());
			}
			RedisCacheUtil.hashPutAll(ModuleConstants.VIDEO_PLAN_INFO + planId,map,false);

			//创建定时任务
			VideoTask videoTask = null;
			for (int i = 1; i < 8 ;i++) {
				videoTask = new VideoTask();
				videoTask.setExecuteEndTime("23:59:59");
				videoTask.setExecuteStartTime("00:00:00");
				videoTask.setTaskDate(""+i);
				videoTask.setPlanId(sqlPlan.getPlanId());
				videoTask.setUserId(plan.getUserId());
				videoTask.setTenantId(plan.getTenantId());
//				videoTask.setTaskId(RedisCacheUtil.incr(ModuleConstants.DB_TABLE_VIDEO_TASK, 0L));
				videoManageService.createVideoTask(videoTask);
			}

			PayRecordVo recordVo = new PayRecordVo();
			BeanUtil.copyProperties(plan, recordVo);
			recordVo.setPlanId(planId);
			recordVo.setCounts(plan.getCounts());
			recordVo.setPayTime(now);
			recordVo.setPlanStartTime(now);
			recordVo.setPlanEndTime(endTime);
			recordVo.setPlanStatus(PlanStatusEnum.START.getCode());
			recordVo.setPackageId(sqlPlan.getPackageId());
			insertPayRecord(recordVo, plan.getTenantId(), plan.getUserId());
		}catch (Exception e){
			String err = "create plan in db occur error.";
			err.concat("plan_id=" + planId);
			err.concat(", plan_order=" + planOrder);
			err.concat(", counts=" + plan.getCounts());
			err.concat(", packageId=" + planOrder);
			err.concat(", packageName=" + plan.getPackageName());
			err.concat(", tenantId=" + plan.getTenantId());
			err.concat(", userId=" + plan.getUserId());
			logger.error(err, e);
			throw new BusinessException(BusinessExceptionEnum.CREATE_PLAN_FAILED, err);
		}

		return planId;
	}

	/**
	 *
	 * 描述：更新计划名
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:02:37
	 * @since
	 * @param tenantId
	 * @param userId
	 * @param planId
	 * @param planName
	 */
	public void updatePlanName(Long tenantId, String userId, String planId, String planName)
	{
		if(tenantId == null){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "tenant id is null or blank");
		}
		if(StringUtil.isBlank(userId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "user id is null or blank");
		}
		if(StringUtil.isBlank(planId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan id is null or blank");
		}
		if(StringUtil.isBlank(planName)) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan name is null or blank");
		}
		try {
//			long tenantIdLong = CommonUtil.decryptId(tenantId);
			videoManageService.updatePlanName(tenantId, userId, planId, planName);
		}catch (Exception e){
			String err = "udpate plan name in db occur error.";
			err.concat("plan_id=" + planId);
			err.concat(", tenantId=" + tenantId);
			err.concat(", userId=" + userId);
			err.concat(", new name=" + planName);
			logger.error(err, e);
			throw new BusinessException(BusinessExceptionEnum.UPDATE_PLAN_NAME_FAILED, err);
		}
	}

	/**
	 *
	 * 描述：查询历史记录
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:02:47
	 * @since
	 * @param searchParam
	 * @return
	 */
	public PageInfo<PayRecordDto> getBuyHisRecordList(HisRecordSearchParam searchParam)
	{
		if(null == searchParam) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search param is null");
		}
		if(searchParam.getTenantId() == null || searchParam.getTenantId() == 0){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search tenant id is null or blank");
		}
		if(StringUtil.isBlank(searchParam.getUserId())){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search user id is null or blank");
		}
		if( StringUtil.isBlank(searchParam.getOrderId())) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search order id is null or blank");
		}
		Integer pageNum = searchParam.getPageNum();
		Integer pageSize = searchParam.getPageSize();

		if (null == pageNum) {
			pageNum = 0;
		}
		if (null == pageSize || 0 == pageSize) {
			pageSize = 10;
		}

		PageHelper.startPage(pageNum, pageSize,true);

		Page<PayRecordDto> tmpPage = new Page<PayRecordDto>();
		Page<PayRecordVo> tmpVoList = null;
		PayRecordDto payRecordDto = null;
		try {
			tmpVoList = videoManageService.getBuyHisRecordList(searchParam.getTenantId(),
					searchParam.getUserId(), searchParam.getPlanId(), searchParam.getOrderId());
			for(PayRecordVo payRecordVo : tmpVoList) {
				//查套餐信息
				GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(payRecordVo.getPackageId());
				if(null != goodsInfo){
					payRecordVo.setPackageName(goodsInfo.getGoodsName());
					payRecordVo.setPackagePrice(goodsInfo.getPrice() == null ? 0.00f : goodsInfo.getPrice().floatValue());
					payRecordVo.setCurrency(goodsInfo.getCurrency());
				}
				payRecordDto = new PayRecordDto();
				BeanUtils.copyProperties(payRecordVo, payRecordDto);
				payRecordDto.setPackageId(CommonUtil.encryptId(payRecordVo.getPackageId()));
				payRecordDto.setPayRecordId(CommonUtil.encryptId(payRecordVo.getPayRecordId()));
				tmpPage.add(payRecordDto);
			}
		}catch (Exception e){
			String err = "get buy history record list in db occur error.";
			err.concat("plan_id=" + searchParam.getPlanId());
			err.concat(", tenantId=" + searchParam.getTenantId());
			err.concat(", userId=" + searchParam.getUserId());
			err.concat(", order id=" + searchParam.getOrderId());
			logger.error(err, e);
			throw new BusinessException(BusinessExceptionEnum.GET_BUY_HIS_RECORD_FAILED, err);
		}
		tmpPage.setTotal(tmpVoList.getTotal());
		tmpPage.setPageNum(pageNum);
		tmpPage.setPageSize(pageSize);
		PageInfo<PayRecordDto> pageInfo = new PageInfo<PayRecordDto>(tmpPage);
		return pageInfo;
	}

	/**
	 *
	 * 描述：查询购买记录
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:03:03
	 * @since
	 * @param searchParam
	 * @return
	 */
	public PageInfo<PayRecordDto> getBuyRecordList(RecordSearchParam searchParam)
	{
		if(null == searchParam) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search param is null");
		}

		if(searchParam.getTenantId() == null || searchParam.getTenantId() == 0){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search tenant id is null or blank");
		}
		if(StringUtil.isBlank(searchParam.getUserId())) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "search user id is null or blank");
		}

		Integer pageNum = searchParam.getPageNum();
		Integer pageSize = searchParam.getPageSize();

		if (null == pageNum) {
			pageNum = 0;
		}
		if (null == pageSize || 0 == pageSize) {
			pageSize = 10;
		}

		PageHelper.startPage(pageNum, pageSize,true);

		Page<PayRecordDto> tmpPage = new Page<PayRecordDto>();
		Page<PayRecordVo> tmpVoList = null;
		PayRecordDto payRecordDto = null;
		try {
			tmpVoList = videoManageService.getBuyRecordList(searchParam.getTenantId(), searchParam.getUserId());
			for(PayRecordVo payRecordVo : tmpVoList) {
				//查套餐信息
				GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(payRecordVo.getPackageId());
				if(null != goodsInfo){
					payRecordVo.setCurrency(goodsInfo.getCurrency());
					payRecordVo.setPackageName(goodsInfo.getGoodsName());
					payRecordVo.setPackagePrice(goodsInfo.getPrice() == null ? 0.00f : goodsInfo.getPrice().floatValue());
				}

				payRecordDto = new PayRecordDto();
				BeanUtils.copyProperties(payRecordVo, payRecordDto);
				payRecordDto.setPackageId(CommonUtil.encryptId(payRecordVo.getPackageId()));
				payRecordDto.setPayRecordId(CommonUtil.encryptId(payRecordVo.getPayRecordId()));
				tmpPage.add(payRecordDto);
			}
		}catch (Exception e){
			String err = "get buy record list in db occur error";
			err.concat(", tenantId=" + searchParam.getTenantId());
			err.concat(", userId=" + searchParam.getUserId());
			logger.error(err, e);
			throw new BusinessException(BusinessExceptionEnum.GET_BUY_RECORD_FAILED, err);
		}
		tmpPage.setTotal(tmpVoList.getTotal());
		tmpPage.setPageNum(pageNum);
		tmpPage.setPageSize(pageSize);
        PageInfo<PayRecordDto> pageInfo = new PageInfo<PayRecordDto>(tmpPage);
		return pageInfo;
	}

	/**
	 *
	 * 描述：查询购买记录
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:03:03
	 * @since
	 * @param searchParam
	 * @return
	 */
	public List<ALLRecordDto> getAllBuyRecordList(AllRecordSearchParam searchParam)
	{
//		Integer pageNum = searchParam.getPageNum();
//		Integer pageSize = searchParam.getPageSize();
//
//		if (null == pageNum) {
//			pageNum = 0;
//		}
//		if (null == pageSize || 0 == pageSize) {
//			pageSize = 10;
//		}
//
//		PageHelper.startPage(pageNum, pageSize,true);
		List<ALLRecordDto> tmpList = new ArrayList<ALLRecordDto>();
		List<AllRecordVo> tmpVoList = null;
		ALLRecordDto payRecordDto = null;
		try {
			if(!StringUtil.isEmpty(searchParam.getTimeType())){
				if(StringUtil.isEmpty(searchParam.getStartTime())){
//					throw new BusinessException(VideoExceptionEnum.STARTTIME_IS_NULL);
				}
				if(StringUtil.isEmpty(searchParam.getEndTime())){
//					throw new BusinessException(VideoExceptionEnum.ENDTIME_IS_NULL);
				}
//				if("0".equals(searchParam.getTimeType())){
//					paramMap.put("search_timeType0", "1");
//				} else if("1".equals(paramMap.get("search_timeType"))){
//					paramMap.put("search_timeType1", "1");
//				}
			}
			//处理订单状态查询条件 --0-支付成功，1-服务开通，2-服务开通失败，3-服务已关闭
			if(!StringUtil.isEmpty(searchParam.getRecordState())){
//				if("0".equals(paramMap.get("search_payState"))){
//					paramMap.put("search_payState0", "1");
//				} else if("1".equals(paramMap.get("search_payState"))){
//					paramMap.put("search_payState1", "1");
//				} else if("2".equals(paramMap.get("search_payState"))){
//					paramMap.put("search_payState2", "1");
//				} else if("3".equals(paramMap.get("search_payState"))){
//					paramMap.put("search_payState3", "1");
//				} 
			}
			tmpVoList = videoManageService.getAllBuyRecordList(searchParam);
			for(AllRecordVo payRecordVo : tmpVoList) {
				//查套餐信息
				GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(payRecordVo.getPackageId());
				if(null != goodsInfo){
					payRecordVo.setPackageName(goodsInfo.getGoodsName());
					payRecordVo.setPackagePrice(goodsInfo.getPrice() == null ? 0.00f : goodsInfo.getPrice().floatValue());
					payRecordVo.setCurrency(goodsInfo.getCurrency());
				}
				payRecordDto = new ALLRecordDto();
				BeanUtils.copyProperties(payRecordVo, payRecordDto);
				payRecordDto.setPackageId(CommonUtil.encryptId(payRecordVo.getPackageId()));
				payRecordDto.setPayRecordId(CommonUtil.encryptId(payRecordVo.getPayRecordId()));
				tmpList.add(payRecordDto);
			}
		}catch (Exception e){
			String err = "get buy record list in db occur error";
			logger.error(err, e);
			throw new BusinessException(BusinessExceptionEnum.GET_BUY_RECORD_FAILED, err);
		}

//		return new PageInfo<ALLRecordDto>(tmpList);
		return tmpList;
	}

	/**
	 *
	 * 描述：保存redis,将orderId作为key值
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:03:15
	 * @since
	 * @param webPlan
	 * @param orderId
	 */
	public void saveWebPlan(WebPlanDto webPlan, String orderId) {
		if(webPlan == null){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "web plan object is null");
		}
		if(StringUtils.isBlank(orderId)) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "order id is null or blank");
		}
		RedisCacheUtil.valueObjSet(ModuleConstants.REDIS_PRE_PLAN_ORDERID + orderId, webPlan,48*60*60L);
	}

	/**
	 *
	 * 描述：从redis中获取数据
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:03:34
	 * @since
	 * @param orderId
	 * @return
	 */
	public WebPlanDto getWebPlan(String orderId) {
		if(StringUtils.isBlank(orderId)) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "order id is null or blank");
		}
		String key = ModuleConstants.REDIS_PRE_PLAN_ORDERID + orderId;
		WebPlanDto webPlan = RedisCacheUtil.valueObjGet(key,WebPlanDto.class);
		RedisCacheUtil.delete(key);
		return webPlan;
	}

	/**
	 *
	 * 描述：获取套餐价格
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:04:04
	 * @since
	 * @param packageId
	 * @return
	 */
	public AppPayDto getPackagePriceById(String packageId) {
		if(StringUtils.isBlank(packageId)) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "package id is null or blank");
		}
		AppPayDto appPayDto = new AppPayDto();
		GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(Long.valueOf(packageId));
		if(null != goodsInfo){
			appPayDto.setPackageName(goodsInfo.getGoodsName());
			appPayDto.setPackagePrice(goodsInfo.getPrice() == null ? 0.00d : goodsInfo.getPrice().doubleValue());
			appPayDto.setCurrency(goodsInfo.getCurrency());
		}
		return appPayDto;
	}

	/**
	 *
	 * 描述：保存redis记录，存payId,planId
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:04:22
	 * @since
	 * @param payId
	 * @param planId
	 */
	public void saveAppPay(String payId, String planId) {
		if(StringUtils.isBlank(payId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "pay id is null or blank");
		}
		if(StringUtils.isBlank(planId)) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan id is null or blank");
		}

		RedisCacheUtil.valueSet(payId, JsonUtil.toJsonObject(
				new StringBuilder().append(planId).toString()).toString(), 30000L);
	}

	/**
	 *
	 * 描述：获取planId
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:04:44
	 * @since
	 * @param payId
	 * @return
	 */
	public String getAppPay(String payId) {
		if(StringUtils.isBlank(payId) ) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "pay id is null or blank");
		}

		if(RedisCacheUtil.valueGet(payId) == null) {
			return null;
		}

		return RedisCacheUtil.valueGet(payId).toString();
	}

	public int renewPlan(Long tenantId, String userId, String planId, int counts,String orderId){
		if(tenantId == null){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "tenant id is null or blank");
		}
		if( StringUtils.isBlank(userId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "user id is null or blank");
		}
		if(StringUtils.isBlank(planId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan id is null or blank");
		}
		if( counts <= 0) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "count <= 0");
		}
		try {
			VideoPlanVo planVo = videoManageService.getPlanEndTime(tenantId, userId, planId);
			if(null == planVo){
				throw new BusinessException(BusinessExceptionEnum.RENEW_PLAN_FAILED, "get plan desc failed");
			}
			Date endTime = planVo.getPlanEndTime();
			if(null == endTime){
				throw new BusinessException(BusinessExceptionEnum.RENEW_PLAN_FAILED, "get plan end time failed");
			}

			long endTimeInSec = counts * ModuleConstants.PLAN_DURATION_30_IN_SEC;
			Date newEndTime = new Date(endTime.getTime() + endTimeInSec);
			planVo.setPlanEndTime(newEndTime);
			VideoPlan newPlan = new VideoPlan();
			newPlan.setTenantId(tenantId);
			newPlan.setUserId(userId);
			newPlan.setPlanId(planId);
			newPlan.setPlanEndTime(newEndTime);
			newPlan.setPlanExecStatus(PlanStatusEnum.START.getCode());
			newPlan.setPlanStatus(PlanStatusEnum.START.getCode());
			videoManageService.renewPlan(newPlan);

			PayRecordVo recordVo = new PayRecordVo();
			BeanUtil.copyProperties(newPlan, recordVo);
			recordVo.setPlanId(planId);
			recordVo.setCounts(counts);
			recordVo.setPackageId(planVo.getPackageId());
			recordVo.setPayTime(new Date());
			Date newStartTime = new Date(endTime.getTime() + ModuleConstants.ONE_DAY_IN_MS);
			recordVo.setPlanStartTime(newStartTime);
			recordVo.setPlanEndTime(newEndTime);
			recordVo.setPlanStatus(PlanStatusEnum.START.getCode());
			recordVo.setOrderId(orderId);
			insertPayRecord(recordVo, tenantId, userId);
			//清除redis中计划过期标志
			RedisCacheUtil.hashPut(ModuleConstants.VIDEO_PLAN_INFO + planId,ModuleConstants.VIDEO_PLAN_INFO_KEY_EEPIREFLAG, PlanExpireFlagEnum.no_expire.getCode(), false);
		}catch (Exception e){
			throw new BusinessException(BusinessExceptionEnum.RENEW_PLAN_FAILED, "update plan desc failed");
		}
		return 0;
	}

	private int getPlanOrder(String userId)
	{
		int order = 0;
		String key = new StringBuilder().append(userId).append("_").append(ModuleConstants.PLAN_ORDER_KEY).toString();
		Object orderRedis = RedisCacheUtil.valueGet(key);
		if (orderRedis == null) {
			RedisCacheUtil.valueSet(key, String.valueOf(order));
		} else {
			Object value = RedisCacheUtil.valueGet(key);
			order = Integer.parseInt(value.toString());
			order++;
			RedisCacheUtil.valueSet(key, String.valueOf(order));
		}
		return order;
	}

	private void insertPayRecord(PayRecordVo recordVo, long tenantId, String userId){
		if(null == recordVo) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
		}
		if(tenantId < 0 || StringUtil.isBlank(userId) || recordVo.getPackageId() < 0){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
		}
		if(recordVo.getCounts() <= 0){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
		}
//		Long payRecordId = RedisCacheUtil.incr(ModuleConstants.DB_TABLE_VIDEO_PAY_RECORD, 0L);
//		recordVo.setPayRecordId(payRecordId);
		videoManageService.insertPayRecord(recordVo, tenantId, userId);
	}

	@SuppressWarnings("unused")
	private int getPlanLeftDays(String tenantId, String userId, String planId){
		if(StringUtil.isBlank(tenantId) || StringUtil.isBlank(userId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
		}
		return videoManageService.getPlanLeftDays(tenantId, userId, planId);
	}

	/**
	 * 描述：计划绑定设备
	 * @author 490485964@qq.com
	 * @date 2018/4/8 13:42
	 * @param planId 计划id
	 * @param deviceId 设备id
	 * @return void
	 */
	public void planBandingDevice(Long tenantId, String userId, String planId, String deviceId) {
		if( StringUtil.isBlank(deviceId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "device id is null or blank");
		}
		if(StringUtil.isBlank(planId) ){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan id is null or blank");
		}
		String deviceIdOld = videoManageService.getDeviceId(planId);
		if(StringUtil.isNotBlank(deviceIdOld)){
			throw new BusinessException(BusinessExceptionEnum.PLAN_BANDING_DEVICE_ALREADYBOUND, "device already bind");
		}
		try {
			videoManageService.planBandingDevice(tenantId, userId, planId, deviceId);
			//维护redis中设备与计划的关系
			RedisCacheUtil.hashPut(ModuleConstants.VIDEO_PLAN_INFO + planId,ModuleConstants.VIDEO_PLAN_INFO_KEY_DEVICEID,deviceId,false);
		} catch(Exception e) {
			logger.error("", e);
			throw new BusinessException(BusinessExceptionEnum.PLAN_BANDING_DEVICE_FAILED,e);
		}
	}

	/**
	 * 描述：删除录影事件
	 * @author 490485964@qq.com
	 * @date 2018/4/8 13:42
	 * @param planId 计划id
	 * @param eventId 事件id
	 * @return void
	 */
	public void deleteVideoEvent(Long tenantId, String userId, String planId, String eventId) {
		if( StringUtil.isBlank(eventId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "event id is null or blank");
		}
		if(StringUtil.isBlank(planId) ){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan id is null or blank");
		}
		try {
			//删除并获取事件信息
			VideoEventEntity videoEvent = videoEventMongoService.findByPlanIdAndEventId(tenantId, planId, eventId);
			if(videoEvent != null) {
				//删除事件文件数据
				List<String> filePaths = videoFileMongoService.findFilePathByPlanIdAndEventUuid(planId, videoEvent.getEventUuid());
				if (!filePaths.isEmpty()){
					videoFileApi.deleteFileByFilePath(filePaths);
				}
				//删除事件对应的文件
				videoFileMongoService.deleteVideoEventFile(planId,eventId);
				//删除事件信息
				videoEventMongoService.deleteByPlanIdAndEventUuidAndDataStatus(planId, eventId, DataStatusEnum.VALID.getCode());
				//修改redis中计划的已录事件录影
				RedisVideoPlanInfoVo redisVideoPlanInfoVo = videoPlanManager.getVideoPlanInfoFromRedis(planId);
				if (VideoPlanTypeEnum.EVENT.getCode().compareTo(redisVideoPlanInfoVo.getPlanType())==0){
					//事件录影才有记录已录事件数
					int usedEventNum = videoEventMongoService.countVideoEventByPlanId(planId);
					RedisCacheUtil.valueSet(ModuleConstants.VIDEO_PLAN_USED_EVENT_NUM + planId,String.valueOf(usedEventNum),ModuleConstants.VIDEO_PLAN_USED_EVENT_NUM_EXPIRETIME);
				}
			}else {
				throw new BusinessException(BusinessExceptionEnum.EVENT_NOT_EXIST, "Video event does not exist");
			}
		} catch(Exception e) {
			logger.error("deleteVideoEvent error", e);
			throw new BusinessException(BusinessExceptionEnum.DELETE_VIDEO_EVENT_FAILED,e);
		}
	}

	/**
	 * 描述：根据计划Id获取设备Id
	 * @author 490485964@qq.com
	 * @date 2018/4/8 13:42
	 * @param planId 计划id
	 * @return String
	 */
	public String getDeviceId(String planId){
		if( StringUtil.isBlank(planId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan id is null or blank");
		}
		try {
			String devId = videoManageService.getDeviceId(planId);
			return devId;
		} catch(Exception e) {
			logger.error("", e);
			throw new BusinessException(BusinessExceptionEnum.GET_DEVICEID_FAILED,e);
		}
	}

	public String getPlanId(String deviceId){
		if( StringUtil.isBlank(deviceId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "device id is null or blank");
		}
		try {
			return videoManageService.getPlanId(deviceId);
		} catch(Exception e) {
			logger.error("", e);
			throw new BusinessException(BusinessExceptionEnum.GET_PLANID_FAILED,e);
		}
	}

	/**
	 * 描述：获取需要同步给设备的任务信息
	 * @author 490485964@qq.com
	 * @date 2018/4/8 14:04
	 * @param planId 计划id
	 * @return List<VideoPlanTaskDto>
	 */
	public List<VideoPlanTaskDto> getSyncTaskInfo(String planId){
		if(StringUtil.isBlank(planId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "planId id is null or blank");
		}
		try {
			List<VideoPlanTaskDto> videoPlanTaskDtoList =  videoManageService.getSyncTaskInfo(planId, CalendarUtil.getNowTime());
			if(null != videoPlanTaskDtoList && videoPlanTaskDtoList.size() > 0){
				videoPlanTaskDtoList.forEach(
						videoPlanTaskDto->{
							GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(videoPlanTaskDto.getPackageId());
							if(null != goodsInfo){
								videoPlanTaskDto.setPackageType(goodsInfo.getTypeId());
								videoPlanTaskDto.setAmount(StringUtil.isBlank(goodsInfo.getStandard()) ? 0 : Integer.parseInt(goodsInfo.getStandard()));
							}
						}
				);
			}
			return videoPlanTaskDtoList;
		} catch(Exception e) {
			logger.error("", e);
			throw new BusinessException(BusinessExceptionEnum.GET_SYNCTASKINFO_FAILED,e);
		}
	}


	public void createVideoFile(VideoFile videoFile)
	{
		if(null == videoFile){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "video file object is null");
		}
		if(StringUtil.isBlank(videoFile.getFileId())){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "file id is null or blank");
		}
		if( StringUtil.isBlank(videoFile.getFileType())){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "file type is null or blank");
		}
		if(StringUtil.isBlank(videoFile.getDeviceId())) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "device id is null or blank");
		}
		try{
			//视频类型(从缓存里获取信息)
			String redisKey = ModuleConstants.VIDEO_PLAN_INFO + videoFile.getPlanId();
			Integer videoType = RedisCacheUtil.hashGet(redisKey,ModuleConstants.VIDEO_PLAN_INFO_KEY_PLANTYPE,Integer.class);
			if (videoType==null) {
				VideoPayRecord videoPayRecord = videoPlanService.getLastVideoPayRecord(videoFile.getPlanId());
				VideoPlanTypeResp resp = orderApi.getVideoPlanType(videoPayRecord.getOrderId(), videoPayRecord.getPackageId());
				if (resp!=null){
					videoType = resp.getPackageType();
					RedisCacheUtil.hashPut(redisKey,ModuleConstants.VIDEO_PLAN_INFO_KEY_PLANTYPE,videoType.toString(),false);
					RedisCacheUtil.hashPut(redisKey,ModuleConstants.VIDEO_PLAN_INFO_KEY_PACKAGE_EVENTNUM_FULLHOUR, resp.getEventOrFulltimeAmount(),false);
				}
			}
			videoFile.setVideoType(videoType.toString());
			//计算视频结束时间
			if(null != videoFile.getVideoStartTime()){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(videoFile.getVideoStartTime());
				calendar.add(Calendar.SECOND,Math.round(videoFile.getVideoLength()));
				videoFile.setVideoEndTime(calendar.getTime());
			}else{
				//videoStartTime是分片片键，需给个默认时间，否则mongodb报错
				videoFile.setVideoStartTime(new Date());
			}
			VideoFileEntity videoFileEntity = new VideoFileEntity();
			BeanUtil.copyProperties(videoFile, videoFileEntity);
			videoFileEntity.setCreateTime(new Date());
			videoFileEntity.setDataStatus(DataStatusEnum.VALID.getCode());
			VideoFileQueue.put(videoFileEntity);
			//全时录影的话需要记录到redis里，方便后期定时任务扫描全时录影溢出的数据
			if (VideoPlanTypeEnum.ALL_TIME.getCode().compareTo(videoType)==0 && null != videoFile.getVideoStartTime()){
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd:HH");
				String dateStr = simpleDateFormat.format(videoFile.getVideoStartTime());
				RedisCacheUtil.setPush(ModuleConstants.VIDEO_PLAN_ALL_TIME + dateStr,videoFile.getPlanId(),false);
			}
		}catch (Exception e){
			String err = "create videoFile in db occur error.";
			err.concat(", TenantId=" + videoFile.getTenantId());
			err.concat(", FileId=" + videoFile.getFileId());
			err.concat(", PlanId=" + videoFile.getPlanId());
			err.concat(", DeviceId=" + videoFile.getDeviceId());
			logger.error(err, e);
			throw new BusinessException(BusinessExceptionEnum.CREATE_VIDEOFILE_FAILED, err);
		}
	}
	/**
	  * @despriction：插入录影事件
	  * @author  yeshiyuan
	  * @created 2018/4/8 10:50
	  * @param videoEvent
	  * @return
	  */
	public void insertVideoEvent(VideoEvent videoEvent){
		if (videoEvent==null){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR,"param is null");
		}
		if (videoEvent.getEventOddurTime() == null){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR,"event_oddur_time is null");
		}
		if (StringUtil.isEmpty(videoEvent.getEventUuid())){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR,"eventUuid is null");
		}
		//事件是否已上传过，去重
		VideoEvent oldVideoEvent = videoEventMongoService.getEventByPlanIdAndEventUuid(videoEvent.getPlanId(),videoEvent.getEventUuid());
		boolean needRecord = true; //是否需要记录事件数
		RedisVideoPlanInfoVo redisVideoPlanInfoVo = null;
		if (oldVideoEvent!=null){
			needRecord = false;
			oldVideoEvent.setEventCode(videoEvent.getEventCode());
			oldVideoEvent.setEventName(videoEvent.getEventName());
			oldVideoEvent.setEventDesc(videoEvent.getEventDesc());
			oldVideoEvent.setEventStatus(videoEvent.getEventStatus());
			oldVideoEvent.setUpdateTime(new Date());
			videoEventMongoService.updateVideoEvent(oldVideoEvent);
		}else {
			//先从redis里获取已使用事件数量
			redisVideoPlanInfoVo = videoPlanManager.getVideoPlanInfoFromRedis(videoEvent.getPlanId());
			videoEvent.setTenantId(redisVideoPlanInfoVo.getTenantId());
			Date now = new Date();
			videoEvent.setCreateTime(now);
			videoEvent.setUpdateTime(now);
			videoEvent.setDataStatus(DataStatusEnum.VALID.getCode());
			try{
				VideoEventEntity eventEntity = new VideoEventEntity();
				BeanUtil.copyProperties(videoEvent, eventEntity);
				VideoEventQueue.put(eventEntity);
			}catch (Exception e){
				logger.error("插入录影事件失败：{}",JsonUtil.toJson(videoEvent),e);
				throw new BusinessException(BusinessExceptionEnum.INSERT_VIDEO_EVENT_FAILED, e);
			}
		}
		if (needRecord){
			//如果是计划类型为事件录影的话需要记录已录事件数
			if (VideoPlanTypeEnum.EVENT.getCode().compareTo(redisVideoPlanInfoVo.getPlanType())==0){
				videoPlanManager.updateUsedEventNum(videoEvent.getPlanId(),redisVideoPlanInfoVo.getPackageEventNumOrFullHour());
			}
		}
	}


	public List<String> judgeDeviceBandPlan(List<String> deviceIdList){
		if( null == deviceIdList || deviceIdList.size() == 0){
			return deviceIdList;
		}
		try {
			//查询已绑定计划的设备id
			List<String> banedIdList = videoManageService.selectBandedPlanDeviceIds(deviceIdList);
			deviceIdList.removeAll(banedIdList);
			return deviceIdList;
		} catch(Exception e) {
			logger.error("", e);
			throw new BusinessException(BusinessExceptionEnum.COUNT_DEVICEBANDINGPLAN_FAILED,e);
		}
	}

	/**
	 * @despriction：删除购买计划订单（redis数据）
	 * @author  yeshiyuan
	 * @created 2018/4/28 11:19
	 * @return
	 */
	public void deletePlanOrder(String orderId){
		RedisCacheUtil.delete(ModuleConstants.REDIS_PRE_PLAN_ORDERID + orderId);
	}

	public boolean countPlanById(String planId) {
		boolean exist = false;
		int count = this.videoManageService.countPlanById(planId);
		if(count > 0 ){
			exist = true;
		}
		return exist;
	}

	/**
	  * @despriction：校验设备是否已绑定计划
	  * @author  yeshiyuan
	  * @created 2018/5/17 14:11
	  * @param deviceId 设备id
	  * @return
	  */
	public boolean checkDeviceHasBindPlan(String deviceId){
		if (StringUtil.isBlank(deviceId)){
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_DEVICE_ID);
		}
		boolean result = false;
		int i = this.videoManageService.countDeviceHasBindPlan(deviceId);
		if (i>0){
			result = true;
		}
		return result;
	}
	/**
	 * 描述：获取最新的支付成功的订单
	 * @author 490485964@qq.com
	 * @date 2018/5/21 16:53
	 * @param
	 * @return
	 */
	public VideoPayRecordDto getLatestOrderByPlanIdAndStatus(Long tenantId, String planId, Integer planStatus) {
		if (null == tenantId) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "tenant id is null or blank");
		}
		if (StringUtils.isBlank(planId)) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan id is null or blank");
		}
		if(null == planStatus) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "null == planStatus");
		}
		VideoPayRecordDto videoPayRecordDto = videoManageService.getLatestOrderByPlanIdAndStatus(tenantId,planId,planStatus);
		if(null != videoPayRecordDto){
            //查套餐信息
			GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(videoPayRecordDto.getPackageId());
			if(null != goodsInfo){
				videoPayRecordDto.setPackageName(goodsInfo.getGoodsName());
				videoPayRecordDto.setPackagePrice(goodsInfo.getPrice() == null ? 0.00f : goodsInfo.getPrice().floatValue());
				videoPayRecordDto.setCurrency(goodsInfo.getCurrency());
			}
		}
		return videoPayRecordDto;
	}

	public List<Integer> getVideoStartTimeHourByDate(String searchDatePara,String planId){
		if(StringUtil.isBlank(searchDatePara)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "searchDate id is null or blank");
		}
		List<Integer> resultList = new ArrayList<>();
		for(int index = 0; index < 24; index++){
			resultList.add(0);
		}
		Date searchStartDate, searchEndDate = null;
		try {
			searchStartDate = ToolUtils.stringToDate(searchDatePara + " 00:00:00","yyyy-MM-dd HH:mm:ss");
			searchEndDate = ToolUtils.stringToDate(searchDatePara + " 23:59:59","yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			logger.error("getVideoStartTimeHourByDate searchDate format error",e);
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "searchDate format error");
		}
		List<Integer> dateHourList = videoFileMongoService.countSomeDayHasVideoHour(searchStartDate, searchEndDate, planId);
		if(null == dateHourList || dateHourList.size()==0){
			return null;
		}
		for(Integer hour : dateHourList){
			resultList.set(hour,1);
		}
		return resultList;
	}

	/**
	 * @despriction：查询video_pay_record
	 * @author  yeshiyuan
	 * @created 2018/5/22 10:18
	 * @param planId 计划id
	 * @param orderId 订单id
	 * @param tenantId 租户id
	 * @return
	 */
	public VideoPayRecord getVideoPayRecord(String planId, String orderId, Long tenantId){
		if (null == tenantId) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "tenant id is null or blank");
		}
		if (StringUtils.isBlank(planId)) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan id is null or blank");
		}
		if (StringUtil.isBlank(orderId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "order id is null or blank");
		}
		return videoManageService.getVideoPayRecord(planId, orderId, tenantId);
	}

	/**
	  * @despriction：退款计划中的某个订单
	  * @author  yeshiyuan
	  * @created 2018/5/22 13:47
	  * @param planId 计划id
	  * @param orderId 订单id
	  * @return
	  */
	public void refundOneOrderOfPlan(String planId,String orderId,Long tenantId){
		if (null == tenantId) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "tenant id is null or blank");
		}
		if (StringUtils.isBlank(planId)) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan id is null or blank");
		}
		if (StringUtil.isBlank(orderId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "order id is null or blank");
		}
		//video_pay_record对应记录失效
		videoManageService.updateVideoPayRecordOfPlanStatus(planId,orderId,tenantId, VprPlanStatusEnum.REFUND.getCode());
		//获取退款订单时间长度
		int dateDiff = videoManageService.getPayRecordDateDiff(planId, orderId, tenantId);
		//video_plan减去对应的时间
		videoManageService.updatePlanEndTime(planId,dateDiff);
	}
	/**
	 * @despriction：查询计划的其他购买记录
	 * @author  yeshiyuanPlan
	 * @created 2018/5/22 13:47
	 * @param planId 计划id
	 * @param orderId 订单id
	 * @return
	 */
	public List<VideoPlanOrderDto> queryPlanOtherPayRecord(String orderId, String planId) {
		if (StringUtils.isBlank(planId)) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan id is null or blank");
		}
		if (StringUtil.isBlank(orderId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "order id is null or blank");
		}
		List<VideoPlanOrderDto> planOrderDtos = videoManageService.queryPlanOtherPayRecord(orderId, planId);
		if (planOrderDtos!=null && planOrderDtos.size()>0){
			for (VideoPlanOrderDto planOrderDto:planOrderDtos ) {
				planOrderDto.setPackageId(CommonUtil.encryptId(Long.valueOf(planOrderDto.getPackageId())));
			}
		}
		return planOrderDtos;
	}

	/**
	 * 描述：计划解绑设备
	 * @author mao2080@sina.com
	 * @created 2018/5/24 9:57
	 * @param tenantId 租户id
	 * @param deviceId 设备id
	 * @return void
	 */
	public void planUnBandingDevice(Long tenantId, String deviceId){
		logger.info("planUnBandingDevice({}, {})", tenantId, deviceId);
		if (tenantId == null || StringUtil.isBlank(deviceId)){
			logger.error("PlanUnBandingDevice failed ：tenantId or deviceId is null");
			return;
		}
		String planId = this.videoManageService.getBandingDevicePlanId(tenantId, deviceId);
		if(!StringUtil.isBlank(planId)){
			this.videoManageService.planUnBandingDevice(tenantId, planId);
			//清除计划信息里绑定的设备id
			RedisCacheUtil.hashPut(ModuleConstants.VIDEO_PLAN_INFO + planId,ModuleConstants.VIDEO_PLAN_INFO_KEY_DEVICEID,"",false);
			this.videoFileMongoService.setVideoFileDataInvalid(planId);
			this.videoEventMongoService.setVideoEventDataInvalid(planId);
			this.createDeleteTask(planId, deviceId);
			//如果是事件类型 -> 清除已使用事件数
			Integer planType = videoPlanManager.getPlanType(planId);
			if (VideoPlanTypeEnum.EVENT.getCode().equals(planType)){
				RedisCacheUtil.delete(ModuleConstants.VIDEO_PLAN_USED_EVENT_NUM + planId);
			}
		}
		logger.info("PlanUnBandingDevice success.");
	}

	/**
	 * 描述：向缓存中添加任务
	 * @author mao2080@sina.com
	 * @created 2018/5/24 11:48
	 * @param planId 计划id
	 * @param deviceId 设备id
	 * @return void
	 */
	public void createDeleteTask(String planId, String deviceId){
		//添加待删除的任务队列
		String uuid = UUID.randomUUID().toString();
		RedisCacheUtil.listPush(ModuleConstants.IPC_UNBANDING_TASK_KEY_QUEUE, uuid, false);
		String taskKey = ModuleConstants.IPC_UNBANDING_TASK_KEY.concat(uuid);
		String taskVal = ModuleConstants.IPC_UNBANDING_TASK_VAL.replace("@deviceId", deviceId==null?"":deviceId).replace("@planId", planId);
		RedisCacheUtil.valueSet(taskKey, taskVal);
	}

	/**
	 * @despriction：把视频的相关数据（video_file,video_event）置为无效
	 * @author  yeshiyuan
	 * @created 2018/6/12 17:31
	 * @param planId 计划id
	 * @return
	 */
	public void dealPlanAllTimeOver(String planId,String videoStartTime) {
		videoStartTime = videoStartTime + ":59:59.999";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss.SSS");
		Date videoStartDate = null;
		try {
			videoStartDate = simpleDateFormat.parse(videoStartTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//把某一时间前的video相关的数据置为失效（video_file,video_event）
		this.updateVideoDataToInvalid(planId,videoStartDate);
	}

	/**
	 * @despriction：清除计划事件录影溢出数据（video_file,video_event）
	 * @author  yeshiyuan
	 * @created 2018/6/13 17:25
	 * @param planId 计划id
	 * @return
	 */
	public void dealPlanEventOver(String planId) {
		//找到溢出数据的时间节点
		Date eventOverTime = videoEventMongoService.getVideoLimitTime(planId);
		if (eventOverTime==null){
			return;
		}
		//把某一时间前的video相关的数据置为失效（video_file,video_event）
		int delEventNum = this.updateVideoDataToInvalid(planId,eventOverTime);
		//修改计划的实际事件录影数量
		String oldUsedEventNum = RedisCacheUtil.valueGet(ModuleConstants.VIDEO_PLAN_USED_EVENT_NUM + planId);
		Integer nowUsedEventNum = Integer.valueOf(oldUsedEventNum) - delEventNum;
		RedisCacheUtil.valueSet(ModuleConstants.VIDEO_PLAN_USED_EVENT_NUM + planId,nowUsedEventNum.toString(),ModuleConstants.VIDEO_PLAN_USED_EVENT_NUM_EXPIRETIME);
		//把计划从待删除队列中清除
		RedisCacheUtil.setRemove(ModuleConstants.VIDEO_PLAN_EVENT_OVER,planId,false);
	}

	/**
	  * @despriction：把某一时间前的video相关的数据置为失效（video_file,video_event）
	  * @author  yeshiyuan
	  * @created 2018/6/13 19:03
	  * @param null
	  * @return
	  */
	private int updateVideoDataToInvalid(String planId, Date videoStartTime){
		int delEventNum;
		//把录影的文件置为失效
		this.videoFileMongoService.setVideoFileDataInvalid(planId, videoStartTime);
		//找到某一时间前的事件uuid
		//List<String> eventUuids = this.videoEventMongoService.selectEventUuidsByTime(planId, videoStartDate);
		//根据事件uuid找到对应的文件置为失效
		/*if (eventUuids!=null && !eventUuids.isEmpty()){
			List<List<String>> lists = new ArrayList<>();
			if (eventUuids.size()>100){
				lists = com.iot.common.util.CommonUtil.dealBySubList(eventUuids,100);
			}else {
				lists.add(eventUuids);
			}
			for (List<String> list: lists) {
				this.videoManageService.batchUpdateVideoFileByEventUuid(list);
			}
		}*/
		//把对应的事件置为失效
		delEventNum = this.videoEventMongoService.setVideoEventDataInvalid(planId,videoStartTime);
		//生成每一个计划待清除垃圾数据的定时任务key
		//String deviceId = videoManageService.getDeviceId(planId);
		this.createDeleteTask(planId, null);
		return delEventNum;
	}


	/**
	 * @despriction：处理过期计划的相关数据（video_file,video_event，redis缓存信息、解绑设备）
	 * @author  yeshiyuan
	 * @created 2018/6/15 13:41
	 * @param null
	 * @return
	 */
	public void dealPlanExpireRelateData(String planId) {
		if (StringUtil.isBlank(planId)){
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_PLANID);
		}
		RedisVideoPlanInfoVo redisVideoPlanInfoVo = videoPlanManager.getVideoPlanInfoFromRedis(planId);
		//解除计划绑定设备
		this.videoManageService.planUnBandingDevice(redisVideoPlanInfoVo.getTenantId(), planId);
		//计划的相关数据设置失效
		this.videoFileMongoService.setVideoFileDataInvalid(planId);
		this.videoEventMongoService.setVideoEventDataInvalid(planId);
		//清除计划任务
		videoManageService.deleteTaskByPlanId(redisVideoPlanInfoVo.getTenantId(), redisVideoPlanInfoVo.getUserId(), planId);
		//创建一个待删除失效数据的定时任务
		this.createDeleteTask(planId, redisVideoPlanInfoVo.getDeviceId());
		//删除计划在redis中缓存信息
		RedisCacheUtil.delete(ModuleConstants.VIDEO_PLAN_INFO + planId);
		//如果是事件类型 -> 清除已使用事件数
		if (VideoPlanTypeEnum.EVENT.getCode().compareTo(redisVideoPlanInfoVo.getPlanType())==0){
			RedisCacheUtil.delete(ModuleConstants.VIDEO_PLAN_USED_EVENT_NUM + planId);
		}
	}

	/**
	 * @despriction：通过事件id获取事件对应的视频文件
	 * @author  yeshiyuan
	 * @created 2018/6/22 16:44
	 * @param null
	 * @return
	 */
	public List<VideoTsFileDto> getVideoFileListByEventUuid(String planId,String eventUuid,String fileType, Date eventOddurTime) {
		if (StringUtil.isBlank(planId)){
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_PLANID);
		}
		if (StringUtil.isBlank(eventUuid)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR,"eventUuid is null");
		}
		if (eventOddurTime == null){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR,"eventOddurTime is null");
		}
		List<VideoTsFileDto> list = videoFileMongoService.getVideoFileListByEventUuid(planId, eventUuid, fileType, eventOddurTime);
		return list;
	}

	/**
	 * @despriction：统计IPC录影日期
	 * @author  yeshiyuan
	 * @created 2018/7/26 10:56
	 * @return
	 */
	public List<String> countVideoDate(String planId, Date startDate, Date endDate) {
		//校验计划是否存在
		String redisLKey = ModuleConstants.VIDEO_PLAN_INFO + planId;
		String planTypeStr = RedisCacheUtil.hashGetString(redisLKey, ModuleConstants.VIDEO_PLAN_INFO_KEY_PLANTYPE);
		if (StringUtil.isBlank(planTypeStr)){
			throw new BusinessException(BusinessExceptionEnum.PLAN_NOT_EXISTS);
		}
		//时间节点判断
		Date limitTime = videoPlanManager.getLimitTime(planId);
		Date[] dateArray =  videoPlanManager.getStaTimeAndEndTime(startDate, endDate, limitTime);
		if(dateArray == null){
			return Collections.emptyList();
		}
		Integer planType = Integer.valueOf(planTypeStr);
		List<String> dateList = null;
		if (planType == VideoPlanTypeEnum.ALL_TIME.getCode()){
			//全时暂无此需求
			//dateList = videoManageService.countAllTimeVideoDate(planId, dateArray[0], dateArray[1]);
		}else{
			dateList = videoEventMongoService.selectHasEventVideoDay(planId, dateArray[0], dateArray[1]);
		}
		return dateList;
	}



}

package com.iot.video.manager;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.CalendarUtil;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.redis.RedisCacheUtil;
import com.iot.video.contants.ModuleConstants;
import com.iot.video.dto.*;
import com.iot.video.entity.VideoPlan;
import com.iot.video.enums.PlanExecStatusEnum;
import com.iot.video.enums.PlanExpireFlagEnum;
import com.iot.video.enums.VideoPlanTypeEnum;
import com.iot.video.exception.BusinessExceptionEnum;
import com.iot.video.mongo.entity.VideoEventEntity;
import com.iot.video.mongo.entity.VideoFileEntity;
import com.iot.video.mongo.service.VideoEventMongoService;
import com.iot.video.mongo.service.VideoFileMongoService;
import com.iot.video.mongo.vo.MongoPageInfo;
import com.iot.video.service.VideoManageService;
import com.iot.video.service.VideoPlanService;
import com.iot.video.vo.VideoEventJpg;
import com.iot.video.vo.redis.RedisVideoPlanInfoVo;
import com.iot.video.vo.resp.CheckBeforeUploadResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VideoPlanManager {

	private static final Logger logger  = LoggerFactory.getLogger(VideoPlanManager.class);


	@Autowired
	private VideoPlanService videoPlanService;
	
	@Autowired
	private VideoManageService videoManageService;

	@Autowired
	private GoodsServiceApi goodsServiceApi;

	@Autowired
	private VideoEventMongoService videoEventMongoService;

	@Autowired
	private VideoFileMongoService videoFileMongoService;

	/**
	 * 描述：查询一段时间的事件列表
	 * @author mao2080@sina.com
	 * @created 2018/3/23 14:39
	 * @param vespDto 查询参数
	 * @return java.util.List<com.lds.iot.video.dto.VideoEventDto>
	 */
	/*public PageInfo<VideoEventDto> getVideoEventList(VideoEventParamDto vespDto){
		if((StringUtil.isBlank(vespDto.getPlanId()) && StringUtil.isBlank(vespDto.getDeviceId())) || null == vespDto.getTenantId()) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
		}
		Date limitTime = this.getLimitTime(vespDto.getPlanId());
		Date[] dateArray =  this.getStaTimeAndEndTime(vespDto.getStartTime(), vespDto.getEndTime(), limitTime);
		if(dateArray == null){
			return new PageInfo<>();
		}
		Integer pageNum = vespDto.getPageNum();
		Integer pageSize = vespDto.getPageSize();
		if (null == pageNum) {
			pageNum = 0;
			vespDto.setPageNum(pageNum);
		}
		if (null == pageSize || 0 == pageSize) {
			pageSize=10;
			vespDto.setPageSize(pageSize);
		}
		PageHelper.startPage(pageNum,pageSize,true);
		Page<VideoEventDto> videoEventDtoPage = new Page<VideoEventDto>();
		MongoPageInfo<VideoEventEntity> mongoPage = null;
		try {
			vespDto.setStartTime(dateArray[0]);
			vespDto.setEndTime(dateArray[1]);
			if (StringUtil.isEmpty(vespDto.getPlanId()) && StringUtil.isNotEmpty(vespDto.getDeviceId())){
				String planId = this.videoManageService.getPlanId(vespDto.getDeviceId());
				if (!StringUtil.isBlank(planId)){
					vespDto.setPlanId(planId);
				}
			}
			mongoPage = this.videoEventMongoService.getVideoEventList(vespDto);
			VideoEventDto videoEventDto = null;
			for(VideoEventEntity videoEventVo : mongoPage.getList()) {
				videoEventDto = new VideoEventDto();
				BeanUtils.copyProperties(videoEventVo, videoEventDto);
				videoEventDto.setEventId(videoEventVo.getEventUuid());
				videoEventDtoPage.add(videoEventDto);
			}
		}catch (Exception e){
			String err = "get videoEventList from db occur error.";
			err.concat("plan_id=" + vespDto.getPlanId());
			err.concat("device_id=" + vespDto.getDeviceId());
			err.concat(", startTime=" + vespDto.getStartTime());
			err.concat(", endTime=" + vespDto.getEndTime());
			logger.error(err, e);
			throw new BusinessException(BusinessExceptionEnum.GET_VIDEOEVENTLIST_FAILED);
		}
		videoEventDtoPage.setTotal(mongoPage.getTotalCount());
		videoEventDtoPage.setPageNum(pageNum);
		videoEventDtoPage.setPageSize(pageSize);
		PageInfo<VideoEventDto> pageInfo = new PageInfo<VideoEventDto>(videoEventDtoPage);
		return pageInfo;
	}*/

	/**
	 * 描述：查询一段时间的事件数量
	 * @author 490485964@qq.com
	 * @date 2018/6/6 14:30
	 * @param vespDto 查询参数
	 * @return
	 */
	public int getVideoEventCount(VideoEventParamDto vespDto){
		if(StringUtil.isBlank(vespDto.getPlanId()) || null == vespDto.getTenantId()) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
		}
		Date limitTime = this.getLimitTime(vespDto.getPlanId());
		Date[] dateArray =  this.getStaTimeAndEndTime(vespDto.getStartTime(), vespDto.getEndTime(), limitTime);
		if(dateArray == null){
			return 0;
		}
		try{
			vespDto.setStartTime(dateArray[0]);
			vespDto.setEndTime(dateArray[1]);
			int count = this.videoEventMongoService.getVideoEventCount(vespDto).intValue();
			//事件数溢出时则已计划可录事件数为准
			String planEventNum = RedisCacheUtil.hashGetString(ModuleConstants.VIDEO_PLAN_INFO + vespDto.getPlanId(), ModuleConstants.VIDEO_PLAN_INFO_KEY_PACKAGE_EVENTNUM_FULLHOUR);
			int eventNum;
			if (StringUtil.isBlank(planEventNum)){
				RedisVideoPlanInfoVo videoPlanInfoVo = getVideoPlanInfoFromRedis(vespDto.getPlanId());
				eventNum = videoPlanInfoVo.getPackageEventNumOrFullHour();
			}else{
				eventNum = Integer.valueOf(planEventNum);
			}
			if (eventNum < count ){
				count = eventNum;
			}
			return count;
		}catch (Exception e){
			String err = "get getVideoEventCount from db occur error.";
			err.concat("plan_id=" + vespDto.getPlanId());
			err.concat(", startTime=" + vespDto.getStartTime().toString());
			err.concat(", endTime=" + vespDto.getEndTime().toString());
			logger.error(err, e);
			throw new BusinessException(BusinessExceptionEnum.GET_VIDEOFILECOUNT_FAILED);
		}
	}



	/**
	 * 描述：获取一段时间视频文件列表
	 * @author mao2080@sina.com
	 * @created 2018/3/23 14:39
	 * @param vfpDto 查询参数
	 * @return java.util.List<com.lds.iot.video.dto.VideoFileDto>
	 */
	public List<VideoFileDto> getVideoFileList(VideoFileParamDto vfpDto){
		if((StringUtil.isBlank(vfpDto.getPlanId()) && StringUtil.isBlank(vfpDto.getDeviceId()))
				|| StringUtil.isBlank(vfpDto.getFileType())
				|| null == vfpDto.getTenantId()) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
		}
		Date limitTime = this.getLimitTime(vfpDto.getPlanId());
		Date[] dateArray =  this.getStaTimeAndEndTime(vfpDto.getStartTime(), vfpDto.getEndTime(), limitTime);
		if(dateArray == null){
			return Collections.emptyList();
		}
		try {
			vfpDto.setStartTime(dateArray[0]);
			vfpDto.setEndTime(dateArray[1]);
			List<VideoFileDto> dtos = new ArrayList<>();
			List<VideoFileEntity> list = videoFileMongoService.getVideoFileList(vfpDto);
			list.forEach(o ->{
				VideoFileDto dto = new VideoFileDto();
				BeanUtil.copyProperties(o, dto);
				dtos.add(dto);
			});
			return dtos;
		}catch (Exception e){
			String err = "get VideoFileList from db occur error.";
			err.concat("plan_id=" + vfpDto.getPlanId());
			err.concat(", startTime=" + vfpDto.getStartTime().toString());
			err.concat(", endTime=" + vfpDto.getEndTime().toString());
			err.concat(", device_id=" + vfpDto.getDeviceId());
			err.concat(", file_type=" + vfpDto.getFileType());
			logger.error(err, e);
			throw new BusinessException(BusinessExceptionEnum.GET_VIDEOFILELIST_FAILED);
		}
	}

	/**
	 * 描述：获取查询时间
	 * @author mao2080@sina.com
	 * @created 2018/6/15 10:55
	 * @param staTime 开始时间
	 * @param endTime 结束时间
	 * @param limitTime 临界点时间
	 * @return java.util.Date[]
	 */
	public Date[] getStaTimeAndEndTime(Date staTime, Date endTime, Date limitTime) {
		Date nowTime = new Date();
		if(endTime == null || staTime == null || endTime.before(staTime) || staTime.after(nowTime)){
			return null;
		}
		if(endTime.after(nowTime)){
			endTime = nowTime;
		}
		if(limitTime == null){
			return new Date[]{staTime, endTime};
		}
		if(limitTime.after(endTime)){
			return null;
		}
		if(staTime.before(limitTime)){
			staTime = limitTime;
		}
		return new Date[]{staTime, endTime};
	}

	/**
	 * 描述：获取临界点时间（全时录影的当前时间减去套餐数量如：168H，事件录影事件套餐event数desc排序后的时间）
	 * @author mao2080@sina.com
	 * @created 2018/6/15 10:53
	 * @param planId
	 * @return java.util.Date
	 */
	public Date getLimitTime(String planId) {
		Date limitTime = null;
		//获取套餐类型
		Integer planType = null;
		Integer packageEventNumOrFullHour = null;
		RedisVideoPlanInfoVo redisVideoPlanInfoVo = getVideoPlanInfoFromRedis(planId);
		if(null != redisVideoPlanInfoVo ){
			planType = redisVideoPlanInfoVo.getPlanType();
			packageEventNumOrFullHour = redisVideoPlanInfoVo.getPackageEventNumOrFullHour();
		}
		//全时录影 限制时间点 当前时间前168小时 从缓存读取
		if(null !=planType && ModuleConstants.PACKAGE_ALL == planType  && null != packageEventNumOrFullHour){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -packageEventNumOrFullHour.intValue());
			CalendarUtil.clearMSM(cal);
			limitTime = cal.getTime();
		}//事件录影  限制时间点 第301条事件触发时间
		else if(null !=planType && ModuleConstants.PACKAGE_EVENT == planType){
			limitTime =  videoEventMongoService.getVideoLimitTime(planId);
		}
		return limitTime;
	}
	/**
	 * 描述：修改录影排序
	 * @author mao2080@sina.com
	 * @created 2018/3/23 14:38
	 * @param poDto 排序对象
	 * @return void
	 */
	public void updatePlanOrder(PlanOrderParamDto poDto){
		if(null == poDto.getOrderIds()  || poDto.getOrderIds().size() == 0 || null == poDto.getPlanIds()
				|| poDto.getPlanIds().size() == 0 || poDto.getOrderIds().size() != poDto.getPlanIds().size()) {
			logger.error("updatePlanOrder param.error");
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
		}
		try {
			this.videoPlanService.updatePlanOrder(poDto);
		}catch (Exception e){
			String err = "updatePlanOrder in db occur error.";
			err.concat("planIds=" + poDto.getPlanIds());
			err.concat(", orderIds=" + poDto.getOrderIds());
			logger.error(err, e);
			throw new BusinessException(BusinessExceptionEnum.UPDATE_PLANORDER_FAILED);
		}

	}

	/**
	 * 描述：获取事件图片URL列表
	 * @author mao2080@sina.com
	 * @created 2018/3/23 15:29
	 * @param epDto 查询参数VO
	 * @return com.github.pagehelper.PageInfo<com.lds.iot.video.dto.EventDto>
	 */
	public PageInfo<EventDto> getEventPhotoList(EventParamDto epDto){
		if((StringUtil.isBlank(epDto.getPlanId()) && StringUtil.isBlank(epDto.getDeviceId())) || null == epDto.getTenantId()) {
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
		}
		Date limitTime = this.getLimitTime(epDto.getPlanId());
		Date[] dateArray =  this.getStaTimeAndEndTime(epDto.getEventStartTime(), epDto.getEventEndTime(), limitTime);
		if(dateArray == null){
			return new PageInfo<>();
		}
		try {
			epDto.setEventStartTime(dateArray[0]);
			epDto.setEventEndTime(dateArray[1]);
			//先查找事件
			MongoPageInfo<VideoEventEntity> mongoPageInfo = videoEventMongoService.getEventPhotoList(epDto);
			List<String> eventUuidList = mongoPageInfo.getList().stream().map(videoEventEntity -> {
				return videoEventEntity.getEventUuid();
			}).collect(Collectors.toList());
			//再查找事件对应的图片
			Map<String, VideoEventJpg> jpgMap = videoFileMongoService.getVideoEventJpgPicture(epDto.getPlanId(), eventUuidList);
			Page<EventDto> eventDtoPage = new Page<EventDto>();
			for(VideoEventEntity vo : mongoPageInfo.getList()) {
				EventDto eventDto = new EventDto();
				BeanUtils.copyProperties(vo, eventDto);
				eventDto.setEventId(vo.getEventUuid());
				VideoEventJpg jpg = jpgMap.get(vo.getEventUuid());
				if (jpg != null) {
					eventDto.setFilePath(jpg.getFilePath());
					eventDto.setFileId(jpg.getFileId());
					eventDto.setRotation(jpg.getRotation());
				}
				eventDtoPage.add(eventDto);
			}
			eventDtoPage.setTotal(mongoPageInfo.getTotalCount());
			eventDtoPage.setPageNum(mongoPageInfo.getCurrentPage());
			eventDtoPage.setPageSize(mongoPageInfo.getPageSize());
			PageInfo<EventDto> pageInfo = new PageInfo<EventDto>(eventDtoPage);
			return pageInfo;
		}catch (Exception e){
			String err = "get EventPhotoList from db occur error.";
			err.concat("device_id=" + epDto.getDeviceId());
			err.concat(", plan_id=" + epDto.getPlanId());
			err.concat(", eventStartTime=" + epDto.getEventStartTime());
			err.concat(", eventEndTime=" + epDto.getEventEndTime());
			err.concat(", eventCodeList=" + epDto.getEventCodeList());
			logger.error(err, e);
			throw new BusinessException(BusinessExceptionEnum.GET_EVENTPHOTOLIST_FAILED);
		}
	}

	/**
	 * 描述：获取计划最后一帧图片
	 * @author mao2080@sina.com
	 * @created 2018/3/23 15:48
	 * @param lppDto 查询参数VO
	 * @return java.util.List<com.lds.iot.video.dto.PlanLastPicDto>
	 */
	public List<PlanLastPicDto> getPlanLastPic(@RequestBody LastPicParamDto lppDto){
		List<PlanLastPicDto> planLastPicDtoList = new ArrayList<>();
		try {
			if (lppDto.getDeviceIdList()!=null && lppDto.getDeviceIdList().size()>0){
				for (String deviceId : lppDto.getDeviceIdList()) {
					String planId = videoManageService.getPlanId(deviceId);
					VideoFileEntity videoFileEntity = videoFileMongoService.getLastTsVideoFile(planId, deviceId);
					PlanLastPicDto dto = new PlanLastPicDto(planId, deviceId, videoFileEntity.getFileId(), videoFileEntity.getFilePath());
					planLastPicDtoList.add(dto);
				}
			}
		}catch (Exception e){
			String err = "get PlanLastPic from db occur error.";
			err.concat("user_id=" + lppDto.getUserId());
			err.concat(", deviceId=" + lppDto.getDeviceIdList());
			logger.error(err, e);
			throw new BusinessException(BusinessExceptionEnum.GET_PLANLASTPIC_FAILED);
		}
		return planLastPicDtoList;
	}

	/**
	 * 
	 * 描述：根据设备id查询计划类型
	 * @author 李帅
	 * @created 2018年4月27日 下午4:04:09
	 * @since 
	 * @param tenantId
	 * @param userId
	 * @param deviceId
	 * @return com.lds.iot.video.dto.PlanInfoDto
	 */
	public PlanInfoDto getPlanType(Long tenantId, String userId, String deviceId){
		if(StringUtil.isEmpty(deviceId)){
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_DEVICEID);
		}
		PlanInfoDto planInfoDto = null;
		 try {
			 planInfoDto = this.videoPlanService.getPlanType(tenantId, userId, deviceId);
			 if(!CommonUtil.isEmpty(planInfoDto)){
				 String planId = planInfoDto.getPlanId();
				 Long packageId = videoPlanService.getPackageIdByPlanId(planId);
				 if(null == packageId){
					 throw new BusinessException(BusinessExceptionEnum.PACKAGEID_NOT_EXISTS);
				 }
				 GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(packageId);
				 if(null != goodsInfo){
					 planInfoDto.setPackageType(goodsInfo.getPrice() == null ? "" :goodsInfo.getTypeId().toString());
				 }
			 }
		} catch (Exception e) {
		 	logger.error("getPlanType ",e);
			throw new BusinessException(BusinessExceptionEnum.ISBINDPLAN_FAILED);
		}
	 	return planInfoDto;
	}

	/**
	 * @despriction：从redis中获取计划录影最后的一张截图S3url
	 * @author  yeshiyuan
	 * @created 2018/5/8 10:01
	 * @param planId 计划id
	 * @param fileId 文件id
	 * @return
	 *//*
	public String getScreenShowFromRedis(String planId,String fileId){
		if (StringUtil.isBlank(planId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR,"planId is null");
		}
		if (StringUtil.isBlank(fileId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR,"fileId is null");
		}
		//先从redis[hash类型]查询缓存(规则：video:screenshot:计划id  文件id  文件对应s3url)
		String key = VIDEO_SCREENSHOT_KEY + planId;
		return RedisCacheUtil.hashGetString(key,fileId);
	}

	*//**
	 * @despriction：将计划录影最后的一张截图S3url保存S3url
	 * @author  yeshiyuan
	 * @created 2018/5/8 10:01
	 * @param planId 计划id
	 * @param fileId 文件id
	 * @param s3Url s3Url
	 * @return
	 *//*
	public void saveScreenShowInRedis(String planId,String fileId,String s3Url){
		if (StringUtil.isBlank(planId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR,"planId is null");
		}
		if (StringUtil.isBlank(fileId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR,"fileId is null");
		}
		if (StringUtil.isBlank(s3Url)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR,"s3Url is null");
		}
		//缓存时间小于s3保存文件时间（怕两者时间不同步,暂时设1分钟时间差）
		String key = VIDEO_SCREENSHOT_KEY + planId;
		RedisCacheUtil.hashPut(key,fileId,s3Url,3540L,false);
	}*/

	/**
	 * @despriction：获取video_plan详情
	 * @author  yeshiyuan
	 * @created 2018/5/17 15:37
	 * @param planId 计划id
	 * @param userId 用户uuid
	 * @return
	 */
	public VideoPlan getVideoPlan(String planId, String userId){
		if (StringUtil.isBlank(planId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR,"planId is null");
		}
		if (StringUtil.isBlank(userId)){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR,"userId is null");
		}
		return videoPlanService.getVideoPlan(planId,userId);
	}

	/**
	 * @despriction：从redis中获取计划相关信息
	 * @author  yeshiyuan
	 * @created 2018/6/12 13:42
	 * @param null
	 * @return
	 */
	public RedisVideoPlanInfoVo getVideoPlanInfoFromRedis(String planId){
		if (StringUtil.isBlank(planId)){
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_PLANID);
		}
		String redisLKey = ModuleConstants.VIDEO_PLAN_INFO + planId;
		Map<String,String> map = RedisCacheUtil.hashGetAll(redisLKey, String.class,false);
		if (map==null || map.isEmpty() || !map.containsKey(ModuleConstants.VIDEO_PLAN_INFO_KEY_DEVICEID) || !map.containsKey(ModuleConstants.VIDEO_PLAN_INFO_KEY_PLANTYPE)
				|| !map.containsKey(ModuleConstants.VIDEO_PLAN_INFO_KEY_PACKAGE_EVENTNUM_FULLHOUR) || !map.containsKey(ModuleConstants.VIDEO_PLAN_INFO_KEY_TENANTID)
				|| !map.containsKey(ModuleConstants.VIDEO_PLAN_INFO_KEY_USER_ID)){
			VideoPlan videoPlan = videoPlanService.getVideoPlan(planId,null);
			if (videoPlan == null){
				throw new BusinessException(BusinessExceptionEnum.PLAN_NOT_EXISTS);
			}
			Long packageId = videoPlan.getPackageId();
			if(null == packageId){
				throw new BusinessException(BusinessExceptionEnum.PACKAGEID_NOT_EXISTS);
			}
			GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(packageId);
			if(null == goodsInfo){
				throw new BusinessException(BusinessExceptionEnum.PACKAGEINFO_NOT_EXISTS);
			}else{
				map.put(ModuleConstants.VIDEO_PLAN_INFO_KEY_PLANTYPE, goodsInfo.getTypeId().toString());
				map.put(ModuleConstants.VIDEO_PLAN_INFO_KEY_PACKAGE_EVENTNUM_FULLHOUR, goodsInfo.getStandard());
			}
			String deviceId = videoPlan.getDeviceId();
			if (deviceId!=null && !"".equals(deviceId)){
				map.put(ModuleConstants.VIDEO_PLAN_INFO_KEY_DEVICEID,deviceId);
			}
			Long tenantId = videoPlan.getTenantId();
			map.put(ModuleConstants.VIDEO_PLAN_INFO_KEY_TENANTID,tenantId.toString());
			//计划属于哪个用户
			String userId = videoPlan.getUserId();
			map.put(ModuleConstants.VIDEO_PLAN_INFO_KEY_USER_ID, userId);
			//计划是否过期
			if (videoPlan.getPlanExecStatus()>=2){
				map.put(ModuleConstants.VIDEO_PLAN_INFO_KEY_EEPIREFLAG, PlanExpireFlagEnum.expire.getCode());
			}
			RedisCacheUtil.hashPutAll(redisLKey,map,false);
		}
		RedisVideoPlanInfoVo redisVideoPlanInfoVo = new RedisVideoPlanInfoVo();
		redisVideoPlanInfoVo.setPlanId(planId);
		redisVideoPlanInfoVo.setDeviceId( map.get(ModuleConstants.VIDEO_PLAN_INFO_KEY_DEVICEID));
		redisVideoPlanInfoVo.setPackageEventNumOrFullHour(Integer.valueOf(map.get(ModuleConstants.VIDEO_PLAN_INFO_KEY_PACKAGE_EVENTNUM_FULLHOUR)));
		redisVideoPlanInfoVo.setPlanType(Integer.valueOf(map.get(ModuleConstants.VIDEO_PLAN_INFO_KEY_PLANTYPE)));
		redisVideoPlanInfoVo.setTenantId(Long.valueOf(map.get(ModuleConstants.VIDEO_PLAN_INFO_KEY_TENANTID)));
		redisVideoPlanInfoVo.setUserId(map.get(ModuleConstants.VIDEO_PLAN_INFO_KEY_USER_ID));
		redisVideoPlanInfoVo.setExpireFlag(map.get(ModuleConstants.VIDEO_PLAN_INFO_KEY_EEPIREFLAG));
		if (VideoPlanTypeEnum.EVENT.getCode().compareTo(redisVideoPlanInfoVo.getPlanType())==0){
			//事件录影才有记录已录事件数
			redisVideoPlanInfoVo.setUsedEventNum(getUsedEventNum(planId));
		}
		return redisVideoPlanInfoVo;
	}
	
	/**
	  * @despriction：描述
	  * @author  yeshiyuan
	  * @created 2018/6/12 15:04
	  * @param null
	  * @return 
	  */
	public void updateVideoPlanInfoOfRedis(RedisVideoPlanInfoVo redisVideoPlanInfoVo) {
		if (redisVideoPlanInfoVo == null){
			throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR,"RedisVideoPlanInfoVo is null");
		}
		try {
			String planId = redisVideoPlanInfoVo.getPlanId();
			redisVideoPlanInfoVo.setPlanId(null);
			Long tenantId = videoManageService.getTenantId(planId);
			redisVideoPlanInfoVo.setTenantId(tenantId);
			Map<String,Object> map = BeanUtil.convertBeanToMap(redisVideoPlanInfoVo);
			RedisCacheUtil.hashPutAll(ModuleConstants.VIDEO_PLAN_INFO + planId, map,false);
		} catch (Exception e) {
			logger.error("修改redis中计划相关信息报错：",e);
		}
	}

	/**
	 * @despriction：修改redis中事件录影类型计划已录制事件数量
	 * @author  yeshiyuan
	 * @created 2018/6/13 16:10
	 * @param null
	 * @return
	 */
	public void updateUsedEventNum(String planId,Integer packageEventNum) {
		Long usedEventNum = RedisCacheUtil.incr(ModuleConstants.VIDEO_PLAN_USED_EVENT_NUM + planId,0L);
		if (usedEventNum.longValue() > packageEventNum.longValue()){
			//记录事件录影溢出待清除垃圾的计划
			RedisCacheUtil.setPush(ModuleConstants.VIDEO_PLAN_EVENT_OVER,planId,false);
		}
	}

	/**
	 * @despriction：从redis中通过计划id获取设备id
	 * @author  yeshiyuan
	 * @created 2018/6/13 16:53
	 * @param null
	 * @return
	 */
	public String getDeviceIdByPlanIdFromRedis(String planId) {
		String devId = RedisCacheUtil.hashGetString(ModuleConstants.VIDEO_PLAN_INFO + planId,ModuleConstants.VIDEO_PLAN_INFO_KEY_DEVICEID);
		if (StringUtil.isBlank(devId)){
			devId = videoManageService.getDeviceId(planId);
			RedisCacheUtil.hashPut(ModuleConstants.VIDEO_PLAN_INFO+planId,ModuleConstants.VIDEO_PLAN_INFO_KEY_DEVICEID,devId,false);
		}
		return devId;
	}

	/**
	 * @despriction：查找计划绑定的用户id
	 * @author  yeshiyuan
	 * @created 2018/8/13 17:05
	 * @param null
	 * @return
	 */
	public String getUserIdByPlanId(String planId) {
		return this.videoPlanService.getUserIdByPlanId(planId);
	}

	/**
	  * @despriction：获取计划类型
	  * @author  yeshiyuan
	  * @created 2018/8/15 18:14
	  * @param null
	  * @return
	  */
	public Integer getPlanType(String planId){
		String planType = RedisCacheUtil.hashGetString(ModuleConstants.VIDEO_PLAN_INFO + planId, ModuleConstants.VIDEO_PLAN_INFO_KEY_PLANTYPE);
		if (StringUtil.isBlank(planType)) {
			Long packageId = videoPlanService.getPackageIdByPlanId(planId);
			if(null == packageId){
				throw new BusinessException(BusinessExceptionEnum.PACKAGEID_NOT_EXISTS);
			}
			GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(packageId);
			if(null == goodsInfo){
				throw new BusinessException(BusinessExceptionEnum.PACKAGEINFO_NOT_EXISTS);
			}
			RedisCacheUtil.hashPut(ModuleConstants.VIDEO_PLAN_INFO + planId, ModuleConstants.VIDEO_PLAN_INFO_KEY_PLANTYPE, goodsInfo.getTypeId(), false);
			return goodsInfo.getTypeId();
		}else{
			return Integer.valueOf(planType);
		}
	}

	/**
	 * @despriction：获取计划已使用事件数
	 * @author  yeshiyuan
	 * @created 2018/8/30 11:10
	 * @param null
	 * @return
	 */
	public Integer getUsedEventNum(String planId) {
		String usedEventNumKey = ModuleConstants.VIDEO_PLAN_USED_EVENT_NUM + planId;
		String usedEventNumStr = RedisCacheUtil.valueGet(usedEventNumKey);
		if (StringUtil.isBlank(usedEventNumStr)){
			usedEventNumStr = String.valueOf(videoEventMongoService.countVideoEventByPlanId(planId));
			RedisCacheUtil.valueSet(usedEventNumKey,usedEventNumStr);
		}
		return Integer.valueOf(usedEventNumStr);
	}

	/**
	 * @despriction：ipc上报前校验
	 * @author  yeshiyuan
	 * @created 2018/9/3 17:39
	 * @param null
	 * @return
	 */
	public CheckBeforeUploadResult checkBeforeUpload(String planId, String deviceId) {
		if (StringUtil.isBlank(planId)){
			return new CheckBeforeUploadResult(false, "planId is null");
		}
		//校验计划是否绑定该设备
		String devId = videoManageService.getDeviceId(planId);
		if(null == devId || "".equals(devId) || !devId.equals(deviceId)){
			return new CheckBeforeUploadResult(false, "this planId( "+ planId +" ) isn't bind deviceId( "+ deviceId +" )");
		}
		if (checkPlanIsExpire(planId)) {
			return new CheckBeforeUploadResult(false, "this plan had expired");
		}
		if (checkPlanIsClose(planId)) {
			return new CheckBeforeUploadResult(false, "this plan status is closed");
		}
		//获取租户id
		Long tenantId = videoManageService.getTenantId(planId);
		return new CheckBeforeUploadResult(true, "", tenantId);
	}

	/**
	  * @despriction：校验计划是否过期
	  * @author  yeshiyuan
	  * @created 2018/9/4 14:15
	  * @return
	  */
	public boolean checkPlanIsExpire(String planId) {
		boolean isExpire = false;
		//校验计划是否过期
		String expireFlag = RedisCacheUtil.hashGetString(ModuleConstants.VIDEO_PLAN_INFO + planId, ModuleConstants.VIDEO_PLAN_INFO_KEY_EEPIREFLAG);
		if (StringUtil.isBlank(expireFlag)){
			VideoPlan videoPlan = videoPlanService.getVideoPlan(planId, null);
			if (videoPlan == null){
				throw new BusinessException(BusinessExceptionEnum.PLAN_NOT_EXISTS);
			}
			expireFlag = videoPlan.getPlanExecStatus()>=2 ? PlanExpireFlagEnum.expire.getCode() : PlanExpireFlagEnum.no_expire.getCode();
			RedisCacheUtil.hashPut(ModuleConstants.VIDEO_PLAN_INFO + planId,ModuleConstants.VIDEO_PLAN_INFO_KEY_EEPIREFLAG, expireFlag, false);
		}
		if (PlanExpireFlagEnum.expire.getCode().equals(expireFlag)){
			isExpire = true;
		}
		return isExpire;
	}

	/**
	  * @despriction：校验计划是否开启录影
	  * @author  yeshiyuan
	  * @created 2018/9/18 16:01
	  * @return
	  */
	public boolean checkPlanIsClose(String planId) {
		String redisKey = ModuleConstants.VIDEO_PLAN_INFO + planId;
		boolean isClose = true;
		//计划是否开启
		String planExecStatus = RedisCacheUtil.hashGetString(redisKey, ModuleConstants.VIDEO_PLAN_INFO_KEY_PLANEXECSTATUS);
		if (StringUtil.isBlank(planExecStatus)) {
			VideoPlan videoPlan = videoPlanService.getVideoPlan(planId, null);
			if (videoPlan==null){
				throw new BusinessException(BusinessExceptionEnum.PLAN_NOT_EXISTS);
			}
			planExecStatus = String.valueOf(videoPlan.getPlanExecStatus());
			RedisCacheUtil.hashPut(redisKey, ModuleConstants.VIDEO_PLAN_INFO_KEY_PLANEXECSTATUS, planExecStatus, false);
		}
		Integer openFalgInt = Integer.valueOf(planExecStatus);
		if (openFalgInt.intValue() == PlanExecStatusEnum.OPEN.getCode()){
			isClose = false;
		}
		return isClose;
	}
}

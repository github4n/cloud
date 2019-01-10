package com.iot.center.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.allocation.api.ActivityRecordApi;
import com.iot.building.allocation.vo.ActivityRecordReq;
import com.iot.building.allocation.vo.ActivityRecordResp;
import com.iot.building.device.api.DeviceBusinessTypeApi;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.building.ifttt.api.AutoTobApi;
import com.iot.building.ifttt.api.IftttApi;
import com.iot.building.ifttt.vo.req.RuleListReq;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.scene.api.SceneControlApi;
import com.iot.building.scene.vo.req.LocationSceneReq;
import com.iot.building.scene.vo.req.SceneTemplateManualReq;
import com.iot.building.scene.vo.resp.LocationSceneResp;
import com.iot.building.shortcut.api.ShortcutApi;
import com.iot.building.shortcut.vo.ShortcutVo;
import com.iot.building.space.api.SpaceApi;
import com.iot.building.template.api.SpaceTemplateApi;
import com.iot.building.template.api.TemplateTobApi;
import com.iot.building.template.vo.ScheduledDetailVo;
import com.iot.building.template.vo.ScheduledVo;
import com.iot.building.template.vo.req.BuildIftttReq;
import com.iot.building.template.vo.req.BuildTemplateReq;
import com.iot.building.template.vo.req.CreateSceneFromTemplateReq;
import com.iot.building.template.vo.req.SaveIftttTemplateReq;
import com.iot.building.template.vo.req.SpaceTemplateReq;
import com.iot.building.template.vo.req.TemplateReq;
import com.iot.building.template.vo.rsp.SceneTemplateResp;
import com.iot.building.template.vo.rsp.SpaceTemplateResp;
import com.iot.building.template.vo.rsp.TemplateResp;
import com.iot.center.helper.Constants;
import com.iot.center.helper.SpaceEnum;
import com.iot.center.utils.CronDateUtils;
import com.iot.center.utils.DateUtils;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;
import com.iot.device.api.ProductApi;
import com.iot.device.vo.rsp.DataPointResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.schedule.api.ScheduleApi;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.schedule.vo.AddJobReq;
import com.iot.user.vo.LoginResp;


@Service
public class TemplateService {

	private static final Logger logger = LoggerFactory.getLogger(TemplateService.class);

	@Autowired
	private IftttApi iftttApi;
	@Autowired
	private ProductApi productApi;
	@Autowired
	private SpaceApi buildSpaceApi;
	@Autowired
	private com.iot.control.space.api.SpaceApi commonSpaceApi;
	@Autowired
	private com.iot.control.space.api.SpaceDeviceApi commonSpaceDeviceApi;
	@Autowired
	private ScheduleApi scheduleApi;
	@Autowired
	private SceneControlApi sceneControlApi;
	@Autowired
	private SpaceTemplateApi spaceTemplateApi;
	@Autowired
	private ActivityRecordApi activityRecordApi;
	@Autowired
	private DeviceBusinessTypeApi deviceBusinessTypeApi;
	@Autowired
	private TemplateTobApi templateTobApi;
	@Autowired
	private ShortcutApi shortcutApi;


	public Page<RuleResp> findTemplateList(String name, String templateType, String pageNum, String pageSize,
										   Long tenantId) {
		int num = Strings.isNullOrEmpty(pageNum) ? 0 : Integer.parseInt(pageNum);
		int size = Strings.isNullOrEmpty(pageSize) ? 0 : Integer.parseInt(pageSize);
		RuleListReq ruleReq = new RuleListReq();
		if (!Strings.isNullOrEmpty(name)) {
			ruleReq.setName(name);
		}
		ruleReq.setPageNum(num);
		ruleReq.setPageSize(size);
		ruleReq.setTenantId(tenantId);
		ruleReq.setTemplateFlag(Byte.valueOf("1"));
		ruleReq.setShowTime(false);
		Page<RuleResp> rulePage = iftttApi.list(ruleReq);
		return rulePage;
	}


	public List<Map<String, Object>> getAllTemplateList(String name, Long tenantId, Long locationId) {
		TemplateReq templateReq = setRequestTemplatReq(name, 0, 100, tenantId, locationId);
		Page<TemplateResp> scenePage = templateTobApi.findTemplateList(templateReq);
		Page<RuleResp> rulePage = this.findTemplateList(name, "", "0", "100", tenantId);
		//情景的
		List<TemplateResp> sceneList = scenePage.getResult();
		//ifttt的
		List<RuleResp> ruleList = rulePage.getResult();
		//整校的
		LocationSceneReq locationSceneReq = new LocationSceneReq();
		locationSceneReq.setTenantId(tenantId);
		/*locationSceneReq.setLocationId(locationId);*/
		List<LocationSceneResp> locationSceneList = sceneControlApi.findLocationSceneList(locationSceneReq);
		List<Map<String, Object>> result = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(ruleList)) {
			for (RuleResp rule : ruleList) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("templateType", Constants.SCHEDULE_IFTTT);
				map.put("templateId", rule.getId());
				map.put("templateName", rule.getName());

				result.add(map);
			}
		}

		if (CollectionUtils.isNotEmpty(locationSceneList)) {
			for (LocationSceneResp locationSceneResp : locationSceneList) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("templateId", locationSceneResp.getId());
				map.put("templateType", Constants.SCHEDULE_LOCATION);
				map.put("templateName", locationSceneResp.getName());

				result.add(map);
			}
		}

		return result;
	}

	public void addTemplate(SaveIftttTemplateReq iftttReq) {
		templateTobApi.saveIftttTemplate(iftttReq);
	}

	public void updateTemplate(SaveIftttTemplateReq iftttReq) {
//		Long id = iftttReq.getId();
//		templateTobApi.deleteIftttTemplate(id,iftttReq.getTenantId());
//		try {
//			iftttReq.setTenantId(constantUtil.getTenantId());
//			List<SensorVo> sensors = iftttReq.getSensors();
//			if (!CollectionUtils.isEmpty(sensors)) {
//				for (SensorVo sensor : sensors) {
//					sensor.setType(sensor.getType() + Constants.IFTTT_2B_FLAG);
//				}
//			}
//
//			templateTobApi.saveIftttTemplate(iftttReq);
//			Long newId = iftttReq.getId();
//			spaceTemplateApi.spaceTemplateUpdate(newId, id);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public void deleteTemplate(Long templateId,Long tenantId) {
		templateTobApi.deleteIftttTemplate(templateId,tenantId);
		SpaceTemplateReq req = new SpaceTemplateReq();
		req.setTemplateId(templateId);
		spaceTemplateApi.spaceTemplateDelete(req);
	}

	public void addSpaceTemplate(SpaceTemplateReq spaceTemplateReq) throws BusinessException{
		Long templateId = spaceTemplateReq.getTemplateIds().get(0);
		spaceTemplateReq.setTemplateId(templateId);
		Long startTime =spaceTemplateReq.getStartTimes().get(0);
		spaceTemplateReq.setStartTime(startTime);
		Long endTime = spaceTemplateReq.getEndTimes().get(0);
		spaceTemplateReq.setEndTime(endTime);
		String renTime=spaceTemplateReq.getRunTime();
		String loopType = spaceTemplateReq.getLoopType();
		String week = spaceTemplateReq.getWeek();
		// 生成定时任务
		String startTimeStr = DateUtils.getDateTime(startTime);
		//运行时间 开始日期加运行时间
		String time=startTimeStr.split(" ")[0]+" "+renTime;
		String endTimeStr = DateUtils.getDateTime(endTime);
		String cron = CronDateUtils.generateCron(time, endTimeStr, loopType, week);
		spaceTemplateReq.setStartCron(cron);
		spaceTemplateReq.setProperties(JSON.toJSONString(spaceTemplateReq));
		//保存或更新模板
		saveOrUpdateScheduleTemplate(spaceTemplateReq);
		// 生成定时任务
		addSchedule(spaceTemplateReq);
	}
	
	public ScheduledDetailVo addSpaceTemplate(Long id,Long tenantId,Long locationId,Long orgId) throws BusinessException{
		SpaceTemplateReq spaceTemplateReq=new SpaceTemplateReq();
		spaceTemplateReq.setId(id);
		spaceTemplateReq.setTenantId(tenantId);
		spaceTemplateReq.setLocationId(locationId);
		return spaceTemplateApi.findScheduledDetalAndLog(spaceTemplateReq);
	}


	private Long saveOrUpdateScheduleTemplate(SpaceTemplateReq req) {
		if(req.getId() ==null) {
			Long id=spaceTemplateApi.spaceTemplateSave(req);// 保存到space_template
			req.setId(id);
			return id;
		}else {
			spaceTemplateApi.spaceTemplateUpdate(req);
			return req.getId();
		}
	}

	public void updateSpaceTemplate(SpaceTemplateReq spaceTemplateReq) {
		scheduleApi.delJob(ScheduleConstants.TEMPLATE_JOB + spaceTemplateReq.getId());
		addSpaceTemplate(spaceTemplateReq);
	}


	public Integer deleteSpaceTemplate(long id, String templateType) {
		SpaceTemplateReq req = new SpaceTemplateReq();
		req.setId(id);
		req.setTemplateType(templateType);
		int deleteRec = spaceTemplateApi.spaceTemplateDelete(req);
		return deleteRec;
	}

	public List<DataPointResp> findDataPointListByProductId(Long productId) {
		List<DataPointResp> list = productApi.findDataPointListByProductId(productId);
		return list;
	}

	public List<ScheduledVo> findLogList(Long locatinoId,Long spaceId, Long startTime, Long endTime,Long tenantId,
			String week,Long timeDiff) {
//		Map<String, Long> queryTime = getUTCTime(dateType, dateData, year);
		SpaceTemplateReq spaceTemplateReq = new SpaceTemplateReq();
		spaceTemplateReq.setSpaceId(spaceId);
		spaceTemplateReq.setStartTime(startTime);
		spaceTemplateReq.setEndTime(endTime);
		spaceTemplateReq.setWeek(week);//这里的week代表前端时区当月的或者当周第一天是星期几 
		spaceTemplateReq.setTimeDiff(timeDiff);
		spaceTemplateReq.setLocationId(locatinoId);
		return spaceTemplateApi.findByValidityDate(spaceTemplateReq);
	}
	
	public static void main(String[] args) {
		LocalDateTime locatl=LocalDateTime.of(2018,11,1,0,0,0,0);
		Long mm=locatl.toInstant(ZoneOffset.of("+8")).toEpochMilli();
		Long nn=locatl.toInstant(ZoneOffset.of("-8")).toEpochMilli();
		System.out.println(mm);
		System.out.println(nn);
		Date date=localToUTC("2018-11-01 00:00:00");
		System.out.println(date.getTime());
	}
	
	public static Date localToUTC(String localTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date localDate= null;
        try {
			localDate = sdf.parse(localTime);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
        long localTimeInMillis=localDate.getTime();
        /** long时间转换成Calendar */
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(localTimeInMillis);
        /** 取得时间偏移量 */
        int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
        /** 取得夏令时差 */
        int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
        /** 从本地时间里扣除这些差量，即可以取得UTC时间*/
        calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        /** 取得的时间就是UTC标准时间 */
        Date utcDate=new Date(calendar.getTimeInMillis());
        return utcDate;
    }

	public SceneTemplateResp getSceneTemplate(Long templateId) {
		SceneTemplateResp sceneTemplateResp = templateTobApi.getSceneTemplate(templateId);
		return sceneTemplateResp;
	}

	public void sceneTemplateBuild(BuildTemplateReq buildTemplateReq) throws Exception{
		templateTobApi.buildSceneTemplate2B(buildTemplateReq);
	}

	public void sceneTemplateDelete(Long templateId) throws BusinessException {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		SpaceTemplateReq spaceTemplateReq = new SpaceTemplateReq();
		spaceTemplateReq.setCreateBy(user.getUserId());
		spaceTemplateReq.setTenantId(user.getTenantId());
		spaceTemplateReq.setLocationId(user.getLocationId());
		spaceTemplateReq.setTemplateId(templateId);
//		delScene(spaceTemplateReq);
		templateTobApi.deleteSceneTemplate(templateId);
	}

	public TemplateResp findSceneSpaceTemplateList(TemplateReq templateReq) {
		SpaceTemplateReq spaceTemplateReq = new SpaceTemplateReq();
		spaceTemplateReq.setSpaceId(templateReq.getSpaceId());
		spaceTemplateReq.setTenantId(templateReq.getTenantId());
		spaceTemplateReq.setTemplateType(templateReq.getTemplateType());
		List<Long> templateIds = spaceTemplateApi.findTemplateIdListByCondition(spaceTemplateReq);
		if (CollectionUtils.isNotEmpty(templateIds)) {
			templateReq.setTemplateList(templateIds);
		}
		TemplateResp templateResp = new TemplateResp();
		templateReq.setMountLogo(0);
		List<TemplateResp> unMountList = templateTobApi.findSceneSpaceTemplateList(templateReq);
		templateResp.setUnMountList(unMountList);
		templateReq.setName("");
		templateReq.setMountLogo(1);
		List<TemplateResp> mountList = templateTobApi.findSceneSpaceTemplateList(templateReq);
		templateResp.setMountList(mountList);
		return templateResp;
	}

	public Integer saveTemplateMount(SpaceTemplateReq spaceTemplateReq) throws BusinessException {
//		Integer result = spaceTemplateApi.spaceTemplateSave(spaceTemplateReq);
		//TODO 结果返回值
		Integer result = 1;
		buildScene(spaceTemplateReq.getCreateBy(), spaceTemplateReq.getTemplateId(), spaceTemplateReq.getSpaceId(),
				spaceTemplateReq.getLocationId(), spaceTemplateReq.getBusiness());
		return result;
	}


	public List<Long> buildScene(Long userId, Long templateId, Long spaceId, Long locationId, String businessType) {
		List<Long> sceneIds = Lists.newArrayList();
		try {
			// 查找spaceId下所有房间，将情景拆分到各个房间下
//			SpaceResp spaceResp = spaceApi.findSpaceInfoBySpaceId(spaceId);
			SpaceReq sReq = new SpaceReq();
			sReq.setId(spaceId);
			List<SpaceResp> resps = commonSpaceApi.findSpaceByCondition(sReq);
			SpaceReq spaceReq = new SpaceReq();
			if (CollectionUtils.isEmpty(resps)) {
				return sceneIds;
			}
			BeanUtil.copyProperties(resps.get(0), spaceReq);
			List<Long> spaceList = buildSpaceApi.getAllSpace(spaceReq);
			if (CollectionUtils.isNotEmpty(spaceList)) {
				for (Long roomId : spaceList) {
					List<String> deviceIds = getAllSpaceDevices(spaceReq.getTenantId(), roomId, businessType);
					CreateSceneFromTemplateReq req = new CreateSceneFromTemplateReq();
					req.setUserId(userId);
					req.setTemplateId(templateId);
					req.setDeviceIdList(deviceIds);
					req.setSpaceId(roomId);
					req.setLocationId(locationId);
					Long sceneId = templateTobApi.createSceneFromTemplate(req);
					sceneIds.add(sceneId);
				}
			}
		} catch (BusinessException e) {
			logger.error("buildScene error : " + e);
		}

		return sceneIds;
	}


	public Integer deleteTemplateMount(SpaceTemplateReq spaceTemplateReq) {
//		Integer result = spaceTemplateApi.spaceTemplateDelete(spaceTemplateReq);
		//TODO 结果返回值
		Integer result = 1;
		delScene(spaceTemplateReq);
		return result;
	}

	public void delScene(SpaceTemplateReq spaceTemplateReq) {
		try {
			templateTobApi.delSceneFromTemplate(spaceTemplateReq);
		} catch (BusinessException e) {
			logger.error("buildScene error : " + e);
		}
	}

	
	public List<DeviceBusinessTypeResp> findBusinessTypeList(LoginResp user) {
		List<DeviceBusinessTypeResp> deviceBusinessTypes = deviceBusinessTypeApi.getBusinessTypeList(user.getOrgId(), user.getTenantId(),"");
		return deviceBusinessTypes;
	}


	/**
	 * 从Mongo获取schedule执行的log
	 */
//	private List<ActivityRecordResp> getMongoLogList(Long spaceId, Long tenantId, String dateType,
//													 Integer dateData, Integer year) {
//		Map<String, Long> queryTime = getQueryLogTime(dateType, dateData, year);
//		ActivityRecordReq req = new ActivityRecordReq();
//		req.setStartTime(String.valueOf(queryTime.get("start")));
//		req.setEndTime(String.valueOf(queryTime.get("end")));
//		req.setTenantId(tenantId);
//		req.setForeignId(String.valueOf(spaceId));
//		req.setPageSize(10);
//		List<ActivityRecordResp> list = activityRecordApi.queryScheduleLog(req);
//		return list;
//	}
	
	/**
	 */
	private List<ActivityRecordResp> getActiveLogList(List<SpaceTemplateResp> respList,Long spaceId, Long tenantId, String dateType,
													 Integer dateData, Integer year) {
		Map<String, Long> queryTime = getQueryLogTime(dateType, dateData, year);
		ActivityRecordReq req = new ActivityRecordReq();
		req.setStartTime(String.valueOf(queryTime.get("start")));
		req.setEndTime(String.valueOf(queryTime.get("end")));
		req.setTenantId(tenantId);
		req.setForeignId(String.valueOf(spaceId));
		SpaceTemplateReq spaceTemplateReq=new SpaceTemplateReq();
		spaceTemplateReq.setSpaceId(spaceId);
		List<ActivityRecordResp> list=Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(respList)) {
			for(SpaceTemplateResp spaceTemplate:respList) {
				req.setForeignId(String.valueOf(spaceTemplate.getId()));
				req.setSpaceId(spaceId);
				list.addAll(activityRecordApi.queryScheduleLog(req));
			}
		}
		return list;
	}

	private Map<String, Long> getQueryLogTime(String dateType, Integer dateData, Integer year) {
		Map<String, Long> result = Maps.newHashMap();
		Date startDate = null;
		Date endDate = null;
		if (Constants.QUERY_WEEK.equals(dateType)) {
			startDate = DateUtils.getFirstDayOfWeek(year, dateData);
			endDate = DateUtils.getLastDayOfWeek(year, dateData);
		} else if (Constants.QUERY_MONTH.equals(dateType)) {
			startDate = DateUtils.getFirstDayOfMonth(year, dateData);
			endDate = DateUtils.getLastDayOfMonth(year, dateData);
		}

		result.put("start", startDate.getTime());
		result.put("end", endDate.getTime());
        
		return result;
	}
	
	private Map<String, Long> getUTCTime(String dateType, Integer dateData, Integer year) {
		SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Long> result = Maps.newHashMap();
		Date startDate = null;
		Date endDate = null;
		if (Constants.QUERY_WEEK.equals(dateType)) {
			startDate = DateUtils.getFirstDayOfWeek(year, dateData);
			endDate = DateUtils.getLastDayOfWeek(year, dateData);
		} else if (Constants.QUERY_MONTH.equals(dateType)) {
			startDate = DateUtils.getFirstDayOfMonth(year, dateData);
			endDate = DateUtils.getLastDayOfMonth(year, dateData);
		}
		Date dateStart=localToUTC(simple.format(startDate));
		Date dateEnd=localToUTC(simple.format(endDate));
		result.put("start", dateStart.getTime());
		result.put("end", dateEnd.getTime());
		return result;
	}


//	public List<SpaceTemplateResp> getUndoTemplateList(List<SpaceTemplateResp> spaceTemplateList, String dateType,
//			Integer dateData, Integer year) {
//		List<SpaceTemplateResp> result = Lists.newArrayList();
//		if (CollectionUtils.isNotEmpty(spaceTemplateList)) {
//			Date now = new Date();
//			Date formatNow = DateUtils.dateParse("", DateUtils.dateFormat("", now));
//			Map<String, String> queryTime = getQueryTime(dateType, dateData, year);
//			// 每个星期或者每个月的开始结束日期
//			Date queryStart = DateUtils.dateParse("", queryTime.get("start"));
//			Date queryEnd = DateUtils.dateParse("", queryTime.get("end"));
//			for (SpaceTemplateResp spaceTemplate : spaceTemplateList) {
//				String loopType = spaceTemplate.getLoopType();
//				String startCron = spaceTemplate.getStartCron();
//				String endCron = spaceTemplate.getEndCron();
//				String properties = spaceTemplate.getProperties();
//				Map<String, Object> propertiesMap = JSON.parseObject(properties, Map.class);
//				// 模板的开始结束时间
//				String startTime = String.valueOf(propertiesMap.get("startTime"));
//				String endTime = String.valueOf(propertiesMap.get("endTime"));
//				Date startDate = DateUtils.dateParse("", startTime);
//				Date endDate = DateUtils.dateParse("", endTime);
//
//				Date areaStart = queryStart.before(startDate) ? startDate : queryStart;
//				Date areaEnd = queryEnd.before(endDate) ? queryEnd : endDate;
//				if (areaStart.before(areaEnd) && formatNow.before(areaEnd)) {
//					Date start = formatNow.before(areaStart) ? areaStart : formatNow;
//					Date end = areaEnd;
//					List<String> startCronTimeList = null;
//					if ("0".equals(loopType)) { // 没有设置星期循环，则直接将开始结束时间放在 startCronTimeList 中
//						startCronTimeList = Lists.newArrayList();
//						startCronTimeList.add(startTime);
//						if (!startTime.equals(endTime)) {
//							startCronTimeList.add(endTime);
//						}
//					} else {
//						startCronTimeList = CronTriggerUtils.trigger(startCron, start, end);
//					}
//					if (startCronTimeList != null && startCronTimeList.size() > 0) {
//						spaceTemplate.setCronTimeList(startCronTimeList);
//						result.add(spaceTemplate);
//					}
//					if (!startCron.equals(endCron)) { // 开始和结束时间设置成一样则不需要生成结束任务
//						List<String> endCronTimeList = CronTriggerUtils.trigger(endCron, start, end);
//						if (endCronTimeList != null && endCronTimeList.size() > 0) {
//							SpaceTemplateResp copySpaceTemplateResp = new SpaceTemplateResp();
//							BeanUtils.copyProperties(spaceTemplate, copySpaceTemplateResp);
//							copySpaceTemplateResp.setCronTimeList(endCronTimeList);
//							result.add(copySpaceTemplateResp);
//						}
//					}
//				}
//			}
//		}
//		return result;
//	}
	
	public List<SpaceTemplateResp> getUndoTemplateList(Long spaceId){
		
		return null;
	}

	public List<Long> buildIfttt(long spaceId, long templateId, long tenantId, String businessType) {
		List<Long> ruleIds = Lists.newArrayList();
		try {
			List<String> deviceIds = getAllSpaceDevices(tenantId, spaceId, businessType);
			BuildIftttReq buildReq = new BuildIftttReq();
			buildReq.setDeviceIds(deviceIds);
			buildReq.setSpaceId(spaceId);
			buildReq.setTemplateId(templateId);
			buildReq.setTenantId(tenantId);
			Long ruleId = templateTobApi.buildIfttt2B(buildReq);
			ruleIds.add(ruleId);
		} catch (BusinessException e) {
			logger.error("buildIfttt error : " + e);
		}

		return ruleIds;
	}


	private List<Long> buildIftttTemplate(Long spaceId, Long templateId, Long tenantId, String businessType) {
		List<Long> ids = Lists.newArrayList();
		try {
			List<String> deviceIds = getAllSpaceDevices(tenantId, spaceId, businessType);
			RuleListReq req = new RuleListReq();
			req.setTemplateId(templateId);
			req.setPageNum(1);
			req.setPageSize(Integer.MAX_VALUE);
//			List<RuleResp> ifttts = templateTobApi.getIftttTemplateList(templateId);
			// 查找ifttt模板下的具体ifttt
			Page<RuleResp> ifttts = iftttApi.list(req);
			for (RuleResp rule : ifttts.getResult()) {
				BuildIftttReq buildReq = new BuildIftttReq();
				buildReq.setSpaceId(spaceId);
				buildReq.setDeviceIds(deviceIds);
				buildReq.setTemplateId(rule.getId());
				buildReq.setTenantId(tenantId);
				Long ruleId = templateTobApi.buildIfttt2B(buildReq);
				ids.add(ruleId);
			}
		} catch (BusinessException e) {
			logger.error("build ifttt template : " + e.getMessage());
		}
		return ids;
	}

//	public List<String> getDevicesBySpace(Long tenantId, long spaceId, String businessType) {
//		List<Map<String, Object>> deviceList = buildSpaceApi.findDeviceByRoom(tenantId, spaceId);
//		List<String> deviceIds = Lists.newArrayList();
//		if (CollectionUtils.isNotEmpty(deviceList)) {
//			for (Map<String, Object> device : deviceList) {
//				deviceIds.add(String.valueOf(device.get("deviceId")));
//			}
//		}
//
//		return deviceIds;
//	}


	public void addSchedule(SpaceTemplateReq req) {
		String jsonStr=JSON.toJSONString(req);
		Map<String, Object> data = JSON.parseObject(jsonStr, Map.class);
		AddJobReq jobReq = new AddJobReq();
		jobReq.setCron(req.getStartCron());
		jobReq.setData(data);
		jobReq.setJobClass(ScheduleConstants.TEMPLATE_JOB);
		jobReq.setJobName(ScheduleConstants.TEMPLATE_JOB + req.getId());
		jobReq.setTimeZone(Constants.getTimeZone(req.getZone()));
		SaaSContextHolder.setCurrentTenantId(req.getTenantId());
		scheduleApi.addJob(jobReq);// 保存到GRTZ_CRON_TRIGGERS
	}

	public Map<String, String> getQueryTime(String dateType, Integer dateData, Integer year) {
		Map<String, String> result = Maps.newHashMap();
		String start = "";
		String end = "";
		if (Constants.QUERY_WEEK.equals(dateType)) {
			start = DateUtils.dateFormat("yyyy-MM-dd 00:00", DateUtils.getFirstDayOfWeek(year, dateData));
			end = DateUtils.dateFormat("yyyy-MM-dd 23:59", DateUtils.getLastDayOfWeek(year, dateData));
		} else if (Constants.QUERY_MONTH.equals(dateType)) {
			start = DateUtils.dateFormat("yyyy-MM-dd 00:00", DateUtils.getFirstDayOfMonth(year, dateData));
			end = DateUtils.dateFormat("yyyy-MM-dd 23:59", DateUtils.getLastDayOfMonth(year, dateData));
		}

		result.put("start", start);
		result.put("end", end);

		return result;
	}

	public List<String> getAllSpaceDevices(Long tenantId, Long spaceId, String business) {
		List<String> deviceIds = Lists.newArrayList();
		List<Long> spaceIds = Lists.newArrayList();spaceIds.add(spaceId);
//		List<String> currentDeviceIds = getDevicesBySpace(tenantId, spaceId, business);
//		deviceIds.addAll(currentDeviceIds);
		List<SpaceResp> spaceList = commonSpaceApi.findChild(tenantId, spaceId);
		if(CollectionUtils.isNotEmpty(spaceList)) {
			spaceList.forEach(space->{
				spaceIds.add(space.getId());
			});
		}
		SpaceAndSpaceDeviceVo req=new SpaceAndSpaceDeviceVo();
		req.setTenantId(tenantId);
		req.setSpaceIds(spaceIds);
		 List<SpaceDeviceResp> spaceDeviceList=commonSpaceDeviceApi.findSpaceDeviceBySpaceIdsOrDeviceIds(req);
		if (CollectionUtils.isNotEmpty(spaceDeviceList)) {
			for (SpaceDeviceResp spaceDevice : spaceDeviceList) {
//				List<String> childDeviceIds = getDevicesBySpace(tenantId, space.getId(), business);
				deviceIds.add(spaceDevice.getDeviceId());
			}
		}
		
		return deviceIds;
	}

	public Map<String, Object> checkSchedule(Long spaceTemplateId, String startTime, String endTime, Long templateId,
			Long spaceId) {
		Map<String, Object> result = Maps.newHashMap();
		result.put("flag", true);
		result.put("code", "10000");
		result.put("msg", "操作成功！");
		Date now = new Date();
		Date formatNow = DateUtils.dateParse("", DateUtils.dateFormat("", now));
		Date startDate = DateUtils.dateParse("", startTime);
		Date endDate = DateUtils.dateParse("", endTime);
		if (startDate.before(formatNow)) {
			result.put("flag", false);
			result.put("code", "10001");
			result.put("msg", "计划开始时间不能小于当前时间！");
			return result;
		}
		SpaceTemplateReq spaceTemplateReq = new SpaceTemplateReq();
		List<Long> spaceIds = Lists.newArrayList();
		spaceIds.add(spaceId);
		spaceTemplateReq.setSpaceIds(spaceIds);
		spaceTemplateReq.setTemplateId(templateId);
		List<SpaceTemplateResp> spaceTemplateList = spaceTemplateApi.findByCondition(spaceTemplateReq);
		if (CollectionUtils.isNotEmpty(spaceTemplateList)) {
			for (SpaceTemplateResp spaceTemplate : spaceTemplateList) {
				String properties = spaceTemplate.getProperties();
				Map<String, Object> propertiesMap = JSON.parseObject(properties, Map.class);
				String startTimeDb = String.valueOf(propertiesMap.get("startTime"));
				String endTimeDb = String.valueOf(propertiesMap.get("endTime"));
				Date startDateDb = DateUtils.dateParse("", startTimeDb);
				Date endDateDb = DateUtils.dateParse("", endTimeDb);
				if (!(endDate.before(startDateDb) || startDate.after(endDateDb))) {
					if (spaceTemplate.getId() != spaceTemplateId) {
						result.put("flag", false);
						result.put("code", "10002");
						result.put("msg", "当前模板已经在此时间段设置过！");
						return result;
					}
				}
			}
		}
		return result;
	}


	public Page<TemplateResp> findSceneTemplatePage(String name, String templateType, Integer pageNum, Integer pageSize,
			Long tenantId, Long locationId) {
		TemplateReq templateReq = setRequestTemplatReq(name, pageNum, pageSize, tenantId, locationId);
		Page<TemplateResp> scenePage = templateTobApi.findTemplateList(templateReq);
		return scenePage;
	}


	private TemplateReq setRequestTemplatReq(String name, Integer pageNum, Integer pageSize, Long tenantId,
			Long locationId) {
		TemplateReq templateReq = new TemplateReq();
		templateReq.setName(name == null ? "" : name);
		templateReq.setTemplateType(Constants.SCHEDULE_SCENE);
		templateReq.setPageNum(pageNum);
		templateReq.setPageSize(pageSize);
		templateReq.setTenantId(tenantId);
		templateReq.setLocationId(locationId);
		return templateReq;
	}


	public void sceneTemplateInit(List<Long> spaceIds, LoginResp user,String type) {
		SceneTemplateManualReq req = new SceneTemplateManualReq();
		req.setLocationId(user.getLocationId());
		req.setTenantId(user.getTenantId());
		req.setUserId(user.getUserId());
		req.setSpaceIdList(spaceIds);
		req.setType(type);
		sceneControlApi.templateManualInit(req);
	}


	/**
	 * 1. BUILD 和 FLOOR 级别从templete表获取
	 * 2. ROOM 级别从sence表获取
	 */
	public List<Map<String, Object>> getListBySpace(Long tenantId,Long orgId,Long locationId, Long spaceId, String type, String name) {
		ShortcutVo vo=new ShortcutVo();
		vo.setTenantId(tenantId);
		vo.setLocationId(locationId);
		vo.setSpaceId(spaceId);
		List<ShortcutVo> voList=shortcutApi.getShortcutList(vo);
		List<Map<String, Object>> backList=Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(voList)) {
			for(ShortcutVo shortcutVo:voList) {
				Map<String, Object> map = Maps.newHashMap();
				if(shortcutVo.getType().toLowerCase().contains(type)) {
					map.put("templateId", shortcutVo.getId());
					map.put("templateName", shortcutVo.getName());
					map.put("templateType", shortcutVo.getType());
					backList.add(map);
				}
			}
		}
		return backList;
	}

	private Page<RuleResp> findIftttList(Long tenantId, Integer pageNo, Integer pageSize) {
		RuleListReq ruleReq = new RuleListReq();
		ruleReq.setPageNum(pageNo);
		ruleReq.setPageSize(pageSize);
		ruleReq.setTenantId(tenantId);
		ruleReq.setTemplateFlag(Byte.valueOf("0"));
		ruleReq.setShowTime(false);
		Page<RuleResp> rulePage = iftttApi.list(ruleReq);
		return rulePage;
	}

	public List<RuleResp> findTemplateListNoPage(Long tenantId,Long deployMentId) {
		RuleListReq ruleReq = new RuleListReq();
		ruleReq.setTenantId(tenantId);
		ruleReq.setTemplateFlag(Byte.valueOf("1"));
		ruleReq.setShowTime(false);
		ruleReq.setDeployMentId(deployMentId);
		List<RuleResp> list = iftttApi.listNoPage(ruleReq);
		return list;
	}

	public boolean findTemplateListByName(SaveIftttTemplateReq iftttReq) {
		return iftttApi.findTemplateListByName(iftttReq);
	}
}
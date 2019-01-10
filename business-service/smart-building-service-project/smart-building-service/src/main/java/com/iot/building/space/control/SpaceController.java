package com.iot.building.space.control;

import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.iot.building.ifttt.api.AutoTobApi;
import com.iot.building.ifttt.vo.ActuatorVo;
import com.iot.building.ifttt.vo.SensorVo;
import com.iot.building.ifttt.vo.TriggerVo;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.common.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.iot.building.gateway.MultiProtocolGatewayNewThread;
import com.iot.building.scene.service.SceneService;
import com.iot.building.scene.util.RedisKeyUtil;
import com.iot.building.space.api.SpaceApi;
import com.iot.building.space.service.IBuildingSpaceDeviceService;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.building.space.vo.CalendarReq;
import com.iot.building.space.vo.CalendarResp;
import com.iot.building.space.vo.DeploymentReq;
import com.iot.building.space.vo.DeploymentResp;
import com.iot.building.space.vo.LocationReq;
import com.iot.building.space.vo.LocationResp;
import com.iot.building.space.vo.QueryParamReq;
import com.iot.building.space.vo.SpaceBackgroundImgReq;
import com.iot.building.space.vo.SpaceBackgroundImgResp;
import com.iot.building.space.vo.SpaceExcelReq;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;
import com.iot.control.space.vo.SpaceVo;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.req.DeviceBusinessTypeIDSwitchReq;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.redis.RedisCacheUtil;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;

import springfox.documentation.spring.web.json.Json;

@RestController()
public class SpaceController implements SpaceApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpaceController.class);
	@Autowired
	private IBuildingSpaceService buildSpaceService;
	@Autowired
	private IBuildingSpaceDeviceService buildSpaceDeviecService;
	@Autowired
	private SpaceDeviceApi spaceDeviceApi;
	@Autowired
	private DeviceCoreApi deviceCoreApi;
	@Autowired
	private SceneApi sceneApi;
	@Autowired
	private UserApi userApi;
	@Autowired
	private AutoTobApi autoTobApi;

	@Override
	public Page<SpaceResp> findSpacePageByLocationId(Long locationId,Long orgId, Long tenantId, String name, int pageNumber,
			int pageSize) {
		return buildSpaceService.findSpacePageByLocationId(locationId,orgId, tenantId, name, pageNumber, pageSize);
	}

	@Override
	public List<SpaceResp> findSpaceByLocationId(Long locationId,Long orgId, Long tenantId, String name) {
		return buildSpaceService.findSpaceByLocationId(locationId, orgId,tenantId, name);
	}

	@Override
	public List<Map<String, Object>> findTreeSpaceByLocationId(Long tenantId,Long orgId, Long locationId) {
		return buildSpaceService.findTreeSpaceByLocationId(locationId,orgId, tenantId);
	}

//	@Override
//	public List<Map<String, Object>> findTree(Long locationId,Long orgId, Long tenantId) {
//		return buildSpaceService.findTree(locationId,orgId, tenantId);
//	}

	@Override
	public void mount(@RequestBody SpaceDeviceReq spaceDeviceReq) throws Exception {
		buildSpaceService.mount(spaceDeviceReq);
	}

	@Override
	public void removeMount(@RequestBody SpaceDeviceReq spaceDeviceReq) throws Exception {
		buildSpaceService.removeMount(spaceDeviceReq);
	}

	@Override
	public void deleteMountByDeviceIds(Long tenantId,Long orgId, String deviceIds) throws Exception {
		buildSpaceService.deleteMountByDeviceIds(deviceIds,orgId, tenantId);
	}

	@Override
	public List<Map<String, Object>> getFloorAndDeviceCount(Long tenantId,Long orgId, Long buildId, String types)
			throws BusinessException {
		return buildSpaceService.getFloorAndDeviceCount(buildId, types,orgId,tenantId);
	}

	@Override
	public List<Map<String, Object>> findDeviceByRoom(Long spaceId,Long orgId, Long tenantId) throws BusinessException {
		return buildSpaceService.findDeviceByRoom(spaceId,orgId, tenantId);
	}

	@Override
	public List<Map<String, Object>> findDeviceByRoomAndDeviceBusinessType(@RequestBody QueryParamReq req)
			throws BusinessException {
		Long roomId = req.getSpaceId();
		List<String> businessTypes = req.getBusinessTypeIds();
		return buildSpaceService.findDeviceByRoomAndDeviceBusinessType(roomId, businessTypes,req.getOrgId(), req.getTenantId());
	}

	@Override
	public List<Map<String, Object>> findDeviceByRoomAndDeviceType(@RequestBody QueryParamReq req)
			throws BusinessException {
		Long roomId = req.getSpaceId();
		List<String> types = req.getDeviceTypeIds();
		return buildSpaceService.findDeviceByRoomAndDeviceType(roomId, types,req.getOrgId(),req.getTenantId());
	}

	@Override
	public Boolean groupControl(@RequestBody Map<String, Object> propertyMap) throws BusinessException {
		Long spaceId = Long.valueOf(propertyMap.get("spaceId").toString());
		Long tenantId = Long.valueOf(propertyMap.get("tenantId").toString());
		Long orgId = Long.valueOf(propertyMap.get("orgId").toString());
		return buildSpaceService.groupControl(spaceId, null, propertyMap,orgId,tenantId);
	}

	@Override
	public Boolean control(@RequestBody Map<String, Object> propertyMap) throws BusinessException {
		String deviceId = propertyMap.get("deviceId").toString();
		propertyMap.remove("deviceId");
		return buildSpaceService.control(deviceId, propertyMap);
	}

	@Override
	public List<Long> getAllSpace(SpaceReq spaceReq) {
		return buildSpaceService.getAllSpace(spaceReq);
	}


//	@Override
//	public List<SpaceResp> getAllChildSpace(Long spaceId, Long tenantId) {
//		return buildSpaceService.getAllChildSpace(spaceId, tenantId);
//	}

	@Override
	public Integer countOnDevice(@RequestBody DeviceBusinessTypeIDSwitchReq req) {
		return buildSpaceService.countOnDevice(req);
	}

	@Override
	public Integer countOnLightDevice(@RequestBody DeviceBusinessTypeIDSwitchReq req) {
		return buildSpaceService.countOnDevice(req);
	}

	@Override
	public void setSpaceRelation(Long tenantId,Long orgId, Long spaceId, String childIds) throws BusinessException {
		buildSpaceService.setSpaceRelation(spaceId, childIds, orgId,tenantId);
	}

	@Override
	public List<SpaceResp> getChildSpace(@RequestBody SpaceReq spaceReq) throws BusinessException {
		return buildSpaceService.getChildSpace(spaceReq);
	}

	@Override
	public List<SpaceVo> getChildSpaceStatus(Long tenantId,Long orgId, Long buildId, String type) throws BusinessException {
		return buildSpaceService.getChildSpaceStatus(buildId, type,orgId,tenantId);
	}

	@Override
	public List<SpaceResp> getSpaceByCondition(@RequestBody SpaceReq spaceReq) throws BusinessException {
		return buildSpaceService.getSpaceByCondition(spaceReq);
	}

	@Override
	public List<Map<String, Object>> getDirectDeviceBySpace(Long tenantId,Long orgId, Long spaceId) {
		return buildSpaceService.getDirectDeviceBySpace(spaceId,orgId,tenantId);
	}

	@Override
	public List<String> getMountDeviceBySpaceId(@RequestBody SpaceDeviceReq spaceDeviceReq) {
		return null;
	}

	@Override
	public void updateSpaceDevicePosition(@RequestBody SpaceDeviceReq spaceDeviceReq) {
		buildSpaceService.updateSpaceDevicePosition(spaceDeviceReq);
	}

	@Override
	public int updateSpaceDeviceStatus(@RequestBody SpaceDeviceReq spaceDeviceReq) {
		buildSpaceService.updateSpaceDevice(spaceDeviceReq);
		return 1;
	}

	@Override
	public Integer countBySpaceAndDevice(@RequestBody QueryParamReq queryParamReq) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SpaceResp> findSpaceByTenantId(Long tenantId,Long orgId) {
		return buildSpaceService.findSpaceByTenantId(tenantId,orgId);
	}

	@Override
	public void syncSpaceStatus(String deviceId) {
		buildSpaceService.syncSpaceStatus(deviceId);
	}

	@Override
	public int findSpaceCountByParentId(@RequestBody SpaceReq space) {
		return buildSpaceService.findSpaceCountByParentId(space);
	}

	@Override
	public boolean checkSpaceName(@RequestBody SpaceReq spaceReq) {
		return buildSpaceService.checkSpaceName(spaceReq);
	}

	@Override
	public List<SpaceResp> findSpaceByType(@RequestBody SpaceReq spaceReq) {
		return buildSpaceService.findSpaceByType(spaceReq);
	}

	@Override
	public Set<String> countGatewayDeviceOnRoom(Long tenantId,Long orgId, Long spaceId) {
		return null;
	}

	@Override
	public List<SpaceResp> findSpaceUnMount(@RequestBody SpaceReq spaceReq) {
		return null;
	}

	@Override
	public List<Map<String, Object>> getMeetingSpaceTree(Long tenantId,Long orgId, Long locatinoId) {
		return buildSpaceService.getMeetingSpaceTree(tenantId,orgId,locatinoId);
	}

	public List<LocationResp> findLocationByCondition(@RequestBody LocationReq locationReq) {
		return buildSpaceService.findLocationByCondition(locationReq);
	}

	@Override
	public void saveLocation(@RequestBody LocationReq locationReq) {
		buildSpaceService.saveLocation(locationReq);
	}

	@Override
	public void updateLocation(@RequestBody LocationReq locationReq) {
		buildSpaceService.updateLocation(locationReq);
	}

	@Override
	public void delLocation(Long tenantId,Long orgId, Long id) {
		buildSpaceService.delLocation(id);
	}

	@Override
	public CommonResponse spaceDataImport(@RequestBody SpaceExcelReq spaceExcelReq) {
		return buildSpaceService.spaceDataImport(spaceExcelReq);
	}

	@Override
	public String saveSpaceBackgroundImgImport(@RequestBody SpaceBackgroundImgReq req) {
		return buildSpaceService.saveSpaceBackgroundImgImport(req);
	}

	@Override
	public List<SpaceBackgroundImgResp> getSpaceBackgroundImg(@RequestBody SpaceBackgroundImgReq req) {
		return buildSpaceService.getSpaceBackgroundImg(req);
	}

	@Override
	public SpaceBackgroundImgResp getSpaceBackgroundImgById(Long tenantId,Long orgId, Long id) {
		return buildSpaceService.getSpaceBackgroundImgById(id);
	}

	@Override
	public Integer deleteSpaceBackgroundImg(Long tenantId,Long orgId, Long id) {
		return buildSpaceService.deleteSpaceBackgroundImg(id);
	}

	@Override
	public Integer updateSpaceBackgroundImg(@RequestBody SpaceBackgroundImgReq req) {
		return buildSpaceService.updateSpaceBackgroundImg(req);
	}

	@Override
	public List<DeploymentResp> getDeploymentList(@RequestBody DeploymentReq req) {
		return buildSpaceService.getDeploymentList(req);
	}

	@Override
	public DeploymentResp findDeploymentById(Long tenantId,Long orgId, Long id) {
		return buildSpaceService.findDeploymentById(tenantId,orgId, id);
	}

	@Override
	public List<SpaceResp> findSpaceListByDeployId(Long deployId, Long tenantId,Long orgId, Long locationId) {
		return buildSpaceService.findSpaceListByDeployId(deployId, tenantId, orgId,locationId);
	}

	@Override
	public int getSpaceStatus(Long tenatId,Long orgId, Long spaceId) {
		return buildSpaceService.getSpaceStatus(tenatId,orgId,spaceId);
	}

	@Override
	public int setSpaceStatus(Long tenatId,Long orgId, Long spaceId) {
		return buildSpaceService.setSpaceStatus(tenatId, orgId,spaceId);
	}

	@Override
	public List<SpaceResp> findBySpaceIds(@RequestBody QueryParamReq req) {
		return buildSpaceService.findByIds(req.getTenantId(),req.getOrgId(), req.getSpaceIds());
	}

	@Override
	public void addOrUpdateCalendar(@RequestBody CalendarReq calendarReq) {
		buildSpaceService.addOrUpdateCalendar(calendarReq);
	}

	@Override
	public void deleteCalendar(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("id") Long id) {
		buildSpaceService.deleteCalendar(tenantId,orgId, id);
	}

	@Override
	public Page<CalendarResp> findCalendarList(@RequestParam("pageNum") String pageNum,
			@RequestParam("pageSize") String pageSize, @RequestParam(value = "name", required = false) String name)
			throws BusinessException {
		return buildSpaceService.findCalendarList(pageNum, pageSize, name);
	}

	@Override
	public List<CalendarResp> findCalendarListNoPage(@RequestParam(value = "name", required = false) String name)
			throws BusinessException {
		return buildSpaceService.findCalendarListNoPage(name);
	}

	@Override
	public CalendarResp getCalendarIofoById(@RequestParam("tenantId") Long tenantId,@RequestParam("orgId") Long orgId, @RequestParam("id") Long id) {
		CalendarResp calendarResp = buildSpaceService.getCalendarIofoById(tenantId,orgId, id);
		return calendarResp;
	}

	@Override
	public void saveOrUpdateDeploy(@RequestBody DeploymentReq req) {
		buildSpaceService.saveOrUpdate(req);
	}

	@Override
	public void deleteBatchDeploy(String deployIds) {
		buildSpaceService.deleteBatchDeploy(deployIds);
	}

	@Override
	public Page<DeploymentResp> getDeploymentPage(@RequestBody DeploymentReq req) {
		return buildSpaceService.getDeploymentPage(req);
	}

	@Override
	public List<Long> getLocationTenant() {
		return buildSpaceService.getLocationTenant();
	}

	@Override
	public void replaceDevice(@RequestBody SpaceDeviceReq spaceDeviceReq) {
		// spaceId deviceId oldDeviceId tenantId locationId userId
		GetDeviceInfoRespVo oldDeviceInfo = deviceCoreApi.get(spaceDeviceReq.getDeviceId());
		FetchUserResp user = userApi.getUser(spaceDeviceReq.getUserId());
		SpaceDeviceResp spaceDeviceResp = new SpaceDeviceResp();
		if (oldDeviceInfo != null) {
			// 替换旧设备空间关系
			List<SpaceDeviceResp> spaceDeviceResps = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
			if (CollectionUtils.isNotEmpty(spaceDeviceResps) && spaceDeviceResps.size() > 0) {
				spaceDeviceResp = spaceDeviceResps.get(0);
			}
			BeanUtil.copyProperties(spaceDeviceResp, spaceDeviceReq);
			spaceDeviceReq.setDeviceId(spaceDeviceReq.getNewDeviceId());
			spaceDeviceApi.updateSpaceDevice(spaceDeviceReq);
			// 替换旧设备的sceneDetail的数据 uploadStatus = 0
			SceneDetailReq sceneDetailReq = new SceneDetailReq();
			sceneDetailReq.setTenantId(spaceDeviceReq.getTenantId());
			sceneDetailReq.setLocationId(spaceDeviceReq.getLocationId());
			sceneDetailReq.setDeviceId(oldDeviceInfo.getUuid());
			List<SceneDetailResp> sceneDetailResps = sceneApi.sceneDetailByParamDoing(sceneDetailReq);
			if (CollectionUtils.isNotEmpty(sceneDetailResps)) {
				for (SceneDetailResp sceneDetailResp : sceneDetailResps) {
					SceneDetailReq sceneDetail = new SceneDetailReq();
					sceneDetail.setTenantId(sceneDetailResp.getTenantId());
					BeanUtil.copyProperties(sceneDetailResp, sceneDetail);
					// 替换情景详情 旧设备Id 和 target_value 里面的deviceId
					Map<String, Object> propertyMap = (Map<String, Object>) JSON.parse(sceneDetail.getTargetValue());
					if (propertyMap != null && propertyMap.get("deviceId") != null) {
						propertyMap.put("deviceId", spaceDeviceReq.getNewDeviceId());
						sceneDetail.setTargetValue(JSON.toJSONString(propertyMap));
					}
					sceneDetail.setDeviceId(spaceDeviceReq.getNewDeviceId());
					sceneApi.updateSceneDetail(sceneDetail);
					// 更新scene主表 下发标识
					SceneReq sceneReq = new SceneReq();
					sceneReq.setUploadStatus(0);
					sceneReq.setUpdateTime(new Date());
					sceneReq.setId(sceneDetailResp.getSceneId());
					sceneReq.setTenantId(sceneDetailResp.getTenantId());
					sceneReq.setLocationId(sceneDetailResp.getLocationId());
					sceneApi.updateScene(sceneReq);
					// 删除情景相关缓存
					sceneCacheDelete(user, sceneDetailResp);
					// 删除网关情景
					MultiProtocolGatewayNewThread newThread = new MultiProtocolGatewayNewThread();
					newThread.deleteScene(oldDeviceInfo.getParentId(), sceneDetailResp.getSceneId());
				}
			}
			// 替换旧设备的ifttt数据
			replaceIfttt(oldDeviceInfo.getUuid(),spaceDeviceReq.getOrgId(),sceneDetailReq.getTenantId(),spaceDeviceReq.getNewDeviceId());
		}
	}
    /**
     * 删除情景缓存数据
     * @param user
     * @param sceneDetailResp
     */
	private void sceneCacheDelete(FetchUserResp user, SceneDetailResp sceneDetailResp) {
		String sceneUserListIdKey = RedisKeyUtil.getSceneUserListIdKey(user.getId(), user.getTenantId());
		List<Map> sceneIdList = RedisCacheUtil.listGetAll(sceneUserListIdKey, Map.class);
		String sceneListIdKey = RedisKeyUtil.getSceneListIdKey(sceneDetailResp.getSceneId(), user.getTenantId());
		List<String> sceneIdListKey = new ArrayList();
		sceneIdList.forEach(m -> {
			sceneIdListKey.add(RedisKeyUtil.getSceneListIdKey(new Long(m.get("sceneId").toString()), user.getTenantId()));
		});
		String gatewaySceneKey = RedisKeyUtil.getSceneGatewayDeviceMapIdKey(sceneDetailResp.getSceneId(),user.getTenantId());
		String externalSceneKey = RedisKeyUtil.getSceneExternalDeviceListIdKey(sceneDetailResp.getSceneId(),user.getTenantId());
		// 移除缓存
		RedisCacheUtil.delete(sceneUserListIdKey);
		RedisCacheUtil.delete(sceneIdListKey);
		RedisCacheUtil.delete(sceneListIdKey);
		
		RedisCacheUtil.delete(gatewaySceneKey);
		RedisCacheUtil.delete(externalSceneKey);
		
		SceneReq scene = new SceneReq();
		scene.setId(sceneDetailResp.getSceneId());
		scene.setTenantId(user.getTenantId());
		sceneApi.sceneById(scene);
	}

	@Override
	public List<Map<String, Object>> getDeviceListBySpace(@RequestBody SpaceDeviceReq spaceDeviceReq) {
		return buildSpaceDeviecService.getDeviceListBySpace(spaceDeviceReq);
	}

	/**
	 * 替换旧设备的ifttt数据
	 */
	private void replaceIfttt(String deviceId,Long orgId,Long tenantId,String newDeviceId) {
		LOGGER.info("================replaceIfttt==============start");
		try {
			SaveIftttReq ruleVO = new SaveIftttReq();
			String newDeviceName = deviceCoreApi.get(newDeviceId).getName();
			ruleVO.setSensorDeviceId(deviceId);
			ruleVO.setActuatorDeviceId(deviceId);
			ruleVO.setTenantId(tenantId);
			List<TriggerVo> triggerList = autoTobApi.getTriggerTobListByDeviceId(ruleVO);
			List<Long> sensorRuleIdList = Lists.newArrayList();
			List<Long> actuatorRuleIdList = Lists.newArrayList();
			boolean isSingleGatewayFlag = false;
			if (CollectionUtils.isNotEmpty(triggerList)) {
				LOGGER.info("================replaceIfttt==============process1");
				LOGGER.info("================replaceIfttt==============triggerList====="+triggerList.size());
				for (TriggerVo triggerVo : triggerList) {
					if (triggerVo.getSensorDeviceId().equals(deviceId)) {
						//替换的设备为sensor
						LOGGER.info("================replaceIfttt==============替换的设备为sensor=====");
						sensorRuleIdList.add(triggerVo.getRuleId());
					} else if (triggerVo.getActuctorDeviceId().equals(deviceId)) {
						//替换的设备为actuator
						LOGGER.info("================replaceIfttt==============替换的设备为actuator=====");
						actuatorRuleIdList.add(triggerVo.getRuleId());
					}
				}
				LOGGER.info("================replaceIfttt==============sensorRuleIdList====="+sensorRuleIdList.size());
				LOGGER.info("================replaceIfttt==============actuatorRuleIdList====="+actuatorRuleIdList.size());
			}
			if(CollectionUtils.isNotEmpty(sensorRuleIdList)){
				LOGGER.info("================replaceIfttt==============process2.1");
				for(Long ruleId:sensorRuleIdList){
					RuleResp ruleResp = autoTobApi.get(orgId,tenantId,ruleId);
					LOGGER.info("================replaceIfttt==============process2.1=======sensorVos====="+ruleResp.getSensors().size());
					if(CollectionUtils.isNotEmpty(ruleResp.getSensors())){
						for(SensorVo sensorVo : ruleResp.getSensors()){
							if(sensorVo.getDeviceId().equals(deviceId)){
								sensorVo.setDeviceId(newDeviceId);
								sensorVo.setName(newDeviceName);
							}
						}
						LOGGER.info("================replaceIfttt==============process2.1=======getTriggers.size()====="+ruleResp.getTriggers().size());
						for(TriggerVo triggerVo : ruleResp.getTriggers()){
							if(StringUtil.isNotEmpty(triggerVo.getSensorDeviceId())){
								LOGGER.info("================replaceIfttt==============process2.1=======triggerVo.getId()====="+triggerVo.getId());
								LOGGER.info("================replaceIfttt==============process2.1=======deviceId====="+deviceId);
								LOGGER.info("================replaceIfttt==============process2.1=======getTriggers====="+triggerVo);
								LOGGER.info("================replaceIfttt==============process2.1=======triggerVo.getSensorDeviceId()====="+triggerVo.getSensorDeviceId());
								if(triggerVo.getSensorDeviceId().equals(deviceId)){
									triggerVo.setSourceLabel(newDeviceName);
									LOGGER.info("======triggerVo.getPosition======="+triggerVo.getPosition());
									LOGGER.info("======triggerVo.getSensorPosition======="+triggerVo.getSensorPosition());
								}
							}
						}
					}
					//ruleResp转化为SaveIftttReq
					SaveIftttReq saveIftttReq = new SaveIftttReq();
					saveIftttReq = ruleRespChangeToIftttReq(ruleResp,ruleResp.getSpaceId());
					//判断是同网关还是跨网关
					isSingleGatewayFlag = isSingleGateway(saveIftttReq);
					LOGGER.info("================replaceIfttt==============process2.1=======isSingleGatewayFlag====="+isSingleGatewayFlag);
					LOGGER.info("================replaceIfttt==============process2.1=======addOrUpdateIfttt=====start");
					//如果有主键则先删除旧规则
					//if (saveIftttReq.getId() != null) {
					LOGGER.info("================replaceIfttt==============process2.2=======如果有主键则先删除旧规则"+saveIftttReq.getId());
					LOGGER.info("================replaceIfttt==============process2.2=======如果有主键则先删除旧规则");
					autoTobApi.delete(saveIftttReq.getOrgId(),saveIftttReq.getTenantId(),saveIftttReq.getId(),false) ;
					//}
					addOrUpdateIfttt(isSingleGatewayFlag,saveIftttReq);
					LOGGER.info("================replaceIfttt==============process2.1=======addOrUpdateIfttt=====success");
				}
			}
			if(CollectionUtils.isNotEmpty(actuatorRuleIdList)){
				LOGGER.info("================replaceIfttt==============process2.2");
				for(Long ruleId:actuatorRuleIdList){
					RuleResp ruleResp = autoTobApi.get(orgId,tenantId,ruleId);
					LOGGER.info("================replaceIfttt==============process2.2=======actuators====="+ruleResp.getActuators().size());
					if(CollectionUtils.isNotEmpty(ruleResp.getActuators())){
						for(ActuatorVo actuatorVo : ruleResp.getActuators()){
							if(actuatorVo.getDeviceId().equals(deviceId)){
								actuatorVo.setDeviceId(newDeviceId);
								actuatorVo.setName(newDeviceName);
							}
						}
						for(TriggerVo triggerVo : ruleResp.getTriggers()){
							if(StringUtil.isNotEmpty(triggerVo.getActuctorDeviceId())){
								LOGGER.info("================replaceIfttt==============process2.1=======triggerVo.getId()====="+triggerVo.getId());
								LOGGER.info("================replaceIfttt==============process2.1=======deviceId====="+deviceId);
								LOGGER.info("================replaceIfttt==============process2.1=======getTriggers====="+triggerVo);
								LOGGER.info("================replaceIfttt==============process2.1=======triggerVo.getActuctorDeviceId()====="+triggerVo.getSensorDeviceId());
								if(triggerVo.getActuctorDeviceId().equals(deviceId)){
									triggerVo.setDestinationLabel(newDeviceName);
									LOGGER.info("======triggerVo.getPosition======="+triggerVo.getPosition());
									LOGGER.info("======triggerVo.getActuctorPosition======="+triggerVo.getActuctorPosition());
								}
							}
						}
					}
					//ruleResp转化为SaveIftttReq
					SaveIftttReq saveIftttReq = new SaveIftttReq();
					saveIftttReq = ruleRespChangeToIftttReq(ruleResp,ruleResp.getSpaceId());
					//判断是同网关还是跨网关
					isSingleGatewayFlag = isSingleGateway(saveIftttReq);
					LOGGER.info("================replaceIfttt==============process2.2=======isSingleGatewayFlag====="+isSingleGatewayFlag);
					LOGGER.info("================replaceIfttt==============process2.2=======addOrUpdateIfttt=====start");
					//如果有主键则先删除旧规则
					//if (saveIftttReq.getId() != null) {
					LOGGER.info("================replaceIfttt==============process2.2=======如果有主键则先删除旧规则");
					autoTobApi.delete(orgId,saveIftttReq.getTenantId(),saveIftttReq.getId(),false) ;
					//}
					addOrUpdateIfttt(isSingleGatewayFlag,saveIftttReq);
					LOGGER.info("================replaceIfttt==============process2.2=======addOrUpdateIfttt=====success");
				}
			}
			LOGGER.info("================replaceIfttt==============end");
		}catch (Exception e){
			e.printStackTrace();
		}
	}


	public void addOrUpdateIfttt(boolean isSingleGatewayFlag,SaveIftttReq ruleVO){
		if(isSingleGatewayFlag){//单网关
			LOGGER.info("================replaceIfttt==============process2.1=======单网关=====");
			String client = deviceCoreApi.get(ruleVO.getSensors().get(0).getDeviceId()).getParentId();
			if(ruleVO.getId() !=null){
				//修改，要删除网关里的ifttt
				autoTobApi.deleteLowerHair(ruleVO.getId(),client);
			}
			ruleVO.setUploadStatus("1");
			//重构后，分四步
			//第一步先保存联动信息（sensor、actuator、or或and）,表：applet,applet_this,applet_that,applet_item
			Long appletId = autoTobApi.saveAuto(ruleVO);
			//第二步保存/修改ifttt记录（保存/修改build_tob_rule表）,相当于之前的ifttt_rule
			Long buildToRuleId = autoTobApi.saveBuildTobRule(ruleVO,appletId);
			//第三步保存点、线、sensor的位置、类型、属性，actuctor的位置、类型、属性  表为tob_trigger,相当于之前的ifttt_trigger
			autoTobApi.saveTobTrigger(ruleVO,buildToRuleId);
			//第四步保存tob_relation,相当于之前的ifttt_relation
			autoTobApi.saveTobRelation(ruleVO,buildToRuleId);
			//创建下发给网关
			ruleVO.setClientId(client);
			ruleVO.setId(buildToRuleId);
			autoTobApi.createLowerHair(ruleVO);
		}else {//多网关
			LOGGER.info("================replaceIfttt==============process2.1=======多网关=====");
			ruleVO.setUploadStatus("0");//跨网关//重构后，分四步
			//第一步先保存联动信息（sensor、actuator、or或and）,表：applet,applet_this,applet_that,applet_item
			Long appletId = autoTobApi.saveAuto(ruleVO);
			//第二步保存/修改ifttt记录（保存/修改build_tob_rule表）,相当于之前的ifttt_rule
			Long buildToRuleId = autoTobApi.saveBuildTobRule(ruleVO,appletId);
			//第三步保存点、线、sensor的位置、类型、属性，actuctor的位置、类型、属性  表为tob_trigger,相当于之前的ifttt_trigger
			autoTobApi.saveTobTrigger(ruleVO,buildToRuleId);
			//第四步保存tob_relation,相当于之前的ifttt_relation
			autoTobApi.saveTobRelation(ruleVO,buildToRuleId);
		}
	}

	public boolean isSingleGateway(SaveIftttReq ruleVO){
		List<String> deviceIds = Lists.newArrayList();
		List<Long> sceneIds = Lists.newArrayList();
		Map<String,Object> map = new HashMap<String,Object>();
		boolean isSingleGatewayFlag = false;
		for(ActuatorVo actuatorVo : ruleVO.getActuators()){
			if(StringUtil.isNotBlank(actuatorVo.getType())){
				if (actuatorVo.getType().equals("sence")) {//情景
					sceneIds.add(Long.valueOf(actuatorVo.getDeviceId()));
				} else if(actuatorVo.getType().equals("space")){//空间
					map = jsonStringToMap(actuatorVo.getProperties());
					sceneIds.add(Long.valueOf(map.get("senceId").toString()));
				}else {//设备
					deviceIds.add(actuatorVo.getDeviceId());
				}
			}else {//设备
				deviceIds.add(actuatorVo.getDeviceId());
			}
		}
		ruleVO.setDeviceIds(deviceIds);
		ruleVO.setSceneIds(sceneIds);
		isSingleGatewayFlag = autoTobApi.isSingleGateway(ruleVO);
		return isSingleGatewayFlag;

	}

	/**
	 * json字符串转map
	 * @param s
	 * @return
	 */
	public static Map jsonStringToMap(String s){
		JSONObject jsonObject = JSON.parseObject(s);
		Map map=new HashMap();
		map = jsonObject;
		return map;
	}

	private SaveIftttReq ruleRespChangeToIftttReq(RuleResp resp,Long spaceId) {
		SaveIftttReq iftttReq=new SaveIftttReq();
		iftttReq.setSpaceId(spaceId);
		iftttReq.setAppletId(resp.getAppletId());
		iftttReq.setDelay(resp.getDelay());
		iftttReq.setDirectId(resp.getDirectId());
		iftttReq.setActuators(resp.getActuators());
		iftttReq.setIcon(resp.getIcon());
		iftttReq.setId(resp.getId());
		iftttReq.setIftttType(resp.getIftttType());
		iftttReq.setIsMulti(resp.getIsMulti());
		iftttReq.setLocationId(resp.getLocationId());
		iftttReq.setName(resp.getName());
		iftttReq.setProductId(resp.getProductId());
		iftttReq.setRelations(resp.getRelations());
		iftttReq.setRuleType(resp.getRuleType());
		iftttReq.setSecurityType(resp.getSecurityType());
		iftttReq.setSensors(resp.getSensors());
		//iftttReq.setSpaceId(resp.getSpaceId());
		iftttReq.setStatus(resp.getStatus());
		iftttReq.setTemplateFlag(resp.getTemplateFlag());
		iftttReq.setTemplateId(resp.getTemplateId());
		iftttReq.setTenantId(resp.getTenantId());
		iftttReq.setTriggers(resp.getTriggers());
		iftttReq.setType(resp.getType());
		iftttReq.setUserId(resp.getUserId());
		iftttReq.setOrgId(resp.getOrgId());
		return iftttReq;
	}
	
	@Override
	public DeploymentResp findByName(Long orgId,String name) {
		return buildSpaceService.findByName(orgId,name);
	}

}

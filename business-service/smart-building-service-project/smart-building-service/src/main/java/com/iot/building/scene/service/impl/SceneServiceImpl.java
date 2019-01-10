package com.iot.building.scene.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.allocation.service.IAllocationService;
import com.iot.building.common.service.ShortcutService;
import com.iot.building.device.api.DeviceBusinessTypeApi;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.building.excepiton.BusinessExceptionEnum;
import com.iot.building.gateway.MultiProtocolGatewayHepler;
import com.iot.building.gateway.MultiProtocolGatewayNewThread;
import com.iot.building.group.service.IGroupService;
import com.iot.building.helper.BeanChangeUtil;
import com.iot.building.helper.Constants;
import com.iot.building.helper.DevicePropertyDTO;
import com.iot.building.helper.ThreadPoolUtil;
import com.iot.building.helper.ToolUtils;
import com.iot.building.mqtt.BusinessDispatchMqttHelper;
import com.iot.building.scene.domain.LocationScene;
import com.iot.building.scene.domain.LocationSceneDetail;
import com.iot.building.scene.domain.LocationSceneRelation;
import com.iot.building.scene.mapper.LocationSceneMapper;
import com.iot.building.scene.service.SceneService;
import com.iot.building.scene.util.RedisKeyUtil;
import com.iot.building.scene.vo.req.LocationSceneDetailReq;
import com.iot.building.scene.vo.req.LocationSceneRelationReq;
import com.iot.building.scene.vo.req.LocationSceneReq;
import com.iot.building.scene.vo.req.SceneTemplateManualReq;
import com.iot.building.scene.vo.resp.LocationSceneDetailResp;
import com.iot.building.scene.vo.resp.LocationSceneRelationResp;
import com.iot.building.scene.vo.resp.LocationSceneResp;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.building.template.service.TemplateService;
import com.iot.building.template.vo.req.CreateSceneFromTemplateReq;
import com.iot.building.template.vo.req.TemplateReq;
import com.iot.building.template.vo.rsp.TemplateResp;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.JsonUtil;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.exception.SceneExceptionEnum;
import com.iot.control.scene.vo.req.SceneAddReq;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.req.SceneUpdateReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceVo;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.api.DeviceTypeCoreApi;
import com.iot.device.vo.req.device.ListDeviceInfoReq;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeByDeviceRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.redis.RedisCacheUtil;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.util.AssertUtils;

/**
 * 项目名称：IOT云平台 模块名称： 功能描述：情景service实现 创建人： wujianlong 创建时间：2017年11月9日 下午2:57:16
 * 修改人： wujianlong 修改时间：2017年11月9日 下午2:57:16
 */
@Service
@Transactional
public class SceneServiceImpl implements SceneService {

	private static final Logger log = LoggerFactory.getLogger(SceneServiceImpl.class);

	@Autowired
	private UserApi userApi;
	@Autowired
	private DeviceCoreApi deviceCoreApi;
	@Autowired
	private Environment dnvironment;
	@Autowired
	private DeviceTypeCoreApi deviceTypeApi;
	@Autowired
	private LocationSceneMapper locationSceneMapper;
	@Autowired
	private DeviceBusinessTypeApi deviceBusinessTypeApi;
	@Autowired
	private SceneApi sceneApi;
	@Autowired
	private SpaceApi spaceApi;
	@Autowired
	private SpaceDeviceApi spaceDeviceApi;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private IBuildingSpaceService iBuildingSpaceService;
	@Autowired
	private IAllocationService allocationService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private ShortcutService shortcutService;
	@Autowired
	private DeviceStateCoreApi deviceStateCoreApi;

	private final static Logger LOGGER = LoggerFactory.getLogger(SceneServiceImpl.class);

	/**
	 * 描述：保存情景微调
	 *
	 * @param deviceTarValues
	 *            json
	 * @param setType
	 *            1.设备类型 2.全量设置 3.业务类型
	 * @author wujianlong
	 * @created 2017年11月16日 下午4:22:27
	 * @since
	 */
	@Override
	public void saveSceneAndSceneDetail(String deviceTarValues, Long userId, Integer setType, Long locationId) {
		if (StringUtils.isBlank(deviceTarValues)) {
			throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);// 参数不能为空
		}
		// 解析出 sceneName、spaceId
		String sceneName = "";
		Long spaceId = null;
		Integer silenceStatus = null;
		if (StringUtils.isNotBlank(deviceTarValues)) {
			List<Map> valueList = JSON.parseArray(deviceTarValues, Map.class);
			for (Map map : valueList) {
				sceneName = ((String) map.get(Constants.SCENE_NAME));
				spaceId = Long.valueOf(map.get(Constants.SPACE_ID).toString());
				silenceStatus = (Integer) map.get(Constants.SCENE_SILENCE);
				break;
			}
		}
		int existSceneName = sceneApi.countBySceneName(sceneName, userId, new Long(0));
		if (existSceneName != 0) {
			throw new BusinessException(SceneExceptionEnum.SCENE_NAME_IS_EXIST);
		} else {
			SceneAddReq sceneAddReq = new SceneAddReq();
			sceneAddReq.setSceneName(sceneName);
			sceneAddReq.setUserId(userId);
			sceneAddReq.setSpaceId(spaceId);
			sceneAddReq.setLocationId(locationId);
			sceneAddReq.setSetType(setType);
			sceneAddReq.setSilenceStatus(silenceStatus);
			int sceneSort = getSceneSort(spaceId);
			sceneAddReq.setSort(sceneSort);
			SceneReq scene = saveScene(sceneAddReq);
			List<SceneDetailReq> sceneDetailList = (List<SceneDetailReq>) JsonUtil.fromJsonArray(deviceTarValues,
					SceneDetailReq.class);
			if (CollectionUtils.isNotEmpty(sceneDetailList)) {
				for (SceneDetailReq sceneDetail : sceneDetailList) {
					if (sceneDetail.getDeviceTypeId().intValue() == -1019) {
						Map<String, Object> propertyMap = JSON.parseObject(sceneDetail.getTargetValue());
						Map<String, Object> targetValue = Maps.newHashMap();
						targetValue.put("OnOff", propertyMap.get("OnOff"));
						targetValue.put("deviceId", propertyMap.get("deviceId"));
						sceneDetail.setTargetValue(JSON.toJSONString(targetValue));
					}
					saveSceneDetaiCommon(userId, scene, sceneDetail);
				}
				RedisCacheUtil.delete(RedisKeyUtil.getSceneDetailListIdKey(scene.getId(), scene.getTenantId()));
			}
		}
	}

	private void saveSceneDetaiCommon(Long userId, SceneReq scene, SceneDetailReq sceneDetail) {
		sceneDetail.setSceneId(scene.getId());
		sceneDetail.setCreateBy(userId);
		sceneDetail.setUpdateBy(userId);
		sceneDetail.setCreateTime(new Date());
		sceneDetail.setUpdateTime(new Date());
		sceneDetail.setTenantId(scene.getTenantId());
		sceneDetail.setLocationId(scene.getLocationId());
		targetSort(sceneDetail);
		sceneApi.insertSceneDetail(sceneDetail);
	}

	private void targetSort(SceneDetailReq sceneDetail) {
		Map<String, Object> propertyMap = JSON.parseObject(sceneDetail.getTargetValue());
		Map<String, Object> newMap = Maps.newHashMap();
		if (propertyMap.containsKey("OnOff")) {
			newMap.put("OnOff", propertyMap.get("OnOff"));
		}
		for (String key : propertyMap.keySet()) {
			if (!key.equals("OnOff")) {
				newMap.put(key, propertyMap.get(key));
			}
		}
	}

	public SceneReq saveScene(SceneAddReq sceneAddReq) {
		if (sceneAddReq == null) {
			log.error("------saveScene() error! sceneSaveReq is null");
			throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
		}

		if (StringUtils.isBlank(sceneAddReq.getSceneName())) {
			throw new BusinessException(SceneExceptionEnum.SCENE_NAME_NOT_ALLOW_EMPTY);
		}
		// 判断scene名称是否符合要求
		// String language = LocaleContextHolder.getLocale().toString();
		// String name = sceneAddReq.getSceneName();
		// if ("zh_CN".equals(language)) {
		// CommonStringUtil.checkStringParam(name, 0, 30, language);
		// } else {
		// CommonStringUtil.checkStringParam(name, 0, 30, language);
		// }
		FetchUserResp user = userApi.getUser(sceneAddReq.getUserId());
		SceneReq sceneReq = new SceneReq();
		sceneReq.setTenantId(user.getTenantId());
		sceneReq.setOrgId(user.getOrgId());
		sceneReq.setLocationId(user.getLocationId());
		sceneReq.setSpaceId(sceneAddReq.getSpaceId());
		sceneReq.setSceneName(sceneAddReq.getSceneName());
		List<SceneResp> sceneResps = sceneApi.sceneByParamDoing(sceneReq);
		if (sceneResps != null && sceneResps.size() > 0) {
			throw new BusinessException(SceneExceptionEnum.SCENE_NAME_IS_EXIST);
		}
		// int existSceneName = sceneApi.countBySceneName(sceneAddReq.getSceneName(),
		// sceneAddReq.getUserId(), 0L);
		// if (existSceneName > 0) {
		// throw new BusinessException(SceneExceptionEnum.SCENE_NAME_IS_EXIST);
		// }

		Date currentTime = new Date();
		SceneReq scene = new SceneReq();
		scene.setSceneName(sceneAddReq.getSceneName());
		scene.setSpaceId(sceneAddReq.getSpaceId());
		scene.setCreateBy(sceneAddReq.getUserId());
		scene.setUpdateBy(sceneAddReq.getUserId());
		scene.setCreateTime(currentTime);
		scene.setUpdateTime(currentTime);
		scene.setIcon(sceneAddReq.getIcon());
		scene.setSetType(sceneAddReq.getSetType());
		scene.setSort(sceneAddReq.getSort());
		scene.setTenantId(user.getTenantId());
		scene.setOrgId(user.getOrgId());
		scene.setLocationId(sceneAddReq.getLocationId());
		scene.setTemplateId(sceneAddReq.getTemplateId());
		scene.setUploadStatus(0);
		if (sceneAddReq.getSilenceStatus() != null) {
			scene.setSilenceStatus(sceneAddReq.getSilenceStatus());
		}
		SceneResp sceneResp = sceneApi.insertScene(scene);
		scene.setId(sceneResp.getId());
		// 移除缓存
		String sceneUserListIdKey = RedisKeyUtil.getSceneUserListIdKey(sceneAddReq.getUserId(), user.getTenantId());
		List<Map> sceneIdList = RedisCacheUtil.listGetAll(sceneUserListIdKey, Map.class);
		List<String> sceneIdListKey = new ArrayList();
		sceneIdList.forEach(m -> {
			sceneIdListKey
					.add(RedisKeyUtil.getSceneListIdKey(new Long(m.get("sceneId").toString()), user.getTenantId()));
		});
		// 移除缓存
		RedisCacheUtil.delete(sceneUserListIdKey);
		RedisCacheUtil.delete(sceneIdListKey);
		return scene;
	}

	private int getSceneSort(Long spaceId) {
		int sort = 1;
		SceneResp existScene = sceneApi.maxSortSceneBySpaceId(spaceId);
		if (existScene != null && existScene.getSort() != null) {
			sort = existScene.getSort() + 1;
		}
		return sort;
	}

	@Override
	public List<SceneDetailResp> findSceneDetailInfo(SceneDetailReq sceneDetailReq) {
		List<SceneDetailResp> respList = sceneApi.sceneDetailByParam(sceneDetailReq);
		if (respList != null) {
			for (SceneDetailResp resp : respList) {
				// 获取scene名称
				SceneReq sceneReq = new SceneReq();
				sceneReq.setTenantId(resp.getTenantId());
				sceneReq.setOrgId(resp.getOrgId());
				sceneReq.setLocationId(resp.getLocationId());
				sceneReq.setId(resp.getSceneId());
				SceneResp sceneResp = sceneApi.sceneById(sceneReq);
				resp.setSceneName(sceneResp.getSceneName());
				resp.setSilenceStatus(sceneResp.getSilenceStatus());
				// 设置属性值
				GetDeviceInfoRespVo deviceResp = deviceCoreApi.get(resp.getDeviceId());
				if (deviceResp != null) {
					resp.setDeviceName(deviceResp.getName());
					resp.setDeviceTypeId(deviceResp.getDeviceTypeId());

					GetDeviceTypeInfoRespVo deviceTypeResp = deviceTypeApi.get(deviceResp.getDeviceTypeId());
					DeviceBusinessTypeResp deviceBusinessType = deviceBusinessTypeApi.findById(deviceResp.getOrgId(),deviceResp.getTenantId(),
							deviceResp.getBusinessTypeId());

					if (deviceTypeResp != null) {
						resp.setDeviceTypeName(deviceTypeResp.getName());
					}
					if (deviceBusinessType != null) {
						resp.setBusinessType(deviceBusinessType.getBusinessType());
						resp.setBusinessTypeId(deviceBusinessType.getId());
					}
				}
			}
		}

		return respList;
	}

	/**
	 * 描述：更新情景微调信息
	 *
	 * @param sceneId
	 * @param deviceTarValues
	 * @param userId
	 * @param setType
	 *            1.设备类型 2.全量设置 3.业务类型 排序值
	 * @return
	 * @author linjihuang
	 * @created 2018年3月1日 上午10:26:46
	 */
	@Override
	public void updateSceneDetailInfo(Long sceneId, String deviceTarValues, Long userId, Integer setType) {
		if (sceneId != null && StringUtils.isBlank(deviceTarValues)) {
			throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);// 参数不能为空
		}

		// 获取 情景名称
		String sceneName = null;
		Long spaceId = null;
		Integer silenceStatus = null;
		List<Map> valueList = JSON.parseArray(deviceTarValues, Map.class);
		for (Map map : valueList) {
			sceneName = ((String) map.get(Constants.SCENE_NAME));
			spaceId = Long.parseLong(map.get("spaceId").toString());
			silenceStatus = (Integer) map.get(Constants.SCENE_SILENCE);
			if (StringUtils.isNotBlank(sceneName) && spaceId != null && silenceStatus != null) {
				break;
			}
		}

		// 更新 scene
		SceneUpdateReq sceneUpdateReq = new SceneUpdateReq();
		sceneUpdateReq.setSceneId(sceneId);
		sceneUpdateReq.setSceneName(sceneName);
		sceneUpdateReq.setUserId(userId);
		sceneUpdateReq.setSetType(setType);
		sceneUpdateReq.setSilenceStatus(silenceStatus);
		SceneResp scene = this.updateScene(sceneUpdateReq);
		// 删除缓存
		RedisCacheUtil.delete(RedisKeyUtil.getSceneListIdKey(sceneUpdateReq.getSceneId(), scene.getTenantId()));
		if (scene == null) {
			return;
		}

		// 更新 sceneDetail
		List<SceneDetailReq> sceneDetailList = (List<SceneDetailReq>) JsonUtil.fromJsonArray(deviceTarValues,
				SceneDetailReq.class);
		if (CollectionUtils.isNotEmpty(sceneDetailList)) {
			for (SceneDetailReq sceneDetail : sceneDetailList) {
				sceneDetail.setUpdateBy(userId);
				sceneDetail.setUpdateTime(new Date());
				sceneDetail.setSceneId(sceneId);
				sceneApi.updateSceneDetail(sceneDetail);
			}
		}
		delSceneData(spaceId, sceneId, userId, scene.getLocationId());
	}

	public SceneResp updateScene(SceneUpdateReq sceneUpdateReq) {
		SceneReq scene = new SceneReq();
		// 判断房间名称长度
		String language = LocaleContextHolder.getLocale().toString();
		String name = sceneUpdateReq.getSceneName();
		// if ("zh_CN".equals(language)) {
		// CommonStringUtil.checkStringParam(name, 0, 30, language);
		// } else {
		// CommonStringUtil.checkStringParam(name, 0, 30, language);
		// }
		// 存在名称重复的标识
		int existSceneName = sceneApi.countBySceneName(sceneUpdateReq.getSceneName(), sceneUpdateReq.getUserId(),
				sceneUpdateReq.getSceneId());
		FetchUserResp user = userApi.getUser(sceneUpdateReq.getUserId());
		if (existSceneName == 0) {
			scene.setId(sceneUpdateReq.getSceneId());
			scene.setTenantId(user.getTenantId());
			scene.setOrgId(user.getOrgId());
			scene.setLocationId(user.getLocationId());
			scene.setSceneName(sceneUpdateReq.getSceneName());
			scene.setIcon(sceneUpdateReq.getIcon());
			scene.setUpdateTime(new Date());
			scene.setUpdateBy(sceneUpdateReq.getUserId());
			scene.setUploadStatus(0);
			if (sceneUpdateReq.getSilenceStatus() != null) {
				scene.setSilenceStatus(sceneUpdateReq.getSilenceStatus());
			}
			if (sceneUpdateReq.getSpaceId() != null) {
				scene.setSpaceId(sceneUpdateReq.getSpaceId());
			}
			scene.setSetType(sceneUpdateReq.getSetType());
			if (sceneUpdateReq.getSort() != null) {
				scene.setSort(sceneUpdateReq.getSort());
			}

			int updateCount = sceneApi.updateScene(scene);
			if (updateCount == 0) {
				return null;
			}

			String sceneUserListIdKey = RedisKeyUtil.getSceneUserListIdKey(sceneUpdateReq.getUserId(),
					user.getTenantId());
			List<Map> sceneIdList = RedisCacheUtil.listGetAll(sceneUserListIdKey, Map.class);
			List<String> sceneIdListKey = new ArrayList();
			sceneIdList.forEach(m -> {
				sceneIdListKey
						.add(RedisKeyUtil.getSceneListIdKey(new Long(m.get("sceneId").toString()), user.getTenantId()));
			});
			// 移除缓存
			RedisCacheUtil.delete(sceneUserListIdKey);
			RedisCacheUtil.delete(sceneIdListKey);
		} else {
			throw new BusinessException(SceneExceptionEnum.SCENE_NAME_IS_EXIST);
		}
		SceneResp sceneResp = new SceneResp();
		sceneResp = getById(user.getOrgId(), user.getTenantId(), sceneUpdateReq.getSceneId());
		return sceneResp;
	}

	public SceneResp getById(Long orgId, Long tenantId, Long sceneId) {
		String sceneListIdKey = RedisKeyUtil.getSceneListIdKey(sceneId, tenantId);
		SceneResp sceneResp = RedisCacheUtil.valueObjGet(sceneListIdKey, SceneResp.class);
		if (sceneResp == null) {
			SceneReq sceneReq = new SceneReq();
			sceneReq.setId(sceneId);
			sceneReq.setOrgId(orgId);
			sceneReq.setTenantId(tenantId);
			sceneResp = sceneApi.sceneById(sceneReq);
			RedisCacheUtil.valueObjSet(sceneListIdKey, sceneResp, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT);
		}
		return sceneResp;
	}

	private void delSceneData(Long spaceId, Long sceneId, Long userId, Long locationId) throws BusinessException {
		FetchUserResp user = userApi.getUser(userId);
		List<SceneDetailResp> diffSceneList = new ArrayList<>();
		SceneDetailReq sceneDetailReq = new SceneDetailReq();
		sceneDetailReq.setSpaceId(spaceId);
		sceneDetailReq.setSceneId(sceneId);
		sceneDetailReq.setTenantId(user.getTenantId());
		sceneDetailReq.setLocationId(locationId);
		diffSceneList = this.sceneApi.sceneDetailByParam(sceneDetailReq);
		GetDeviceInfoRespVo deviceResp = null;
		// Set<String> clientSet = new HashSet<>();
		for (SceneDetailResp sceneDetailResp : diffSceneList) {
			deviceResp = deviceCoreApi.get(sceneDetailResp.getDeviceId());
			// clientSet = Constants.SCENE_GATEWAY_MAP.get(sceneId);
			// if (clientSet == null) {
			// clientSet = new HashSet<>();
			// }
			// clientSet.add(deviceResp.getParentId());
			// Constants.SCENE_GATEWAY_MAP.put(sceneId, clientSet);
			if (deviceResp != null && deviceResp.getParentId() != null) {
				MultiProtocolGatewayNewThread newGateWay = new MultiProtocolGatewayNewThread();
				newGateWay.deleteScene(deviceResp.getParentId(), sceneId);
			}
		}
		String sceneUserListIdKey = RedisKeyUtil.getSceneUserListIdKey(userId, user.getTenantId());
		List<Map> sceneIdList = RedisCacheUtil.listGetAll(sceneUserListIdKey, Map.class);
		List<String> sceneIdListKey = new ArrayList();
		sceneIdList.forEach(m -> {
			sceneIdListKey
					.add(RedisKeyUtil.getSceneListIdKey(new Long(m.get("sceneId").toString()), user.getTenantId()));
		});
		// 移除缓存
		RedisCacheUtil.delete(sceneUserListIdKey);
		RedisCacheUtil.delete(sceneIdListKey);
		deleteRedisSceneOtherInfo(user.getOrgId(), user.getTenantId(), sceneId);
		// for (String clientId : clientSet) {
		// MultiProtocolGatewayNewThread newGateWay = new
		// MultiProtocolGatewayNewThread();
		// newGateWay.delScene(clientId, sceneId);
		// Constants.SCENE_DETAIL_MAP.remove(spaceId + "," + clientId);
		// }
	}

	private void deleteRedisSceneOtherInfo(Long orgId, Long tenantId, Long sceneId) {
		SceneReq sceneReq = new SceneReq();
		sceneReq.setId(sceneId);
		sceneReq.setTenantId(tenantId);
		sceneReq.setOrgId(orgId);
		SceneResp scene = sceneApi.sceneById(sceneReq);
		String gatewaySceneKey = RedisKeyUtil.getSceneGatewayDeviceMapIdKey(sceneId, scene.getTenantId());
		String externalSceneKey = RedisKeyUtil.getSceneExternalDeviceListIdKey(sceneId, scene.getTenantId());
		RedisCacheUtil.delete(gatewaySceneKey);
		RedisCacheUtil.delete(externalSceneKey);
	}

	@Override
	public void deleteSceneDetail(Long tenantId, Long sceneId, Long spaceId, Long userId) {
		// 向网关发送删除情景请求
		SceneReq sceneReq = new SceneReq();
		sceneReq.setTenantId(tenantId);
//		sceneReq.setOrgId(orgId);
		sceneReq.setId(sceneId);
		sceneReq.setSpaceId(spaceId);
		SceneResp scene = sceneApi.sceneById(sceneReq);
		delSceneData(spaceId, sceneId, userId, scene.getLocationId());
		// 删除数据库表数据
		if (sceneId != null) {
			SceneDetailReq sceneDetailReq = new SceneDetailReq();
			sceneDetailReq.setSceneId(sceneId);
			sceneDetailReq.setTenantId(tenantId);
			sceneApi.deleteSceneDetail(sceneDetailReq);
			sceneReq.setId(sceneId);
			sceneApi.deleteScene(sceneReq);
			String sceneUserListIdKey = RedisKeyUtil.getSceneUserListIdKey(userId, tenantId);
			List<Map> sceneIdList = RedisCacheUtil.listGetAll(sceneUserListIdKey, Map.class);
			List<String> sceneIdListKey = new ArrayList();
			sceneIdList.forEach(m -> {
				sceneIdListKey.add(RedisKeyUtil.getSceneListIdKey(new Long(m.get("sceneId").toString()), tenantId));
			});
			// 移除缓存
			RedisCacheUtil.delete(sceneUserListIdKey);
			RedisCacheUtil.delete(sceneIdListKey);
		}
	}

	public void sceneExecute(Long tenantId, Long sceneId) {
		if (sceneId != null) {
			SceneReq sceneReq = new SceneReq();
			sceneReq.setId(sceneId);
			sceneReq.setTenantId(tenantId);
//			sceneReq.setOrgId(orgId);
			SceneResp scene = sceneApi.sceneById(sceneReq);
			// List<SceneResp> sceneResps = sceneApi.sceneByParamDoing(sceneReq);
			// if (sceneResps == null || sceneResps.size() == 0) {
			// return;
			// }
			// SceneResp scene = sceneResps.get(0);
			// Long spaceId = scene.getSpaceId();
			excuteScene(scene);
			// 用于存放情景执行
			Set<Object> tempSet = null;
			if (RedisCacheUtil.valueObjGet(Constants.REDIS_SCENE_STR + scene.getSpaceId(), Set.class) == null) {
				tempSet = new HashSet<Object>();
				tempSet.add(sceneId);
				RedisCacheUtil.valueObjSet(Constants.REDIS_SCENE_STR + scene.getSpaceId(), tempSet);
			} else {
				tempSet = RedisCacheUtil.valueObjGet(Constants.REDIS_SCENE_STR + scene.getSpaceId(), Set.class);
				tempSet.clear();
				tempSet.add(sceneId);
				RedisCacheUtil.delete(Constants.REDIS_SCENE_STR + scene.getSpaceId());
				RedisCacheUtil.valueObjSet(Constants.REDIS_SCENE_STR + scene.getSpaceId(), tempSet);
			}
			// 缓存space当前的情景Id
			setSpaceSceneStatus(tenantId, sceneId);
			// 向窗口发送情景信息
			Map<String, Object> map = new HashMap<>();
			map.put("sceneId", sceneId);
			String uuid = dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
			String topic = "iot/v1/c/" + uuid + "/center/scene";
			BusinessDispatchMqttHelper.sendSceneTopic(map, topic);
		}
	}

	/**
	 * 根据模执行情景
	 *
	 * @throws Exception
	 * @throws BusinessException
	 */
	public void excuteScene(SceneResp scene) throws BusinessException {
		List<SceneDetailResp> diffSceneList = new ArrayList<>();
		String jsonInfo = RedisCacheUtil.valueObjGet(
				RedisKeyUtil.getSceneGatewayDeviceMapIdKey(scene.getId(), scene.getTenantId()), String.class);
		Map<String, Map> sceneDeviceMap = JSON.parseObject(jsonInfo, Map.class);
		if (scene.getUploadStatus() == 0) {
			// Map<String, Object> map = new HashMap<>();
			// map.put("sceneId", scene.getId());
			// map.put("code", 10086);
			// String uuid = dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
			// String topic = "iot/v1/c/" + uuid + "/center/sceneExcute";
			// BusinessDispatchMqttHelper.sendSceneTopic(map, topic);
			throw new BusinessException(BusinessExceptionEnum.SCENE_UN_UPLOAD);// 情景未下发
		} else {
			// 非首次情景执行 向网关发送情景执行请求
			List<DevicePropertyDTO> ExternalList = RedisCacheUtil.listGetAll(
					RedisKeyUtil.getSceneExternalDeviceListIdKey(scene.getId(), scene.getTenantId()),
					DevicePropertyDTO.class);
			if (sceneDeviceMap != null) {
				Map<String, Object> gatewayInfoMap = sceneDeviceMap.get(String.valueOf(scene.getId()));
				if (gatewayInfoMap != null) {
					for (String clientId : gatewayInfoMap.keySet()) {
						ThreadPoolUtil.instance().execute(new Runnable() {
							@Override
							public void run() {
								try {
									MultiProtocolGatewayHepler.executeScene(clientId, scene.getId());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				}
			}
			if (CollectionUtils.isNotEmpty(ExternalList)) {
				for (DevicePropertyDTO devicePropertyDTO : ExternalList) {
					iBuildingSpaceService.control(devicePropertyDTO.getDeviceId(), devicePropertyDTO.getPropertyMap());
				}
			}
		}
	}

	private void singleControl(List<SceneDetailResp> deviceIds, Map<String, Object> propertyMap) {
		for (SceneDetailResp sceneDetailResp : deviceIds) {
			iBuildingSpaceService.control(sceneDetailResp.getDeviceId(), propertyMap);
		}
	}

	private void assembleTargetValue(SceneDetailResp sceneDetail) {
		if (sceneDetail.getTargetValue() != null) {
			Map<String, Object> targetValue = JSON.parseObject(sceneDetail.getTargetValue());
			Map<String, Object> tempValue = new HashMap<>();
			tempValue.putAll(targetValue);
			for (String key : targetValue.keySet()) {
				if (key.contains("Status") || key.equals("deviceId")) {
					tempValue.remove(key);
				} else if (key.equals("RGBW")) {
					int RGBW = ToolUtils.convertToColorInt(targetValue.get("RGBW").toString());
					tempValue.put(key, RGBW);
				} else if (key.contains("tenantId")) {
					tempValue.remove(key);
				} else if (key.contains("EnableIllum")) {
					tempValue.remove(key);
				} else if (key.contains("TargetDim")) {
					tempValue.remove(key);
				}
			}
			sceneDetail.setTargetValue(JSON.toJSONString(tempValue));
		}
	}

	private Map<String, Object> assembleTargetValueControlInfo(SceneDetailResp sceneDetail) {
		Map<String, Object> params = new HashMap<>();
		if (sceneDetail.getTargetValue() != null) {
			Map<String, Object> targetValue = JSON.parseObject(sceneDetail.getTargetValue());
			Map<String, Object> tempValue = new HashMap<>();
			tempValue.putAll(targetValue);
			for (String key : targetValue.keySet()) {
				if (key.contains("Status") || key.equals("deviceId")) {
					tempValue.remove(key);
				} else if (key.contains("Blink") || key.contains("Duration")) {
					tempValue.remove(key);
				} else if (key.contains("tenantId")) {
					tempValue.remove(key);
				} else if (key.contains("EnableIllum")) {
					params.put(key, tempValue.get(key));
					tempValue.remove(key);
				} else if (key.contains("TargetDim")) {
					params.put(key, tempValue.get(key));
					tempValue.remove(key);
				}
			}
			sceneDetail.setTargetValue(JSON.toJSONString(tempValue));
		}
		return params;
	}

	private void sendSceneMsgToGateway(SceneResp scene, Map<String, Map> sceneDeviceMap, boolean silence)
			throws BusinessException {
		// 向网关发送情景数据
		MultiProtocolGatewayNewThread newGateWay = new MultiProtocolGatewayNewThread();
		if (sceneDeviceMap != null) {
			Map<String, Object> gatewayInfoMap = sceneDeviceMap.get(String.valueOf(scene.getId()));
			if (gatewayInfoMap != null) {
				for (String clientId : gatewayInfoMap.keySet()) {
					List<DevicePropertyDTO> devicePropertyDTOs = (List<DevicePropertyDTO>) gatewayInfoMap.get(clientId);
					if (CollectionUtils.isNotEmpty(devicePropertyDTOs)) {
						for (DevicePropertyDTO devicePropertyDTO : devicePropertyDTOs) {
							SceneDetailResp sceneDetail = new SceneDetailResp();
							sceneDetail.setDeviceId(devicePropertyDTO.getDeviceId());
							sceneDetail.setTargetValue(JSON.toJSONString(devicePropertyDTO.getPropertyMap()));
							assembleTargetValue(sceneDetail);// 组装目标值，删除带Status字段
							newGateWay.synScene(clientId, scene.getId(), sceneDetail, silence);
						}
					}
				}
			}
		}
		SceneReq sceneReq = new SceneReq();
		BeanUtils.copyProperties(scene, sceneReq);
		// 更新情景标识
		sceneReq.setUploadStatus(1);
		sceneReq.setUpdateTime(new Date());
		sceneReq.setTenantId(scene.getTenantId());
		sceneReq.setOrgId(scene.getOrgId());
		sceneApi.updateScene(sceneReq);
		scene = sceneApi.sceneById(sceneReq);
		String sceneListIdKey = RedisKeyUtil.getSceneListIdKey(scene.getId(), scene.getTenantId());
		RedisCacheUtil.valueObjSet(sceneListIdKey, scene, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT);
	}

	private Map<String, Map> CommonSceneData(SceneResp scene, List<SceneDetailResp> diffSceneList,
			Map<String, Map> sceneDeviceMap) {
		for (SceneDetailResp sceneDetail : diffSceneList) {
			GetDeviceInfoRespVo deviceResp = deviceCoreApi.get(sceneDetail.getDeviceId());
			GetDeviceTypeByDeviceRespVo getDeviceTypeByDeviceRespVo = deviceCoreApi
					.getDeviceTypeByDeviceId(deviceResp.getUuid());
			String venderFlag = "";
			if (getDeviceTypeByDeviceRespVo != null) {
				venderFlag = getDeviceTypeByDeviceRespVo.getDeviceTypeInfo().getVenderFlag();
			}
			// 当类型是多协议设备的时候，放在缓存中，方便下发至网关
			if (StringUtils.isBlank(venderFlag) || Constants.VENDER_FLAG_MULTI_GATEWAY.equals(venderFlag)) {
				// redis获取scene --> 网关ID列表
				if (sceneDeviceMap == null) {
					sceneDeviceMap = new HashMap<>();
				}
				Map<String, List<DevicePropertyDTO>> deviceProMap = sceneDeviceMap
						.get(String.valueOf(sceneDetail.getSceneId()));
				if (deviceProMap == null) {
					deviceProMap = new HashMap<>();
				}
				List<DevicePropertyDTO> devicePropertyDTOs = deviceProMap.get(deviceResp.getParentId());
				if (CollectionUtils.isEmpty(devicePropertyDTOs)) {
					devicePropertyDTOs = Lists.newArrayList();
				}
				Map<String, Object> targetValueMap = Maps.newHashMap();
				if (sceneDetail.getTargetValue() != null) {
					targetValueMap = (Map) JSON.parse(sceneDetail.getTargetValue());
				}
				DevicePropertyDTO devicePropertyDTO = new DevicePropertyDTO();
				devicePropertyDTO.setDeviceId(sceneDetail.getDeviceId());
				devicePropertyDTO.setPropertyMap(targetValueMap);
				devicePropertyDTOs.add(devicePropertyDTO);
				deviceProMap.put(deviceResp.getParentId(), devicePropertyDTOs);
				sceneDeviceMap.put(String.valueOf(sceneDetail.getSceneId()), deviceProMap);
				RedisCacheUtil.valueObjSet(
						RedisKeyUtil.getSceneGatewayDeviceMapIdKey(sceneDetail.getSceneId(), sceneDetail.getTenantId()),
						sceneDeviceMap);
			} else {
				// redis获取外接设备及目标值
				List<DevicePropertyDTO> externalList = RedisCacheUtil.listGetAll(RedisKeyUtil
						.getSceneExternalDeviceListIdKey(sceneDetail.getSceneId(), sceneDetail.getTenantId()),
						DevicePropertyDTO.class);
				if (CollectionUtils.isEmpty(externalList)) {
					externalList = new ArrayList<>();
				}
				Map<String, Object> targetValueMap = Maps.newHashMap();
				if (sceneDetail.getTargetValue() != null) {
					targetValueMap = (Map) JSON.parse(sceneDetail.getTargetValue());
				}
				DevicePropertyDTO devicePropertyDTO = new DevicePropertyDTO();
				devicePropertyDTO.setDeviceId(sceneDetail.getDeviceId());
				devicePropertyDTO.setPropertyMap(targetValueMap);
				externalList.add(devicePropertyDTO);
				RedisCacheUtil.listSet(RedisKeyUtil.getSceneExternalDeviceListIdKey(sceneDetail.getSceneId(),
						sceneDetail.getTenantId()), externalList);
			}
		}
		return sceneDeviceMap;
	}

	@Override
	public List<SceneResp> findSceneDetailList(SceneReq sceneReq) {
		return sceneApi.sceneByParamDoing(sceneReq);
	}

	/**
	 * 保存/修改整校的locationScene
	 *
	 * @param locationSceneReq
	 */
	@Override
	public void saveOrUpdateLocationScene(LocationSceneReq locationSceneReq) {
		LocationScene locationScene = new LocationScene();
		BeanUtil.copyProperties(locationSceneReq, locationScene);
		// 通过是否存在id，判断是执行保存操作还是修改操作
		if (locationScene.getId() == null) {// 执行保存操作
			locationSceneMapper.save(locationScene);
		} else {// 执行修改操作
			locationSceneMapper.update(locationScene);
		}
	}

	@Override
	public Long saveLocationScene(LocationSceneReq locationSceneReq) {
		LocationScene locationScene = new LocationScene();
		BeanUtil.copyProperties(locationSceneReq, locationScene);
		// 通过是否存在id，判断是执行保存操作还是修改操作
		locationSceneMapper.save(locationScene);
		return locationScene.getId();
	}

	/**
	 * 查询整校locationScene的列表，通过tenantId查询
	 *
	 * @param locationSceneReq
	 * @return
	 */
	@Override
	public List<LocationSceneResp> findLocationSceneList(LocationSceneReq locationSceneReq) {
		List<LocationSceneResp> list = locationSceneMapper.findLocationSceneList(locationSceneReq);
		return list;
	}

	@Override
	public Page<LocationSceneResp> findLocationSceneListStr(LocationSceneReq locationSceneReq) {
		com.github.pagehelper.PageHelper.startPage(locationSceneReq.getPageNum(), locationSceneReq.getPageSize());
		List<LocationSceneResp> list = locationSceneMapper.findLocationSceneList(locationSceneReq);
		for (LocationSceneResp locationSceneResp : list) {
			String createName = userApi.getUser(locationSceneResp.getCreateBy()).getUserName();
			locationSceneResp.setCreateName(createName);
		}
		PageInfo<LocationSceneResp> info = new PageInfo(list);
		Page<LocationSceneResp> page = new Page<LocationSceneResp>();
		BeanUtil.copyProperties(info, page);
		page.setResult(info.getList());
		return page;
	}

	/**
	 * 保存/修改整校的locationSceneDetail
	 *
	 * @param locationSceneDetailReq
	 */
	@Override
	public void saveOrUpdateLocationSceneDetail(LocationSceneDetailReq locationSceneDetailReq) {
		LocationSceneDetail locationSceneDetail = new LocationSceneDetail();
		BeanUtil.copyProperties(locationSceneDetailReq, locationSceneDetail);
		// 通过是否存在id，判断是执行保存操作还是修改操作
		if (locationSceneDetail.getId() == null) {// 执行保存操作
			locationSceneMapper.saveLocationSceneDetail(locationSceneDetail);
		} else {// 执行修改操作
			locationSceneMapper.updateLocationSceneDetail(locationSceneDetail);
		}
	}

	/**
	 * 查询整校locationSceneDetail的列表，通过tenantId和locationSceneId查询
	 *
	 * @param locationSceneDetailReq
	 * @return
	 */
	@Override
	public List<LocationSceneDetailResp> findLocationSceneDetailList(LocationSceneDetailReq locationSceneDetailReq) {
		List<LocationSceneDetailResp> list = locationSceneMapper.findLocationSceneDetailList(locationSceneDetailReq);
		return list;
	}

	/**
	 * 删除location_scene表中的数据,通过id
	 *
	 * @param id
	 */
	@Override
	public void deleteLocationScene(Long id) {
		locationSceneMapper.deleteLocationScene(id);
	}

	/**
	 * 删除location_scene_detail表中的数据,通过tenantId和locationSceneId
	 *
	 * @param tenantId
	 * @param locationSceneId
	 */
	@Override
	public void deleteLocationSceneDetail(Long tenantId, Long locationSceneId) {
		locationSceneMapper.deleteLocationSceneDetail(tenantId, locationSceneId);
	}

	@Override
	public void deleteLocationSceneDetailStr(Long locationSceneId) {
		locationSceneMapper.deleteLocationSceneDetailStr(locationSceneId);
	}

	@Override
	public void updateLocationScene(LocationSceneReq locationSceneReq) {
		LocationScene locationScene = new LocationScene();
		BeanUtil.copyProperties(locationSceneReq, locationScene);
		// 通过是否存在id，判断是执行保存操作还是修改操作
		locationSceneMapper.update(locationScene);
	}

	@Override
	public List<Long> findSceneIds(Long locationSceneId) {
		List<Long> list = locationSceneMapper.findSceneIds(locationSceneId);
		return list;
	}

	@Override
	public List<Map<String, Object>> findTree(Long orgId, Long tenantId, Long locationId) {
		List<SpaceResp> spaceList = findRootByLocation(orgId, tenantId, locationId);
		List<Map<String, Object>> mapList = BeanChangeUtil.spaceToMapList(spaceList);
		findChild(tenantId, mapList);
		// findChildScene(mapList);
		// return filterMountSpace(mapList);
		return mapList;
	}

	private List<Map<String, Object>> filterMountSpace(List<Map<String, Object>> mapList) {
		List<Map<String, Object>> backUpList = new ArrayList<>();
		backUpList.addAll(mapList);
		for (Map<String, Object> map : backUpList) {
			if (map == null || map.get("parentId") == null
					|| Long.valueOf(map.get("parentId").toString()).compareTo(-1L) > 0) {
				mapList.remove(map);
			}
		}
		return mapList;
	}

	private void findChild(Long tenantId, List<Map<String, Object>> list) {
		if (!list.isEmpty()) {
			for (Map<String, Object> mapSpace : list) {
				Long id = Long.parseLong(mapSpace.get("id").toString());
				SpaceReq space = new SpaceReq();
				SceneReq scene = new SceneReq();
				space.setParentId(id);
				space.setTenantId(tenantId);
				if (mapSpace.get("type") != null) {
					String type = mapSpace.get("type").toString();
					if (type.equals("ROOM")) {
						// 查询房间下对应的情景
						scene.setSpaceId(id);
						scene.setTenantId(tenantId);
						List<Map<String, Object>> childList = sceneToMap(sceneApi.sceneByParamDoing(scene));
						mapSpace.put("child", childList);
					} else {
						List<Map<String, Object>> childList = spaceListToListMap(spaceApi.findSpaceByParentId(space));
						mapSpace.put("child", childList);
						findChild(tenantId, childList);
					}
				}
			}
		}
	}

	private List<Map<String, Object>> spaceListToListMap(List<SpaceResp> spaceList) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(spaceList)) {
			spaceList.forEach(space -> {
				mapList.add(BeanChangeUtil.spaceToMap(space, null));
			});
		}
		return mapList;
	}

	private List<Map<String, Object>> sceneToMap(List<SceneResp> sceneList) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(sceneList)) {
			sceneList.forEach(scene -> {
				mapList.add(BeanChangeUtil.sceneToMap(scene));
			});
		}
		return mapList;
	}

	public List<SpaceResp> findRootByLocation(Long orgId, Long tenantId, Long locationId) {
		SpaceReq spaceReq = new SpaceReq();
		spaceReq.setTenantId(tenantId);
		spaceReq.setOrgId(orgId);
		spaceReq.setLocationId(locationId);
		spaceReq.setParentId(-1L);
		List<SpaceResp> spaceRespList = spaceApi.findSpaceByCondition(spaceReq);
		// for (SpaceResp spaceResp : spaceRespList) {
		// if (Constants.SPACE_ROOM.equalsIgnoreCase(spaceResp.getType()) &&
		// Constants.MEETING_TRUE == spaceResp.getModel()) {
		// spaceResp.setType(Constants.SPACE_MEETING);
		// }
		// }
		// TODO 下面转换 里面有判断 IF room and model = 1 , type = MEETING
		// List<SpaceResp> spaceRespList =
		// BeanCopyUtil.spaceListToSpaceRespList(spaceList);
		return spaceRespList;
	}

	@Override
	public void saveLocationSceneRelation(LocationSceneRelationReq locationSceneRelationReq) {
		LocationSceneRelation locationSceneRelation = new LocationSceneRelation();
		BeanUtil.copyProperties(locationSceneRelationReq, locationSceneRelation);
		// 通过是否存在id，判断是执行保存操作还是修改操作
		if (locationSceneRelation.getId() == null) {// 执行保存操作
			locationSceneMapper.saveLocationSceneRelation(locationSceneRelation);
		}
		/*
		 * else {//执行修改操作
		 * locationSceneMapper.updateLocationSceneDetail(locationSceneRelation); }
		 */
	}

	/**
	 * 获取location_scene_list 的列表
	 *
	 * @return
	 */
	@Override
	public List<LocationSceneRelationResp> findLocationSceneRelationList() {
		List<LocationSceneRelationResp> list = locationSceneMapper.findLocationSceneRelationList();
		return list;
	}

	@Override
	public List<SceneResp> querySceneList(@RequestBody SceneDetailReq req) {
		SceneReq sceneReq = new SceneReq();
		BeanUtil.copyProperties(req, sceneReq);
		List<SceneResp> list = sceneApi.sceneByParamDoing(sceneReq);
		// 如果是楼和层和园区需要添加组合情景
		getBuildOrFloorScene(sceneReq, list);
		return list;
	}

	/**
	 * 楼和层和园区需要添加组合情景
	 * 
	 * @param req
	 * @param list
	 */
	private void getBuildOrFloorScene(SceneReq req, List<SceneResp> list) {
		List<LocationSceneResp> localList = shortcutService.getLocalListSecneByBuildOrFloor(req.getOrgId(), req.getSpaceId(),
				req.getTenantId(), req.getLocationId());
		if (CollectionUtils.isNotEmpty(localList)) {
			List<LocationSceneDetailResp> detailList = Lists.newArrayList();
			List<Long> sceneIds = Lists.newArrayList();
			for (LocationSceneResp resp : localList) {
				LocationSceneDetailReq detailReq = new LocationSceneDetailReq();
				detailReq.setLocationSceneId(resp.getId());
				detailReq.setTenantId(resp.getTenantId());
				detailList.addAll(findLocationSceneDetailList(detailReq));
			}
			if (CollectionUtils.isNotEmpty(detailList)) {
				for (LocationSceneDetailResp resp : detailList) {
					sceneIds.add(resp.getSceneId());
				}
				;
				req.setSceneIds(sceneIds);
				req.setSpaceId(null);
				list.addAll(sceneApi.sceneByParamDoing(req));
			}
		}
	}

	@Override
	public Long getSpaceSceneStatus(Long tenantId, Long spaceId) {
		Long sceneId = 0L;
		SpaceResp spaceResp = spaceApi.findSpaceInfoBySpaceId(tenantId, spaceId);
		if (spaceResp != null) {
			String spaceSceneStatusKey = RedisKeyUtil.getSceneSpaceStatusIdKey(spaceId, spaceResp.getTenantId());
			if (StringUtils.isNotBlank(spaceSceneStatusKey)) {
				sceneId = RedisCacheUtil.valueObjGet(spaceSceneStatusKey, Long.class);
			}
		}
		return sceneId;
	}

	@Override
	public Long setSpaceSceneStatus(Long tenantId, Long sceneId) {
		SceneReq sceneReq = new SceneReq();
		sceneReq.setId(sceneId);
//		sceneReq.setOrgId(orgId);
		sceneReq.setTenantId(tenantId);
		SceneResp scene = sceneApi.sceneById(sceneReq);
		if (scene != null) {
			String spaceSceneStatusKey = RedisKeyUtil.getSceneSpaceStatusIdKey(scene.getSpaceId(), scene.getTenantId());
			if (StringUtils.isNotBlank(spaceSceneStatusKey)) {
				RedisCacheUtil.valueObjSet(spaceSceneStatusKey, sceneId);
			}
		}
		return sceneId;
	}

	@Override
	public void sceneExecuteNext(Long tenantId, Long spaceId) throws BusinessException {
		Long sceneId = null;
		try {
			if (spaceId != null) {
				Integer index = Constants.SCENE_CURRENT_IDX.get(spaceId) == null ? 0
						: Constants.SCENE_CURRENT_IDX.get(spaceId);
				List<Long> sceneIds = getSceneBySpace(tenantId, spaceId);
				if (sceneIds.size() - 1 < index) {
					index = 0;
				}
				sceneId = sceneIds.get(index);
				Constants.SCENE_CURRENT_IDX.put(spaceId, index + 1);
				sceneExecute(tenantId, sceneId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
		}
	}

	private List<Long> getSceneBySpace(Long tenantId, Long spaceId) {
		List<Long> sceneIds = new ArrayList<>();
		SceneReq sceneReq = new SceneReq();
		sceneReq.setSpaceId(spaceId);
		sceneReq.setTenantId(tenantId);
		List<SceneResp> sceneList = sceneApi.sceneByParamDoing(sceneReq);
		if (!CollectionUtils.sizeIsEmpty(sceneList)) {
			sceneList.forEach(scene -> {
				sceneIds.add(scene.getId());
			});
		}
		return sceneIds;
	}

	/**
	 * 根据情景名称和房间执行固定名称的情景
	 *
	 * @param spaceId
	 * @param name
	 * @return
	 */
	@Override
	public void excuteSceneBySpaceAndSceneName(Long tenantId, Long spaceId, String name) {
		SceneReq sceneReq = new SceneReq();
		sceneReq.setSpaceId(spaceId);
		sceneReq.setTenantId(tenantId);
		List<SceneResp> sceneList = sceneApi.sceneByParamDoing(sceneReq);
		if (!CollectionUtils.sizeIsEmpty(sceneList)) {
			sceneList.forEach(scene -> {
				if (scene.getSceneName().contains(name)) {
					sceneExecute(tenantId, scene.getId());
				}
			});
		}
	}

	/**
	 * 情景模板手动初始化
	 */
	@Override
	public void sceneTemplateManualInit(SceneTemplateManualReq req) {
		if (CollectionUtils.isNotEmpty(req.getSpaceIdList())) {
			log.info("scene init start ..........");
			for (Long spaceId : req.getSpaceIdList()) {
				try {
					SpaceResp resp = spaceApi.findSpaceInfoBySpaceId(req.getTenantId(), spaceId);
					if (resp.getDeployId() == null) {
						log.info("space :" + spaceId + " 房间缓存有问题getDeployId=null");
						continue;
					}
					log.info("----1.删除该房间下所有模板生成的情景----");
					delBatchScene(resp);// 1.删除该房间下所有模板生成的情景
					log.info("space :" + spaceId + " 删除该房间下所有模板生成的情景----");
					log.info("----2.模板生成情景----");
					getAllSceneTemplateAndCreateScene(req.getTenantId(), req.getLocationId(), resp, req.getUserId());// 2.查询该房间下对应部署方式的模板重新生成该房间的情景
					// 执行情景初始化
					log.info("----3.初始化情景----");
					sceneInit(req.getTenantId(), spaceId, req.getInterval());
				} catch (Exception e) {
					continue;
				}
			}
			log.info("scene init end ..........");
		}
	}

	// 执行情景初始化
	private void sceneInit(Long tenantId, Long spaceId, Integer interval) {
		interval = interval == null ? 10 : interval;// 默认三十秒
		// 通过房间的spaceId 查找scene列表
		SceneReq sceneReq = new SceneReq();
		sceneReq.setSpaceId(spaceId);
		sceneReq.setTenantId(tenantId);
		sceneReq.setUploadStatus(0);// 未执行过的情景
		List<SceneResp> list = findSceneDetailList(sceneReq);
		// 循环执行scene
		if (CollectionUtils.isNotEmpty(list)) {
			for (SceneResp sceneResp : list) {
				SceneReq sceneReqVo = new SceneReq();
				sceneReqVo.setId(sceneResp.getId());
				sceneReqVo.setLocationId(sceneResp.getLocationId());
				sceneReqVo.setTenantId(sceneResp.getTenantId());
				sceneReqVo.setSilenceStatus(sceneResp.getSilenceStatus());
				issueScene(sceneReqVo);
				// sceneExecute(sceneResp.getTenantId(), sceneResp.getId());
				try {
					Thread.sleep(1000 * interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			log.info("space :" + spaceId + " 没有找到未执行的情景 ...");
		}
	}

	/**
	 * 批量删除情景
	 *
	 */
	private void delBatchScene(SpaceResp resp) {
		Long deployId = resp.getDeployId();
		TemplateReq templateReq = new TemplateReq();
		templateReq.setDeployId(deployId);
		templateReq.setTenantId(resp.getTenantId());
		templateReq.setLocationId(resp.getLocationId());
		List<TemplateResp> tempList = templateService.findSceneTemplateList(templateReq);
		if (CollectionUtils.isNotEmpty(tempList)) {// 如果模板被删除会出现无法删除情景有该模板的
			for (TemplateResp tempResp : tempList) {
				try {
					SceneReq sceneReq = new SceneReq();
					sceneReq.setSpaceId(resp.getId());
					sceneReq.setTenantId(resp.getTenantId());
					sceneReq.setTemplateId(tempResp.getId());
					List<SceneResp> sceneList = findSceneDetailList(sceneReq);
					log.info("----sceneList-----"+sceneList.size());
					if (CollectionUtils.isNotEmpty(sceneList)) {
						for (SceneResp sceneResp : sceneList) {
							deleteSceneDetail(sceneResp.getTenantId(), sceneResp.getId(), resp.getId(), resp.getCreateBy());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}

	}

	/**
	 * 初始化该房间模板情景
	 *
	 * @param tenantId
	 * @param spaceResp
	 */
	private void getAllSceneTemplateAndCreateScene(Long tenantId, Long locationId, SpaceResp spaceResp, Long userId) {
		TemplateReq templateReq = new TemplateReq();
		templateReq.setTenantId(tenantId);
		templateReq.setLocationId(locationId);
		templateReq.setDeployId(spaceResp.getDeployId());
		List<TemplateResp> respList = templateService.findSceneTemplateList(templateReq);
		if (CollectionUtils.isNotEmpty(respList)) {
			log.info("该房间：" + spaceResp.getId() + "情景模板:" + respList.size());
			for (TemplateResp resp : respList) {
				// 根据情景模板
				Long sceneId = addSceneToRoomByTemplate(tenantId, spaceResp.getLocationId(), spaceResp.getId(),
						resp.getId(), userId, resp.getSilenceStatus());
				log.info("该房间：" + spaceResp.getId() + "生成scene =" + sceneId);
			}
		} else {
			log.info("该房间：" + spaceResp.getId() + "没有找到对应的情景模板...");
		}
	}

	public List<String> getAllSpaceDevices(Long tenantId,Long orgId, Long spaceId, String business) {
		List<String> deviceIds = Lists.newArrayList();
		List<String> currentDeviceIds = getDevicesBySpace(tenantId,orgId, spaceId, business);
		deviceIds.addAll(currentDeviceIds);
		SpaceReq spaceReq = new SpaceReq();
		spaceReq.setParentId(spaceId);
		spaceReq.setTenantId(tenantId);
		List<SpaceResp> spaceList = spaceApi.findSpaceByParentId(spaceReq);
		if (CollectionUtils.isNotEmpty(spaceList)) {
			for (SpaceResp space : spaceList) {
				Long childSpaceId = space.getId();
				List<String> childDeviceIds = getDevicesBySpace(tenantId,orgId, childSpaceId, business);
				deviceIds.addAll(childDeviceIds);
			}
		}
		return deviceIds;
	}

	/**
	 * 描述:情景模板生成情景微调到房间
	 *
	 * @param spaceId
	 * @param templateId
	 */
	public Long addSceneToRoomByTemplate(Long tenantId, Long locationId, Long spaceId, Long templateId, Long userId,
			Integer silenceStatus) {
		List<String> deviceIds = getDeviceIds(tenantId, spaceId);
		Long sceneId = null;
		LOGGER.info("xxxxxx--spaceId=" + spaceId + "共有" + deviceIds.size());
		if (CollectionUtils.isNotEmpty(deviceIds)) {
			CreateSceneFromTemplateReq req = new CreateSceneFromTemplateReq();
			req.setUserId(userId);
			req.setTemplateId(templateId);
			req.setDeviceIdList(deviceIds);
			req.setSpaceId(spaceId);
			req.setLocationId(locationId);
			req.setTenantId(tenantId);
			req.setSilenceStatus(silenceStatus);
			sceneId = templateService.createSceneFromTemplate(req);
		}
		return sceneId;
	}

	private List<String> getDeviceIds(Long tenantId, Long spaceId) {
		List<String> deviceIds = Lists.newArrayList();
		List<SpaceDeviceVo> voList = spaceDeviceApi.findSpaceDeviceVOBySpaceId(tenantId, spaceId);
		if (CollectionUtils.isNotEmpty(voList)) {
			voList.forEach(vo -> {
				deviceIds.add(vo.getDeviceId());
			});
		}
		return deviceIds;
	}

	public List<String> getDevicesBySpace(Long tenantId, Long orgId,Long spaceId, String businessType) {
		List<Map<String, Object>> deviceList = iBuildingSpaceService.findDeviceByRoom(spaceId,orgId,tenantId);
		List<String> deviceIds = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(deviceList)) {
			for (Map<String, Object> device : deviceList) {
				deviceIds.add(String.valueOf(device.get("deviceId")));
			}
		}
		return deviceIds;
	}

	@Override
	public void templateManualInit(SceneTemplateManualReq req) {
		if (StringUtils.isBlank(req.getType())) {
			return;
		}
		// 情景初始化
		if (req.getType().toUpperCase().contains(Constants.INIT_SCENE)) {
			log.info("********** 情景初始化 *********** start ");
			sceneTemplateManualInit(req);
			log.info("********** 情景初始化 *********** end ");
		}
		// ifttt初始化group初始化
		iftttTamplateAndGroupInit(req);
	}

	private void iftttTamplateAndGroupInit(SceneTemplateManualReq req) {
		List<Long> spaecIds = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(req.getSpaceIdList())) {
			for (Long spaceId : req.getSpaceIdList()) {
				spaecIds.add(spaceId);
			}
			SpaceAndSpaceDeviceVo vo = new SpaceAndSpaceDeviceVo();
			vo.setSpaceIds(spaecIds);
			vo.setTenantId(req.getTenantId());
			List<SpaceResp> list = spaceApi.findSpaceInfoBySpaceIds(vo);
			if (CollectionUtils.isNotEmpty(list)) {
				list.forEach(space -> {
					if (req.getType().toUpperCase().contains(Constants.INIT_IFTTT)) {
						log.info("********** ifttt初始化 *********** start space:" + space.getId());
						allocationService.iftttTemplateToIftttBySpace(space.getTenantId(),space.getOrgId(), space.getDeployId(),
								space.getId());
						log.info("********** ifttt初始化 *********** end space:" + space.getId());
					}
					if (req.getType().toUpperCase().contains(Constants.INIT_GOURP)) {
						// group初始化
						log.info("********** group初始化 *********** start space:" + space.getId());
						SpaceReq spaceReq = new SpaceReq();
						spaceReq.setTenantId(space.getTenantId());
						spaceReq.setLocationId(space.getLocationId());
						spaceReq.setId(space.getId());
						groupService.initGroupBySpaceId(spaceReq);
						log.info("********** group初始化 *********** end space:" + space.getId());
					}
				});
			}
		}
	}

	@Override
	public Boolean isSingleGateway(List<String> deviceList, List<Long> sceneIds, Long tenantId) {
		String parentId = null;
		int error = 0;
		if (CollectionUtils.isNotEmpty(sceneIds)) {
			SceneDetailReq sceneDetailReq = new SceneDetailReq();
			// 循环对比scene
			for (Long sceneId : sceneIds) {
				sceneDetailReq.setTenantId(tenantId);
				sceneDetailReq.setSceneId(sceneId);
				List<SceneDetailResp> detail = sceneApi.sceneDetailByParamDoing(sceneDetailReq);
				if (CollectionUtils.isNotEmpty(detail)) {
					List<String> deviceIds = Lists.newArrayList();
					detail.forEach(de -> {
						deviceIds.add(de.getDeviceId());
					});
					// 1.判断只有一个parentId
					Set<String> haveParentIdSet = judgeSameParentId(deviceIds);
					if (haveParentIdSet.size() != 1) {
						error++;
						return false;
					}
					String backParentId = String.valueOf(haveParentIdSet.toArray()[0]);
					if (StringUtils.isBlank(parentId)) {
						parentId = backParentId;
					} else if (!parentId.equals(backParentId)) {
						error++;
						return false;
					}
				}
			}
		}
		if (CollectionUtils.isNotEmpty(deviceList)) {
			// 2.循环对比senor
			Set<String> haveParentIdSet = judgeSameParentId(deviceList);
			if (haveParentIdSet.size() != 1) {
				error++;
				return false;
			}
			String backParentId = String.valueOf(haveParentIdSet.toArray()[0]);
			if (parentId != null && !parentId.equals(backParentId)) {
				error++;
				return false;
			}
		}
		return error == 0;
	}

	private Set<String> judgeSameParentId(List<String> deviceIds) {
		Set<String> parentId = new HashSet();
		ListDeviceInfoReq req = new ListDeviceInfoReq();
		req.setDeviceIds(deviceIds);
		List<ListDeviceInfoRespVo> deviceVoList = deviceCoreApi.listDevices(req);
		if (CollectionUtils.isNotEmpty(deviceVoList)) {
			for (ListDeviceInfoRespVo vo : deviceVoList) {
				parentId.add(vo.getParentId());
			}
		}
		return parentId;
	}

	@Override
	public void issueScene(SceneReq sceneReq) {
		AssertUtils.notNull(sceneReq, "sceneResp.notnull");
		AssertUtils.notNull(sceneReq.getId(), "sceneId.notnull");
		AssertUtils.notNull(sceneReq.getTenantId(), "tenantId.notnull");
		AssertUtils.notNull(sceneReq.getOrgId(), "orgId.notnull");
		SceneResp sceneResp = sceneApi.sceneById(sceneReq);
		boolean silence = false;
		if (sceneResp != null) {
			List<SceneDetailResp> diffSceneList = new ArrayList<>();
			String jsonInfo = RedisCacheUtil.valueObjGet(
					RedisKeyUtil.getSceneGatewayDeviceMapIdKey(sceneResp.getId(), sceneResp.getTenantId()),
					String.class);
			Map<String, Map> sceneDeviceMap = JSON.parseObject(jsonInfo, Map.class);
			SceneDetailReq sceneDetailReq = new SceneDetailReq();
			sceneDetailReq.setSceneId(sceneResp.getId());
			sceneDetailReq.setTenantId(sceneResp.getTenantId());
			diffSceneList = sceneApi.sceneDetailByParam(sceneDetailReq);
			if (sceneResp != null && sceneResp.getSilenceStatus() != null && sceneResp.getSilenceStatus() == 0) {
				// 非静默
				if (sceneResp.getUploadStatus() != null && sceneResp.getUploadStatus() == 0) {
					List<SceneDetailResp> deviceIds = Lists.newArrayList();
					List<SceneDetailResp> targetDims = Lists.newArrayList();
					if (CollectionUtils.isNotEmpty(diffSceneList)) {
						Map<String, Object> params = Maps.newHashMap();
						// 单控设备
						for (SceneDetailResp diffScene : diffSceneList) {
							params = assembleTargetValueControlInfo(diffScene);// 组装目标值，删除带Status字段,删除Blink和Duration
							Map<String, Object> propertyMap = JSON.parseObject(diffScene.getTargetValue(), Map.class);
							if (params != null) {
								if (Constants.IlluminanceTypeMap()
										.containsKey(diffScene.getDeviceTypeId().toString())) {
									if (params.get("EnableIllum") != null
											&& params.get("EnableIllum").toString().equals("1")) {
										deviceIds.add(diffScene);
									}
									if (params.get("TargetDim") != null
											&& params.get("TargetDim").toString().equals("0")) {
										targetDims.add(diffScene);
									}
								}
							}
							// 检查状态 下发必答
							checkStateMethod(diffScene, propertyMap);
						}
						try {
							// 控灯生效 延时2S
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (MapUtils.isNotEmpty(params)) {
							if (params.get("TargetDim") != null) {
								// 下发恒照度 参考值
								Map<String, Object> TargetDimMap = Maps.newHashMap();
								TargetDimMap.put("TargetDim", Integer.valueOf(params.get("TargetDim").toString()));
								singleControl(targetDims, TargetDimMap);
							}
							if (params.get("EnableIllum") != null) {
								// 下发恒照度开启功能
								Map<String, Object> EnableIllumMap = Maps.newHashMap();
								EnableIllumMap.put("EnableIllum",
										Integer.valueOf(params.get("EnableIllum").toString()));
								singleControl(deviceIds, EnableIllumMap);
							}
						}
					}
				}
			} else {
				// 开启静默
				silence = true;
			}
			sceneDeviceMap = CommonSceneData(sceneResp, diffSceneList, sceneDeviceMap);
			// 向网关发送情景数据 并且更新情景标识
			sendSceneMsgToGateway(sceneResp, sceneDeviceMap, silence);
		}
	}

	private void checkStateMethod(SceneDetailResp diffScene, Map<String, Object> propertyMap) {
		int count = 0;
		boolean check = false;
		while (count < 3 && !check) {
			iBuildingSpaceService.control(diffScene.getDeviceId(), propertyMap);
			Map<String, Object> stateMap = deviceStateCoreApi.get(diffScene.getTenantId(), diffScene.getDeviceId());
			if (MapUtils.isNotEmpty(stateMap) && MapUtils.isNotEmpty(propertyMap)) {
				if (stateMap.get("OnOff") != null && propertyMap.get("OnOff") != null) {
					if (stateMap.get("OnOff").toString().equals(propertyMap.get("OnOff").toString())) {
						check = true;
					}
				}
			}
			count++;
		}
	}
	
	@Override
	public List<LocationSceneResp> findLocationSceneListByName(LocationSceneReq req) {
		List<LocationSceneResp> list = locationSceneMapper.findLocationSceneListByName(req);
		return list;
	}
}

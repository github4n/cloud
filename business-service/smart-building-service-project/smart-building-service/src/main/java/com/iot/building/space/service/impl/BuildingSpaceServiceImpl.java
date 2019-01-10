package com.iot.building.space.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.airswitch.api.AirSwitchApi;
import com.iot.building.callback.impl.SpaceCallback;
import com.iot.building.common.service.IProtocolAdaptationService;
import com.iot.building.common.vo.ProtocolParamVo;
import com.iot.building.device.service.IDeviceBusinessTypeService;
import com.iot.building.device.service.IDeviceRemoteService;
import com.iot.building.device.service.impl.DeviceService;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.building.device.vo.DeviceRemoteControlResp;
import com.iot.building.excepiton.BusinessExceptionEnum;
import com.iot.building.gateway.MultiProtocolGatewayHepler;
import com.iot.building.helper.BeanChangeUtil;
import com.iot.building.helper.BeanCopyUtil;
import com.iot.building.helper.CenterControlDeviceStatus;
import com.iot.building.helper.Constants;
import com.iot.building.helper.MapCallBack;
import com.iot.building.helper.ProtocolConstants;
import com.iot.building.helper.ThreadPoolUtil;
import com.iot.building.helper.ToolUtils;
import com.iot.building.space.mapper.DeploymentMapper;
import com.iot.building.space.mapper.LocationMapper;
import com.iot.building.space.mapper.SpaceBackgroundImgMapper;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.building.space.util.RedisKeyUtil;
import com.iot.building.space.vo.CalendarReq;
import com.iot.building.space.vo.CalendarResp;
import com.iot.building.space.vo.DeploymentReq;
import com.iot.building.space.vo.DeploymentResp;
import com.iot.building.space.vo.DeviceVo;
import com.iot.building.space.vo.LocationReq;
import com.iot.building.space.vo.LocationResp;
import com.iot.building.space.vo.SpaceBackgroundImgReq;
import com.iot.building.space.vo.SpaceBackgroundImgResp;
import com.iot.building.space.vo.SpaceExcelReq;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.enums.APIType;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;
import com.iot.control.space.vo.SpaceVo;
import com.iot.device.api.CentralControlDeviceApi;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.api.DeviceStatusCoreApi;
import com.iot.device.api.DeviceTypeCoreApi;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.vo.req.DeviceBusinessTypeIDSwitchReq;
import com.iot.device.vo.req.device.ListDeviceByParamsReq;
import com.iot.device.vo.req.device.ListDeviceInfoReq;
import com.iot.device.vo.req.device.ListDeviceStateReq;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceByParamsRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;

@Service("spaceService")
@Transactional
public class BuildingSpaceServiceImpl implements IBuildingSpaceService {

	private final static Logger LOGGER = LoggerFactory.getLogger(BuildingSpaceServiceImpl.class);

	@Autowired
	private LocationMapper locationMapper;
	@Autowired
	private SpaceBackgroundImgMapper spaceBackgroundImgMapper;
	@Autowired
	private DeploymentMapper deploymentMapper;
	@Autowired
	private SpaceApi commonSpaceService;
	@Autowired
	private SpaceDeviceApi commonSpaceDeviceService;
	@Autowired
	private IProtocolAdaptationService iProtocolAdaptationService;
	@Autowired
	private CentralControlDeviceApi centralControlDeviceApi;
	@Autowired
	private IDeviceRemoteService deviceRemoteService;
	@Autowired
	private DeviceCoreApi deviceCoreApi;
	@Autowired
	private ProductCoreApi productApi;
	@Autowired
	private DeviceTypeCoreApi deviceTypeCoreApi;
	@Autowired
	private IDeviceBusinessTypeService deviceBusinessTypeService;
	@Autowired
	private DeviceStateCoreApi deviceStateService;
	@Autowired
	private DeviceStatusCoreApi deviceStatusCoreApi;
	@Autowired
	private Environment dnvironment;
	@Autowired
	private AirSwitchApi airSwitchApi;
	@Autowired
	private DeviceService deviceService;

	/**
	 * 根据ID删除空间
	 *
	 * @param id
	 * @return
	 * @author wanglei
	 */
	public void deleteSpaceById(Long id) throws BusinessException {
		// 需要判断是否自身有子空间或者挂子设备
		SpaceReq spaceReq = new SpaceReq();
		spaceReq.setParentId(id);
		List<SpaceResp> childList = commonSpaceService.findSpaceByCondition(spaceReq);
		if (CollectionUtils.isNotEmpty(childList)) {
			throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);
		}
		SpaceDeviceReq req = new SpaceDeviceReq();
		req.setSpaceId(id);
		Integer deviceCount = commonSpaceDeviceService.countSpaceDeviceByCondition(req);
		if (deviceCount > 0) {
			throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);
		}
		commonSpaceService.deleteSpaceBySpaceId(req.getTenantId(), id);
	}

	/**
	 * 根据顶级区域查询空间列表
	 *
	 * @param locationId
	 * @return
	 */
	public List<SpaceResp> findByLocation(Long locationId) {
		SpaceReq spaceReq = new SpaceReq();
		spaceReq.setLocationId(locationId);
		List<SpaceResp> spaceList = commonSpaceService.findSpaceByCondition(spaceReq);
		return spaceList;
	}

	@Override
	public List<SpaceResp> findSpaceByLocationId(Long locationId,Long orgId, Long tenantId, String name) {
		SpaceReq spaceReq = new SpaceReq();
		spaceReq.setLocationId(locationId);
		spaceReq.setTenantId(tenantId);
		spaceReq.setName(name);
		spaceReq.setOrgId(orgId);
		List<SpaceResp> spaceRespList = commonSpaceService.findSpaceByCondition(spaceReq);
		spaceRespList = BeanCopyUtil.SpaceRespListSetType(spaceRespList);
		return spaceRespList;
	}

	@Override
	public Page<SpaceResp> findSpacePageByLocationId(Long locationId,Long orgId, Long tenantId, String name, int pageNumber,
													 int pageSize) {
		com.github.pagehelper.PageHelper.startPage(pageNumber, pageSize);
		SpaceReq spaceReq = new SpaceReq();
		spaceReq.setLocationId(locationId);
		spaceReq.setTenantId(tenantId);
		spaceReq.setName(name);
		spaceReq.setOrgId(orgId);
		List<SpaceResp> spaceRespList = commonSpaceService.findSpaceByCondition(spaceReq);
		PageInfo<SpaceResp> info = new PageInfo(spaceRespList);
		Page<SpaceResp> page = new Page<SpaceResp>();
		BeanUtil.copyProperties(info, page);
		page.setResult(info.getList());
		return page;
	}

	@Override
	public List<Map<String, Object>> findTreeSpaceByLocationId(Long locationId,Long orgId, Long tenantId) {
		SpaceReq spaceReq = new SpaceReq();
		spaceReq.setTenantId(tenantId);
		spaceReq.setType(Constants.SPACE_BUILD);
		spaceReq.setLocationId(locationId);
		spaceReq.setOrgId(orgId);
		List<SpaceResp> spaceList = commonSpaceService.findSpaceByCondition(spaceReq);
		List<Map<String, Object>> listMap = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(spaceList)) {
			listMap = findTreeChild(spaceList);
		}
		return listMap;
	}

	private List<Map<String, Object>> findTreeChild(List<SpaceResp> spaceList) {
		List<Map<String, Object>> listMap = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(spaceList)) {
			spaceList.forEach(space -> {
				Map<String, Object> backMap = new HashMap<>();
				backMap.put("id", space.getId());
				backMap.put("name", space.getName());
				backMap.put("type", space.getType());
				backMap.put("parentId", space.getParentId());
				SpaceReq spaceReq = new SpaceReq();
				spaceReq.setParentId(space.getId());
				backMap.put("child", findTreeChild(commonSpaceService.findSpaceByCondition(spaceReq)));
				listMap.add(backMap);
			});
		}
		return listMap;
	}

	/**
	 * 查询没有挂载过的空间
	 *
	 * @param locationId
	 * @param name
	 * @return
	 * @author wanglei
	 */
	// public List<SpaceResp> findSpaceUnMountByLocationIdAndName(Long locationId,
	// String name) {
	// SpaceReq req=new SpaceReq();req.setLocationId(locationId);req.setName(name);
	// List<SpaceResp> spaceList = commonSpaceService.findSpaceUnMount(req);
	// return spaceList;
	// }

	/**
	 * 根据用户查询顶级空间
	 *
	 * @param locationId
	 * @return
	 * @author wanglei
	 */
	public List<SpaceResp> findRootByLocationIdAndTenantId(Long locationId,Long orgId, Long tenantId) {
		SpaceReq spaceReq = new SpaceReq();
		spaceReq.setLocationId(locationId);
		spaceReq.setTenantId(tenantId);
		spaceReq.setParentId(-1L);
		List<SpaceResp> spaceList = commonSpaceService.findSpaceByCondition(spaceReq);
		return spaceList;
	}

	/**
	 * 根据父节点查询子节点Map
	 *
	 * @param parentId
	 * @return
	 * @author wanglei
	 */
	public List<SpaceResp> findByParent(Long parentId) {
		try {
			SpaceReq spaceReq = new SpaceReq();
			spaceReq.setParentId(parentId);
			List<SpaceResp> spaceList = commonSpaceService.findSpaceByCondition(spaceReq);
			return spaceList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	/**
//	 * 根据用户的的根节点查询空间树结构   已经迁移到control-service
//	 *
//	 * @param locationId
//	 * @return
//	 * @author wanglei
//	 */
//	@Override
//	public List<Map<String, Object>> findTree(Long locationId,Long orgId, Long tenantId) {
//		List<SpaceResp> spaceList = findRootByLocationIdAndTenantId(locationId, tenantId);
//		List<Map<String, Object>> mapList = BeanChangeUtil.spaceToMapList(spaceList);
//		findChild(tenantId, mapList);
//		return mapList;
//	}

	// private List<Map<String, Object>> filterMountSpace(List<Map<String, Object>>
	// mapList) {
	// List<Map<String, Object>> backUpList = new ArrayList<>();
	// backUpList.addAll(mapList);
	// for (Map<String, Object> map : backUpList) {
	// if (MapUtils.isEmpty(map) || !Constants.SPACE_BUILD.equals(map.get("type")))
	// {
	// mapList.remove(map);
	// }
	// }
	// return mapList;
	// }

	@Override
	public void mount(SpaceDeviceReq spaceDeviceReq) throws Exception {
		SpaceResp space = commonSpaceService.findSpaceInfoBySpaceId(spaceDeviceReq.getTenantId(), spaceDeviceReq.getSpaceId());
		if (!space.getType().equals(Constants.SPACE_ROOM) && !space.getType().equals(Constants.SPACE_MEETING)) {
			return;
		}
		if (spaceDeviceReq != null && StringUtil.isNotBlank(spaceDeviceReq.getDeviceIds())) {
			String[] deviceIds = spaceDeviceReq.getDeviceIds().split(",");
			for (String deviceId : deviceIds) {
				GetDeviceInfoRespVo deviceInfoRespVo = deviceCoreApi.get(deviceId);
				String clientId = "";
				SpaceDeviceReq req = new SpaceDeviceReq();
				req.setDeviceId(deviceId);
				req.setTenantId(spaceDeviceReq.getTenantId());
				req.setLocationId(spaceDeviceReq.getLocationId());
				List<SpaceDeviceResp> spaceDeviceResps = commonSpaceDeviceService.findSpaceDeviceByCondition(req);
				req.setSpaceId(spaceDeviceReq.getSpaceId());
				if (deviceInfoRespVo != null) {
					req.setBusinessTypeId(deviceInfoRespVo.getBusinessTypeId() == null ? null : deviceInfoRespVo.getBusinessTypeId());
					req.setDeviceTypeId(deviceInfoRespVo.getDeviceTypeId() == null ? null : deviceInfoRespVo.getDeviceTypeId());
					req.setProductId(deviceInfoRespVo.getProductId() == null ? null : deviceInfoRespVo.getProductId());
				    clientId = deviceInfoRespVo.getParentId() == null ? "" : deviceInfoRespVo.getParentId();
				}
			    if (CollectionUtils.isEmpty(spaceDeviceResps)) {
			    	commonSpaceDeviceService.inserSpaceDevice(req);
				} else {
					SpaceDeviceResp resultResp = spaceDeviceResps.get(0);
					if (resultResp != null) {
						req.setId(resultResp.getId());
						if (req.getSpaceId() != resultResp.getSpaceId()) {
							if (StringUtil.isNotBlank(clientId)) {
								// 删除旧房间相关数据信息 scene ifttt
								deviceService.deleteDeviceRelation(space.getOrgId(),deviceId, spaceDeviceReq.getTenantId(), false, clientId);
							}
						}
					}
					commonSpaceDeviceService.updateSpaceDevice(req);
				}
			}
		}
		// 迁移过来的
		// Map<String, Object> map = new HashMap<>();
		// map.put("UnConnect", "UnConnect");
		// BusinessDispatchMqttHelper.sendDeviceMountTopic(map);
	}

	@Override
	public void removeMount(SpaceDeviceReq spaceDeviceReq) throws Exception {
		if (spaceDeviceReq != null && StringUtil.isNotBlank(spaceDeviceReq.getDeviceIds())) {
			String[] deviceIds = spaceDeviceReq.getDeviceIds().split(",");
			for (String deviceId : deviceIds) {
				GetDeviceInfoRespVo deviceInfoRespVo = deviceCoreApi.get(deviceId);
				if (deviceInfoRespVo != null) {
					String clientId = deviceInfoRespVo.getParentId() == null ? null : deviceInfoRespVo.getParentId();
					if (StringUtil.isNotBlank(clientId)) {
						// 删除旧房间相关数据信息 scene ifttt
						deviceService.deleteDeviceRelation(deviceInfoRespVo.getOrgId(),deviceId, spaceDeviceReq.getTenantId(), false, clientId);
					}
				}
				commonSpaceDeviceService.deleteSpaceDeviceByDeviceId(spaceDeviceReq.getTenantId(), deviceId);
			}
		}
	}

	/**
	 * 查询没有挂载过的空间
	 *
	 * @param spaceReq
	 * @return
	 * @author wanglei
	 */
	// public List<SpaceResp> findSpaceUnMount(SpaceReq spaceReq) {
	// List<SpaceResp> spaceList = commonSpaceService.findSpaceUnMount(spaceReq);
	// return spaceList;
	// }

	/**
	 * 自定义条件查询
	 *
	 * @param spaceReq
	 * @return
	 * @author wanglei
	 */
	public List<SpaceResp> getSpaceByCondition(SpaceReq spaceReq) {
		List<SpaceResp> spaceList = commonSpaceService.findSpaceByCondition(spaceReq);
		return spaceList;
	}

	/**
	 * 递归查询子节点
	 *
	 * @param list
	 * @return
	 */
	private void findChild(Long tenantId, List<Map<String, Object>> list) {
		if (!list.isEmpty()) {
			for (Map<String, Object> mapSpace : list) {
				Long id = Long.parseLong(mapSpace.get("id").toString());
				SpaceReq space = new SpaceReq();
				space.setTenantId(tenantId);
				space.setParentId(id);
				List<Map<String, Object>> childList = spaceListToListMap(
						commonSpaceService.findSpaceByCondition(space));
				mapSpace.put("child", childList);
				findChild(tenantId, childList);
			}
		}
	}

	private List<Map<String, Object>> spaceListToListMap(List<SpaceResp> spaceList) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		String deployName = null;
		if (CollectionUtils.isNotEmpty(spaceList)) {
			for (SpaceResp resp : spaceList) {
				if (resp.getDeployId() != null) {
					DeploymentResp deploymentResp = deploymentMapper.findById(resp.getDeployId());
					deployName = deploymentResp == null ? null : deploymentResp.getDeployName();
				}
				mapList.add(BeanChangeUtil.spaceToMap(resp, deployName));
			}
		}
		return mapList;
	}

	/**
	 * 挂载设备信息组装
	 *
	 * @param deviceIds
	 * @return
	 */
	private Map<String, List<String>> sortDeviceIds(String deviceIds) {
		Map<String, List<String>> deviceMap = new HashMap<String, List<String>>();
		DeviceVo device = new DeviceVo();
		if (StringUtils.isNotBlank(deviceIds)) {
			String[] deviceIdList = deviceIds.split(",");
			for (String deviceId : deviceIdList) {
				GetDeviceInfoRespVo deviceInfoVo = deviceCoreApi.get(deviceId);
				if (!deviceMap.containsKey(deviceInfoVo.getParentId())) {
					List<String> addDeviceIds = new ArrayList<>();
					deviceMap.put(device.getParentId(), addDeviceIds);
				}
				deviceMap.get(device.getParentId()).add(deviceId);
			}
		}
		return deviceMap;
	}

	/**
	 * 组装实体参数
	 *
	 * @param locationId
	 * @param spaceId
	 * @return
	 */
	private SpaceDeviceResp createTempSpaceDevice(GetDeviceInfoRespVo device, Long locationId, Long spaceId) {
		SpaceDeviceResp spaceDevice = new SpaceDeviceResp();
		spaceDevice.setDeviceId(device.getUuid());
		spaceDevice.setDeviceCategoryId(device.getDeviceTypeId());
		spaceDevice.setDeviceTypeId(device.getDeviceTypeId());
		spaceDevice.setBusinessTypeId(device.getBusinessTypeId());
		spaceDevice.setProductId(device.getProductId());
		spaceDevice.setLocationId(locationId);
		spaceDevice.setSpaceId(spaceId);
		spaceDevice.setCreateTime(new Date());
		spaceDevice.setUpdateTime(new Date());
		spaceDevice.setTenantId(SaaSContextHolder.currentTenantId());
		return spaceDevice;
	}

	/**
	 * 根据建筑获取楼层
	 *
	 * @param buildId
	 * @param types
	 *            逗号相隔设备类型
	 * @return
	 * @author wanglei
	 */
	@Override
	public List<Map<String, Object>> getFloorAndDeviceCount(Long buildId, String types,Long orgId, Long tenantId) {
		SpaceReq space = new SpaceReq();
		space.setType(Constants.SPACE_FLOOR);
		space.setParentId(buildId);
		space.setTenantId(tenantId);
		List<SpaceResp> spaceList = commonSpaceService.findSpaceByCondition(space);
		return spaceToMapAndFindDevice(spaceList,types,orgId,tenantId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findDeviceByRoom(Long spaceId,Long orgId, Long tenantId) throws BusinessException {
		List<Long> spaceIds = new ArrayList<>();
		spaceIds.add(spaceId);// 添加自己本身的spaceId
		SpaceDeviceReq req = new SpaceDeviceReq();
		req.setSpaceId(spaceId);
		req.setTenantId(tenantId);
		List<String> deviceIds = new ArrayList<>();
		List<Map<String, Object>> bakList = new ArrayList<>();
		List<SpaceDeviceResp> spaceDeviceList = commonSpaceDeviceService.findSpaceDeviceByCondition(req);
		if (CollectionUtils.isNotEmpty(spaceDeviceList)) {
			spaceDeviceList.forEach(spaceDevice -> {
				deviceIds.add(spaceDevice.getDeviceId());
			});
		}
		List<ListDeviceByParamsRespVo> deviceParamResp = null;
		if (!CollectionUtils.sizeIsEmpty(deviceIds)) {
			ListDeviceByParamsReq params = new ListDeviceByParamsReq();
			params.setDeviceIds(deviceIds);
			deviceParamResp = deviceCoreApi.listDeviceByParams(params);
			for (ListDeviceByParamsRespVo resp : deviceParamResp) {
				Map<String, Object> map = new HashMap<>();
				String jsonStr = JSON.toJSONString(resp);
				map = JSON.parseObject(jsonStr, Map.class);
				DeviceBusinessTypeResp deviceBusinessTypeResp = new DeviceBusinessTypeResp();
				if (map.get("businessTypeId") != null) {
					deviceBusinessTypeResp = deviceBusinessTypeService
							.findById(orgId,tenantId,Long.valueOf(map.get("businessTypeId").toString()));
				}
				if (deviceBusinessTypeResp != null) {
					map.put("businessType", deviceBusinessTypeResp.getBusinessType() == null ? ""
							: deviceBusinessTypeResp.getBusinessType());
					map.put("businessTypeId",
							deviceBusinessTypeResp.getId() == null ? 0L : deviceBusinessTypeResp.getId());
				}
				map = getPosition(spaceDeviceList, resp.getUuid(), map);
				map.put("switch", map.get("switchStatus"));
				map.put("type", map.get("devType") == null ? "Light_Dimmable" : map.get("devType"));
				map.remove("switchStatus");
				map.remove("devType");
				map.put("targetValue", CenterControlDeviceStatus.getDeviceStatus(resp.getUuid()));
				map.put("deviceId", resp.getUuid());
				bakList.add(map);
			}
		}
		// 添加门窗位置信息
		SpaceResp space = commonSpaceService.findSpaceInfoBySpaceId(tenantId, spaceId);
		if (space != null && StringUtils.isNotBlank(space.getPosition())) {
			List<Map> doorWindowList = JSONArray.parseArray(space.getPosition(), Map.class);
			bakList = setDoorWindowList(doorWindowList, bakList);
		}
		return bakList;
	}

	private Map<String, Object> getPosition(List<SpaceDeviceResp> spaceList, String deviceId, Map<String, Object> map) {
		if (!CollectionUtils.sizeIsEmpty(spaceList)) {
			for (SpaceDeviceResp spaceDevice : spaceList) {
				if (spaceDevice.getDeviceId().equals(map.get("uuid").toString())) {
					map.put("position", spaceDevice.getPosition());
					break;
				}
			}
		}
		return map;
	}

	private List<String> getDeivceIds(List<SpaceDeviceResp> respLsit) {
		List<String> list = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(respLsit)) {
			respLsit.forEach(resp -> {
				list.add(resp.getDeviceId());
			});
		}
		return list;
	}

	/**
	 * space对象转map并且查询空间下的设备
	 *
	 * @param list
	 * @param types
	 * @return
	 */
	private List<Map<String, Object>> spaceToMapAndFindDevice(List<SpaceResp> list, String types, Long orgId, Long tenantId) {
		List<Map<String, Object>> spaceMapList = null;
		List<Long> spaceIds = null;
		if (!list.isEmpty()) {
			spaceMapList = new ArrayList<Map<String, Object>>();
			for (SpaceResp spaceResp : list) {
				Map<String, Object> map = BeanChangeUtil.spaceToMap(spaceResp, null);
				Map<String, Object> businessTypeMap = new HashMap<>();
				map.put("businessType", businessTypeMap);
				SpaceReq req = BeanCopyUtil.spaceRespToSpaceReq(spaceResp);
				spaceIds = getAllSpace(req);
				if (!spaceIds.isEmpty()) {
					countDevice(businessTypeMap, spaceIds, types, orgId,tenantId);// 统计设备
				} else {
					resetDeviceCount(businessTypeMap, types);// 初始化设备
				}
				spaceMapList.add(map);
			}
		}
		return spaceMapList;
	}

	/**
	 * 当没有设备是初始化数据，为前段提供方便
	 *
	 * @param map
	 * @param types
	 */
	private void resetDeviceCount(Map<String, Object> map, String types) {
		String[] typeArr = types.split(",");
		for (String type : typeArr) {
			map.put(type, "0/0");
		}
	}

	/**
	 * 通过设备大类和空间统计设备数量
	 *
	 * @param map
	 * @param spaceIds
	 * @param deviceTypes
	 */
	private void countDevice(Map<String, Object> map, List<Long> spaceIds, String deviceTypes,Long orgId, Long tenantId) {
		// String typeArr[] = deviceTypes.split(",");
		List<DeviceBusinessTypeResp> list = deviceBusinessTypeService.getBusinessTypeList(orgId,tenantId, "");
		Integer totalCount = 0;// 某空间下的总设备数
		Integer onCount = 0;// 某空间下启开关的设备数
		for (DeviceBusinessTypeResp deviceType : list) {
			totalCount = getCountByRoomIdsAndBusinessTypes(tenantId,orgId, spaceIds, deviceType.getBusinessType(), null);
			onCount = getCountByRoomIdsAndBusinessTypes(tenantId,orgId, spaceIds, deviceType.getBusinessType(), 1);
			map.put(deviceType.getBusinessType(), onCount + "/" + totalCount);
		}
	}

	private Integer getCountByRoomIdsAndBusinessTypes(Long tenantId,Long orgId, List<Long> groupIds, String deviceType,
													  Integer switchStatus) {
		List<String> deviceIdList = getDeviceIdsBySpaceIds(tenantId,orgId, groupIds);
		if (CollectionUtils.isEmpty(deviceIdList)) {// 没有设备之间返回
			return 0;
		}
		List<String> deviceTypeList = new ArrayList<>();
		DeviceBusinessTypeIDSwitchReq req = new DeviceBusinessTypeIDSwitchReq();
		deviceTypeList.add(deviceType);
		req.setBusinessTypeList(deviceTypeList);
		req.setDeviceIds(deviceIdList);
		req.setSwitchStatus(switchStatus);
		req.setOrgId(orgId);
		return centralControlDeviceApi.getCountByDeviceIdsAndBusinessTypesAndSwitch(req);
	}

	/**
	 * 根据功能类型查询业务类型
	 *
	 * @param deviceType
	 * @param typeList
	 */
	private void setDeviceType(String deviceType, List<String> typeList) {
		if (deviceType.equals("light")) {
			typeList.addAll(Constants.getLightTypeList());
		} else if (deviceType.equals("plug")) {
			typeList.addAll(Constants.getPlugTypeList());
		} else {
			typeList.add(deviceType);
		}
	}

	/**
	 * 根据空间ID和设备大类查询设备空间下打开的设备
	 *
	 * @param req
	 */
	public Integer countOnDevice(DeviceBusinessTypeIDSwitchReq req) {
		return centralControlDeviceApi.getCountByDeviceIdsAndBusinessTypesAndSwitch(req);
	}

	/**
	 * 根据空间ID和设备大类查询灯空间下打开的设备
	 *
	 * @param params
	 */
	public Integer countOnLightDevice(Map<String, Object> params) {
		DeviceBusinessTypeIDSwitchReq req = new DeviceBusinessTypeIDSwitchReq();
		req.setDeviceIds((List<String>) params.get("deviceIds"));
		req.setSwitchStatus((Integer) params.get("switch"));
		req.setBusinessTypeList((List<String>) params.get("businessType"));
		return countOnDevice(req);
	}

	/**
	 * 查询某个空间下所有的子空间
	 *
	 * @return
	 */
	public List<Long> getAllSpace(SpaceReq space) {
		List<Long> spaceIdList = new ArrayList<>();// 空间ids集合
		LOGGER.info("space info = " + JSON.toJSONString(space));
		if (!space.getType().equals(Constants.SPACE_ROOM)) {// 如果该空间本身是房间，无需在查询子集
			List<SpaceResp> allSapceList = new ArrayList<SpaceResp>();// 保存所有空间
			List<SpaceResp> childSpaceList = findByParentId(space.getTenantId(), space.getId(), null);// 子空间
			allSapceList.addAll(childSpaceList);
			findSpaceChild(allSapceList, childSpaceList, space.getTenantId());
			allSapceList.forEach(s -> {
				spaceIdList.add(s.getId());
			});
		}
		spaceIdList.add(space.getId());// 父级本身添加
		return spaceIdList;
	}

	private List<SpaceResp> findByParentId(Long tenantId, Long parentId, String name) {
		SpaceReq space = new SpaceReq();
		space.setParentId(parentId);
		space.setName(name);
		space.setTenantId(tenantId);
		List<SpaceResp> childSpaceList = commonSpaceService.findSpaceByCondition(space);
		return childSpaceList;
	}

	/**
	 * 查询某个空间下所有的子空间
	 *
	 * @param spaceId
	 * @return
	 */
	public List<SpaceResp> getAllChildSpace(Long spaceId, Long tanantId) {
		List<SpaceResp> allSapceList = new ArrayList<SpaceResp>();// 保存所有空间
		List<SpaceResp> childSpaceList = findByParentId(tanantId, spaceId, null);// 子空间
		allSapceList.addAll(childSpaceList);
		findSpaceChild(allSapceList, childSpaceList, tanantId);
		return allSapceList;
	}

	/**
	 * 递归查询List<Sapce>
	 *
	 * @param oldList
	 * @param newList
	 */
	public void findSpaceChild(List<SpaceResp> oldList, List<SpaceResp> newList, Long tanantId) {
		List<SpaceResp> spaceList = new ArrayList<SpaceResp>();
		for (SpaceResp space : newList) {
			spaceList = findByParentId(tanantId, space.getId(), null);
			if (!spaceList.isEmpty()) {
				oldList.addAll(spaceList);
				findSpaceChild(oldList, spaceList, tanantId);
			}
		}
	}

	/**
	 * 解析门窗信息
	 *
	 * @param doorWindowList
	 */
	private List<Map<String, Object>> setDoorWindowList(List<Map> doorWindowList, List<Map<String, Object>> bakList) {
		List<Map<String, Object>> backList = new ArrayList<>();
		backList.addAll(bakList);
		if (CollectionUtils.isNotEmpty(doorWindowList)) {
			for (Map<String, Object> map : doorWindowList) {
				map.put("switch", "");
				map.put("realItyId", "");
				map.put("name", "");
				map.put("parentId", "");
				map.put("deviceCategoryId", "");
				map.put("position", map.get("position") + "");
				backList.add(map);
			}
		}
		return backList;
	}

	@Override
	public List<Map<String, Object>> findDeviceByRoomAndDeviceType(Long roomId, List<String> deviceTypes,
																  Long orgId, Long tenantId) {
		return commonGetMapList(tenantId,orgId, roomId, deviceTypes, null);
	}

	@Override
	public List<Map<String, Object>> findDeviceByRoomAndDeviceBusinessType(Long roomId, List<String> businessType,Long orgId,
																		   Long tenantId) {
		return commonGetMapList(tenantId, orgId,roomId, null, businessType);
	}

	private List<Map<String, Object>> commonGetMapList(Long tenantId, Long orgId,Long roomId, List<String> deviceTypes,
													   List<String> businessType) {
		List<Long> spaceIds = new ArrayList<>();//
		spaceIds.add(roomId);// 添加自己本身的spaceId
		List<String> deviceIds = getDeviceIdsBySpaceIds(tenantId,orgId,spaceIds);
		DeviceBusinessTypeIDSwitchReq req = new DeviceBusinessTypeIDSwitchReq();
		req.setDeviceTypeList(deviceTypes);
		req.setBusinessTypeList(businessType);
		req.setDeviceIds(deviceIds);
		req.setTenantId(tenantId);
		req.setOrgId(orgId);
		List<Map<String, Object>> respList = findDeviceByRoom(req);
		return respList;
	}

	private List<Map<String, Object>> findDeviceByRoom(DeviceBusinessTypeIDSwitchReq req) {
		List<DeviceResp> respList = centralControlDeviceApi.findDeviceByCondition(req);
		List<Map<String, Object>> backList = new ArrayList();
		respList.forEach(rep -> {
			String jsonStr = JSON.toJSONString(rep);
			Map<String, Object> params = JSON.parseObject(jsonStr, Map.class);
			backList.add(params);
		});
		return backList;
	}

	/**
	 * 群控设备公共服务
	 *
	 * @param spaceId
	 * @param deviceType
	 * @param propertyMap
	 *            目标值
	 * @return
	 * @author linjihuang
	 */
	public void common(Long spaceId, String deviceType, Map<String, Object> propertyMap,Long orgId, Long tenantId) {
		SpaceResp spaceResp = commonSpaceService.findSpaceInfoBySpaceId(tenantId, spaceId);
		if (spaceResp != null) {
			SpaceReq spaceReq = BeanCopyUtil.spaceRespToSpaceReq(spaceResp);
			List<Long> groupIds = getAllSpace(spaceReq);// 查询某个空间下所有的spaceId包括自身
			for (Long groupId : groupIds) {
				Map<String, String> map = new HashMap<String, String>();
				new Thread(() -> {
					try {
						spaceGroupControl(propertyMap, map, groupId, deviceType,orgId, tenantId);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}).start();
			}
		}
	}

	/**
	 * 描述：群控消息发送
	 *
	 * @param propertyMap
	 * @param map
	 * @param groupId
	 * @throws Exception
	 */
	private void spaceGroupControl(Map<String, Object> propertyMap, Map<String, String> map, Long groupId,
								   String deviceType,Long orgId, Long tenantId) throws Exception {
		DeviceBusinessTypeResp deviceBusinessType = deviceBusinessTypeService.getBusinessTypeIdByType(orgId,deviceType);
		SpaceDeviceReq spaceDeviceReq = setSpaceDeviceReqInfo(groupId, deviceBusinessType, tenantId);
		List<SpaceDeviceResp> spaceDeviceseList = commonSpaceDeviceService.findSpaceDeviceByCondition(spaceDeviceReq);
		if (CollectionUtils.isNotEmpty(spaceDeviceseList)) {
			Set<String> gatewaySet = Constants.SPACE_GATEWAY_MOUNT
					.get("device-" + groupId + "-" + deviceBusinessType.getId());
			Set<String> uuidSet = Constants.SPACE_GROUP_UUID.get("group-" + groupId + "-" + deviceBusinessType.getId());
			Set<String> externalDeviceSet = Constants.SPACE_EXTERNAL_DEVICE_MOUNT
					.get(groupId + "and" + deviceBusinessType.getId());
			if (uuidSet != null && gatewaySet != null) {
				for (String uuid : uuidSet) {
					for (String clientId : gatewaySet) {
						MultiProtocolGatewayHepler.groupControl(clientId, uuid, propertyMap);
					}
				}
			}
			// 外接设备缓存 走单控
			if (externalDeviceSet != null) {
				for (String deviceId : externalDeviceSet) {
					control(deviceId, propertyMap);
				}
			}
			UpdateDeviceStatus(propertyMap, groupId, spaceDeviceseList);
		}
	}

	private SpaceDeviceReq setSpaceDeviceReqInfo(Long groupId, DeviceBusinessTypeResp deviceBusinessType,
												 Long tenantId) {
		SpaceResp spaceResp = commonSpaceService.findSpaceInfoBySpaceId(tenantId, groupId);
		SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
		spaceDeviceReq.setSpaceId(groupId);
		spaceDeviceReq.setTenantId(spaceResp.getTenantId());
		spaceDeviceReq.setLocationId(spaceResp.getLocationId());
		if(deviceBusinessType != null){
			spaceDeviceReq.setBusinessTypeId(deviceBusinessType.getId());
		}
		return spaceDeviceReq;
	}

	/**
	 * 描述：更新设备状态和内存数据
	 *
	 * @param propertyMap
	 * @param spaceIds
	 */
	private void UpdateDeviceStatus(Map<String, Object> propertyMap, Long spaceIds,
									List<SpaceDeviceResp> spaceDeviceseList) {
		new Thread(() -> {
			List<UpdateDeviceInfoReq> paramsList = Lists.newArrayList();
			for (SpaceDeviceResp spaceDevice : spaceDeviceseList) {
				if (spaceDevice.getDeviceTypeId() == null
						|| !Constants.getLightTypeMap().containsKey(spaceDevice.getDeviceTypeId().toString())) {
					continue;
				}
				Integer switchStatus = propertyMap.get("switch") != null ? (Integer) propertyMap.get("switch")
						: (Integer) propertyMap.get("OnOff");
				Integer Dimming = propertyMap.get("Dimming") != null ? (Integer) propertyMap.get("Dimming") : null;
				UpdateDeviceInfoReq upReq = new UpdateDeviceInfoReq();
				upReq.setTenantId(spaceDevice.getTenantId());
				upReq.setUuid(spaceDevice.getDeviceId());
				paramsList.add(upReq);
				Map<String, Object> valueMap = new HashMap<String, Object>();
				valueMap = CenterControlDeviceStatus.getDeviceStatus(spaceDevice.getDeviceId());
				if (valueMap == null) {
					valueMap = Maps.newHashMap();
				}
				if (switchStatus != null) {
					valueMap.put("OnOffStatus", switchStatus);
					valueMap.put("OnOff", switchStatus);
				}
				if (Dimming != null) {
					valueMap.put("DimmingStatus", Dimming);
					valueMap.put("Dimming", Dimming);
				}
				CenterControlDeviceStatus.putDeviceStatus(spaceDevice.getDeviceId(), valueMap);
				ListenCallBack(spaceDevice.getDeviceId(), spaceIds, String.valueOf(switchStatus));
			}
			deviceCoreApi.saveOrUpdateBatch(paramsList);
		}).start();
	}

	private GetDeviceInfoRespVo ListenCallBack(String deviceId, Long spaceIds, String property) {
		GetDeviceInfoRespVo device = deviceCoreApi.get(deviceId);
		Map<String, Object> map = new HashMap<>();
		map.put("spaceId", spaceIds);
		map.put("deviceId", deviceId);
		map.put("OnOffStatus", property);
		MapCallBack.mapCallBack(device, map, APIType.MultiProtocolGateway);
		return device;
	}

	/**
	 * 情景或者群控控制
	 *
	 * @param spaceId
	 *            空间Id
	 * @param targerValue
	 *            设备的目标值
	 * @param templateId
	 *            模板ID 和 targerValue 二选一
	 * @author wanglei
	 */
	@Override
	public void publicGroupOrSceneControl(Long spaceId, Map<String, Object> targerValue, Long templateId,Long orgId, Long tenantId)
			throws BusinessException {
		if (templateId == null && targerValue != null) {
			common(spaceId, null, targerValue,orgId, tenantId);
		}
	}

	@Override
	public Boolean groupControl(Long spaceId, String deviceType, Map<String, Object> propertyMap,Long orgId, Long tenantId)
			throws BusinessException {
		if (StringUtils.isNotBlank(deviceType)) {
			String[] typeArray = deviceType.split(",");
			for (String type : typeArray) {
				ThreadPoolUtil.instance().execute(new Runnable() {
					@Override
					public void run() {
						try {
							common(spaceId, type, propertyMap,orgId,tenantId);
						} catch (Exception e) {
							e.printStackTrace();
							throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
						}
					}
				});
			}
		}
		return true;
	}

	/**
	 * 控制选择
	 *
	 * @param device
	 * @param controlCode
	 * @param propertyMap
	 * @return
	 */
	private Boolean switchControl(GetDeviceInfoRespVo device, String controlCode, Map<String, Object> propertyMap) {
		try {
			Map<String, Object> attrMap = new LinkedHashMap();
			setMap(controlCode, propertyMap, attrMap);
			LOGGER.info("new new sort value=====" + JSON.toJSONString(attrMap));
			ProtocolParamVo protocolParamVo = setProtocloParamVo(device, controlCode, attrMap);
			switchControl(device, propertyMap, protocolParamVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void switchControl(GetDeviceInfoRespVo device, Map<String, Object> propertyMap,
			ProtocolParamVo protocolParamVo) {
		if(protocolParamVo.getApiType().name().equals(APIType.ManTunSci.name())) {
			propertyMap.put("deviceId", device.getUuid());
			airSwitchControl(propertyMap,device.getTenantId());
		}else {
			iProtocolAdaptationService.controlProtocolAdaptation(protocolParamVo);
		}
	}
	
	private void airSwitchControl(Map<String,Object> map,Long tenantId) {
		if(MapUtils.isNotEmpty(map)) {
			String deviceId=map.get("deviceId").toString();
			Integer onOff = 9999;
			if (map.containsKey("OnOff")) {
				onOff = Integer.valueOf(map.get("OnOff").toString());
			} else if (map.containsKey("switch")) {
				onOff = Integer.valueOf(map.get("switch").toString());
			}
			if (onOff == 1) {
				airSwitchApi.switchOn(deviceId, tenantId);
			} else if (onOff == 0) {
				airSwitchApi.switchOff(deviceId, tenantId);
			}
		}
	}

	private ProtocolParamVo setProtocloParamVo(GetDeviceInfoRespVo device, String controlCode,
											   Map<String, Object> attrMap) {
		ProtocolParamVo protocolParamVo = new ProtocolParamVo();
		APIType apiType = APIType.valueOf(controlCode);
		protocolParamVo.setApiType(apiType);
		protocolParamVo.setControlParams(attrMap);
		protocolParamVo.setDeviceId(device.getUuid());
		protocolParamVo.setMethod(ProtocolConstants.CONTROL_METHOD);
		return protocolParamVo;
	}

	/**
	 * 设备不同厂商不同的控制参数
	 *
	 * @param controlCode
	 * @param propertyMap
	 * @param attrMap
	 */
	private void setMap(String controlCode, Map<String, Object> propertyMap, Map<String, Object> attrMap) {
		switch (controlCode) {
			case Constants.VENDER_FLAG_AIRCONDITIONING:
				setAirParam(propertyMap, attrMap);
			case Constants.VENDER_FLAG_MULTI_GATEWAY:
				attrMap.putAll(propertyMap);
				attrMap.remove("deviceId");
			default:
				break;
		}
	}

	/**
	 * 空调配置控制参数
	 *
	 * @param propertyMap
	 * @param attrMap
	 */
	private void setAirParam(Map<String, Object> propertyMap, Map<String, Object> attrMap) {
		attrMap.clear();
		String temperature = propertyMap.get("temperature") == null ? "-1" : propertyMap.get("temperature").toString();// 温度
		String onOff = propertyMap.get("OnOff") == null ? "-1" : propertyMap.get("OnOff").toString();// 开关
		String mode = propertyMap.get("mode") == null ? "-1" : propertyMap.get("mode").toString();// 模式
		String windSpeed = propertyMap.get("windSpeed") == null ? "-1" : propertyMap.get("windSpeed").toString();// 分速
		String windStatus = propertyMap.get("windStatus") == null ? "1" : propertyMap.get("windStatus").toString();// 扫风类型
		String keyParam = temperature + "," + onOff + "," + mode + "," + windSpeed + windStatus;
		attrMap.put(ProtocolConstants.ATTR, keyParam);
	}

	@Override
	public Boolean control(String deviceId, Map<String, Object> propertyMap) throws BusinessException {
		setRGBAndOnOff(propertyMap);
		GetDeviceInfoRespVo vo = deviceCoreApi.get(deviceId);
		GetDeviceStatusInfoRespVo online=deviceStatusCoreApi.get(vo.getTenantId(), deviceId);
		if(online == null || online.getOnlineStatus() ==null || !online.getOnlineStatus().equals("connected")) {
			return false;
		}
		GetDeviceTypeInfoRespVo deviceType = deviceTypeCoreApi.get(vo.getDeviceTypeId());
		return switchControl(vo, deviceType.getVenderFlag(), propertyMap);
	}

	/**
	 * 设置rgb 和 开关转换
	 *
	 * @param propertyMap
	 */
	private void setRGBAndOnOff(Map<String, Object> propertyMap) {
		if (propertyMap.containsKey("switch")) {
			propertyMap.put("OnOff", propertyMap.get("switch"));
			propertyMap.remove("switch");
		}
		if (propertyMap.containsKey("RGBW")) {
			int RGBW = ToolUtils.convertToColorInt(propertyMap.get("RGBW").toString());
			propertyMap.put("RGBW", RGBW);
		}
	}

	/**
	 * 设置空间从属关系
	 *
	 * @param spaceId
	 * @param childIds
	 */
	@Override
	public void setSpaceRelation(Long spaceId, String childIds,Long orgId, Long tenantId) throws BusinessException {
		// 判断设置的空间类型等级判断
		if (!judgeMountRule(spaceId, childIds,orgId,tenantId)) {
			throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);
		}
		// 首先把之前空间是该spaceId的ParentID设置为-1
		updateChildUnMountByParentSpace(spaceId,orgId,tenantId);
		// 现有的子空间更新挂载
		updateChildMountBySpace(spaceId, childIds,orgId,tenantId);
	}

	// 子空间更新挂载
	private void updateChildMountBySpace(Long spaceId, String childIds,Long orgId, Long tenantId) {
		if (StringUtils.isNotBlank(childIds)) {
			String[] childArray = childIds.split(",");
			List<Long> childList = new ArrayList<Long>();
			SpaceReq req = new SpaceReq();
			req.setParentId(spaceId);
			req.setTenantId(tenantId);
			req.setOrgId(orgId);
			for (String str : childArray) {
				// 重新更新该space的子空间
				req.setId(Long.valueOf(str));
				commonSpaceService.update(req);
			}

		}
	}

	// 首先把之前空间是该spaceId的ParentID设置为-1
	private void updateChildUnMountByParentSpace(Long spaceId,Long orgId, Long tenantId) {
		SpaceReq req = new SpaceReq();
		req.setTenantId(tenantId);
		req.setParentId(spaceId);
		List<SpaceResp> respList = commonSpaceService.findSpaceByParentId(req);
		if (CollectionUtils.isNotEmpty(respList)) {
			for (SpaceResp resp : respList) {
				SpaceReq spaceReq = BeanCopyUtil.spaceRespToSpaceReq(resp);
				spaceReq.setParentId(-1L);
				commonSpaceService.update(spaceReq);
			}
		}
	}

	/**
	 * 房间类型对比防止大类挂小类
	 *
	 * @param spaceId
	 * @param childIds
	 * @return
	 */
	private Boolean judgeMountRule(Long spaceId, String childIds,Long orgId, Long tenantId) {
		SpaceResp rootSpace = commonSpaceService.findSpaceInfoBySpaceId(tenantId, spaceId);
		Boolean flag = true;
		List<Long> spaceIds = Lists.newArrayList();
		if (StringUtils.isNotBlank(childIds)) {
			String[] childArray = childIds.split(",");
			for (String childSpaceId : childArray) {
				spaceIds.add(Long.valueOf(childSpaceId));
			}
			// 批量获取到在做对比
			SpaceAndSpaceDeviceVo req = new SpaceAndSpaceDeviceVo();
			req.setTenantId(tenantId);
			req.setSpaceIds(spaceIds);
			List<SpaceResp> respList = commonSpaceService.findSpaceInfoBySpaceIds(req);
			if (CollectionUtils.isNotEmpty(respList)) {
				for (SpaceResp childResp : respList) {
					if (spaceTypeChangeToIntVlaue(rootSpace.getType()) <= spaceTypeChangeToIntVlaue(
							childResp.getType())) {
						flag = false;
						break;
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 房间类型转换数字
	 *
	 * @param spaceType
	 * @return
	 */
	public int spaceTypeChangeToIntVlaue(String spaceType) {
		if (spaceType.equals("BUILD")) {
			return 100;
		} else if (spaceType.equals("ROOM")) {
			return 80;
		} else if (spaceType.equals("FLOOR")) {
			return 90;
		} else if (spaceType.equals("GROUP")) {
			return 120;
		} else {
			return 200;
		}
	}

	/**
	 * 获取直接子空间
	 *
	 * @param spaceReq
	 */
	public List<SpaceResp> getChildSpace(SpaceReq spaceReq) {
		List<SpaceResp> childList = commonSpaceService.findSpaceByCondition(spaceReq);
		return childList;
	}

	/**
	 * 获取直接子空间
	 *
	 * @param floorId
	 * @param spaceType
	 */
	@Override
	public List<SpaceVo> getChildSpaceStatus(Long floorId, String spaceType,Long orgId, Long tenantId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("parentId", floorId);
		params.put("type", spaceType);
		SpaceReq space = new SpaceReq();
		space.setParentId(floorId);
		space.setType(spaceType);
		space.setTenantId(tenantId);
		space.setOrgId(orgId);
		List<SpaceResp> childList = commonSpaceService.findSpaceByCondition(space);
		return setSpaceSwitchStatus(floorId, childList);
	}

	private List<SpaceVo> setSpaceSwitchStatus(Long floorId, List<SpaceResp> spaceList) {
		List<SpaceVo> spaceVoList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(spaceList)) {
			for (SpaceResp space : spaceList) {
				SpaceVo vo = new SpaceVo();
				vo.setId(space.getId());
				vo.setSort(space.getSort());
				vo.setName(space.getName());
				// 查询设备ids
				// SpaceDeviceReq req =new SpaceDeviceReq();req.setSpaceId(space.getId());
				// List<SpaceDeviceResp> respList =
				// commonSpaceDeviceService.findSpaceDeviceByCondition(req);
				// List<String> deviceIds=getDeivceIds(respList);
				vo.setFlag(getSpaceStatus(space.getId(),space.getOrgId(), space.getTenantId()));
				spaceVoList.add(vo);
			}
		}
		return spaceVoList;
	}

	/**
	 * 获取空间下直接挂载的设备
	 *
	 * @param spaceId
	 */
	@Override
	public List<Map<String, Object>> getDirectDeviceBySpace(Long spaceId,Long orgId, Long tenantId) {
		List<Long> spaceIds = new ArrayList<>();
		spaceIds.add(spaceId);
		List<String> deviceIds = getDeviceIdsBySpaceIds(tenantId,orgId,spaceIds);
		DeviceBusinessTypeIDSwitchReq req = new DeviceBusinessTypeIDSwitchReq();
		req.setTenantId(tenantId);
		req.setDeviceIds(deviceIds);
		req.setIsDirectDevice(0);
		return findDeviceByRoom(req);
	}

	/**
	 * 根据房间ids查询设备IDS
	 *
	 * @param spaceIds
	 * @return
	 */
	private List<String> getDeviceIdsBySpaceIds(Long tenantId,Long orgId, List<Long> spaceIds) {
		SpaceAndSpaceDeviceVo vo = new SpaceAndSpaceDeviceVo();
		vo.setSpaceIds(spaceIds);
		vo.setTenantId(tenantId);
		vo.setOrgId(orgId);
		List<SpaceDeviceResp> respList = commonSpaceDeviceService.findSpaceDeviceBySpaceIdsOrDeviceIds(vo);
		List<String> deviceIds = Lists.newArrayList();
		if (CollectionUtils.isEmpty(respList)) {
			return null;
		}
		respList.forEach(resp -> {
			deviceIds.add(resp.getDeviceId());
		});
		return deviceIds;
	}

	/**
	 * 获取已挂载的设备列表
	 *
	 * @param spaceDeviceReq
	 */
	// public List<String> getMountDeviceBySpaceId(SpaceDeviceReq spaceDeviceReq) {
	// return commonSpaceService.getMountDeviceBySpaceId(spaceDeviceReq);
	// }

	/**
	 * 查询未挂载到网关的房间信息
	 *
	 * @param status
	 * @return
	 */
	// public List<SpaceDeviceVo> findSpaceInfo(String status) {
	// List<SpaceDeviceVo> list = commonSpaceService.findSpaceInfo(status);
	// return list;
	// }

	// public int findSpaceMount(SpaceDeviceVo spaceDeviceVo) {
	// int result = commonSpaceService.findSpaceMount(spaceDeviceVo);
	// return result;
	// }

	/**
	 * 获取空间下直接挂载的设备
	 *
	 * @param spaceDeviceReq
	 */
	@SuppressWarnings("unchecked")
	public void updateSpaceDevicePosition(SpaceDeviceReq spaceDeviceReq) {
		if (spaceDeviceReq != null) {
			Long spaceId = spaceDeviceReq.getSpaceId();
			List<Map<String, Object>> mapList = spaceDeviceReq.getSpaceInfoMapList();
			if (CollectionUtils.isNotEmpty(mapList)) {
				List<Map<String, Object>> doorWindowList = new ArrayList<>();
				doorWindowList.addAll(mapList);
				List<SpaceDeviceReq> spaceDeviceReqs=Lists.newArrayList();
				for (Map map : mapList) {
					// 获取位置保存到空间关系
					if (map.get("type") != null) {
						if (!(map.get("type").toString().contains("door")
								|| map.get("type").toString().contains("window"))) {
							SpaceDeviceReq sdReq = new SpaceDeviceReq();
							String deviceId = map.get("deviceId").toString();
							sdReq.setSpaceId(spaceId);
							sdReq.setDeviceId(deviceId);
							sdReq.setTenantId(spaceDeviceReq.getTenantId());
							List<SpaceDeviceResp> respList = commonSpaceDeviceService.findSpaceDeviceByCondition(sdReq);
							SpaceDeviceResp spaceDevice = CollectionUtils.isEmpty(respList) ? null : respList.get(0);
							String position = String.valueOf(map.get("position"));
							if (spaceDevice != null && position != null && StringUtils.isNotBlank(position)) {
								spaceDevice.setPosition(position);
								SpaceDeviceReq req = BeanCopyUtil.SpaceDeviceRespToSpaceDeviceReq(spaceDevice);
								req.setTenantId(spaceDeviceReq.getTenantId());
								spaceDeviceReqs.add(req);
								doorWindowList.remove(map);
							}
						}
					}
				}
				commonSpaceDeviceService.updateSpaceDevices(spaceDeviceReqs);
				// 获取到门窗保存到空间space中
				SpaceResp space = commonSpaceService.findSpaceInfoBySpaceId(spaceDeviceReq.getTenantId(), spaceId);
				space.setTenantId(spaceDeviceReq.getTenantId());
				space.setPosition(JSON.toJSONString(doorWindowList));
				SpaceReq req = BeanCopyUtil.spaceRespToSpaceReq(space);
				commonSpaceService.update(req);
			}
		}
	}

	/**
	 * 根据设备和空间统计数量
	 */
	// public Integer countBySpaceAndDevice(String deviceId, List<String> spaceList)
	// {
	// SpaceDeviceReq params=new SpaceDeviceReq();
	// params.setDeviceId(deviceId);
	// params.setSpaceId(spaceId);(spaceList);
	// return commonSpaceService.countSpaceByCondition(params);
	// }

	@Override
	public int saveSpaceDevice(List<SpaceDeviceReq> spaceDeviceList) {
		int i = 0;
		if (CollectionUtils.isNotEmpty(spaceDeviceList)) {
			for (SpaceDeviceReq spaceDeviceReq : spaceDeviceList) {
				int rec = commonSpaceService.deleteSpaceBySpaceId(spaceDeviceReq.getTenantId(),
						spaceDeviceReq.getSpaceId()) == true ? 1 : 0;
			}
			for (SpaceDeviceReq spaceDeviceReq : spaceDeviceList) {
				Long deviceTypeId = getDeviceTypeId(spaceDeviceReq);
				spaceDeviceReq.setDeviceTypeId(deviceTypeId);
				spaceDeviceReq.setCreateTime(new Date());
				spaceDeviceReq.setUpdateTime(new Date());
				commonSpaceDeviceService.inserSpaceDevice(spaceDeviceReq);
				i++;
			}
			loadDeviceFromGateway();
		}
		return i;
	}

	public Long getDeviceTypeId(SpaceDeviceReq spaceDeviceReq) {
		Long deviceTypeId = 0L;
		try {
			List<DeviceRemoteControlResp> drcList = deviceRemoteService
					.findRemoteControlByDeviceType(spaceDeviceReq.getTenantId(), spaceDeviceReq.getDeviceTypeId());
			if (CollectionUtils.isNotEmpty(drcList)) {
				deviceTypeId = drcList.get(0).getDeviceTypeId();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return deviceTypeId;
	}

	/**
	 * 从网关同步设备
	 */
	public void loadDeviceFromGateway() {
		// TODO
//		List<GetDeviceInfoRespVo> deviceList = centralControlDeviceApi
//				.findDirectDeviceByDeviceCatgory(APIType.MultiProtocolGateway.name());
//		List<String> deviceIds = Lists.newArrayList();
//		if (CollectionUtils.isNotEmpty(deviceList)) {
//			for (GetDeviceInfoRespVo device : deviceList) {
//				String deviceId = device.getUuid();
//				deviceIds.add(deviceId);
//				try {
//					// GetDeviceInfoRespVo deviceResp = deviceCoreApi.get(deviceId);
//					deviceCoreApi.deleteByDeviceId(deviceId);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			deviceCoreApi.deleteBatchByDeviceIds(deviceIds);
//		}
	}

	@Override
	public List<SpaceResp> findSpaceByTenantId(Long tenantId,Long orgId) {
		SpaceReq space = new SpaceReq();
		space.setTenantId(tenantId);
		space.setOrgId(orgId);
		List<SpaceResp> spaceList = commonSpaceService.findSpaceByCondition(space);
		return spaceList;
	}

	@Override
	public SpaceDeviceResp findSpaceIdByDeviceId(String deviceId,Long orgId, Long tenantId) {
		SpaceDeviceReq req = new SpaceDeviceReq();
		req.setDeviceId(deviceId);
		req.setTenantId(tenantId);
		req.setOrgId(orgId);
		List<SpaceDeviceResp> respList = commonSpaceDeviceService.findSpaceDeviceByCondition(req);
		return CollectionUtils.isEmpty(respList) ? null : respList.get(0);
	}

	@Override
	public int findSpaceCountByParentId(SpaceReq space) {
		if (space.getParentId() == null) {
			return 0;
		}
		SpaceReq req = new SpaceReq();
		req.setParentId(space.getParentId());
		return commonSpaceService.countSpaceByCondition(req);
	}

	@Override
	public void syncSpaceStatus(String deviceId) {
		if (StringUtils.isNotBlank(deviceId)) {
			GetDeviceInfoRespVo deviceResp = deviceCoreApi.get(deviceId);
			if (deviceResp != null) {
				SpaceCallback spaceCallBack = new SpaceCallback();
				Map<String, Object> proertyMap = new HashMap<>();
				proertyMap.put("AlarmStatus", 1);
				spaceCallBack.callback(deviceResp, proertyMap, APIType.MultiProtocolGateway);
			}
		}
	}

	@Override
	public List<Map<String, Object>> getMeetingSpaceTree(Long tenantId,Long orgId, Long locationId) {
		SpaceReq space = new SpaceReq();
		space.setType(Constants.SPACE_BUILD);
		space.setLocationId(locationId);
		space.setTenantId(tenantId);
		space.setOrgId(orgId);
		List<SpaceResp> buildList = commonSpaceService.findSpaceByCondition(space);
		List<Map<String, Object>> treeList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(buildList)) {
			buildList.forEach(build -> {
				space.setType(Constants.SPACE_FLOOR);
				space.setParentId(build.getId());
				Map<String, Object> map = new HashMap<>();
				map.put("spaceId", build.getId());
				map.put("name", build.getName());
				// 获取楼层
				List<SpaceResp> respList = commonSpaceService.findSpaceByCondition(space);
				List<Map<String, Object>> mapList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(respList)) {
					respList.forEach(resp -> {
						Map<String, Object> floorMap = new HashMap<>();
						floorMap.put("spaceId", resp.getId());
						floorMap.put("name", resp.getName());
						space.setParentId(resp.getId());
						space.setType(Constants.SPACE_ROOM);
						space.setModel(1);// 会议室
						// 获取会议室
						List<SpaceResp> childList = commonSpaceService.findSpaceByCondition(space);
						floorMap.put("child", spaceListToMapList(childList));
						mapList.add(floorMap);
					});
				}
				map.put("child", mapList);
				treeList.add(map);
			});
		}
		return treeList;
	}

	private List<Map<String, Object>> spaceListToMapList(List<SpaceResp> spaceList) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(spaceList)) {
			spaceList.forEach(child -> {
				Map<String, Object> mapChild = new HashMap<>();
				mapChild.put("spaceId", child.getId());
				mapChild.put("name", child.getName());
				mapList.add(mapChild);
			});
		}
		return mapList;
	}

	@Override
	public List<String> getDeviceIdBySpaceId(Long spaceId,Long orgId, Long tenantId) {
		SpaceDeviceReq req = new SpaceDeviceReq();
		req.setTenantId(tenantId);
		req.setSpaceId(spaceId);
		req.setOrgId(orgId);
		List<String> stringList = new ArrayList<>();
		List<SpaceDeviceResp> respList = commonSpaceDeviceService.findSpaceDeviceByCondition(req);
		if (CollectionUtils.isNotEmpty(respList)) {
			for (SpaceDeviceResp resp : respList) {
				stringList.add(resp.getDeviceId());
			}
		}
		return stringList;
	}

	@Override
	public List<LocationResp> findLocationByCondition(LocationReq locationReq) {
		return locationMapper.findLocationByCondition(locationReq);
	}

	@Override
	public void saveLocation(LocationReq locationReq) {
		locationMapper.save(locationReq);
	}

	@Override
	public void updateLocation(LocationReq locationReq) {
		locationMapper.update(locationReq);
	}

	@Override
	public void delLocation(Long id) {
		locationMapper.delete(id);
	}

	public List<SpaceResp> findSpaceListByName(String name,Long tenantId){
		SpaceReq space = new SpaceReq();
		space.setName(name);
		space.setTenantId(tenantId);
		List<SpaceResp> list = Lists.newArrayList();
		for(SpaceResp spaceResp : commonSpaceService.findSpaceByCondition(space)){
			if(spaceResp.getName().equals(name)){
				list.add(spaceResp);
			}
		}
		return list;
	}

	@Override
	public CommonResponse spaceDataImport(SpaceExcelReq spaceExcelReq) {
		try {
			Map<String, Object> result = spaceExcelReq.getResult();
			Long tenantId = spaceExcelReq.getTenantId();
			Long locationId = spaceExcelReq.getLocationId();
			Long userId = spaceExcelReq.getUserId();
			List successSb = Lists.newArrayList();
	        List failSb = Lists.newArrayList();
			if (result != null) {
				for (Map.Entry<String, Object> map : result.entrySet()) {
					List<Map<String, Object>> locationList = (List<Map<String, Object>>) map.getValue();
					if (CollectionUtils.isNotEmpty(locationList)) {
						for (int i=0; i<locationList.size() ; i++) {
							Map<String, Long> tmpMap = Maps.newHashMap();
							String name =  String.valueOf(locationList.get(i).get("name"));
							String type =  String.valueOf(locationList.get(i).get("type"));
							String parent =  String.valueOf(locationList.get(i).get("parent"));
							String deployment=locationList.get(i).get("template_name")==null?"":locationList.get(i).get("template_name").toString();
							if(StringUtil.isBlank(type)){
								failSb.add("第"+(i+2)+"行 : type为空");
								continue;
//								return new CommonResponse(ResultMsg.FAIL, "第"+(i+1)+"行,"+"type不能为空");
							}

							if (StringUtils.isBlank(name)) {
								failSb.add("第"+(i+2)+"行 : name为空");
								continue;
//								return new CommonResponse(ResultMsg.FAIL, "第"+(i+1)+"行,"+"name不能为空");
							}else {//判断name是否重复
								int count = findSpaceListByName(name,tenantId).size();
								if(count !=0){//build 已经存在了
									failSb.add("第"+(i+2)+"行 : name已经存在");
									continue;
//									return new CommonResponse(ResultMsg.FAIL, "第"+(i+1)+"行,"+"name已经存在");
								}
							}

							Long deploymentId=null;
							//房间模板（部署类型）
							LOGGER.info("----------deployment--------"+deployment);
							if(StringUtils.isNotBlank(deployment)) {
								DeploymentResp resp=deploymentMapper.findByName(spaceExcelReq.getOrgId(),deployment);
								if(resp !=null) {
									deploymentId=resp.getId();
								}
							}
							
							if(StringUtil.isNotBlank(type) && (type.equals("build") || type.equals("BUILD"))) {//类型为build的时候
								tmpMap = saveSpace(tmpMap, name, tenantId, locationId, userId, Constants.SPACE_BUILD,
										"buildId", 0L,deploymentId);
							}

							if(StringUtil.isNotBlank(type) && (type.equals("floor") || type.equals("FLOOR")) && StringUtil.isNotBlank(parent)){//类型为floor，且有parent
								//判断parent在库中是否存在
								List<SpaceResp> list = findSpaceListByName(parent,tenantId);
								int count = list.size();
								if(count == 0){
									//parent不存在,添加parent
									tmpMap = saveSpace(tmpMap, parent, tenantId, locationId, userId, Constants.SPACE_BUILD,
											"buildId", 0L,deploymentId);
									//添加floor
									tmpMap = saveSpace(tmpMap, name, tenantId, locationId, userId, Constants.SPACE_FLOOR,
											"floorId", tmpMap.get("buildId"),deploymentId);
								}else {
									//parent已经存在，添加floor
									tmpMap = saveSpace(tmpMap, name, tenantId, locationId, userId, Constants.SPACE_FLOOR,
											"floorId", list.get(0).getId(),deploymentId);
								}
							}else if(StringUtil.isNotBlank(type) && (type.equals("floor") || type.equals("FLOOR")) && StringUtil.isBlank(parent)){//类型为floor，但没有parent
								//只需要保存floor
								tmpMap = saveSpace(tmpMap, name, tenantId, locationId, userId, Constants.SPACE_FLOOR,
										"floorId", 0L,deploymentId);
							}

							if(StringUtil.isNotBlank(type) && (type.equals("room") || type.equals("ROOM")) && StringUtil.isNotBlank(parent)){//类型为room，且有parent
								//判断parent在库中是否存在
								List<SpaceResp> list = findSpaceListByName(parent,tenantId);
								int count = list.size();
								if(count == 0){
									//parent不存在,添加parent
									tmpMap = saveSpace(tmpMap, parent, tenantId, locationId, userId, Constants.SPACE_FLOOR,
											"floorId", 0L,deploymentId);
									//添加room
									tmpMap = saveSpace(tmpMap, name, tenantId, locationId, userId, Constants.SPACE_ROOM, "",
											tmpMap.get("floorId"),deploymentId);
								}else {
									//parent已经存在，添加room
									tmpMap = saveSpace(tmpMap, name, tenantId, locationId, userId, Constants.SPACE_ROOM, "",
											list.get(0).getId(),deploymentId);
								}
							}else if(StringUtil.isNotBlank(type) && (type.equals("room") || type.equals("ROOM"))  && StringUtil.isBlank(parent)){//类型为room，但没有parent
								//只需要保存room
								tmpMap = saveSpace(tmpMap, name, tenantId, locationId, userId, Constants.SPACE_ROOM, "",
										0L,deploymentId);
							}
							// tmpMap存在name表示成功
							if (tmpMap != null && tmpMap.get(name) != null) {
								successSb.add(name);
							}
						}
						Map<String,Object> backMap=Maps.newHashMap();
				        backMap.put("success", successSb);
				        backMap.put("fail", failSb);
						return CommonResponse.success(backMap);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, Long> saveSpace(Map<String, Long> tmpMap, String spaceName, Long tenantId, Long locationId,
									   Long userId, String type, String key, Long parentId,Long deployId) {
		SpaceReq space = new SpaceReq();
		space.setName(spaceName);
		space.setTenantId(tenantId);
		space.setLocationId(locationId);
		space.setCreateTime(new Date());
		space.setCreateBy(userId);
		space.setType(type);
		space.setDeployId(deployId);
		if (!Constants.SPACE_BUILD.equals(type)) {
			space.setParentId(parentId);
		}
		Long spaceId = commonSpaceService.save(space);
		if (StringUtils.isNotBlank(key)) {
			tmpMap.put(key, spaceId);
		}
		if (spaceId != null) {
			tmpMap.put(spaceName, spaceId);
		}

		return tmpMap;
	}

	@Override
	public String saveSpaceBackgroundImgImport(SpaceBackgroundImgReq req) {
		spaceBackgroundImgMapper.save(req);
		return "success";
	}

	@Override
	public List<SpaceBackgroundImgResp> getSpaceBackgroundImg(SpaceBackgroundImgReq req) {
		List<SpaceBackgroundImgResp> respList = spaceBackgroundImgMapper.getSpaceBackgroundImgByCondition(req);
		return respList;
	}

	@Override
	public SpaceBackgroundImgResp getSpaceBackgroundImgById(Long id) {
		SpaceBackgroundImgResp resp = spaceBackgroundImgMapper.getSpaceBackgroundImgById(id);
		return resp;
	}

	@Override
	public Integer deleteSpaceBackgroundImg(Long id) {
		Integer rec = spaceBackgroundImgMapper.delete(id);
		return rec;
	}

	@Override
	public Integer updateSpaceBackgroundImg(SpaceBackgroundImgReq req) {
		Integer rec = spaceBackgroundImgMapper.update(req);
		return rec;
	}

	@Override
	public List<DeploymentResp> getDeploymentList(DeploymentReq req) {
		return deploymentMapper.getDeploymentList(req);
	}

	@Override
	public DeploymentResp findDeploymentById(Long tenantId,Long orgId,Long id) {
		return deploymentMapper.findById(id);
	}

	@Override
	public List<SpaceResp> findSpaceListByDeployId(Long deployId, Long tenantId,Long orgId, Long locationId) {
		SpaceReq req = new SpaceReq();
		req.setDeployId(deployId);
		req.setTenantId(tenantId);
		req.setLocationId(locationId);
		req.setOrgId(orgId);
		return commonSpaceService.findSpaceByCondition(req);
	}

	@Override
	public void deleteMountByDeviceIds(String deviceIds,Long orgId, Long tenantId) {
		if (StringUtils.isNotBlank(deviceIds)) {
			String[] devices = deviceIds.split(",");
			for (String deviceId : devices) {
				commonSpaceDeviceService.deleteSpaceDeviceByDeviceId(tenantId, deviceId);
			}
		}
	}

	@Override
	public int getSpaceStatus(Long spaceId,Long orgId, Long tenantId) {
		SpaceResp space = commonSpaceService.findSpaceInfoBySpaceId(tenantId, spaceId);
		Integer spaceStatus = null;
		if (space != null) {
			spaceStatus = RedisCacheUtil.valueObjGet(RedisKeyUtil.getSpaceStatusKey(space.getTenantId(), spaceId),
					Integer.class);
			if (spaceStatus == null) {
				spaceStatus = 0;
				spaceStatus = setSpaceStatusMethod(spaceId, space, spaceStatus);
			}
		}
		return spaceStatus;
	}

	@Override
	public int setSpaceStatus(Long spaceId,Long orgId, Long tenantId) {
		SpaceResp space = commonSpaceService.findSpaceInfoBySpaceId(spaceId, tenantId);
		Integer spaceStatus = 0;
		if (space != null) {
			spaceStatus = setSpaceStatusMethod(spaceId, space, spaceStatus);
		}
		return spaceStatus;
	}

	private Integer setSpaceStatusMethod(Long spaceId, SpaceResp space, Integer spaceStatus) {
		List<String> spaceDeviceIdList = getSpaceDeviceList(spaceId, space);
		if (CollectionUtils.isNotEmpty(spaceDeviceIdList)) {
			ListDeviceStateReq params = new ListDeviceStateReq();
			params.setTenantId(space.getTenantId());
			params.setDeviceIds(spaceDeviceIdList);
			Map<String, Map<String, Object>> map = deviceStateService.listStates(params);
			for (String deviceId : map.keySet()) {
				Map<String, Object> stateMap = map.get(deviceId);
				if (stateMap != null && stateMap.get("OnOff") != null) {
					if (Integer.valueOf(stateMap.get("OnOff").toString()) == 1) {
						spaceStatus = 1;
						break;
					}
				}
			}
		}
		RedisCacheUtil.valueObjSet(RedisKeyUtil.getSpaceStatusKey(space.getTenantId(), spaceId), spaceStatus);
		return spaceStatus;
	}

	private List<String> getSpaceDeviceList(Long spaceId, SpaceResp space) {
		List<String> spaceDeviceIdList = Lists.newArrayList();
		String spaceDevIdKey = RedisKeyUtil.getSpaceDevIdBySpaceIdKey(space.getTenantId(), spaceId);
		spaceDeviceIdList = RedisCacheUtil.listGetAll(spaceDevIdKey, String.class);
		if (CollectionUtils.isEmpty(spaceDeviceIdList)) {
			if (CollectionUtils.isNotEmpty(spaceDeviceIdList)) {
				RedisCacheUtil.listSet(spaceDevIdKey, spaceDeviceIdList);
			}
		}
		return spaceDeviceIdList;
	}

	@Override
	public List<SpaceResp> findByIds(Long tenantId,Long orgId, List<Long> spaceIds) {
		SpaceAndSpaceDeviceVo req = new SpaceAndSpaceDeviceVo();
		req.setTenantId(tenantId);
		req.setSpaceIds(spaceIds);
		return commonSpaceService.findSpaceInfoBySpaceIds(req);
	}

	@Override
	public boolean checkSpaceName(SpaceReq spaceReq) {
		List<SpaceResp> respList = commonSpaceService.findSpaceByCondition(spaceReq);
		if(CollectionUtils.isNotEmpty(respList)) {
			for(SpaceResp resp:respList) {
				if(resp.getName().equals(spaceReq.getName())) {
					if(spaceReq.getId()==null) {
						return false;
					}else if(spaceReq.getId().compareTo(resp.getId()) !=0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public List<SpaceResp> findSpaceByType(SpaceReq spaceReq) {
		return commonSpaceService.findSpaceByCondition(spaceReq);
	}

	@Override
	public List<String> getDirectDeviceUuidBySpaceId(Long spaceId) {
		SpaceDeviceReq req = new SpaceDeviceReq();
		req.setSpaceId(spaceId);
		List<SpaceDeviceResp> list = commonSpaceDeviceService.findSpaceDeviceByCondition(req);
		List<String> deviceIds = getDeivceIds(list);
		ListDeviceInfoReq deviceInfo = new ListDeviceInfoReq();
		deviceInfo.setDeviceIds(deviceIds);
		List<ListDeviceInfoRespVo> deviceList = deviceCoreApi.listDevices(deviceInfo);
		List<String> directDeviceIds = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(deviceList)) {
			deviceList.forEach(vo -> {
				directDeviceIds.add(vo.getUuid());
			});
		}
		return directDeviceIds;
	}

	@Override
	public void updateSpaceDevice(SpaceDeviceReq spaceDeviceReq) {
		commonSpaceDeviceService.updateSpaceDevice(spaceDeviceReq);

	}

	@Override
	public void saveOrUpdate(DeploymentReq req) {
		if (req.getId() == null) {
			deploymentMapper.save(req);
		} else {
			deploymentMapper.update(req);
		}
	}

	@Override
	public void deleteBatchDeploy(String deployIds) {
		DeploymentReq req = new DeploymentReq();
		if (StringUtils.isNotBlank(deployIds)) {
			String[] idArry = deployIds.split(",");
			for (String idStr : idArry) {
				req.setId(Long.valueOf(idStr));
				deploymentMapper.delDeploymentById(req);
			}
		}

	}

	@Override
	public void addOrUpdateCalendar(CalendarReq calendarReq) {
		if(calendarReq.getId() ==null){//add
			deploymentMapper.addCalendar(calendarReq);
		}else {//update
			deploymentMapper.updateCalendar(calendarReq);
		}
	}
	
	@Override
	public Integer countExistCalendar(Long currentTime,int type,Long locationId,Long orgId,Long tenantId) {
		return deploymentMapper.countExistCalendar(currentTime,type,locationId,orgId,tenantId);
	}

	@Override
	public void deleteCalendar(Long tenantId,Long orgId, Long id) {
		deploymentMapper.deleteCalendar(tenantId,orgId,id);
	}

	@Override
	public Page<CalendarResp> findCalendarList(String pageNum, String pageSize, String name) {
		Page<CalendarResp> page = new Page<CalendarResp>();
		try {
			com.github.pagehelper.PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
			CalendarResp calendarResp = new CalendarResp();
			if(StringUtil.isNotEmpty(name)){
				calendarResp.setName(name);
			}
			List<CalendarResp> respList = deploymentMapper.findCalendarList(calendarResp);
			PageInfo<CalendarResp> info = new PageInfo(respList);
			BeanUtil.copyProperties(info, page);
			page.setResult(info.getList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}

	@Override
	public List<CalendarResp> findCalendarListNoPage(String name) {
		try {
			CalendarResp calendarResp = new CalendarResp();
			if(StringUtil.isNotEmpty(name)){
				calendarResp.setName(name);
			}
			//List<CalendarResp> respList = deploymentMapper.findCalendarList(calendarResp);
			List<CalendarResp> respList = deploymentMapper.findSameName(calendarResp);
			return respList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



	@Override
	public CalendarResp getCalendarIofoById(Long tenantId,Long orgId, Long id) {
		CalendarResp calendarResp = deploymentMapper.getCalendarIofoById(id);
		return calendarResp;
	}

	@Override
	public Page<DeploymentResp> getDeploymentPage(DeploymentReq req) {
		com.github.pagehelper.PageHelper.startPage(req.getPageNumber(),req.getPageSize());
		List<DeploymentResp> deploymentRespList = deploymentMapper.getDeploymentList(req);
		PageInfo<DeploymentResp> info = new PageInfo(deploymentRespList);
		Page<DeploymentResp> page = new Page<DeploymentResp>();
		BeanUtil.copyProperties(info, page);
		page.setResult(info.getList());
		return page;
	}

	@Override
	public List<Long> getLocationTenant() {
		List<Long> tenantIds=Lists.newArrayList();
		List<LocationResp> locationList=locationMapper.getAll();
		if(CollectionUtils.isNotEmpty(locationList)) {
			for(LocationResp location:locationList){
				tenantIds.add(location.getTenantId());
				break;
			}
		}
		return tenantIds;
	}

	public DeploymentResp findByName(Long orgId,String name) {
		return deploymentMapper.findByName(orgId,name);
	}
}

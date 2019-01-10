package com.iot.center.service;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.iot.airswitch.api.AirSwitchApi;
import com.iot.building.allocation.api.EnvironmentPropertyApi;
import com.iot.building.device.api.DeviceBusinessTypeApi;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.building.device.vo.DeviceDetailVo;
import com.iot.building.ifttt.api.AutoTobApi;
import com.iot.building.ifttt.vo.TriggerVo;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ota.api.OtaControlApi;
import com.iot.building.space.api.SpaceApi;
import com.iot.center.helper.BusinessExceptionEnum;
import com.iot.center.helper.Constants;
import com.iot.common.enums.APIType;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceDeviceVo;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;
import com.iot.device.api.CentralControlDeviceApi;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceExtendsCoreApi;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.api.DeviceStatusCoreApi;
import com.iot.device.api.DeviceTypeCoreApi;
import com.iot.device.api.OTAServiceApi;
import com.iot.device.vo.req.CommDeviceInfoReq;
import com.iot.device.vo.req.DevicePageReq;
import com.iot.device.vo.req.OtaPageReq;
import com.iot.device.vo.req.device.ListDeviceTypeReq;
import com.iot.device.vo.req.device.PageDeviceTypeByParamsReq;
import com.iot.device.vo.req.device.UpdateDeviceConditionReq;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import com.iot.device.vo.req.device.UpdateSetDeviceInfoReq;
import com.iot.device.vo.req.device.UpdateWrapperDeviceReq;
import com.iot.device.vo.req.ota.OtaFileInfoReq;
import com.iot.device.vo.req.ota.UpgradePlanReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.IftttDeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceExtendInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeByDeviceRespVo;
import com.iot.device.vo.rsp.device.ListDeviceTypeRespVo;
import com.iot.device.vo.rsp.ota.OtaFileInfoResp;
import com.iot.device.vo.rsp.ota.UpgradePlanResp;
import com.iot.file.api.LocalFileApi;
import com.iot.ifttt.api.IftttApi;
import com.iot.ifttt.vo.AppletItemVo;
import com.iot.ifttt.vo.AppletThisVo;
import com.iot.ifttt.vo.AppletVo;
import com.iot.user.vo.LoginResp;
import com.iot.util.AssertUtils;
import com.iot.util.GetBigFileMD5;

@Service
public class DeviceService {

	@Autowired
	SpaceApi spaceApi;
	@Autowired
	com.iot.control.space.api.SpaceApi commonSpaecApi;
	@Autowired
	DeviceCoreApi deviceApi;
	@Autowired
	DeviceStateCoreApi deviceStateCoreApi;
	@Autowired
	DeviceTypeCoreApi deviceTypeApi;
	@Autowired
	OTAServiceApi otaServiceApi;
	@Autowired
	OtaControlApi otaControlApi;
	@Autowired
	private LocalFileApi localFileApi;
	@Autowired
	private SpaceDeviceApi spaceDeviceApi;
	@Autowired
	DeviceBusinessTypeApi deviceBusinessTypeApi;
	@Autowired
	CentralControlDeviceApi centralControlDeviceApi;
	@Autowired
	com.iot.building.device.api.CentralControlDeviceApi controlDeviceApi;
	@Autowired
	private EnvironmentPropertyApi environmentPropertyApi;
	@Autowired
	DeviceStatusCoreApi deviceStatusCoreApi;
	@Autowired
	com.iot.control.space.api.SpaceApi spaceControlApi;
	@Autowired
	DeviceExtendsCoreApi deviceExtendsCoreApi;
	@Autowired
	private AutoTobApi autoTobApi;
	@Autowired
	private IftttApi iftttApi;
	@Autowired
	private SceneApi sceneApi;
	@Autowired
	private AirSwitchApi airSwitchApi;
	
	private final static Logger logger = LoggerFactory.getLogger(DeviceService.class);

	public static final String LOCAL_FILE_UPLOAD_PATH = "local.file.upload-path";

	public static final String HTTP_FILE_DOWN_PATH = "local.file.download-path";

	public static final Integer DEFAULT_PAGE_NUM = 1;

	public static final Integer DEFAULT_PAGE_SIZE = 100;

	public final static String SPACE_ROOM = "ROOM";// 房间

	public final static String SPACE_FLOOR = "FLOOR";// 楼层

	public final static String SPACE_BUILD = "BUILD";// 楼宇

	/**
	 * 子设备删除后删除对应的情景,ifttt
	 * @param deviceId
	 * @param tenantId
	 * @return
	 */
	public boolean deleteDeviceRelation(String deviceId, Long tenantId) {
		boolean flag = false;
		try{
			SceneDetailReq sceneDetailReq = new SceneDetailReq();
			sceneDetailReq.setDeviceId(deviceId);
			List<SceneDetailResp> list = sceneApi.sceneDetailByParam(sceneDetailReq);
			//删除情景，先删除sceneDetail
			sceneApi.deleteSceneDetailByDeviceId(sceneDetailReq);
			//再删除scene
			for (SceneDetailResp sceneDetailResp : list) {
				SceneReq sceneReq = new SceneReq();
				sceneReq.setId(sceneDetailResp.getSceneId());
				sceneReq.setCreateBy(sceneDetailResp.getCreateBy());
				sceneReq.setTenantId(sceneDetailResp.getTenantId());
				sceneApi.deleteScene(sceneReq);
			}
			//删除ifttt
			SaveIftttReq ruleVO = new SaveIftttReq();
			ruleVO.setSensorDeviceId(deviceId);
			ruleVO.setActuatorDeviceId(deviceId);
			List<TriggerVo> triggerList = autoTobApi.getTriggerTobListByDeviceId(ruleVO);
			if (triggerList != null) {
				for (TriggerVo triggerVo : triggerList) {
					if (triggerVo.getSensorDeviceId().equals(deviceId)) {
						triggerVo.setSensorProperties(null);
						triggerVo.setSensorType(null);
						triggerVo.setSensorPosition(null);
						triggerVo.setSensorDeviceId(null);
						//1,删除applet_item，通过itemId 和this or that
						AppletVo appletVo = iftttApi.get(triggerVo.getAppletId());
						for (AppletThisVo appletThisVo : appletVo.getThisList()) {
							for (AppletItemVo appletItemVo : appletThisVo.getItems()) {
								String jsonSensor = appletItemVo.getJson();
								Long itemId = appletItemVo.getId();
								if (!jsonSensor.contains("msg")) {//this 的情况
									JSONObject jsonObjectStr = JSON.parseObject(jsonSensor);
									String devId = (String) jsonObjectStr.get("devId");
									if (devId.equals(deviceId)) {
										iftttApi.delItem(itemId);
									}
								} else {
									continue;
								}
							}
						}
					} else if (triggerVo.getActuctorDeviceId().equals(deviceId)) {
						triggerVo.setActuctorProperties(null);
						triggerVo.setActuctorType(null);
						triggerVo.setActuctorPosition(null);
						triggerVo.setActuctorDeviceId(null);
						//1,删除applet_item，通过itemId 和this or that
						AppletVo appletVo = iftttApi.get(triggerVo.getAppletId());
						for (AppletThisVo appletThisVo : appletVo.getThisList()) {
							for (AppletItemVo appletItemVo : appletThisVo.getItems()) {
								String jsonActuctor = appletItemVo.getJson();
								Long itemId = appletItemVo.getId();
								if (jsonActuctor.contains("msg")) {//that 的情况
									Map map = jsonStringToMap(jsonActuctor);
									String msg = map.get("msg").toString();
									Map maps = (Map) JSON.parse(msg);
									String devId = (String) maps.get("deviceId");
									if (devId.equals(deviceId)) {
										iftttApi.delItem(itemId);
									}
								} else {
									continue;
								}
							}
						}
					}
					SaveIftttReq ruleVOStr = new SaveIftttReq();
					ruleVOStr.setId(triggerVo.getRuleId());
					ruleVOStr.setAppletId(triggerVo.getAppletId());
					List<TriggerVo> triggers = Lists.newArrayList();
					triggers.add(triggerVo);
					ruleVOStr.setTriggers(triggers);
					//2,删除 tob_trigger
					autoTobApi.saveTobTrigger(ruleVOStr, triggerVo.getRuleId());
				}
			}
			flag = true;
		}catch (Exception e){
			flag = false;
			e.printStackTrace();
		}finally {
			return flag;
		}
	}

	public static Map jsonStringToMap(String s){
		JSONObject jsonObject = JSON.parseObject(s);
		Map map=new HashMap();
		map = jsonObject;
		return map;
	}

	/**
	 * 
	 * 描述：更新设备信息
	 * 
	 * @author zhouzongwei
	 * @created 2017年11月29日 下午6:03:03
	 * @since
	 * @return
	 */
	public void updateDevice(UpdateDeviceInfoReq params) {
		deviceApi.saveOrUpdate(params);
	}

	public Map<String, Object> getDeviceProerty(String deviceId) {
		GetDeviceInfoRespVo vo = deviceApi.get(deviceId);
		Map<String,Object> property=deviceStateCoreApi.get(vo.getTenantId(), deviceId);
		GetDeviceStatusInfoRespVo online=deviceStatusCoreApi.get(vo.getTenantId(), deviceId);
		property.put("online", online.getOnlineStatus());
		return property;
	}

	/**
	 * 
	 * 描述：添加设备
	 * 
	 * @author zhouzongwei
	 * @created 2017年11月30日 上午10:40:13
	 * @since
	 * @param deviceReq
	 * @throws BusinessException
	 */
	public void saveDevice(UpdateDeviceInfoReq deviceReq) throws BusinessException {
		deviceApi.saveOrUpdate(deviceReq);
	}

	/**
	 * 
	 * 描述：查询直连设备列表
	 * 
	 * @author zhouzongwei
	 * @created 2017年11月27日 下午2:20:36
	 * @since
	 * @param pageReq
	 * @return
	 */
	public Page<DeviceResp> findDirectDevicePageToCenter(DevicePageReq pageReq) {
		Page<DeviceResp> pageResult = new Page<>();
		Page<DeviceResp> pageResultStr = new Page<>();
		pageResultStr = centralControlDeviceApi.findDirectDevicePageToCenter(pageReq);
		pageResult.setPageNum(pageResultStr.getPageNum());
		pageResult.setPageSize(pageResultStr.getPageSize());
		pageResult.setTotal(pageResultStr.getTotal());
		pageResult.setPages(pageResultStr.getPages());
		List<DeviceResp> userDeviceProductRespList = pageResultStr.getResult();
		for(DeviceResp deviceResp:userDeviceProductRespList){
			if(StringUtil.isBlank(deviceResp.getIp())){
				deviceResp.setIp("");
			}
		}
		pageResult.setResult(pageResultStr.getResult());
		return pageResult;
	}

	/**
	 * 
	 * 描述：查询非直连设备列表
	 * 
	 * @author zhouzongwei
	 * @created 2017年11月27日 下午2:20:36
	 * @since
	 * @param pageReq
	 * @return
	 */
	public Page<DeviceResp> findUnDirectDevicePage(DevicePageReq pageReq) {
		return centralControlDeviceApi.findUnDirectDevicePage(pageReq);
	}

	/**
	 * 
	 * 描述：删除设备
	 * 
	 * @author zhouzongwei
	 * @created 2017年11月27日 下午2:20:36
	 * @since
	 * @param deviceId
	 * @return
	 */
	public List<String> deleteDeviceByDeviceId(String deviceId) {
		return deviceApi.deleteByDeviceId(deviceId);
	}

	/**
	 * 
	 * 描述：获取设备相关信息
	 * 
	 * @author linjihuang
	 * @created 2018年11月8日 下午2:20:36
	 * @since
	 * @param deviceId
	 * @return
	 */
	public DeviceDetailVo getDeviceByDeviceId(Long orgId, Long tenantId, String deviceId) {
		DeviceDetailVo detailVo = new DeviceDetailVo();
		GetDeviceTypeByDeviceRespVo deviceTypeResp = deviceApi.getDeviceTypeByDeviceId(deviceId);
		if (deviceTypeResp != null) {
			if (deviceTypeResp.getDeviceInfo() != null) {
				// 收集设备基本信息
				GetDeviceExtendInfoRespVo extendInfoRespVo = deviceExtendsCoreApi.get(tenantId, deviceId);
				Long locationId = deviceTypeResp.getDeviceInfo().getLocationId();
				String ip = deviceTypeResp.getDeviceInfo().getIp() == null ? "" : deviceTypeResp.getDeviceInfo().getIp();
				if (extendInfoRespVo != null) {
					detailVo.setReportInterval(extendInfoRespVo.getReportInterval());
				}
				detailVo.setIp(ip);
				detailVo.setMac(deviceTypeResp.getDeviceInfo().getMac());
				detailVo.setDeviceId(deviceTypeResp.getDeviceInfo().getUuid());
				detailVo.setDeviceName(deviceTypeResp.getDeviceInfo().getName());
				detailVo.setVersion(deviceTypeResp.getDeviceInfo().getVersion());
				detailVo.setHwVersion(deviceTypeResp.getDeviceInfo().getHwVersion());
				if (deviceTypeResp.getDeviceInfo().getParentId() != null) {
					GetDeviceInfoRespVo getDeviceInfoRespVo = deviceApi
							.get(deviceTypeResp.getDeviceInfo().getParentId());
					if (getDeviceInfoRespVo != null) {
						detailVo.setParentId(getDeviceInfoRespVo.getUuid());
						detailVo.setParentName(getDeviceInfoRespVo.getName());
					}
				}
				if (deviceTypeResp.getDeviceInfo().getBusinessTypeId() != null) {
					DeviceBusinessTypeResp businessTypeResp = deviceBusinessTypeApi.findById(orgId, tenantId,
							deviceTypeResp.getDeviceInfo().getBusinessTypeId());
					if (businessTypeResp != null) {
						detailVo.setBusinessTypeId(businessTypeResp.getId());
						detailVo.setBusinessType(businessTypeResp.getBusinessType());
					}
				}
				GetDeviceStatusInfoRespVo deviceStatus = deviceStatusCoreApi
						.get(tenantId, deviceId);
				if (deviceStatus != null) {
					detailVo.setOnlineStatus(deviceStatus.getOnlineStatus());
				}
				// 收集设备归属 空间信息
				collectSpaceDeviceInfo(orgId, deviceId, detailVo, tenantId, locationId);
			}
			// 收集设备类型信息
			if (deviceTypeResp.getDeviceTypeInfo() != null) {
				detailVo.setDeviceTypeId(deviceTypeResp.getDeviceTypeInfo().getId());
				detailVo.setDeviceType(deviceTypeResp.getDeviceTypeInfo().getName());
			}
			// 收集设备产品信息
			if (deviceTypeResp.getProductInfo() != null) {
				detailVo.setProductId(deviceTypeResp.getProductInfo().getId());
				detailVo.setProductName(deviceTypeResp.getProductInfo().getProductName());
				detailVo.setModel(deviceTypeResp.getProductInfo().getModel());
			}
		}
		return detailVo;
	}

	private void collectSpaceDeviceInfo(Long orgId, String deviceId, DeviceDetailVo detailVo, Long tenantId, Long locationId) {
		SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
		spaceDeviceReq.setTenantId(tenantId);
		spaceDeviceReq.setOrgId(orgId);
		spaceDeviceReq.setLocationId(locationId);
		spaceDeviceReq.setDeviceId(deviceId);
		List<SpaceDeviceResp> spaceDeviceResps = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
		if (CollectionUtils.isNotEmpty(spaceDeviceResps) && spaceDeviceResps.size() > 0) {
			SpaceDeviceResp spaceDeviceResp = spaceDeviceResps.get(0);
			SpaceReq req = new SpaceReq();
			req.setTenantId(tenantId);
			req.setOrgId(orgId);
			req.setLocationId(locationId);
			req.setId(spaceDeviceResp.getSpaceId());
			setSpaceName(detailVo, req);
		}
	}

	private DeviceDetailVo setSpaceName(DeviceDetailVo detailVo, SpaceReq spaceReq) {
		List<SpaceResp> spaceResps = spaceControlApi.findSpaceByCondition(spaceReq);
		if (CollectionUtils.isNotEmpty(spaceResps) && spaceResps.size() > 0) {
			SpaceResp spaceResp = spaceResps.get(0);
			if (spaceResp.getType().equals(SPACE_ROOM)) {
				detailVo.setRoomId(spaceResp.getId());
				detailVo.setRoomName(spaceResp.getName());
			} else if (spaceResp.getType().equals(SPACE_FLOOR)) {
				detailVo.setFloorName(spaceResp.getName());
			} else if (spaceResp.getType().equals(SPACE_BUILD)) {
				detailVo.setBuildName(spaceResp.getName());
			}
			if (spaceResp.getParentId() != null) {
				spaceReq.setId(spaceResp.getParentId());
				setSpaceName(detailVo, spaceReq);
			}
		}
		return detailVo;
	}

	public void synchronization(Long orgId, String deviceId, Long tenantId, Long locationId) throws Exception {
		AssertUtils.notNull(orgId, "orgId.notnull");
		AssertUtils.notNull(tenantId, "tenantId.notnull");
		AssertUtils.notNull(locationId, "locationId.notnull");
		List<GetDeviceInfoRespVo> gatewayList = Lists.newArrayList();
		List<GetDeviceInfoRespVo> externalDeviceList = Lists.newArrayList();
		if (StringUtils.isBlank(deviceId)) {
			gatewayList = centralControlDeviceApi.findDirectDeviceListByVenderCode(tenantId, locationId,
					APIType.MultiProtocolGateway.name(),1);
			externalDeviceList = centralControlDeviceApi.findDirectDeviceListByVenderCode(tenantId, locationId,
					APIType.ManTunSci.name(),1);
		} else {
			GetDeviceInfoRespVo deviceResp = deviceApi.get(deviceId);
			if (deviceResp != null && deviceResp.getDeviceTypeId() != null) {
				if (Constants.getExternalDeviceType().containsKey(deviceResp.getDeviceTypeId().toString())) {
					externalDeviceList.add(deviceResp);
				}else {
					gatewayList.add(deviceResp);
				}
			}
		}
		// 同步网关设备
		for (GetDeviceInfoRespVo device : gatewayList) {
			controlDeviceApi.getDeviceList(device.getUuid());
		}
		// 同步外接设备
		for (GetDeviceInfoRespVo externalDevice : externalDeviceList) {
			if (externalDevice.getIp() != null) {
				airSwitchApi.synAirSwitch(externalDevice.getIp());
			}
		}
	}

	public List<DeviceResp> findAllUnDirectDeviceList(DevicePageReq pageReq) throws Exception {
		return centralControlDeviceApi.findAllUnDirectDeviceList(pageReq);
	}

	public List<Map<String, Object>> findIftttDeviceList(String iftttType, String name, Long spaceId) {
		LoginResp user = (LoginResp) SecurityUtils.getSubject().getPrincipal();
		List<String> deviceIds = Lists.newArrayList();

		// find deviceId through spaceId or spaceName
		if (spaceId != null) {
			deviceIds.addAll(getDeviceIdsBySpaceId(user.getOrgId(), user.getTenantId(), spaceId));
		} else if (!Strings.isNullOrEmpty(name)) {
			SpaceReq spaceReq = new SpaceReq();
			spaceReq.setName(name);
			spaceReq.setTenantId(user.getTenantId());
			spaceReq.setOrgId(user.getOrgId());
			List<SpaceResp> spaceRespList = spaceApi.getSpaceByCondition(spaceReq);
			if (CollectionUtils.isNotEmpty(spaceRespList)) {
				for (SpaceResp space : spaceRespList) {
					deviceIds.addAll(getDeviceIdsBySpaceId(user.getOrgId(), user.getTenantId(), space.getId()));
				}
			}
		}

		// deviceIdsStr.add("0");
		CommDeviceInfoReq req = new CommDeviceInfoReq();
		req.setIftttType(iftttType);
		req.setName(name);
		req.setDeviceIds(deviceIds);
		req.setTenantId(user.getTenantId());
		req.setOrgId(user.getOrgId());

		List<IftttDeviceResp> deviceRespList = centralControlDeviceApi.findIftttDeviceList(req);
		List<Map<String, Object>> result = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(deviceRespList)) {
			for (IftttDeviceResp deviceResp : deviceRespList) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("iftttType", iftttType);
				map.put("deviceId", deviceResp.getDeviceId());
				map.put("deviceName", deviceResp.getName());
				map.put("type", deviceResp.getDevType());

				result.add(map);
			}
		}
		return result;
	}

	private List<String> getDeviceIdsBySpaceId(Long orgId, Long tenantId, Long spaceId) {
		List<String> list = Lists.newArrayList();
		List<SpaceDeviceVo> deviceResps = spaceDeviceApi.findSpaceDeviceVOBySpaceId(tenantId, spaceId);
		if (CollectionUtils.isNotEmpty(deviceResps)) {
			for (SpaceDeviceVo spaceDevice : deviceResps) {
				list.add(spaceDevice.getDeviceId());
			}
		}
		return list;
	}

	public List<ListDeviceTypeRespVo> findDeviceTypeList() {
		ListDeviceTypeReq req = new ListDeviceTypeReq();
		return deviceTypeApi.listDeviceType(req);
	}

	public GetDeviceInfoRespVo getDeviceByDeviceIp(Long orgId, Long tenantId, @RequestParam("deviceIp") String deviceIp) {
		return centralControlDeviceApi.getDeviceByDeviceIp(orgId, tenantId, deviceIp);
	}

	public List<DeviceBusinessTypeResp> findBusinessTypeList(Long orgId, Long tenantId, String model) throws BusinessException {
		return deviceBusinessTypeApi.findBusinessTypeList(orgId,tenantId,model);
	}

	public Page<DeviceResp> queryAirCondition(DevicePageReq pageReq) {
		return centralControlDeviceApi.queryAirCondition(pageReq);
	}

	/**
	 * 解析excel数据
	 * 
	 * @param multipartRequest
	 * @param tenantId
	 * @return
	 */
	public List<String[]> fileUpload(MultipartHttpServletRequest multipartRequest, Long tenantId) {
		try {
			MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
			List<String[]> list = this.localFileApi.resolveExcel(multipartFile);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int addAclsToB(String deviceId) {
		return 0;
		// return deviceControlApi.addAclsToB(deviceId);
	}

	public void updateOtaVersion(Long orgId, String deviceId, Long tenantId, Long locationId) {
		otaControlApi.updateOtaVersion(orgId, deviceId, tenantId, locationId);
	}

	public Page<OtaFileInfoResp> getOtaFileList(OtaPageReq pageReq) {
		return otaControlApi.getOtaFileList(pageReq);
	}

	public void downLoadOtaFile(OtaFileInfoReq otaFileInfoReq) {
		otaControlApi.downLoadOtaFile(otaFileInfoReq);
	}

	public UpgradePlanResp getOtaLatestVersion(UpgradePlanReq upgradePlanReq) {
		return otaServiceApi.getUpgradePlan(upgradePlanReq);
	}

	public int otaFileUpload(MultipartHttpServletRequest multipartRequest, OtaFileInfoReq otaFileInfoReq) {
		if ((multipartRequest != null && !multipartRequest.getFileNames().hasNext())) {
			throw new BusinessException(BusinessExceptionEnum.UPLOAD_FILE_EMPTY);
		}
		int result = 0;
		try {
			MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
			String path = environmentPropertyApi.getPropertyValue(LOCAL_FILE_UPLOAD_PATH);
			String fileType = this.uploadFile(multipartFile, otaFileInfoReq, path);
			// 添加 ota_file_info表数据
			result = insertOrUpdateOtaFileInfo(otaFileInfoReq, fileType);
			// 更新同产品的设备最新版本号
			UpdateDeviceHwVersion(otaFileInfoReq);
			// centralControlDeviceApi.updateDeviceVersion(otaFileInfoReq);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 更新同产品的设备最新版本号
	 * 
	 * @param otaFileInfoReq
	 */
	private void UpdateDeviceHwVersion(OtaFileInfoReq otaFileInfoReq) {
		UpdateDeviceConditionReq params = new UpdateDeviceConditionReq();
		UpdateSetDeviceInfoReq setValueEntity = new UpdateSetDeviceInfoReq();
		UpdateWrapperDeviceReq wrapper = new UpdateWrapperDeviceReq();
		setValueEntity.setHwVersion(otaFileInfoReq.getVersion());
		wrapper.setProductId(otaFileInfoReq.getProductId());
		wrapper.setTenantId(otaFileInfoReq.getTenantId());
		wrapper.setLocationId(otaFileInfoReq.getLocationId());
		params.setSetValueEntity(setValueEntity);
		params.setWrapper(wrapper);
		deviceApi.updateByCondition(params);
	}

	private int insertOrUpdateOtaFileInfo(OtaFileInfoReq otaFileInfoReq, String fileType) {
		OtaFileInfoResp otaFileInfoResp = otaControlApi.findOtaFileInfoByProductId(otaFileInfoReq);
		String downloadPath = environmentPropertyApi.getPropertyValue(HTTP_FILE_DOWN_PATH)
				+ otaFileInfoReq.getTenantId() + File.separator + otaFileInfoReq.getLocationId() + File.separator
				+ otaFileInfoReq.getProductId() + fileType;
		File file = new File(otaFileInfoReq.getDownloadUrl());// 获取ota包
		String md5 = GetBigFileMD5.getMD5(file);// 进行md5加密
		if (StringUtils.isNotBlank(downloadPath)) {
			otaFileInfoReq.setDownloadUrl(downloadPath);
		}
		if (StringUtils.isNotBlank(md5)) {
			otaFileInfoReq.setMd5(md5);
		}
		int result = 0;
		if (otaFileInfoResp != null) {
			otaFileInfoReq.setId(otaFileInfoResp.getId());
			otaFileInfoReq.setUpdateTime(new Date());
			result = otaControlApi.updateOtaFileInfo(otaFileInfoReq);
		} else {
			otaFileInfoReq.setCreateTime(new Date());
			otaFileInfoReq.setUpdateTime(new Date());
			otaFileInfoReq.setCreateBy(otaFileInfoReq.getUpdateBy());
			result = otaControlApi.saveOtaFileInfo(otaFileInfoReq);
		}
		return result;
	}

	public String uploadFile(MultipartFile multipartFile, OtaFileInfoReq otaFileInfoReq, String path) {
		if (CommonUtil.isEmpty(multipartFile)) {
			throw new BusinessException(BusinessExceptionEnum.VIDEO_UPLOADFILE_ERROR);
		}
		String fileName = multipartFile.getOriginalFilename();
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		try {
			// path = path/ota/tenantId/productId.zip
			path = path + "ota" + File.separator + otaFileInfoReq.getTenantId() + File.separator
					+ otaFileInfoReq.getLocationId();
			File parentContextFile = new File(path);
			if (!parentContextFile.exists()) {
				parentContextFile.mkdirs();
			}
			path = path + File.separator + otaFileInfoReq.getProductId() + fileType;
			// 判断文件是否上传过 , 如果存在就删除旧文件
			File file = new File(path);
			if (file.exists()) {
				file.delete();
				file.createNewFile();
			}
			multipartFile.transferTo(new File(path));
			otaFileInfoReq.setDownloadUrl(path);
			return fileType;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 描述：获取设备类型列表
	 * 
	 * @author linjh
	 * @created 2018年11月6日 下午2:20:36
	 * @since
	 * @param params
	 * @return
	 */
	public List<ListDeviceTypeRespVo> getDeviceTypeIdList(PageDeviceTypeByParamsReq params) {
		List<ListDeviceTypeRespVo> listDeviceTypeRespVos = Lists.newArrayList();
		params.setPageNumber(DEFAULT_PAGE_NUM);
		params.setPageSize(DEFAULT_PAGE_SIZE);
		Page<ListDeviceTypeRespVo> page = deviceTypeApi.pageDeviceType(params);
		int nums = ((int) page.getTotal() / 100);
		listDeviceTypeRespVos.addAll(page.getResult());
		if (nums > 1) {
			for (int i = 2; i < nums + 2; i++) {
				params.setPageNumber(i);
				params.setPageSize(DEFAULT_PAGE_SIZE);
				page = deviceTypeApi.pageDeviceType(params);
				listDeviceTypeRespVos.addAll(page.getResult());
			}
		}
		return listDeviceTypeRespVos;
	}

	/**
     * 描述：获取直连设备
     *
     * @return
     * @author wl
     * @created 2018年11月9日 
     * @since
     */
	public List<GetDeviceInfoRespVo> getDirectDevice(DevicePageReq devicePageReq) {
		return centralControlDeviceApi.findDirectDeviceListByVenderCode(Long.valueOf(devicePageReq.getTenantId()),
				devicePageReq.getLocationId(), "",1);
	}
	
	/**
     * 描述：获取子设备列表
     *
     * @return
     * @author ljh
     * @created 2018年11月14日 
     * @since
     */
	public List<DeviceDetailVo> getDeviceListByParentId(CommDeviceInfoReq commDeviceInfoReq) {
		List<DeviceDetailVo> deviceDetailVos = Lists.newArrayList();
		List<GetDeviceInfoRespVo> devices = centralControlDeviceApi.getDeviceListByParentId(commDeviceInfoReq);
		if (CollectionUtils.isNotEmpty(devices)) {
			for (GetDeviceInfoRespVo getDeviceInfoRespVo : devices) {
				deviceDetailVos.add(getDeviceByDeviceId(commDeviceInfoReq.getOrgId(), commDeviceInfoReq.getTenantId(), getDeviceInfoRespVo.getUuid()));
			}
		}
		return deviceDetailVos;
	}

	/**
     * 描述：搜索子设备
     *
     * @return
     * @author ljh
     * @created 2018年11月13日 
     * @since
     */
	public void searchStar(CommDeviceInfoReq commDeviceInfoReq) {
		controlDeviceApi.searchStar(commDeviceInfoReq);
	}
	
	/**
     * 描述：停止搜索子设备
     *
     * @return
     * @author ljh
     * @created 2018年11月13日 
     * @since
     */
	public void searchStop(CommDeviceInfoReq commDeviceInfoReq) {
		controlDeviceApi.searchStop(commDeviceInfoReq);
	}
	
	/**
     * 描述：编辑网关信息
     *
     * @return
     * @author ljh
     * @created 2018年11月14日 
     * @since
     */
	public void editGatewayInfo(CommDeviceInfoReq commDeviceInfoReq) {
		controlDeviceApi.editGatewayInfo(commDeviceInfoReq);
	}
	
	/**
     * 描述：编辑子设备信息
     *
     * @return
     * @author ljh
     * @created 2018年11月14日 
     * @since
     */
	public void editDeviceInfo(CommDeviceInfoReq commDeviceInfoReq) {
		controlDeviceApi.editDeviceInfo(commDeviceInfoReq);
	}
	
	public void airSwitchSelfCheck(Long orgId, Long spaceId,Long tenantId) {
		List<SpaceResp> spaceList=commonSpaecApi.findChild(tenantId, spaceId);
		if(spaceId.compareTo(-1l)!=0) {
			SpaceResp spaceResp=commonSpaecApi.findSpaceInfoBySpaceId(tenantId, spaceId);
			spaceList.add(spaceResp);
		}
		List<Long> spaceIds=Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(spaceList)) {
			spaceList.forEach(space->{
				spaceIds.add(space.getId());
			});
			findSpaceDevice(orgId, tenantId, spaceIds);
		}
	}

	private void findSpaceDevice(Long orgId, Long tenantId, List<Long> spaceIds) {
		SpaceAndSpaceDeviceVo req=new SpaceAndSpaceDeviceVo();
		req.setTenantId(tenantId);
		req.setOrgId(orgId);
		req.setSpaceIds(spaceIds);
		List<SpaceDeviceResp> respList=spaceDeviceApi.findSpaceDeviceBySpaceIdsOrDeviceIds(req);
		List<Long> deviceTypeIds=Lists.newArrayList();
		Multimap<Long, String> deviceTypeMap = ArrayListMultimap.create();
		if(CollectionUtils.isNotEmpty(respList)) {
			respList.forEach(resp->{
				deviceTypeIds.add(resp.getDeviceTypeId());//准备批量查询设备类型
				deviceTypeMap.put(resp.getDeviceTypeId(), resp.getDeviceId());//保存设备类型有哪些设备
			});
			findDeviceTypeIdAndAirSwitchCheck(deviceTypeIds, deviceTypeMap); 
		}
	}

	private void findDeviceTypeIdAndAirSwitchCheck(List<Long> deviceTypeIds, Multimap<Long, String> deviceTypeMap) {
		//批量查询设备类型
		ListDeviceTypeReq params=new  ListDeviceTypeReq();
		params.setDeviceTypeIds(deviceTypeIds);
		List<ListDeviceTypeRespVo> voList=deviceTypeApi.listDeviceType(params);
		List<String> deviceIdList=Lists.newArrayList();
		logger.info("deviceIdList-----------------"+voList.size());
		if(CollectionUtils.isNotEmpty(voList)) {
			//循环判断设备类型找到设备空开自检
			voList.forEach(vo->{
				if(vo.getVenderFlag() !=null && vo.getVenderFlag().equals(APIType.ManTunSci.name())) {
					logger.info("deviceIdList-----------------"+voList.size());
					 Collection<String> deviceIds= deviceTypeMap.get(vo.getId());
					 Iterator<String> it = deviceIds.iterator();
			         while(it.hasNext()){
			        	 deviceIdList.add(it.next());
			         }
				}
			});
			logger.info("airSwitchApi--------start---------"+deviceIdList.size());
			airSwitchApi.batchLeakageTest(deviceIdList);
			logger.info("airSwitchApi--------end---------"+deviceIdList.size());
		}
	}
}

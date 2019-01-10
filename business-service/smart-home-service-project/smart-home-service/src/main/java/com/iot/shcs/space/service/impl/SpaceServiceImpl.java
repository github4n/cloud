package com.iot.shcs.space.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceReqVo;
import com.iot.control.space.vo.SpaceReq;
import com.iot.device.api.ProductConfigNetmodeApi;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.vo.req.DeviceParamReq;
import com.iot.device.vo.rsp.DeviceExtendResp;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.ProductConfigNetmodeRsp;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceExtendRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.file.api.FileApi;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.device.service.impl.DeviceService;
import com.iot.shcs.device.utils.DeviceCoreUtils;
import com.iot.shcs.helper.Constants;
import com.iot.shcs.scene.service.SceneService;
import com.iot.shcs.space.enums.SpaceEnum;
import com.iot.shcs.space.exception.SpaceExceptionEnum;
import com.iot.shcs.space.service.ISpaceService;
import com.iot.shcs.space.util.BeanChangeUtil;
import com.iot.shcs.space.util.BeanCopyUtil;
import com.iot.shcs.space.vo.DeviceVo;
import com.iot.shcs.space.vo.RoomSpaceVo;
import com.iot.shcs.space.vo.SpaceDeviceResp;
import com.iot.shcs.space.vo.SpaceDeviceVo;
import com.iot.shcs.space.vo.SpaceReqVo;
import com.iot.shcs.space.vo.SpaceResp;
import com.iot.user.api.UserApi;
import com.iot.util.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("spaceService")
public class SpaceServiceImpl implements ISpaceService {

	private final static Logger LOGGER = LoggerFactory.getLogger(SpaceServiceImpl.class);

	@Autowired
	private SpaceApi spaceApi;

	@Autowired
	private SpaceDeviceApi spaceDeviceService;


	@Autowired
	private DeviceCoreService deviceCoreService;

	@Autowired
	private ProductCoreApi productCoreApi;

	@Autowired
	private DeviceService deviceService;



	@Autowired
	private SceneService sceneService;


	@Autowired
	private UserApi userApi;

	private Environment dnvironment;

	@Autowired
	private FileApi fileApi;

	@Autowired
	private ProductConfigNetmodeApi productConfigNetmodeApi;

	/**
	 * 新建空间
	 *
	 * @param spaceReq
	 * @return
	 * @author wanglei
	 */
	@Transactional
	public Long save(com.iot.shcs.space.vo.SpaceReq spaceReq) {
		SpaceReq req = BeanCopyUtil.copyProperties(spaceReq);
		return spaceApi.save(req);
	}

	/**
	 * 更新空间
	 *
	 * @param spaceReq
	 * @return
	 * @author wanglei
	 */
	@Transactional
	public void update(com.iot.shcs.space.vo.SpaceReq spaceReq) {
		SpaceReq req = BeanCopyUtil.copyProperties(spaceReq);
		spaceApi.update(req);
	}

	@Transactional
	public boolean updateSpaceByCondition(SpaceReqVo reqVo){
		com.iot.control.space.vo.SpaceReqVo req = BeanCopyUtil.copyProperties(reqVo);
		return spaceApi.updateSpaceByCondition(req);
	}

	/**
	 * @Description: 通过Id删除spsce
	 *
	 * @param spaceId
	 * @return: boolean
	 * @author: chq
	 * @date: 2018/10/11 16:22
	 **/
	@Transactional
	public boolean deleteSpaceBySpaceId(Long tenantId, Long spaceId){
		return spaceApi.deleteSpaceBySpaceId(tenantId, spaceId);
	}

	/**
	 * @Description: 批量删除space
	 *
	 * @param reqVo
	 * @return: boolean
	 * @author: chq
	 * @date: 2018/10/9 15:05
	 **/
	@Transactional
	public boolean deleteSpaceByIds(com.iot.shcs.space.vo.SpaceAndSpaceDeviceVo reqVo){
		SpaceAndSpaceDeviceVo req = BeanCopyUtil.copyProperties(reqVo);
		return spaceApi.deleteSpaceByIds(req);
	}

	/**
	 * @Description: 通过spaceId获取space详情
	 *
	 * @param spaceId
	 * @return: com.iot.control.space.vo.SpaceResp
	 * @author: chq
	 * @date: 2018/10/11 17:18
	 **/
	@Override
	public SpaceResp findSpaceInfoBySpaceId(Long tenantId, Long spaceId) {
		com.iot.control.space.vo.SpaceResp resp = spaceApi.findSpaceInfoBySpaceId(tenantId, spaceId);
		return BeanCopyUtil.copyProperties(resp);
	}

	@Override // 房间列表获取
	public List<SpaceResp> findSpaceByParentId(com.iot.shcs.space.vo.SpaceReq space) {
		SpaceReq req = BeanCopyUtil.copyProperties(space);
		List<com.iot.control.space.vo.SpaceResp> resps = spaceApi.findSpaceByParentId(req);
		return BeanCopyUtil.copyProperties(resps);
	}

	/**
	 * @Description: 批量获取空间详细信息
	 *
	 * @param reqVo
	 * @return: java.util.List<com.iot.control.space.vo.SpaceResp>
	 * @author: chq
	 * @date: 2018/10/9 15:31
	 **/
	@Override
	public List<SpaceResp> findSpaceInfoBySpaceIds(com.iot.shcs.space.vo.SpaceAndSpaceDeviceVo reqVo){
		SpaceAndSpaceDeviceVo req = BeanCopyUtil.copyProperties(reqVo);
		List<com.iot.control.space.vo.SpaceResp> resps = spaceApi.findSpaceInfoBySpaceIds(req);
		return BeanCopyUtil.copyProperties(resps);
	}

	/**
	 * @Description: 条件查询space
	 *
	 * @param spaceReq
	 * @return: java.util.List<com.iot.control.space.vo.SpaceResp>
	 * @author: chq
	 * @date: 2018/10/9 17:29
	 **/
	@Override
	public List<SpaceResp> findSpaceByCondition(com.iot.shcs.space.vo.SpaceReq spaceReq){
		SpaceReq req = BeanCopyUtil.copyProperties(spaceReq);
		List<com.iot.control.space.vo.SpaceResp>resp = spaceApi.findSpaceByCondition(req);
		return BeanCopyUtil.copyProperties(resp);
	}

	@Override
	public int countSpaceByCondition(com.iot.shcs.space.vo.SpaceReq spaceReq){
		SpaceReq req = BeanCopyUtil.copyProperties(spaceReq);
		return spaceApi.countSpaceByCondition(req);
	}

	@Override
	public boolean checkSpaceName(com.iot.shcs.space.vo.SpaceReq spaceReq) {
		AssertUtils.notNull(spaceReq, "space.notnull");
		boolean flag = false;
		Long tenantId = spaceReq.getTenantId();
		//判断当前名称和旧名称是否一致，一致则直接返回，不一致检查名称
		if (spaceReq.getId() != null) {
			com.iot.control.space.vo.SpaceResp space = spaceApi.findSpaceInfoBySpaceId(tenantId, spaceReq.getId());
			if (space != null && space.getName().equals(spaceReq.getName())) {
				return flag;
			}
		}

		SpaceReq req = BeanCopyUtil.copyProperties(spaceReq);
		req.setId(null);
		Integer repeatCount = spaceApi.countSpaceByCondition(req);
		LOGGER.info("***** repeatCount={}", repeatCount);
		if (repeatCount != null && repeatCount > 0) {
			flag = true;
		}
		return flag;
	}


	/**
	 * 根据ID删除空间
	 *
	 * @param spaceId
	 * @param userId
	 * @return
	 * @author wanglei
	 */
	public void deleteSpaceBySpaceIdAndUserId(Long spaceId, Long userId, Long tenantId) throws BusinessException {
		if (spaceId == null) {
			throw new BusinessException(SpaceExceptionEnum.SPACE_ID_IS_NULL);
		}
		SpaceResp space = this.findSpaceInfoBySpaceId(tenantId, spaceId);
		if (space == null) {
			throw new BusinessException(SpaceExceptionEnum.SPACE_IS_NULL);
		}
		// 查询默认家庭
		SpaceResp defaultSpace = findUserDefaultSpace(userId, tenantId);
		Long defaultSpaceId = defaultSpace.getId();

		SpaceDeviceReqVo spaceDeviceReqVo = new SpaceDeviceReqVo();
		SpaceDeviceReq setValue = new SpaceDeviceReq();
		SpaceDeviceReq requst = new SpaceDeviceReq();
		setValue.setSpaceId(defaultSpaceId);
		setValue.setTenantId(tenantId);
		requst.setTenantId(tenantId);
		requst.setSpaceId(spaceId);
		spaceDeviceReqVo.setSetValueParam(setValue);
		spaceDeviceReqVo.setRequstParam(requst);
		spaceDeviceService.updateSpaceDeviceByCondition(spaceDeviceReqVo); // 将设备的space转到默认家庭
		spaceApi.deleteSpaceBySpaceId(tenantId, spaceId);
	}

	@Override // 家庭列表获取
	//TODO 缓存处理
	public List<SpaceResp> findSpaceByType(com.iot.shcs.space.vo.SpaceReq spaceReq) {
		SpaceReq req = BeanCopyUtil.copyProperties(spaceReq);
		List<com.iot.control.space.vo.SpaceResp> resp = spaceApi.findSpaceByCondition(req);
		return BeanCopyUtil.copyProperties(resp);
	}

	@Override
	public SpaceResp findUserDefaultSpace(Long userId, Long tenantId) {
		com.iot.shcs.space.vo.SpaceReq space = new com.iot.shcs.space.vo.SpaceReq();
		SpaceResp resp = null;
		space.setDefaultSpace(1);
		space.setUserId(userId);
		space.setTenantId(tenantId);
		space.setType(SpaceEnum.HOME.getCode());
		List<SpaceResp> spaceList = this.findSpaceByCondition(space);
		if (CollectionUtils.isNotEmpty(spaceList)) {
			resp = spaceList.get(0);
		}
		return resp;
	}

//	====================================old ================

	/**
	 * 获取家庭下的设备列表
	 *
	 * @param userId
	 *            用户id
	 * @param spaceDeviceVOList
	 *            space与设备的关联实体
	 * @return
	 */
	public List<Map<String, Object>> getRoomDeviceList(Long tenantId, Long userId, List<SpaceDeviceVo> spaceDeviceVOList) {
		List<Map<String, Object>> deviceMapList = new ArrayList<>();
		if (CollectionUtils.isEmpty(spaceDeviceVOList)) {
			LOGGER.debug("设备列表为空！");
			return Lists.newArrayList();
		}
		// 设备id列表
		List<String> deviceIdList = Lists.newArrayList();
		if (spaceDeviceVOList != null && spaceDeviceVOList.size() > 0) {
			for (SpaceDeviceVo spaceDeviceVO : spaceDeviceVOList) {
				deviceIdList.add(spaceDeviceVO.getDeviceId());
			}
		}

		// 根据 设备id列表 获取 设备实体列表
		List<DeviceResp> deviceList = null;
		if (!CollectionUtils.isEmpty(deviceIdList)) {
			deviceList = deviceCoreService.findDeviceListByUserId(tenantId, userId, deviceIdList);
			log.debug("getRoomDeviceList-by-device:{}", JSON.toJSONString(deviceList));
		}

		if (!CollectionUtils.isEmpty(deviceList)) {
			Map<String, DeviceResp> deviceRespMap = new HashMap();
			for (DeviceResp deviceInfo : deviceList) {
				deviceRespMap.put(deviceInfo.getDeviceId(), deviceInfo);
			}
			for (SpaceDeviceVo vo : spaceDeviceVOList) {
				DeviceResp device = deviceRespMap.get(vo.getDeviceId());
				if (device == null) {
					continue;
				}
				// 获取设备属性
				Map<String, Object> property = device.getStateProperty();
				LOGGER.debug("getRoomDeviceList-get-deviceId {} property:{}", device.getDeviceId(), property);
				Long homeId = (vo.getParentId() == -1 || vo.getParentId() == null) ? vo.getSpaceId() : vo.getParentId();
				Long roomId = (vo.getParentId() == -1 || vo.getParentId() == null) ? 0L : vo.getSpaceId();
				//获取产品配网模式
				List<String> comModes = Lists.newArrayList();
				List<ProductConfigNetmodeRsp> configNetmodeRsps = device.getConfigNetmodeRsps();
				if(CollectionUtils.isNotEmpty(configNetmodeRsps)) {
					configNetmodeRsps.forEach(configNetmode -> {
						comModes.add(configNetmode.getName());
					});
				}
//				List<ProductConfigNetmodeRsp> list = productConfigNetmodeApi.listByProductId(device.getProductId());
//				list.forEach(m->{
//					comModes.add(m.getName());
//				});

				String devType = device.getDevType();
				Map<String, Object> deviceMap = BeanChangeUtil.deviceToMap(device);
				deviceMap.put("homeId", homeId != null ? homeId.toString() : "");
				deviceMap.put("roomId", roomId != null ? roomId.toString() : "");
				deviceMap.put("catalogId", device.getDeviceCatalogId());
				deviceMap.put("devType", devType);
				deviceMap.put("attr", property);
				deviceMap.put("address", device.getAddress());
				deviceMap.put("comMode", comModes);
				deviceMapList.add(deviceMap);
			}
		}
		return deviceMapList;
	}


	/**
	 * 根据家庭和用户获取设备列表
	 *
	 * @param userId
	 * @param homeId
	 * @return payload
	 */
	public Map<String, Object> getHomeDevListAndCount(Long tenantId, Long userId, Long homeId) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		Date startTime = new Date();
		LOGGER.debug("getHomeDevListAndCount({},{}, {})-startTime:{}", tenantId, userId, homeId,
				DateFormatUtils.format(startTime, "yyyy-MM-dd HH:mm:ss"));

		// 查询 SpaceDeviceVO 列表
		List<com.iot.control.space.vo.SpaceDeviceVo> spaceDeviceVOList = spaceDeviceService.findSpaceDeviceVOBySpaceId(tenantId, homeId);
		List<SpaceDeviceVo> spaceDeviceVOs = BeanCopyUtil.copySpaceDeviceVo(spaceDeviceVOList);
		// 获取家庭下的设备列表
		List<Map<String, Object>> deviceMapList = getRoomDeviceList(tenantId, userId, spaceDeviceVOs);
		int totalCount = 0;
		if (!org.springframework.util.CollectionUtils.isEmpty(deviceMapList)) {
			totalCount = deviceMapList.size();
		}
		// 封装返回的数据
		Map<String, Object> payload = new HashMap<>();
		payload.put("totalCount", totalCount);
		payload.put("dev", deviceMapList);

		Date endTime = new Date();

		Long milliseconds = endTime.getTime() - startTime.getTime();
		LOGGER.debug("getHomeDevListAndCount({}, {})-endTime:{},milliseconds:{}", userId, homeId,
				DateFormatUtils.format(endTime, "yyyy-MM-dd HH:mm:ss"), milliseconds);
		double time = stopWatch.getTime();
		if (time > 1000) {
			LOGGER.info("getDevList ({},{}, {}): {} ms", tenantId, userId, homeId, time);
		}

		return payload;
	}


	public List<DeviceVo> getUserUnMountDevice(Long tenantId, Long spaceId, Long userId, Integer room) {
		SpaceResp space = this.findSpaceInfoBySpaceId(tenantId, spaceId);
		List<String> deviceIds = new ArrayList<>();
		Map<String, Integer> deviceIdsOrderMap = Maps.newHashMap();
		List<DeviceResp> deviceParamResp = null;
		RoomSpaceVo roomSpaceVo = new RoomSpaceVo();
		roomSpaceVo.setUserId(userId);
		roomSpaceVo.setHomeId(spaceId);
		if (space != null && space.getType() != null && space.getType().equals(Constants.SPACE_HOME)) {// 类型必须是HOME类型

			List<com.iot.control.space.vo.SpaceDeviceVo> spaceDeviceVOList = spaceDeviceService.findSpaceDeviceVOBySpaceId(tenantId, spaceId);
			List<SpaceDeviceVo> spaceDeviceList = BeanCopyUtil.copySpaceDeviceVo(spaceDeviceVOList);
			if (CollectionUtils.isNotEmpty(spaceDeviceList)) {
				spaceDeviceList.forEach(spaceDevice -> {
					if (room == 0) {
						if (spaceDevice.getDefaultSpace()==1) {
							deviceIds.add(spaceDevice.getDeviceId());
							deviceIdsOrderMap.put(spaceDevice.getDeviceId(), spaceDevice.getOrder());
						}
					} else {
						deviceIds.add(spaceDevice.getDeviceId());
						deviceIdsOrderMap.put(spaceDevice.getDeviceId(), spaceDevice.getOrder());
					}
				});
			}
			if (!CollectionUtils.sizeIsEmpty(deviceIds)) {
				deviceParamResp = deviceCoreService.findDeviceListByDeviceIds(tenantId, deviceIds);
			}
		}
		return deviceParamRespToDeviceVoList(deviceIdsOrderMap, deviceParamResp);
	}

	private List<DeviceVo> deviceParamRespToDeviceVoList(Map<String, Integer> deviceIdsOrderMap,
														 List<DeviceResp> deviceInfoRespVoList) {
		List<DeviceVo> deviceList = new ArrayList<>();
		if (!org.springframework.util.CollectionUtils.isEmpty(deviceInfoRespVoList)) {
			deviceInfoRespVoList.forEach(deviceResp -> {
				DeviceVo vo = new DeviceVo();
				BeanUtil.copyProperties(deviceResp, vo);
				vo.setOrder(deviceIdsOrderMap.get(vo.getDeviceId()));
				deviceList.add(vo);
			});
		}

		return deviceList;
	}

	public List<DeviceVo> getDeviceByUserRoom(Long tenantId, Long userId, Long spaceId) {
		List<DeviceResp> deviceParamResp = null;
		List<String> deviceIds = new ArrayList<>();
		Map<String, Integer> deviceIdsOrderMap = Maps.newHashMap();
		List<com.iot.control.space.vo.SpaceDeviceVo> spaceDeviceVOList = spaceDeviceService.findSpaceDeviceVOBySpaceId(tenantId, spaceId);
		List<SpaceDeviceVo> spaceDeviceList = BeanCopyUtil.copySpaceDeviceVo(spaceDeviceVOList);

		if (CollectionUtils.isNotEmpty(spaceDeviceList)) {
			spaceDeviceList.forEach(spaceDevice -> {
				deviceIds.add(spaceDevice.getDeviceId());
				deviceIdsOrderMap.put(spaceDevice.getDeviceId(), spaceDevice.getOrder());
			});
		}
		if (!CollectionUtils.sizeIsEmpty(deviceIds)) {
			deviceParamResp = deviceCoreService.findDeviceListByUserId(tenantId, userId, deviceIds);
		}
		return deviceParamRespToDeviceVoList(deviceIdsOrderMap, deviceParamResp);
	}


	/**
	 * 根据 deviceUuid 获取 SpaceDeviceResp
	 *
	 * @param deviceUuid
	 * @return
	 */
	@Override
	public SpaceDeviceResp getSpaceDeviceByDeviceUuid(String deviceUuid, Long tenantId) {
		AssertUtils.notEmpty(deviceUuid, "deviceUuid.notnull");
		AssertUtils.notNull(tenantId, "tenantId.notnull");

		SpaceDeviceResp spaceDeviceResp = null;
		SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
		spaceDeviceReq.setDeviceId(deviceUuid);
		spaceDeviceReq.setTenantId(tenantId);
		List<com.iot.control.space.vo.SpaceDeviceResp> spaceDeviceRespList = spaceDeviceService.findSpaceDeviceByCondition(spaceDeviceReq);

		if (CollectionUtils.isNotEmpty(spaceDeviceRespList)) {
			com.iot.control.space.vo.SpaceDeviceResp resp  = spaceDeviceRespList.get(0);
			spaceDeviceResp = BeanCopyUtil.copyProperties(resp);
		}

		return spaceDeviceResp;
	}

	@Override
	public Integer getDeviceSeqByUserId(Long tenantId, Long userId) {
		Integer seq = 0;
		SpaceResp spaceResp = findUserDefaultSpace(userId, tenantId);
		if(spaceResp == null){
			throw new BusinessException(SpaceExceptionEnum.USER_NOT_DEFAULT_SPACE);
		}
		Integer oldSeq = spaceResp.getSeq() == null ? 0 : spaceResp.getSeq();
		seq = oldSeq + 1;
		try {
			com.iot.shcs.space.vo.SpaceReq space = BeanCopyUtil.spaceRespToSpaceReq(spaceResp);
			space.setSeq(seq);
			this.update(space);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("查找设备序列号异常", e);
		}
		return seq;
	}


	/**
	 * 获取 spaceId 关联的 直接设备id
	 *
	 * @param spaceId
	 * @return
	 */
	@Override
	public List<String> getDirectDeviceUuidBySpaceId(Long tenantId, Long spaceId) {
		// 直连设备deviceId
		List<String> resultDeviceUuidList = Lists.newArrayList();
		// 查询 SpaceDeviceVO 列表
		List<com.iot.control.space.vo.SpaceDeviceVo> spaceDeviceVOList = spaceDeviceService.findSpaceDeviceVOBySpaceId(tenantId, spaceId);
		List<SpaceDeviceVo> spaceDeviceList = BeanCopyUtil.copySpaceDeviceVo(spaceDeviceVOList);

		if (CollectionUtils.isNotEmpty(spaceDeviceList)) {
			// 包含了 网关+子设备
			List<String> deviceUuidList = Lists.newArrayList();
			for (SpaceDeviceVo resp : spaceDeviceList) {
				deviceUuidList.add(resp.getDeviceId());
			}

			// 构建参数 获取 List<DeviceResp>
			DeviceParamReq paramReq = new DeviceParamReq();
			paramReq.setDeviceIdList(deviceUuidList);
			paramReq.setCheckUserNotNull(false);

			List<ListDeviceInfoRespVo> deviceRespList = deviceCoreService.listDevicesByDeviceIds(deviceUuidList);

			if (CollectionUtils.isNotEmpty(deviceRespList)) {

				for (ListDeviceInfoRespVo resp : deviceRespList) {
					if (resp.getIsDirectDevice() != null
							&& resp.getIsDirectDevice().intValue() == Constants.IS_DIRECT_DEVICE) {
						GetProductInfoRespVo productResp = productCoreApi.getByProductId(resp.getProductId());
						if (DeviceCoreUtils.isGateWayProduct(productResp)) {
							resultDeviceUuidList.add(resp.getUuid());
						}
					}
				}
			}
		}

		return resultDeviceUuidList;
	}


}

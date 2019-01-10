package com.iot.building.space.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.device.service.IDeviceBusinessTypeService;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.building.space.service.IBuildingSpaceDeviceService;
import com.iot.common.exception.BusinessException;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceTypeCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceTypeByDeviceRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;
import com.iot.util.AssertUtils;

import springfox.documentation.spring.web.json.Json;

/**
 * @description:
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/10/09 10:33
 **/
@Service
@Transactional
public class BuildingSpaceDeviceServiceImpl implements IBuildingSpaceDeviceService {

	private final static Logger LOGGER = LoggerFactory.getLogger(BuildingSpaceDeviceServiceImpl.class);

	@Autowired
	private SpaceDeviceApi spaceDeviceApi;

	@Autowired
	private DeviceCoreApi deviceCoreApi;

	@Autowired
	private DeviceTypeCoreApi deviceTypeCoreApi;

	@Autowired
	private IDeviceBusinessTypeService businessTypeService;

	/**
	 * @Description: 条件查找spaceDevice
	 *
	 * @param spaceDeviceReq
	 * @return: java.util.List<com.iot.control.space.vo.SpaceDeviceResp>
	 * @author: chq
	 * @date: 2018/10/9 21:20
	 **/
	public List<SpaceDeviceResp> findSpaceDeviceByCondition(SpaceDeviceReq spaceDeviceReq) {
		List<SpaceDeviceResp> spaceDeviceList = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
		return spaceDeviceList;
	}

	/**
	 * @Description: 条件查找spaceDevice
	 * @param spaceDeviceReq
	 * @return: java.util.List<com.iot.control.space.vo.SpaceDeviceResp>
	 * @author: chq
	 * @date: 2018/10/9 21:20
	 **/
	public List<SpaceDeviceResp> countSpaceDeviceByCondition(SpaceDeviceReq spaceDeviceReq) {
		List<SpaceDeviceResp> spaceDeviceList = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
		return spaceDeviceList;
	}

	@Override
	public List<Map<String, Object>> getDeviceListBySpace(SpaceDeviceReq spaceDeviceReq) {
		AssertUtils.notNull(spaceDeviceReq, "spaceDeviceReq.notnull");
		AssertUtils.notNull(spaceDeviceReq.getSpaceId(), "spaceId.notnull");
		AssertUtils.notNull(spaceDeviceReq.getTenantId(), "tenantId.notnull");
		List<Map<String, Object>> results = Lists.newArrayList();
		List<SpaceDeviceResp> spaceDeviceResps = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
		if (CollectionUtils.isNotEmpty(spaceDeviceResps)) {
			for (SpaceDeviceResp spaceDeviceResp : spaceDeviceResps) {
				Map<String, Object> map = Maps.newHashMap();
				map = JSON.parseObject(JSON.toJSONString(spaceDeviceResp), Map.class);
				if (MapUtils.isNotEmpty(map)) {
					if (map.get("deviceId") != null) {
						GetDeviceTypeByDeviceRespVo deviceInfo = deviceCoreApi.getDeviceTypeByDeviceId(map.get("deviceId").toString());
					    if (deviceInfo != null) {
							if (deviceInfo.getDeviceInfo() != null) {
								if (deviceInfo.getDeviceInfo().getName() != null) {
									map.put("name", deviceInfo.getDeviceInfo().getName());
								}
							}
							if (deviceInfo.getProductInfo() != null) {
								if (deviceInfo.getProductInfo().getProductName() != null) {
									map.put("type", deviceInfo.getProductInfo().getProductName());
								}
							}
						}
					}
					if (map.get("businessTypeId") != null) {
						DeviceBusinessTypeResp businessTypeResp = businessTypeService
								.findById(spaceDeviceResp.getOrgId(),spaceDeviceResp.getTenantId(),Long.valueOf(map.get("businessTypeId").toString()));
						map.put("businessType", businessTypeResp.getBusinessType());
					}
					results.add(map);
				}
			}
		}
		return results;
	}
}

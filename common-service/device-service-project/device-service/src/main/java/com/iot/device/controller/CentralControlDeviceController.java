package com.iot.device.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.device.api.CentralControlDeviceApi;
import com.iot.device.business.DeviceBusinessService;
import com.iot.device.core.service.DeviceServiceCoreUtils;
import com.iot.device.exception.DeviceExceptionEnum;
import com.iot.device.model.Device;
import com.iot.device.service.IDeviceService;
import com.iot.device.service.IDeviceStatusService;
import com.iot.device.service.IDeviceTypeService;
import com.iot.device.service.IProductService;
import com.iot.device.vo.req.CommDeviceInfoReq;
import com.iot.device.vo.req.DeviceBusinessTypeIDSwitchReq;
import com.iot.device.vo.req.DevicePageReq;
import com.iot.device.vo.req.device.PageDeviceInfoReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.IftttDeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.PageDeviceInfoRespVo;
import com.iot.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 前端控制器
 *
 * @author lucky
 * @since 2018-04-12
 */
@RestController
public class CentralControlDeviceController implements CentralControlDeviceApi {

	public static final Logger LOGGER = LoggerFactory.getLogger(CentralControlDeviceController.class);
	@Autowired
	private IDeviceService deviceService;

	@Autowired
	private IDeviceTypeService deviceTypeService;

	@Autowired
	private IDeviceStatusService deviceStatusService;

	@Autowired
	private IProductService productService;

	@Autowired
	private DeviceBusinessService deviceBusinessService;

	public Page<DeviceResp> findDirectDevicePageToCenter(@RequestBody DevicePageReq pageReq) {

		return deviceService.findDirectDevicePageToCenter(pageReq);
	}

	public Page<DeviceResp> findUnDirectDevicePage(@RequestBody DevicePageReq pageReq) {

		return deviceService.findUnDirectDevicePage(pageReq);
	}

	public List<GetDeviceInfoRespVo> findDirectDeviceListByVenderCode(@RequestParam("tenantId") Long tenantId,
			@RequestParam("locationId") Long locationId,@RequestParam("venderFlag") String venderFlag,
			@RequestParam("isDirectDevice") Integer isDirectDevice) {
		return deviceService.findDirectDeviceListByVenderCode(tenantId, locationId,venderFlag,isDirectDevice);
	}

	@Transactional
	public List<String> deleteDeviceByDeviceId(@RequestParam("deviceId") String deviceId) {
		try {
			List<String> deletesIds = new ArrayList<>();
			AssertUtils.notEmpty(deviceId, "deviceId.notnull");
			EntityWrapper<Device> wrapper = new EntityWrapper<>();
			wrapper.eq("uuid", deviceId);
			Device orig = deviceService.selectOne(wrapper);
			// DeviceServiceCoreUtils.getDeviceInfoByDeviceId(deviceId);
			if (orig == null) {
				throw new BusinessException(DeviceExceptionEnum.DEVICE_NOT_EXIST);
			}
			deviceService.deleteById(orig.getId());
			// 判断有没有子设备 有的话一起删除子设备
			List<DeviceResp> deviceResps = deviceService.findDeviceListByParentId(deviceId);
			deletesIds.add(deviceId);
			// 删除子设备
			if (deviceResps != null && deviceResps.size() > 0) {
				List<Long> ids = deviceResps.stream().map(one -> one.getId()).collect(Collectors.toList());
				deletesIds.addAll(deviceResps.stream().map(one -> one.getDeviceId()).collect(Collectors.toList()));
				deviceService.deleteBatchIds(ids);
			}
			// 删除该设备的缓存
			DeviceServiceCoreUtils.removeDeviceByDeviceId(deviceId);
			// 删除该设备下的全部子设备缓存
			DeviceServiceCoreUtils.removeCacheParentDeviceId(deviceId);
			return deletesIds;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<DeviceResp> findAllUnDirectDeviceList(@RequestBody DevicePageReq pageReq) {
		return deviceService.findAllUnDirectDeviceList(pageReq);
	}

	public List<DeviceResp> findUnDirectDeviceListByParentDeviceId(
			@RequestParam("parentDeviceId") String parentDeviceId) {

		return deviceService.findDeviceListByParentId(parentDeviceId);
	}

	@Override
	public List<IftttDeviceResp> findIftttDeviceList(@RequestBody CommDeviceInfoReq req) {
		List<IftttDeviceResp> resp = deviceService.findIftttDeviceList(req);
		return resp;
	}

	@Override
	public Integer getCountByDeviceIdsAndBusinessTypesAndSwitch(@RequestBody DeviceBusinessTypeIDSwitchReq req) {
		return deviceService.getCountByDeviceIdsAndBusinessTypesAndSwitch(req);
	}

	@Override
	public List<DeviceResp> findDeviceByCondition(@RequestBody DeviceBusinessTypeIDSwitchReq req) {
		return deviceService.findDeviceByCondition(req);
	}

	public Page<DeviceResp> queryAirCondition(@RequestBody DevicePageReq pageReq) {

		return deviceService.queryAirCondition(pageReq);
	}

	@Override
	public List<GetDeviceInfoRespVo> selectAllDeviceToCenter(@RequestBody DevicePageReq pageReq) {
		return deviceService.selectAllDeviceToCenter(pageReq);
	}

	@Override
	public List<GetDeviceInfoRespVo> findDirectDeviceByDeviceCatgory(@RequestParam("venderCode") String venderCode,
			@RequestParam("tenantId") Long tenantId,@RequestParam("locationId") Long locationId) {
		return deviceService.findDirectDeviceByDeviceCatgory(venderCode,tenantId,locationId);
	}

	public GetDeviceInfoRespVo getDeviceByDeviceIp(@RequestParam("orgId") Long orgId,
			@RequestParam("tenantId") Long tenantId,
			@RequestParam(value = "deviceIp") String deviceIp) {
		return deviceService.getDeviceByDeviceIp(orgId, tenantId, deviceIp);
	}

	@Override
	public Map<String, Object> findDataReport(@RequestParam(value = "spaceId") Long spaceId,
			@RequestParam(value = "deviceId") String deviceId, @RequestParam(value = "dateType") String dateType,
			@RequestParam(value = "deviceType", required = false) String deviceType) {
		AssertUtils.notEmpty(deviceId, "deviceId.notnull");
		AssertUtils.notEmpty(deviceId, "spaceId.notnull");
		Map<String, Object> dataMaps = deviceService.findDataReport(spaceId, deviceId, dateType, deviceType);
		return dataMaps;
	}

	@Override
	public Page<DeviceResp> getGatewayAndSubDeviceList(@RequestBody DevicePageReq pageReq) {
		return deviceService.getGatewayAndSubDeviceList(pageReq);
	}

	@Override
	public List<Long> getExistProductList(@RequestBody PageDeviceInfoReq params) {
		List<Long> productIds = Lists.newArrayList();
		if (params != null && params.getProductIds() != null) {
			productIds = deviceService.getExistProductList(params);
		}
		return productIds;
	}

	@Override
	public List<GetDeviceInfoRespVo> getDeviceListByParentId(@RequestBody CommDeviceInfoReq commDeviceInfoReq) {
		if (commDeviceInfoReq.getParentId() == null) {
			return null;
		}
		return deviceService.getDeviceListByParentId(commDeviceInfoReq);
	}

}

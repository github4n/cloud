package com.iot.building.device.controlller;

import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.iot.building.device.service.IDeviceBusinessTypeService;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.req.CommDeviceInfoReq;
import com.iot.device.vo.req.device.ListDeviceInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeByDeviceRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.airswitch.api.AirSwitchApi;
import com.iot.building.callback.impl.DeviceCallback;
import com.iot.building.device.api.CentralControlDeviceApi;
import com.iot.building.device.service.IDeviceRemoteService;
import com.iot.building.device.service.impl.DeviceService;
import com.iot.building.device.vo.DeviceParamReq;
import com.iot.building.device.vo.DeviceParamResp;
import com.iot.building.device.vo.DevicePropertyVo;
import com.iot.building.device.vo.DeviceRespVo;
import com.iot.building.gateway.MultiProtocolGatewayHepler;
import com.iot.building.gateway.MultiProtocolGatewayNewThread;
import com.iot.building.helper.CenterControlDeviceStatus;
import com.iot.common.enums.APIType;
import com.iot.common.util.StringUtil;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.util.AssertUtils;

@RestController
public class CentralControlDeviceController implements CentralControlDeviceApi {

	public static final Logger LOGGER = LoggerFactory.getLogger(CentralControlDeviceController.class);
	@Autowired
	private IDeviceRemoteService deviceRemoteService;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private DeviceCoreApi deviceCoreApi;

	@Autowired
	private IDeviceBusinessTypeService iDeviceBusinessTypeService;
	
	@Autowired
	private AirSwitchApi airSwitchApi;

	private final static Integer DEFAULT_TIME = 180;

	private final static Integer END_TIME = 0;

	@Override
	public DeviceParamResp findDeviceListByDeviceIds(@RequestBody DeviceParamReq paramReq) {

		DeviceParamResp deviceParamResp = new DeviceParamResp();

		AssertUtils.notNull(paramReq, "param.notnull");
		AssertUtils.notEmpty(paramReq.getDeviceIdList(), "deviceId.notnull");

		List<String> deviceIdList = paramReq.getDeviceIdList();
		boolean isCheckUserNotNull = paramReq.isCheckUserNotNull();
		List<ListDeviceInfoRespVo> deviceRespList = Lists.newArrayList();
		List<DeviceResp> deviceRespListStr = Lists.newArrayList();
		if (!org.springframework.util.CollectionUtils.isEmpty(deviceIdList)) {
			ListDeviceInfoReq params = new ListDeviceInfoReq();
			params.setDeviceIds(deviceIdList);
			deviceRespList = deviceCoreApi.listDevices(params);
			for (ListDeviceInfoRespVo listDeviceInfoRespVo : deviceRespList) {
				DeviceResp deviceResp = new DeviceResp();
				String businessType = iDeviceBusinessTypeService.findById(listDeviceInfoRespVo.getOrgId(),listDeviceInfoRespVo.getTenantId(),listDeviceInfoRespVo.getBusinessTypeId())
						.getBusinessType();
				BeanUtils.copyProperties(listDeviceInfoRespVo, deviceResp);
				deviceResp.setBusinessType(businessType);
				deviceRespListStr.add(deviceResp);
			}
			// ListDeviceByParamsReq params = new ListDeviceByParamsReq();
			// params.setDeviceIds(deviceIdList);
			// deviceRespList = deviceCoreApi.listDeviceByParams(params);
			// deviceRespList = deviceRemoteService.findDevListByDeviceIds(deviceIdList,
			// isCheckUserNotNull, paramReq.getUserId());
		}

		if (!org.springframework.util.CollectionUtils.isEmpty(deviceRespList)) {
			deviceParamResp.setCount(deviceRespList.size());
		}
		deviceParamResp.setDeviceResps(deviceRespListStr);
		return deviceParamResp;
	}

	@Override
	public List<ProductResp> listProducts(@RequestBody List<Long> productIds) {
		List<ProductResp> list = deviceRemoteService.listProducts(productIds);
		return list;
	}

	@Override
	public void getDeviceList(@RequestParam("deviceId") String deviceId) {
		try {
			deviceService.getDeviceList(deviceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void searchStar(@RequestBody CommDeviceInfoReq commDeviceInfoReq) {
		try {
			MultiProtocolGatewayNewThread newThread = new MultiProtocolGatewayNewThread();
			// 开启搜索设备 网关默认180秒搜索时间
			newThread.addDevice(commDeviceInfoReq.getDeviceId(), DEFAULT_TIME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void searchStop(@RequestBody CommDeviceInfoReq commDeviceInfoReq) {
		try {
			MultiProtocolGatewayNewThread newThread = new MultiProtocolGatewayNewThread();
			// 停止搜索 传入0表示停止搜索
			newThread.addDevice(commDeviceInfoReq.getDeviceId(), END_TIME);
			// 延时3s再继续同步设备
			Thread.sleep(3000);
			// 同步设备
			newThread.getDeviceList(commDeviceInfoReq.getDeviceId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void editGatewayInfo(@RequestBody CommDeviceInfoReq commDeviceInfoReq) {
		AssertUtils.notNull(commDeviceInfoReq, "commDeviceInfoReq.notnull");
		AssertUtils.notNull(commDeviceInfoReq.getDeviceId(), "deviceId.notnull");
		AssertUtils.notNull(commDeviceInfoReq.getName(), "name.notnull");
		AssertUtils.notNull(commDeviceInfoReq.getTenantId(), "tenantId.notnull");
		AssertUtils.notNull(commDeviceInfoReq.getOrgId(), "orgId.notnull");
		AssertUtils.notNull(commDeviceInfoReq.getLocationId(), "locationId.notnull");
		String venderFlag = "";
		GetDeviceTypeByDeviceRespVo deviceTypeVo = deviceCoreApi.getDeviceTypeByDeviceId(commDeviceInfoReq.getDeviceId());
		if (deviceTypeVo != null && deviceTypeVo.getDeviceTypeInfo() != null) {
			venderFlag = deviceTypeVo.getDeviceTypeInfo().getVenderFlag();
		}
		if (venderFlag.equals(APIType.MultiProtocolGateway.name())) {
			MultiProtocolGatewayNewThread newThread = new MultiProtocolGatewayNewThread();
			// 编辑网关名称
			newThread.renameReq(commDeviceInfoReq.getDeviceId(), commDeviceInfoReq.getName());
			if (StringUtil.isNotEmpty(commDeviceInfoReq.getIp())) {
				// 设置网关MQTT地址
				newThread.setMqttAddr(commDeviceInfoReq.getDeviceId(), commDeviceInfoReq.getIp());
				// 重启网关
				newThread.rebootReq(commDeviceInfoReq.getDeviceId());
			}
		} else if (venderFlag.equals(APIType.ManTunSci.name())) {
			if (!Strings.isNullOrEmpty(commDeviceInfoReq.getIp())) {
				airSwitchApi.setServerAddress(11L, commDeviceInfoReq.getDeviceId(), commDeviceInfoReq.getIp(), "7758");
			}
			airSwitchApi.modifyDeviceName(commDeviceInfoReq.getDeviceId(), commDeviceInfoReq.getName());
		}
		// 更新本地网关数据信息
		UpdateDeviceInfoReq params = new UpdateDeviceInfoReq();
		params.setName(commDeviceInfoReq.getName());
		params.setUuid(commDeviceInfoReq.getDeviceId());
		params.setTenantId(commDeviceInfoReq.getTenantId());
		params.setOrgId(commDeviceInfoReq.getOrgId());
		params.setLocationId(commDeviceInfoReq.getLocationId());
		deviceCoreApi.saveOrUpdate(params);
	}

	@Override
	public void editDeviceInfo(@RequestBody CommDeviceInfoReq commDeviceInfoReq) {
		AssertUtils.notNull(commDeviceInfoReq, "commDeviceInfoReq.notnull");
		AssertUtils.notNull(commDeviceInfoReq.getDeviceId(), "deviceId.notnull");
		AssertUtils.notNull(commDeviceInfoReq.getName(), "name.notnull");
		AssertUtils.notNull(commDeviceInfoReq.getTenantId(), "tenantId.notnull");
		AssertUtils.notNull(commDeviceInfoReq.getOrgId(), "orgId.notnull");
		AssertUtils.notNull(commDeviceInfoReq.getLocationId(), "locationId.notnull");
		// 更新本地网关数据信息
		UpdateDeviceInfoReq params = new UpdateDeviceInfoReq();
		params.setName(commDeviceInfoReq.getName());
		params.setBusinessTypeId(commDeviceInfoReq.getBusinessTypeId());
		params.setUuid(commDeviceInfoReq.getDeviceId());
		params.setTenantId(commDeviceInfoReq.getTenantId());
		params.setOrgId(commDeviceInfoReq.getOrgId());
		params.setLocationId(commDeviceInfoReq.getLocationId());
		deviceCoreApi.saveOrUpdate(params);
	}

	@Override
	public void airSwitchBack(@RequestBody DevicePropertyVo vo) {
		GetDeviceInfoRespVo device=deviceCoreApi.get(vo.getDeviceId());
		CenterControlDeviceStatus.putDeviceStatus(vo.getDeviceId(), vo.getProperty());
		DeviceCallback callBack=new DeviceCallback();
		callBack.callback(device, vo.getProperty(), APIType.ManTunSci);
	}
}

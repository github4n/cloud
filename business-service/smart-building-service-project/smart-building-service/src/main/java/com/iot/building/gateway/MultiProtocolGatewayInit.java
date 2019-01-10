package com.iot.building.gateway;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.env.Environment;

import com.iot.building.device.service.IDeviceBusinessTypeService;
import com.iot.building.helper.Constants;
import com.iot.common.enums.APIType;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.api.CentralControlDeviceApi;
import com.iot.device.api.DataPointApi;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.vo.rsp.DeviceFunResp;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.ListProductRespVo;

public class MultiProtocolGatewayInit {

	private static CentralControlDeviceApi deviceService = ApplicationContextHelper
			.getBean(CentralControlDeviceApi.class);

	private static IDeviceBusinessTypeService deviceBusinessTypeApi = ApplicationContextHelper
			.getBean(IDeviceBusinessTypeService.class);

	private static ProductCoreApi productApi = ApplicationContextHelper.getBean(ProductCoreApi.class);

	private static DataPointApi dataPointApi = ApplicationContextHelper.getBean(DataPointApi.class);

	private static Environment environment = ApplicationContextHelper.getBean(Environment.class);

	public final static String CENTER_CONTROL_TENANTID = "center-control.tenantId";

	public void initGatewayClient() {
		String tenantId = environment.getProperty(CENTER_CONTROL_TENANTID);
		List<GetDeviceInfoRespVo> directDeviceList = deviceService.findDirectDeviceListByVenderCode(Long.valueOf(tenantId), 1L, APIType.MultiProtocolGateway.name(),1);
		initBusinessType();
		// 产品列表初始化
		List<ListProductRespVo> productResps = productApi.listProductAll();
		for (ListProductRespVo productResp : productResps) {
			Constants.PRODUCT_MAP.put(productResp.getDeviceTypeId().toString(), productResp.getId().toString());
		}
		if (CollectionUtils.isNotEmpty(directDeviceList)) {
			for (GetDeviceInfoRespVo directDevice : directDeviceList) {
				try {
					// 获取子设备请求
					MultiProtocolGatewayHepler.queryReq(directDevice.getUuid());
					// 获取设备属性
					List<DeviceResp> deviceResps = deviceService
							.findUnDirectDeviceListByParentDeviceId(directDevice.getUuid());
					if (CollectionUtils.isEmpty(deviceResps)) {
						return;
					}
					for (DeviceResp deviceResp : deviceResps) {
						List<String> attr = new ArrayList<>();
						List<DeviceFunResp> dataPointResps = dataPointApi
								.findDataPointListByDeviceTypeId(deviceResp.getDeviceTypeId());
						if (CollectionUtils.isNotEmpty(dataPointResps)) {
							for (DeviceFunResp dataPointResp : dataPointResps) {
								if (StringUtils.isNotBlank(dataPointResp.getPropertyCode())) {
									attr.add(dataPointResp.getPropertyCode());
								}
							}
							// 获取设备状态属性
							if (CollectionUtils.isNotEmpty(attr)) {
								MultiProtocolGatewayHepler.getDevAttrReq(directDevice.getUuid(),
										deviceResp.getDeviceId(), attr);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}

	}

	private void initBusinessType() {
		// List<DeviceBusinessType> deviceBusinessTypes =
		// deviceBusinessTypeApi.getBusinessTypeList();
		// if (!CollectionUtils.sizeIsEmpty(deviceBusinessTypes)) {
		// for (DeviceBusinessType deviceBusinessType : deviceBusinessTypes) {
		// Constants.DEVICE_BUSINESS_TYPE_MAP.put(deviceBusinessType.getBusinessType(),
		// deviceBusinessType.getId());
		// }
		// }
	}
}

package com.iot.building.allocation.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.iot.building.gateway.MultiProtocolGatewayHepler;
import com.iot.building.helper.ThreadPoolUtil;
import com.iot.building.scene.service.SceneService;
import com.iot.building.scene.vo.req.SceneTemplateManualReq;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.api.CentralControlDeviceApi;
import com.iot.device.api.DataPointApi;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.vo.rsp.DeviceFunResp;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;

/**
 * @Author: Xieby
 * @Date: 2018/10/11
 * @Description: *
 */
public class DevicePropertyInitJob {

    public DevicePropertyInitJob(Long tenantId,Long locationId) {
        this.tenantId = tenantId;
        this.locationId = locationId;
    }

    private Long tenantId;
    private Long locationId;
    
    @Autowired
    private CentralControlDeviceApi centralControlDeviceApi=ApplicationContextHelper.getBean(CentralControlDeviceApi.class);
    @Autowired
    private DeviceCoreApi deviceCoreApi=ApplicationContextHelper.getBean(DeviceCoreApi.class);
    @Autowired
    private DataPointApi dataPointApi=ApplicationContextHelper.getBean(DataPointApi.class);
    
    public void start() {
        System.out.println("start execute allocation job .... " + new Date());
        List<GetDeviceInfoRespVo> directDeviceList = centralControlDeviceApi.
    			findDirectDeviceListByVenderCode(tenantId, locationId,"MultiProtocolGateway",1);
		if (CollectionUtils.isNotEmpty(directDeviceList)) {
			for (GetDeviceInfoRespVo directDevice : directDeviceList) {
				 ThreadPoolUtil.instance().execute(new Runnable() {
					@Override
					public void run() {
						try {
							initParamAndExcute(directDevice);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
        System.out.println("end execute allocation job .... " + new Date());
    }

	private void initParamAndExcute(GetDeviceInfoRespVo directDevice) {
		// 获取设备属性
		List<ListDeviceInfoRespVo> deviceResps = deviceCoreApi.listDevicesByParentId(directDevice.getUuid());
		if (CollectionUtils.isEmpty(deviceResps)) {
			return;
		}
		for (ListDeviceInfoRespVo deviceResp : deviceResps) {
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
					try {
						MultiProtocolGatewayHepler.updatePropReq(directDevice.getUuid(),
								deviceResp.getUuid(), attr);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		}
	}
    
}

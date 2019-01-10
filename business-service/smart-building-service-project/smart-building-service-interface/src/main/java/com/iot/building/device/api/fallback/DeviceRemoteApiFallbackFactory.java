package com.iot.building.device.api.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.iot.building.device.api.DeviceRemoteApi;
import com.iot.building.device.vo.DeviceRemoteControlReq;
import com.iot.building.device.vo.DeviceRemoteControlResp;
import com.iot.building.device.vo.DeviceRemoteTemplatePageReq;
import com.iot.building.device.vo.DeviceRemoteTemplateReq;
import com.iot.building.device.vo.DeviceRemoteTemplateResp;
import com.iot.building.device.vo.DeviceRemoteTemplateSimpleResp;
import com.iot.building.device.vo.DeviceRemoteTypeResp;
import com.iot.common.helper.Page;

import feign.hystrix.FallbackFactory;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 */

@Component
public class DeviceRemoteApiFallbackFactory implements FallbackFactory<DeviceRemoteApi> {
    @Override
    public DeviceRemoteApi create(Throwable throwable) {
        return new DeviceRemoteApi() {
            @Override
            public void addDeviceRemoteTemplate(DeviceRemoteTemplateReq deviceRemoteTemplateReq) {

            }

            @Override
            public void updateDeviceRemoteTemplate(DeviceRemoteTemplateReq deviceRemoteTemplateReq) {

            }

            @Override
            public Page<DeviceRemoteTemplateSimpleResp> pageDeviceRemoteTemplatePage(DeviceRemoteTemplatePageReq pageReq) {
                return null;
            }

            @Override
            public List<DeviceRemoteTypeResp> listDeviceRemoteType(Long tenantId) {
                return null;
            }

            @Override
            public List<Long> listDeviceRemoteBusinessType(Long tenantId) {
                return null;
            }

            @Override
            public void addDeviceRemoteControl(List<DeviceRemoteControlReq> deviceRemoteControlReqs) {

            }

			@Override
			public DeviceRemoteTemplateResp getDeviceRemoteTemplateById(Long tenantId, Long id) {
				return null;
			}

			@Override
			public DeviceRemoteTemplateResp getDeviceRemoteTemplateByBusinessTypeId(Long tenantId, Long id) {
				return null;
			}

			@Override
			public void deleteDeviceRemoteControlIfExsit(Long tenantId, String deviceId, Long businessTypeId) {
				
			}

			@Override
			public List<DeviceRemoteControlResp> listDeviceRemoteControlByBusinessTypeId(Long tenantId,
					Long businessTypeId) {
				return null;
			}

			@Override
			public List<DeviceRemoteControlResp> findRemoteControlByDeviceType(Long tenantId, Long deviceTypeId) {
				return null;
			}

			@Override
			public void deleteDeviceRemoteTemplate(Long tenantId, Long id, Long userId) {
			}

        };
    }
}

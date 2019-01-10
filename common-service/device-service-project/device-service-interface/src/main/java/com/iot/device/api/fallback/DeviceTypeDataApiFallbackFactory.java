
package com.iot.device.api.fallback;

import java.util.List;

import com.iot.device.api.DeviceTypeDataPointApi;
import com.iot.device.vo.req.DeviceType2PointsReq;
import com.iot.device.vo.rsp.DataPointResp;
import com.iot.device.vo.rsp.DeviceType2PointsRes;

import feign.hystrix.FallbackFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class DeviceTypeDataApiFallbackFactory implements FallbackFactory<DeviceTypeDataPointApi> {
	/**
	 *
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceTypeDataApiFallbackFactory.class);

	@Override
	public DeviceTypeDataPointApi create(Throwable cause) {
		return new DeviceTypeDataPointApi() {

			@Override
			public boolean typeMap2Point(DeviceType2PointsReq req) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public DeviceType2PointsRes getPointsByDeviceTypeId(Long deviceTypeId) {
				// TODO Auto-generated method stub
				return null;
			}


		};
	}
}


package com.iot.device.api.fallback;

import com.iot.device.api.DeviceTypeDataPointApi;
import com.iot.device.api.DeviceTypeToStyleApi;
import com.iot.device.vo.req.DeviceType2PointsReq;
import com.iot.device.vo.req.DeviceTypeToStyleReq;
import com.iot.device.vo.rsp.DeviceType2PointsRes;
import com.iot.device.vo.rsp.DeviceTypeToStyleResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class DeviceTypeToStyleApiFallbackFactory implements FallbackFactory<DeviceTypeToStyleApi> {
	/**
	 *
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceTypeToStyleApiFallbackFactory.class);

	@Override
	public DeviceTypeToStyleApi create(Throwable cause) {
		return new DeviceTypeToStyleApi() {


			@Override
			public Long saveOrUpdate(DeviceTypeToStyleReq deviceTypeToStyleReq) {
				return null;
			}

			@Override
			public void saveMore(DeviceTypeToStyleReq deviceTypeToStyleReq) {

			}

			@Override
			public void delete(ArrayList<Long> ids) {

			}

			@Override
			public List<DeviceTypeToStyleResp> listDeviceTypeStyleByDeviceTypeId(Long deviceTypeId) {
				return null;
			}
		};
	}
}

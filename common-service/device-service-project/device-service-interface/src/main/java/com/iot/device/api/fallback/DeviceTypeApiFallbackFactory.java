package com.iot.device.api.fallback;

import com.iot.common.helper.Page;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.vo.req.DeviceType2PointsReq;
import com.iot.device.vo.req.DeviceTypeReq;
import com.iot.device.vo.rsp.DeviceTypeListResp;
import com.iot.device.vo.rsp.DeviceTypeResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class DeviceTypeApiFallbackFactory implements FallbackFactory<DeviceTypeApi> {
	/**
	 *
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceTypeApiFallbackFactory.class);

	@Override
	public DeviceTypeApi create(Throwable cause) {
		return new DeviceTypeApi() {
			@Override
			public DeviceTypeResp getDeviceTypeById(Long deviceTypeId) {
				return null;
			}

			@Override
			public List<DeviceTypeResp> getDeviceTypeByCataLogId(Long catalogId) {
				return null;
			}

			@Override
			public Page<DeviceTypeResp> getDeviceTypeByCondition(DeviceTypeReq req) {
				return null;
			}

			@Override
			public List<DeviceTypeResp> findDeviceTypeList() {
				return null;
			}


			@Override
			public boolean addDeviceType(DeviceTypeReq req) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean deleteByDeviceTypeId(Long deviceTypeId) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean updateDeviceType(DeviceTypeReq req) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean deleteByDeviceTypeIds(ArrayList<Long> ids) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean addDataPoint(DeviceType2PointsReq req) {
				// TODO Auto-generated method stub
				return false;
			}
            @Override
            public String getSmartCode(Integer keyEnum, Long deviceTypeId) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<DeviceTypeListResp> findDeviceTypeListByCatalogId(Long catalogId) {
                return null;
            }

			@Override
			public List<DeviceTypeListResp> findAllDeviceTypeList() {
				LOGGER.info("findAllDeviceTypeList...error.");
				return null;
			}

			@Override
			public void delete(Long id, Long tenantId) {

			}

			@Override
			public List<String> getDeviceTypeNameByIds(List<Long> ids) {
				return null;
			}

			@Override
			public List<DeviceTypeResp> getByIds(List<Long> ids) {
				return null;
			}

			@Override
			public List<DeviceTypeResp> getByIdsAndIfffType(List<Long> ids, String iftttType) {
				return null;
			}

			@Override
			public List<DeviceTypeResp> getDeviceTypeIdAndNameByIds(List<Long> deviceTypeIds) {
				return null;
			}
		};
	}
}

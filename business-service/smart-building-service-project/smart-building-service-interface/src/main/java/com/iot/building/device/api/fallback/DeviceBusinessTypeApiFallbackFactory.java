
package com.iot.building.device.api.fallback;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.iot.building.device.api.DeviceBusinessTypeApi;
import com.iot.building.device.vo.BusinessTypeStatistic;
import com.iot.building.device.vo.DeviceBusinessTypeReq;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.common.helper.Page;
import feign.hystrix.FallbackFactory;


@Component
public class DeviceBusinessTypeApiFallbackFactory implements FallbackFactory<DeviceBusinessTypeApi> {
	/**
	 *
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceBusinessTypeApiFallbackFactory.class);

	@Override
	public DeviceBusinessTypeApi create(Throwable cause) {
		return new DeviceBusinessTypeApi() {



			@Override
			public void saveOrUpdate(DeviceBusinessTypeReq deviceBusinessType) {
				
			}

			@Override
			public List<BusinessTypeStatistic> findStatistic(BusinessTypeStatistic statistic) {
				return null;
			}

			@Override
			public void initStatistic() {
				
			}

			@Override
			public List<DeviceBusinessTypeResp> findByCondition(DeviceBusinessTypeReq req) {
				return null;
			}

			@Override
			public DeviceBusinessTypeResp getBusinessTypeIdByType(Long orgId, Long tenantId, String businessType) {
				return null;
			}

			@Override
			public DeviceBusinessTypeResp findById(Long orgId, Long tenantId, Long id) {
				return null;
			}

			@Override
			public void delBusinessTypeById(Long id) {
				
			}

			@Override
			public Page<DeviceBusinessTypeResp> getBusinessTypeList(Long orgId, String name, Long tenantId,
					String model, int pageNumber, int pageSize) {
				return null;
			}

			@Override
			public List<DeviceBusinessTypeResp> getBusinessTypeList(Long orgId, Long tenantId, String model) {
				return null;
			}

			@Override
			public List<DeviceBusinessTypeResp> businessTypeWithProduct(Long orgId, Long tenantId) {
				return null;
			}

			@Override
			public List<DeviceBusinessTypeResp> findBusinessTypeList(Long orgId, Long tenantId, String businessType) {
				return null;
			}

			@Override
			public List<DeviceBusinessTypeResp> listDeviceRemoteBusinessType(Long orgId, Long tenantId, Long id) {
				return null;
			}

			@Override
			public Boolean businessTypeDataImport(Long orgId, List<String[]> list, Long tenantId, Long userId) {
				return null;
			}

			@Override
			public List<String> getBusinessListByDescription(Long orgId, String description, Long tenantId) {
				return null;
			}
			
		};
	}
}

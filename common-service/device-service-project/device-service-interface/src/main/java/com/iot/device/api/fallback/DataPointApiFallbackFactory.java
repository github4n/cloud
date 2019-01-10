
package com.iot.device.api.fallback;

import com.github.pagehelper.PageInfo;
import com.iot.device.api.DataPointApi;
import com.iot.device.vo.req.DataPointReq;
import com.iot.device.vo.rsp.DataPointResp;
import com.iot.device.vo.rsp.DeviceFunResp;
import com.iot.device.vo.rsp.voicebox.SmartDataPointResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class DataPointApiFallbackFactory implements FallbackFactory<DataPointApi> {
	/**
	 *
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DataPointApiFallbackFactory.class);

	@Override
	public DataPointApi create(Throwable cause) {
		return new DataPointApi() {

			@Override
			public List<DeviceFunResp> findDataPointListByDeviceId(String deviceId) {
				return null;
			}

			@Override
			public List<DeviceFunResp> findDataPointListByProductId(Long productId) {
				return null;
			}

			@Override
			public Map<Long, List<DeviceFunResp>> findDataPointListByProductIds(List<Long> productIds) {
				return null;
			}

			@Override
			public boolean addDataPoint(DataPointReq req) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean updateDataPoint(DataPointReq req) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean deleteByIds(ArrayList<Long> ids) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public List<DeviceFunResp> findDataPointListByDeviceTypeId(Long deviceTypeId) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PageInfo findExceptCustom(DataPointReq req) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getSmartCode(Integer smart, String propertyCode) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean deleteByIdsAndProduct(Long productId, ArrayList<Long> ids) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public List<SmartDataPointResp> getSmartByDataPointId(Long dataPointId) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DataPointResp findExceptCustomById(Long dataPointId) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}

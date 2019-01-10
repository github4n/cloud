
package com.iot.device.api.fallback;

import com.iot.common.helper.Page;
import com.iot.device.api.DeviceCatalogApi;
import com.iot.device.vo.req.DeviceCatalogReq;
import com.iot.device.vo.rsp.DeviceCatalogListResp;
import com.iot.device.vo.rsp.DeviceCatalogRes;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class DeviceCatalogApiFallbackFactory implements FallbackFactory<DeviceCatalogApi> {
	/**
	 *
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceCatalogApiFallbackFactory.class);

	@Override
	public DeviceCatalogApi create(Throwable cause) {
		return new DeviceCatalogApi() {

			@Override
			public boolean addDeviceCatalog(DeviceCatalogReq req) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public List<DeviceCatalogRes> getDeviceCatalog() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Page<DeviceCatalogRes> getDevCatalogPageByCondition(DeviceCatalogReq req) {
				return null;
			}

			@Override
			public boolean updateDeviceCatalog(DeviceCatalogReq req) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Integer deleteCatalogs(ArrayList<Long> ids) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<DeviceCatalogListResp> findAllCatalogList() {
				return null;
			}
		};
	}
}

package com.iot.report.fallback;

import com.iot.common.helper.Page;
import com.iot.report.api.DeviceActivateApi;
import com.iot.report.dto.req.ActivateBaseReq;
import com.iot.report.dto.req.DevDistributionReq;
import com.iot.report.dto.req.DevPageReq;
import com.iot.report.dto.resp.DevActivateResp;
import com.iot.report.dto.resp.DevDistributionResp;
import com.iot.report.dto.resp.DevPageResp;
import com.iot.report.dto.resp.DistributionResp;
import com.iot.report.entity.DeviceActivatedInfo;
import feign.hystrix.FallbackFactory;

import java.util.List;
import java.util.Map;

/**
* @Description:    立达信IOT云平台
* @Author:         nongchongwei
* @CreateDate:     2019/1/4 11:08
* @UpdateUser:     nongchongwei
* @UpdateDate:     2019/1/4 11:08
* @UpdateRemark:
* @Version:        1.0
*/
public class DeviceActivateApiFallbackFactory implements FallbackFactory<DeviceActivateApi> {
	
	@Override
    public DeviceActivateApi create(Throwable cause) {
        return new DeviceActivateApi() {

			@Override
			public DeviceActivatedInfo getDeviceActiveInfoByUuid(String uuid) {
				return null;
			}

			@Override
			public void saveDeviceActiveInfo() {
			
			}

			@Override
			public void cacheDevDistribution(String uuid, String ip) {

			}

			@Override
			public void cacheDevIp(String uuid, String ip) {

			}

			@Override
			public void syncOnlineDev() {

			}

			@Override
			public void syncOnlineUser() {

			}

			@Override
			public List<DevDistributionResp> getActivateDistributionData(ActivateBaseReq activateBaseReq) {
				return null;
			}

			@Override
			public List<DevDistributionResp> getActiveDistributionData(ActivateBaseReq activateBaseReq) {
				return null;
			}

			@Override
			public Page<DevPageResp> getPageDeviceActivate(DevPageReq devPageReq) {
				return null;
			}

			@Override
			public Page<DevPageResp> getPageDeviceActive(DevPageReq devPageReq) {
				return null;
			}

			@Override
			public DevActivateResp getDeviceActive(ActivateBaseReq req) {
				return null;
			}

			@Override
			public DevActivateResp getDeviceActivated(ActivateBaseReq req) {
				return null;
			}

			@Override
			public DevActivateResp getDeviceActiveAndActivated(ActivateBaseReq req) {
				return null;
			}

			@Override
			public Long getDeviceActivatedCount(Long tenantId) {
				return null;
			}
		};
    }
}

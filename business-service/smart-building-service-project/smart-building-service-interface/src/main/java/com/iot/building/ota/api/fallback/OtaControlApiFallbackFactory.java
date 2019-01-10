package com.iot.building.ota.api.fallback;


import org.springframework.stereotype.Component;

import com.iot.building.ota.api.OtaControlApi;
import com.iot.common.helper.Page;
import com.iot.device.vo.req.OtaPageReq;
import com.iot.device.vo.req.ota.OtaFileInfoReq;
import com.iot.device.vo.rsp.ota.OtaFileInfoResp;

import feign.hystrix.FallbackFactory;


/**
 * @Author: linjihuang
 * @Date: 2018/10/12
 * @Description: *
 */
@Component
public class OtaControlApiFallbackFactory implements FallbackFactory<OtaControlApi> {


    @Override
    public OtaControlApi create(Throwable throwable) {
        return new OtaControlApi() {

			@Override
			public void updateOtaVersion(Long orgId, String deviceId, Long tenantId, Long locationId) {
			}

			@Override
			public void downLoadOtaFile(OtaFileInfoReq otaFileInfoReq) {
			}

			@Override
			public Page<OtaFileInfoResp> getOtaFileList(OtaPageReq pageReq) {
				return null;
			}

			@Override
			public int saveOtaFileInfo(OtaFileInfoReq otaFileInfoReq) {
				return 0;
			}

			@Override
			public int updateOtaFileInfo(OtaFileInfoReq otaFileInfoReq) {
				return 0;
			}

			@Override
			public OtaFileInfoResp findOtaFileInfoByProductId(OtaFileInfoReq otaFileInfoReq) {
				return null;
			}
        };
    }
}

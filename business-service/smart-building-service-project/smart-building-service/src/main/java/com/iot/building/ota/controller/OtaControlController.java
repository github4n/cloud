package com.iot.building.ota.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.iot.building.ota.api.OtaControlApi;
import com.iot.building.ota.service.OtaControlService;
import com.iot.common.helper.Page;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.vo.req.OtaPageReq;
import com.iot.device.vo.req.ota.OtaFileInfoReq;
import com.iot.device.vo.rsp.ota.OtaFileInfoResp;

/**
 * @author linjihuang
 */
@RestController
public class OtaControlController implements OtaControlApi {

	@Autowired
	DeviceTypeApi deviceTypeApi;
	private Logger log = LoggerFactory.getLogger(OtaControlController.class);
	@Autowired
	private OtaControlService otaControlService;

	@Override
	public void updateOtaVersion(Long orgId, String deviceId, Long tenantId, Long locationId) {
		otaControlService.updateOtaVersion(orgId, deviceId, tenantId, locationId);
	}

	@Override
	public void downLoadOtaFile(@RequestBody OtaFileInfoReq otaFileInfoReq) {
		otaControlService.downLoadOtaFile(otaFileInfoReq);
	}

	@Override
    @ResponseBody
	public Page<OtaFileInfoResp> getOtaFileList(@RequestBody OtaPageReq pageReq) {
		return otaControlService.getOtaFileList(pageReq);
	}
	
	@Override
	@ResponseBody
	public int saveOtaFileInfo(@RequestBody OtaFileInfoReq otaFileInfoReq) {
		return otaControlService.saveOtaFileInfo(otaFileInfoReq);
	}
	
	@Override
	@ResponseBody
	public int updateOtaFileInfo(@RequestBody OtaFileInfoReq otaFileInfoReq) {
		return otaControlService.updateOtaFileInfo(otaFileInfoReq);
	}
	
	@Override
	@ResponseBody
	public OtaFileInfoResp findOtaFileInfoByProductId(@RequestBody OtaFileInfoReq otaFileInfoReq) {
		return otaControlService.findOtaFileInfoByProductId(otaFileInfoReq);
	}

}

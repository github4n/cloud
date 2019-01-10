package com.iot.building.device.controlller;

import com.iot.building.device.api.DeviceTobApi;
import com.iot.building.device.service.DeviceTobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceTobController implements DeviceTobApi {

	public static final Logger LOGGER = LoggerFactory.getLogger(DeviceTobController.class);

	@Autowired
	private DeviceTobService deviceTobService;

	@Override
	public boolean deleteDeviceRelation(@RequestParam("orgId")Long orgId, @RequestParam("deviceId")String deviceId, @RequestParam("tenantId")Long tenantId,@RequestParam("check")boolean check,@RequestParam("clientId")String clientId) {
		return deviceTobService.deleteDeviceRelation(orgId,deviceId,tenantId,check,clientId);
	}

    @Override
    public void resetReq(@RequestParam("clientId")String clientId) {
		deviceTobService.resetReq(clientId);
    }
}

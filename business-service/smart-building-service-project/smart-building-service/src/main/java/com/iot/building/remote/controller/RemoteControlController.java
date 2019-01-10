package com.iot.building.remote.controller;

import com.iot.building.remote.api.RemoteControlApi;
import com.iot.building.remote.service.RemoteControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linjihuang
 */
@RestController
public class RemoteControlController implements RemoteControlApi {

	@Autowired
	RemoteControlService remoteControlService;

	public void synchronousRemoteControl(Long tenantId, Long spaceId) {
		remoteControlService.synchronousRemoteControl(tenantId, spaceId);
	}

}

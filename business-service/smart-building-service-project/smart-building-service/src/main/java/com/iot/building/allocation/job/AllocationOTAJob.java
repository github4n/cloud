package com.iot.building.allocation.job;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.iot.building.ota.service.OtaControlService;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;

/**
 * @Author: Xieby
 * @Date: 2018/10/11
 * @Description: *
 */
public class AllocationOTAJob{


    private Collection<String> deviceIds;

    public AllocationOTAJob(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public void start() {

        System.out.println("start execute OTA job .... " + new Date());

        for (String deviceUuid : deviceIds) {
            DeviceCoreApi deviceCoreApi = ApplicationContextHelper.getBean(DeviceCoreApi.class);
            GetDeviceInfoRespVo deviceResp = deviceCoreApi.get(deviceUuid);
            if (deviceResp == null) {
                continue;
            }

            OtaControlService otaControlService= ApplicationContextHelper.getBean(OtaControlService.class);

            otaControlService.updateOtaVersion(deviceResp.getOrgId(),deviceResp.getUuid(),
                                        deviceResp.getTenantId(),
                                        deviceResp.getLocationId());
        }

        System.out.println("end execute OTA job .... " + new Date());
    }
}

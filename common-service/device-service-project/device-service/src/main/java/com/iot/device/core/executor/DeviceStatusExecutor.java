package com.iot.device.core.executor;

import com.alibaba.fastjson.JSON;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.model.DeviceStatus;
import com.iot.device.service.IDeviceStatusService;
import com.iot.device.service.impl.DeviceStatusServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 16:13 2018/9/7
 * @Modify by:
 */
public class DeviceStatusExecutor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceStatusExecutor.class);
    private DeviceStatus deviceStatus;

    public DeviceStatusExecutor(DeviceStatus deviceStatus){
        this.deviceStatus = deviceStatus;
    }

    public void run() {

        IDeviceStatusService deviceStatusService = ApplicationContextHelper.getBean(IDeviceStatusService.class);

        try {
            deviceStatusService.insertOrUpdate(deviceStatus);
        } catch (Exception e) {
            LOGGER.error("insert_or_update_error.{}",e);
            LOGGER.info("insert or update deviceStatus error.{}content:{}",e, JSON.toJSONString(deviceStatus));
        }
    }
}

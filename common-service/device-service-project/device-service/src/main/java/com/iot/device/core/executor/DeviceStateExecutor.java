package com.iot.device.core.executor;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.model.DeviceState;
import com.iot.device.service.IDeviceStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 16:13 2018/9/7
 * @Modify by:
 */
public class DeviceStateExecutor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceStateExecutor.class);

    private List<DeviceState> targetDeviceStates;

    public DeviceStateExecutor(List<DeviceState> targetDeviceStates) {
        this.targetDeviceStates = targetDeviceStates;
    }

    public void run() {
        try {
            if (!CollectionUtils.isEmpty(targetDeviceStates)) {
                IDeviceStateService deviceStateService = ApplicationContextHelper.getBean(IDeviceStateService.class);
                deviceStateService.insertOrUpdateBatch(targetDeviceStates);
            }
        } catch (Exception e) {
            LOGGER.error("insert_or_update_error.{}", e);
        }
    }
}

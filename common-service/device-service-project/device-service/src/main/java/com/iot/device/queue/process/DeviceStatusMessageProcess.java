package com.iot.device.queue.process;

import com.alibaba.fastjson.JSON;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.device.business.DeviceStatusBusinessService;
import com.iot.device.model.DeviceStatus;
import com.iot.device.queue.bean.DeviceStatusMessage;
import com.iot.device.service.IDeviceStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: lucky @Descrpiton: @Date: 15:34 2018/10/11 @Modify by:
 */
@Slf4j
@Component
public class DeviceStatusMessageProcess extends AbsMessageProcess<DeviceStatusMessage> {

    @Autowired
    private IDeviceStatusService deviceStatusService;

    @Autowired
    private DeviceStatusBusinessService deviceStatusBusinessService;

    public void processMessage(DeviceStatusMessage message) {
        long startTime = System.currentTimeMillis();
        log.debug("processMessage接收到 DeviceStatusMessage 发送请求->start:{} 信息体：{} ", startTime, JSON.toJSONString(message));
        processStates(message);

        long useTime = System.currentTimeMillis() - startTime;
        log.debug("processMessage调用 {} DeviceStatusMessage 处理完毕，耗时 {}毫秒", useTime);
    }

    private void processStates(DeviceStatusMessage message) {
        try {
            List<DeviceStatus> params = message.getDeviceStatuses();
            if (!CollectionUtils.isEmpty(params)) {
                deviceStatusService.updateBatchById(params);
            }
        } catch (Exception e) {
            log.error("add  deviceStatus message db error.", e);
        }
    }
}

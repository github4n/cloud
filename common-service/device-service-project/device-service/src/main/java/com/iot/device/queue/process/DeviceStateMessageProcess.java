package com.iot.device.queue.process;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.device.business.DeviceBusinessService;
import com.iot.device.business.DeviceStateBusinessService;
import com.iot.device.model.Device;
import com.iot.device.model.DeviceState;
import com.iot.device.queue.bean.DeviceStateMessage;
import com.iot.device.repository.DeviceStateRepository;
import com.iot.device.service.IDeviceStateService;
import com.iot.device.vo.req.device.UpdateDeviceStateReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author: lucky @Descrpiton: @Date: 15:34 2018/10/11 @Modify by:
 */
@Slf4j
@Component
public class DeviceStateMessageProcess extends AbsMessageProcess<DeviceStateMessage> {
    @Autowired
    private DeviceStateRepository deviceStateRepository;

    @Autowired
    private DeviceBusinessService deviceBusinessService;

    @Autowired
    private DeviceStateBusinessService deviceStateBusinessService;


    @Autowired
    private IDeviceStateService deviceStateService;

    public void processMessage(DeviceStateMessage message) {
        long startTime = System.currentTimeMillis();
        log.debug("processMessage接收到 state 发送请求-> 信息体：{} ", JSON.toJSONString(message));
        processStates(message);

        processSaveOrUpdateDBStates(message);
        long useTime = System.currentTimeMillis() - startTime;
        log.debug("processMessage调用 {} state处理完毕，耗时 {}毫秒", useTime);
    }

    private void processSaveOrUpdateDBStates(DeviceStateMessage message) {
        try {
            if (message != null && !CollectionUtils.isEmpty(message.getParams())) {
                List<DeviceState> targetDeviceStates = Lists.newArrayList();
                for (UpdateDeviceStateReq state : message.getParams()) {
                    Long tenantId = state.getTenantId();
                    String deviceId = state.getDeviceId();
                    if (!CollectionUtils.isEmpty(state.getStateList())) {
                        Device device = deviceBusinessService.getDevice(null, deviceId);
                        List<DeviceState> deviceStateList = deviceStateBusinessService.getTargetDeviceStateList(tenantId, device, state.getStateList());
                        targetDeviceStates.addAll(deviceStateList);
                    }
                }
                if (!CollectionUtils.isEmpty(targetDeviceStates)) {
                    log.debug("process-state-SaveOrUpdateDBStates  size {}", targetDeviceStates.size());
                    deviceStateService.insertOrUpdateBatch(targetDeviceStates, 15);
                }
            }
        } catch (Exception e) {
            log.error("add deviceStateDB device state MYSQL error.");
        }
    }
    private void processStates(DeviceStateMessage message) {
        try {
            if (message != null && !CollectionUtils.isEmpty(message.getParams())) {
                List<DeviceState> targetDeviceState = Lists.newArrayList();
                for (UpdateDeviceStateReq state : message.getParams()) {
                    Long tenantId = state.getTenantId();
                    String deviceId = state.getDeviceId();

                    if (!CollectionUtils.isEmpty(state.getStateList())) {
                        Device device = deviceBusinessService.getDevice(null, deviceId);
                        Long productId = device == null ? null : device.getProductId();
                        state.getStateList().forEach(stateInfo -> {

                            DeviceState destState = new DeviceState();
                            destState.setDeviceId(deviceId);
                            destState.setTenantId(tenantId);
                            destState.setLogDate(new Date());
                            destState.setProductId(productId);
                            destState.setPropertyDesc(stateInfo.getPropertyDesc());
                            destState.setPropertyName(stateInfo.getPropertyName());
                            destState.setPropertyValue(stateInfo.getPropertyValue());
                            destState.setGroupId(stateInfo.getGroupId());
                            targetDeviceState.add(destState);
                        });
                    }

                }
                deviceStateRepository.insert(targetDeviceState);
                log.debug("deviceStateRepository.insert(destState={})", JSON.toJSONString(targetDeviceState));
            }

        } catch (Exception e) {
            log.error("add deviceStateRepository device state monogo error.");
        }
    }
}

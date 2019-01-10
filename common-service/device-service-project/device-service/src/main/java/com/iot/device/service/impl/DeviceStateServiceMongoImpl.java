package com.iot.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.device.model.DeviceState;
import com.iot.device.repository.DeviceStateRepository;
import com.iot.device.service.IDeviceStateMongoService;
import com.iot.device.vo.req.DeviceStateInfoReq;
import com.iot.device.vo.req.device.AddCommDeviceStateInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceStateReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
@Slf4j
@Service
public class DeviceStateServiceMongoImpl implements IDeviceStateMongoService {
    /**
     * 线程池
     */
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    private DeviceStateRepository deviceStateRepository;

    @Override
    public void saveOrUpdate(String deviceId, Long productId, UpdateDeviceStateReq deviceStateReq) {
        if (deviceStateReq == null || CollectionUtils.isEmpty(deviceStateReq.getStateList())) {
            return;
        }
        executorService.submit((()->{
            try {
                List<DeviceState> targetDeviceState = Lists.newArrayList();
                for (AddCommDeviceStateInfoReq state : deviceStateReq.getStateList()) {
                    DeviceState destState = new DeviceState();
                    destState.setDeviceId(deviceId);
                    destState.setLogDate(new Date());
                    destState.setProductId(productId);
                    destState.setPropertyDesc(state.getPropertyDesc());
                    destState.setPropertyName(state.getPropertyName());
                    destState.setPropertyValue(state.getPropertyValue());
                    destState.setGroupId(state.getGroupId());
                    targetDeviceState.add(destState);
                }
                deviceStateRepository.insert(targetDeviceState);
                log.debug("deviceStateRepository.insert(destState={})", JSON.toJSONString(targetDeviceState));
            } catch (Exception e) {
                log.error("add device state monogo error.", e);
            }

        }));

    }
}

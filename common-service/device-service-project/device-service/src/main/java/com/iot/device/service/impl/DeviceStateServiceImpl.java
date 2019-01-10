package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.iot.device.mapper.DeviceStateMapper;
import com.iot.device.model.DeviceState;
import com.iot.device.service.IDeviceStateService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
@Service
public class DeviceStateServiceImpl extends ServiceImpl<DeviceStateMapper, DeviceState> implements IDeviceStateService {

    @Override
    public Map<String, Map<String, Object>> findListByDeviceIds(Long tenantId, List<String> deviceIds) {
        Map<String, Map<String, Object>> resultDataMap = Maps.newHashMap();
        if(CollectionUtils.isEmpty(deviceIds)){
            return resultDataMap;
        }
        EntityWrapper<DeviceState> wrapper = new EntityWrapper<>();
        wrapper.in("device_id", deviceIds);
        if (null != tenantId) {
            wrapper.eq("tenant_id", tenantId);
        }
        List<DeviceState> deviceStates = super.selectList(wrapper);
        if (CollectionUtils.isEmpty(deviceStates)) {
            return resultDataMap;
        }
        deviceIds.forEach(deviceId -> {
            resultDataMap.put(deviceId, Maps.newHashMap());
        });
        deviceStates.forEach(deviceState -> {
            Map<String, Object> resultValueMap = resultDataMap.get(deviceState.getDeviceId());
            if (CollectionUtils.isEmpty(resultValueMap)) {
                resultValueMap = Maps.newHashMap();
            }
            resultValueMap.put(deviceState.getPropertyName(), deviceState.getPropertyValue());
            resultDataMap.put(deviceState.getDeviceId(), resultValueMap);
        });

        return resultDataMap;
    }

    @Override
    public Map<String, Object> getByDeviceId(Long tenantId, String deviceId) {
        Map<String, Object> resultDataMap = Maps.newHashMap();
        EntityWrapper<DeviceState> wrapper = new EntityWrapper<>();
        wrapper.eq("device_id", deviceId);
        if (null != tenantId) {
            wrapper.eq("tenant_id", tenantId);
        }
        List<DeviceState> deviceStates = super.selectList(wrapper);
        if (CollectionUtils.isEmpty(deviceStates)) {
            return resultDataMap;
        }
        deviceStates.forEach(deviceState -> {
            Object resultValue = resultDataMap.get(deviceState.getPropertyName());
            if (resultValue == null) {
                resultDataMap.put(deviceState.getPropertyName(), deviceState.getPropertyValue());
            }
        });

        return resultDataMap;
    }

}

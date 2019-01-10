package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.device.mapper.DeviceStatusMapper;
import com.iot.device.model.DeviceStatus;
import com.iot.device.service.IDeviceStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

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
public class DeviceStatusServiceImpl extends ServiceImpl<DeviceStatusMapper, DeviceStatus> implements IDeviceStatusService {

    @Override
    public List<DeviceStatus> findListByDeviceIds(Long tenantId, List<String> deviceIds) {
        if(CollectionUtils.isEmpty(deviceIds)){
            return Lists.newArrayList();
        }
        EntityWrapper<DeviceStatus> wrapper = new EntityWrapper<>();
        wrapper.in("device_id", deviceIds);
        if (null != tenantId) {
            wrapper.eq("tenant_id", tenantId);
        }
        List<DeviceStatus> resultDataList = super.selectList(wrapper);
        return resultDataList;
    }

    @Override
    public DeviceStatus getByDeviceId(Long tenantId, String deviceId) {
        EntityWrapper<DeviceStatus> wrapper = new EntityWrapper<>();
        wrapper.eq("device_id", deviceId);
        if (null != tenantId) {
            wrapper.eq("tenant_id", tenantId);
        }
        DeviceStatus resultDataList = super.selectOne(wrapper);
        return resultDataList;
    }
}

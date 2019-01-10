package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.device.mapper.DeviceExtendMapper;
import com.iot.device.model.DeviceExtend;
import com.iot.device.service.IDeviceExtendService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
@Service
public class DeviceExtendServiceImpl extends ServiceImpl<DeviceExtendMapper, DeviceExtend> implements IDeviceExtendService {

    @Override
    public List<DeviceExtend> findListByDeviceIds(Long tenantId, List<String> deviceIds) {
        List<DeviceExtend> resultDataList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(deviceIds)) {
            return resultDataList;
        }
        EntityWrapper wrapper = new EntityWrapper<>();
        if (deviceIds.size() == 1) {
            wrapper.eq("device_id", deviceIds.get(0));
        } else {
            wrapper.in("device_id", deviceIds);
        }
        if (null != tenantId) {
            wrapper.eq("tenant_id", tenantId);
        }
        resultDataList = super.selectList(wrapper);
        return resultDataList;
    }

    @Override
    public DeviceExtend getByDeviceId(Long tenantId, String deviceId) {
        DeviceExtend resultData = null;
        if (StringUtils.isEmpty(deviceId)) {
            return resultData;
        }
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.eq("device_id", deviceId);
        if (null != tenantId) {
            wrapper.eq("tenant_id", tenantId);
        }
        resultData = super.selectOne(wrapper);
        return resultData;
    }
}

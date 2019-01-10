package com.iot.ifttt.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.iot.ifttt.entity.IftttDevice;
import com.iot.ifttt.mapper.IftttDeviceMapper;
import com.iot.ifttt.service.IIftttDeviceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * IFTTT 关联设备 配置 服务实现类
 * </p>
 *
 * @author laiguiming
 * @since 2018-12-19
 */
@Service
public class IftttDeviceServiceImpl extends ServiceImpl<IftttDeviceMapper, IftttDevice> implements IIftttDeviceService {

    @Override
    public List<IftttDevice> getListByApi(Long id, Long tenantId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("api_id", id);
        params.put("tenant_id", tenantId);
        return selectByMap(params);
    }
}

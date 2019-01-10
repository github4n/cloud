package com.iot.device.service.impl;

import com.iot.device.model.DeviceTypeDataPoint;
import com.iot.device.mapper.DeviceTypeDataPointMapper;
import com.iot.device.service.IDeviceTypeDataPointService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
@Service
public class DeviceTypeDataPointServiceImpl extends ServiceImpl<DeviceTypeDataPointMapper, DeviceTypeDataPoint> implements IDeviceTypeDataPointService {
	
}

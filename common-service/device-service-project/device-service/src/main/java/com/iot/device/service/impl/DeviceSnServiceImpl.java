package com.iot.device.service.impl;

import com.iot.device.model.DeviceSn;
import com.iot.device.mapper.DeviceSnMapper;
import com.iot.device.service.IDeviceSnService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备SN表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
@Service
public class DeviceSnServiceImpl extends ServiceImpl<DeviceSnMapper, DeviceSn> implements IDeviceSnService {
	
}

package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.DeviceStatus;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
public interface IDeviceStatusService extends IService<DeviceStatus> {

    List<DeviceStatus> findListByDeviceIds(Long tenantId, List<String> deviceIds);

    DeviceStatus getByDeviceId(Long tenantId, String deviceId);
}

package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.DeviceExtend;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
public interface IDeviceExtendService extends IService<DeviceExtend> {

    List<DeviceExtend> findListByDeviceIds(Long tenantId, List<String> deviceIds);

    DeviceExtend getByDeviceId(Long tenantId, String deviceId);
}

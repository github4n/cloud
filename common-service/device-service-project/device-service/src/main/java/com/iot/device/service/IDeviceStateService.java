package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.DeviceState;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
public interface IDeviceStateService extends IService<DeviceState> {

    Map<String, Object> getByDeviceId(Long tenantId, String deviceId);

    Map<String, Map<String, Object>> findListByDeviceIds(Long tenantId, List<String> deviceIds);

}

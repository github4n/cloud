package com.iot.device.service;

import com.iot.device.vo.req.device.UpdateDeviceStateReq;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
public interface IDeviceStateMongoService {

    void saveOrUpdate(String deviceId, Long productId, UpdateDeviceStateReq deviceStateReq);
}

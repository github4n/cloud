package com.iot.device.repository;

import com.iot.device.model.DeviceState;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeviceStateRepository extends MongoRepository<DeviceState, Long> {
    /**
     * 根据设备Id获取记录
     *
     * @param deviceId
     * @return
     */
    List<DeviceState> findByDeviceId(String deviceId);

    /**
     * 根据属性名称获取记录
     *
     * @param propertyName
     * @return
     */
    List<DeviceState> findByPropertyName(String propertyName);

    /**
     * 根据设备ID及属性名称查询
     *
     * @param deviceId
     * @param propertyName
     * @return
     */
    List<DeviceState> findByDeviceIdAndPropertyName(String deviceId, String propertyName);
}

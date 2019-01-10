package com.iot.building.device.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.building.device.vo.DeviceRemoteControlScene;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 */
@Mapper
public interface DeviceRemoteControlSceneMapper extends BaseMapper<DeviceRemoteControlScene> {

    @Delete("DELETE FROM device_remote_control_scene WHERE control_id in(select id from device_remote_control where device_id=#{deviceId})")
    void removeByDeviceId(String deviceId);
}

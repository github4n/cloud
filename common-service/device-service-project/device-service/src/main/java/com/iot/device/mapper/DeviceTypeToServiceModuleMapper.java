package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.DeviceTypeToServiceModule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 设备类型对应模组表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-07-03
 */
@Mapper
public interface DeviceTypeToServiceModuleMapper extends BaseMapper<DeviceTypeToServiceModule> {

    @Select("select service_module_id from device_type_to_service_module where device_type_id = #{deviceTypeId}")
    List<Long> findServiceModuleIdByDeviceTypeId(@Param("deviceTypeId") Long deviceTypeId);

}

package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.DeviceStatus;
import org.apache.ibatis.annotations.Mapper;


/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Mapper
public interface DeviceStatusMapper extends BaseMapper<DeviceStatus> {
}
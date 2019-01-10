package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.DeviceSn;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
  * 设备SN表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Mapper
public interface DeviceSnMapper extends BaseMapper<DeviceSn> {

}
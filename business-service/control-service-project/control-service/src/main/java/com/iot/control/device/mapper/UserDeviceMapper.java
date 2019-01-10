package com.iot.control.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.control.device.entity.UserDevice;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户设备关系表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Mapper
public interface UserDeviceMapper extends BaseMapper<UserDevice> {

}
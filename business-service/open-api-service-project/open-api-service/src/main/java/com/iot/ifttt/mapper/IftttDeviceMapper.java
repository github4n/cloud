package com.iot.ifttt.mapper;

import com.iot.ifttt.entity.IftttDevice;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * IFTTT 关联设备 配置 Mapper 接口
 * </p>
 *
 * @author laiguiming
 * @since 2018-12-19
 */
@Mapper
public interface IftttDeviceMapper extends BaseMapper<IftttDevice> {

}

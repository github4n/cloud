package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.ServiceToEvent;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 模组-to-事件表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Mapper
public interface ServiceToEventMapper extends BaseMapper<ServiceToEvent> {

}

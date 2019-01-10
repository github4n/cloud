package com.iot.ifttt.mapper;

import com.iot.ifttt.entity.Service;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 服务表 Mapper 接口
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-27
 */
@Mapper
public interface ServiceMapper extends BaseMapper<Service> {

}

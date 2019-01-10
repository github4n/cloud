package com.iot.tenant.mapper;

import com.iot.tenant.domain.AppVersion;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 应用版本记录 Mapper 接口
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-12
 */
@Mapper
public interface AppVersionMapper extends BaseMapper<AppVersion> {

}

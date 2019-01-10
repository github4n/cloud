package com.iot.tenant.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.tenant.domain.AppVersionLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AppVersionLogMapper extends BaseMapper<AppVersionLog> {


    @Select({
            "select * from app_version_log a where a.`key` = #{key} order by a.create_time desc limit 0,1"
    })
    AppVersionLog versionLogByKey(@Param("key") String key);

}

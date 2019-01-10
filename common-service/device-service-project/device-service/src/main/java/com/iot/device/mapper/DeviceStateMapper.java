package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.mapper.sql.DeviceStateSqlProvider;
import com.iot.device.mapper.sql.DeviceStatusSqlProvider;
import com.iot.device.model.DeviceState;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Mapper
public interface DeviceStateMapper extends BaseMapper<DeviceState> {

    @SelectProvider(type = DeviceStateSqlProvider.class,method = "selectPropertyName")
    List<Map<String,Object>> selectPropertyName(Map<String, Object> params);

    /**
     *  查询传感器报表
     * @param params
     * @return
     */
    @SelectProvider(type = DeviceStateSqlProvider.class,method = "selectDataReport")
    List<Map<String,Object>> selectDataReport(Map<String, Object> params);
}
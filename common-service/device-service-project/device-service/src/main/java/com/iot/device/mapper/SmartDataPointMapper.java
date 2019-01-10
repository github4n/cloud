package com.iot.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.mapper.sql.SmartDataPointSqlProvider;
import com.iot.device.model.SmartDataPoint;


@Mapper
public interface SmartDataPointMapper extends BaseMapper<SmartDataPoint>{

	@SelectProvider(type = SmartDataPointSqlProvider.class,method = "updateByIdAndDataPointId")
	boolean updateByIdAndDataPointId(@Param("et") SmartDataPoint sm);
}

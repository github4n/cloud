package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.mapper.sql.DataPointSqlProvider;
import com.iot.device.model.DataPoint;
import com.iot.device.vo.rsp.DeviceFunResp;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Mapper
public interface DataPointMapper extends BaseMapper<DataPoint> {

    @SelectProvider(type = DataPointSqlProvider.class,method = "selectDataPointsByProductId")
    List<DeviceFunResp> selectDataPointsByProductId(@Param("productId") Long productId);
    @SelectProvider(type = DataPointSqlProvider.class,method = "findDataPointListByDeviceTypeId")
	List<DeviceFunResp> findDataPointListByDeviceTypeId(@Param("deviceTypeId")Long deviceTypeId);
}
package com.iot.building.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.building.device.vo.DeviceRemoteTypeResp;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 */
@Mapper
public interface DeviceRemoteTypeMapper extends BaseMapper<DeviceRemoteTypeResp> {
    @Select({"<script>" +
            " select * " +
            " from device_remote_type " +
            " where data_status = 1 " +
            " <if test=\"tenantId != null\"> and tenant_id=#{tenantId}</if> " +
            " </script>"})
    List<DeviceRemoteTypeResp> listAll(@Param("tenantId") Long tenantId);
}

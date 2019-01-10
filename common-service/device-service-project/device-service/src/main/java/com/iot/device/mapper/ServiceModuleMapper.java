package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.ServiceModule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 模组表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Mapper
public interface ServiceModuleMapper extends BaseMapper<ServiceModule> {

    @Select({
            "select a.*,b.id as otherId,b.status as status from service_module a",
            "JOIN device_type_to_service_module b on b.service_module_id=a.id",
            "WHERE b.tenant_id=#{tenantId} and ISNULL(a.parent_id) and a.tenant_id=#{tenantId} and b.device_type_id = #{deviceTypeId} order by b.create_time desc"
    })
    List<ServiceModule> listByDeviceTypeId(@Param("deviceTypeId") Long deviceTypeId,@Param("tenantId") Long tenantId);


    @Select({
            "select b.*,a.id as otherId from product_to_service_module a ",
            "join service_module b on a.service_module_id=b.id where a.product_id=#{productId}"
    })
    List<ServiceModule> listByProductId(@Param("productId") Long productId);


}

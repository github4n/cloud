package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.StyleTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
public interface StyleTemplateMapper extends BaseMapper<StyleTemplate> {

    @Select({
            "select b.*,a.id as otherId from device_type_to_style a ",
            "join style_template b on a.style_template_id=b.id",
            "where a.device_type_id=#{deviceTypeId} order by a.create_time desc"
    })
    List<StyleTemplate> listByDeviceTypeId(@Param("deviceTypeId") Long deviceTypeId);

    @Select({
            "select b.*,a.id as otherId from service_style_to_template a " ,
            "join style_template b on a.style_template_id=b.id ",
            "where a.module_style_id=#{moduleStyleId} and a.tenant_id=#{tenantId} order by a.create_time desc"
    })
    List<StyleTemplate> listByModuleStyleId(@Param("moduleStyleId") Long moduleStyleId,@Param("tenantId") Long tenantId);


    @Select({
            "select b.*,a.id as otherId from product_to_style a ",
            "join style_template b on a.style_template_id=b.id where a.product_id=#{productId} order by a.create_time desc"
    })
    List<StyleTemplate> listByProductId(@Param("productId") Long productId);

}
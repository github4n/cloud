package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.ServiceModuleStyle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 模组-样式表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Mapper
public interface ServiceModuleStyleMapper extends BaseMapper<ServiceModuleStyle> {

    @Select({
            "select * from service_module a",
            "join service_module_style b on b.service_module_id=a.id",
            "join service_style_to_template c on c.module_style_id=b.id",
            "where c.style_template_id=#{styleTemplateId} and c.tenant_id=#{tenantId} and b.tenant_id=#{tenantId} and a.tenant_id=#{tenantId} and isnull(a.parent_id)"
    })
    List<ServiceModuleStyle> listByStyleTemplateId(@Param("styleTemplateId") Long styleTemplateId,@Param("tenantId") Long tenantId);

}

package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.ModuleActionToProperty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 模组-方法-to-参数表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Mapper
public interface ModuleActionToPropertyMapper extends BaseMapper<ModuleActionToProperty> {

    @Select("select module_property_id from module_action_to_property where module_action_id = #{actionId} and param_type = 1")
    List<Long> getModulePropertyIds(@Param("actionId") Long actionId);

}

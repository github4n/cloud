package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.ModuleEventToProperty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 模组-事件-to-属性表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@Mapper
public interface ModuleEventToPropertyMapper extends BaseMapper<ModuleEventToProperty> {

    @Select("select event_property_id from module_event_to_property where module_event_id = #{eventId} and param_type = 0")
    List<Long> getModulePropertyIds(@Param("eventId") Long eventId);

}

package com.iot.building.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.building.device.vo.DeviceRemoteControlSceneTemplate;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 */
@Mapper
public interface DeviceRemoteControlSceneTemplateMapper extends BaseMapper<DeviceRemoteControlSceneTemplate> {


    @Delete("delete from device_remote_control_scene_template  WHERE control_template_id =#{deviceRemoteControlTemplateId}")
    void deleteByControlTemplateTd(Long deviceRemoteControlTemplateId);

    @Select({"select * " +
            "from device_remote_control_scene_template " +
            "where control_template_id = #{controlTemplateId,jdbcType=BIGINT} order by sort asc"})
    List<DeviceRemoteControlSceneTemplate> findByDeviceRemoteControlTemplateId(Long controlTemplateId);


    @Update("update device_remote_control_scene_template  set data_status = 0 WHERE control_template_id =#{controlTemplateId}")
    void deleteDeviceRemoteControlSceneTemplateById(Long controlTemplateId);
}

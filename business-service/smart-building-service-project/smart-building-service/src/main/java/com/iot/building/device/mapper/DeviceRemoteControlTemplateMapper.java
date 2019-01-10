package com.iot.building.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.building.device.vo.DeviceRemoteControlTemplate;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 */
@Mapper
public interface DeviceRemoteControlTemplateMapper extends BaseMapper<DeviceRemoteControlTemplate> {
    @Insert("INSERT INTO device_remote_control_template ( " +
            "   business_type_id , " +
            "   function , " +
            "   type, " +
            "   press , " +
            "   remote_id , " +
            "   key_code , " +
         //   "   default_value  , " +
            "   data_status , " +
            "   event_status   " +
            ") " +
            "VALUES " +
            "  ( " +
            "    #{deviceRemoteControlTemplate.businessTypeId,jdbcType=BIGINT},  " +
            "    #{deviceRemoteControlTemplate.function,jdbcType=VARCHAR},  " +
            "    #{deviceRemoteControlTemplate.type,jdbcType=VARCHAR},  " +
            "    #{deviceRemoteControlTemplate.press,jdbcType=INTEGER},  " +
            "    #{deviceRemoteControlTemplate.remoteId,jdbcType=BIGINT},  " +
            "    #{deviceRemoteControlTemplate.keyCode,jdbcType=VARCHAR},  " +
        //    "    #{deviceRemoteControlTemplate.defaultValue,jdbcType=VARCHAR},  " +
            "    1 , " +
            "    #{deviceRemoteControlTemplate.eventStatus,jdbcType=INTEGER}  " +
            "  ) " )
    @Options(useGeneratedKeys = true,keyColumn = "deviceRemoteControlTemplate.id",useCache = false)
    @SelectKey(before=false,keyProperty="deviceRemoteControlTemplate.id",statementType= StatementType.PREPARED
            ,statement="SELECT LAST_INSERT_ID() AS id", resultType =
            Long.class)
    Long insertGetId(@Param("deviceRemoteControlTemplate")DeviceRemoteControlTemplate deviceRemoteControlTemplate);

    @Select({"select * " +
            "from device_remote_control_template " +
            "where remote_id = #{remoteId,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT} order by key_code asc"})
    List<DeviceRemoteControlTemplate> selectByRemoteId(@Param("tenantId")Long tenantId, @Param("remoteId")Long remoteId);


    @Update("update  device_remote_control_template  set data_status = 0 WHERE id =#{id}")
    void deleteDeviceRemoteControlTemplateById(@Param("id")Long id);
}

package com.iot.building.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.mapping.StatementType;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.building.device.vo.DeviceRemoteControlReq;
import com.iot.building.device.vo.DeviceRemoteControlResp;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 */
@Mapper
public interface DeviceRemoteControlMapper extends BaseMapper<DeviceRemoteControlReq> {

    @Delete("delete from device_remote_control  WHERE device_id =#{deviceId}")
    void removeByDeviceId(String deviceId);

    @Insert("INSERT INTO device_remote_control ( " +
            "   business_type_id  , " +
            "   function  , " +
            "   type  , " +
            "   press  , " +
            "   device_id  , " +
            "   remote_id  , " +
            "   key_code  , " +
            "   default_value  , " +
            "   data_status  , " +
            "   event_status   " +
            ") " +
            "VALUES " +
            "  ( " +
            "    #{deviceRemoteControl.businessTypeId,jdbcType=BIGINT},  " +
            "    #{deviceRemoteControl.function,jdbcType=VARCHAR},  " +
            "    #{deviceRemoteControl.type,jdbcType=VARCHAR},  " +
            "    #{deviceRemoteControl.press,jdbcType=INTEGER}  " +
            "    #{deviceRemoteControl.deviceId,jdbcType=VARCHAR},  " +
            "    #{deviceRemoteControl.remoteId,jdbcType=BIGINT},  " +
            "    #{deviceRemoteControl.keyCode,jdbcType=VARCHAR},  " +
            "    #{deviceRemoteControl.defaultValue,jdbcType=VARCHAR},  " +
            "    1 , " +
            "    #{deviceRemoteControl.eventStatus,jdbcType=INTEGER}  " +
            "  ) " )
    @Options(useGeneratedKeys = true,keyColumn = "deviceRemoteControl.id",useCache = false)
    @SelectKey(before=false,keyProperty="deviceRemoteControl.id",statementType= StatementType.PREPARED
            ,statement="SELECT LAST_INSERT_ID() AS id", resultType =
            Long.class)
    Long insertGetId(@Param("deviceRemoteControl")DeviceRemoteControlReq deviceRemoteControl);
    
    @Select({
    	"SELECT                                                     ", 
    	"id,							    ",
    	"device_type_id as deviceTypeId,			    ",
    	"key_code as keyCode,					    ",
    	"type,							    ",
    	"default_value as defaultValue,				    ",
    	"event_status as event				    ",
    	"FROM device_remote_control 				    ",
    	"WHERE device_type_id = #{deviceTypeId}    "
    })
    List<DeviceRemoteControlResp> findRemoteControlByDeviceType(Long deviceTypeId);
}

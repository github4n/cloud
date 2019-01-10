package com.iot.device.mapper.ota;

import com.iot.device.model.ota.UpgradeDeviceVersion;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： nongchongwei
 * 创建时间：2018年07月24日 16:58
 * 修改人： nongchongwei
 * 修改时间：2018年07月24日 16:58
 */
public interface UpgradeDeviceVersionMapper {
    @Delete({
        "delete from ota_device_version",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into ota_device_version (id, device_id, ",
        "fw_type, version, ",
        "create_time, update_time)",
        "values (#{id,jdbcType=BIGINT}, #{deviceId,jdbcType=VARCHAR}, ",
        "#{fwType,jdbcType=TINYINT}, #{version,jdbcType=VARCHAR}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(UpgradeDeviceVersion record);

    @Select({
        "select",
        "id, device_id, fw_type, version, create_time, update_time",
        "from ota_device_version",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="device_id", property="deviceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="fw_type", property="fwType", jdbcType=JdbcType.TINYINT),
        @Result(column="version", property="version", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    UpgradeDeviceVersion selectByPrimaryKey(Long id);


    @Update({
        "update ota_device_version",
        "set device_id = #{deviceId,jdbcType=VARCHAR},",
          "fw_type = #{fwType,jdbcType=TINYINT},",
          "version = #{version,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UpgradeDeviceVersion record);

    @Select({
            "<script>",
            "SELECT device_id,version FROM ota_device_version",
            "where version in",
            "<foreach item='version' index='index' collection='versionList' open='(' separator=',' close=')'>",
            "#{version}",
            "</foreach>",
            "</script>"
    })
    @Results({
            @Result(column = "device_id", property = "deviceId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "version", property = "version", jdbcType = JdbcType.VARCHAR)

    })
    @MapKey("deviceId")
    @ResultType(Map.class)
    Map<String, Map<String,String>> getdeviceIdVersionMap(@Param("versionList") List<String> versionList);



    @Insert({ "<script>",
            "insert into ota_device_version (id, device_id,fw_type,version,create_time,update_time) values ",
            "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">",
            "(#{item.id,jdbcType=BIGINT}, #{item.deviceId,jdbcType=VARCHAR},#{item.fwType,jdbcType=TINYINT}, #{item.version,jdbcType=VARCHAR}," ,
             "#{item.createTime,jdbcType=TIMESTAMP}, #{item.updateTime,jdbcType=TIMESTAMP} )",
            "</foreach>",
            "</script>" })
    int batchInsertUpgradeDeviceVersion(List<UpgradeDeviceVersion> upgradeDeviceVersionList);


    @Delete({
            "<script>",
            "delete from ota_device_version",
            "where device_id in",
            "<foreach item='deviceId' index='index' collection='deviceIdList' open='(' separator=',' close=')'>",
            "#{deviceId}",
            "</foreach>",
            "</script>"
    })
    int batchDeleteUpgradeDeviceVersion(@Param("deviceIdList") List<String> deviceIdList);
}
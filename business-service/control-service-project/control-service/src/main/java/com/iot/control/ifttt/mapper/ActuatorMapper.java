package com.iot.control.ifttt.mapper;

import com.iot.control.ifttt.entity.Actuator;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface ActuatorMapper {
    @Delete({
            "delete from ifttt_actuator",
            "where id = #{id,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(@Param("id") Long id, @Param("tenantId") Long tenantId);

    @Insert({
            "insert into ifttt_actuator (id, name, ",
            "label, properties, ",
            "device_id, rule_id, ",
            "position, device_type, ",
            "idx, type, delay, ",
            "tenant_id, product_id, object_id)",
            "values (#{id,jdbcType=BIGINT}, #{name,jdbcType=VARCHAR}, ",
            "#{label,jdbcType=VARCHAR}, #{properties,jdbcType=VARCHAR}, ",
            "#{deviceId,jdbcType=VARCHAR}, #{ruleId,jdbcType=BIGINT}, ",
            "#{position,jdbcType=VARCHAR}, #{deviceType,jdbcType=VARCHAR}, ",
            "#{idx,jdbcType=INTEGER}, #{type,jdbcType=VARCHAR}, #{delay,jdbcType=INTEGER}, ",
            "#{tenantId,jdbcType=BIGINT}, #{productId,jdbcType=BIGINT}, #{objectId,jdbcType=VARCHAR})"
    })
    int insert(Actuator record);

    @InsertProvider(type = ActuatorSqlProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertSelective(Actuator record);

    @Select({
            "select",
            "id, name, label, properties, device_id, rule_id, position, device_type, idx, ",
            "type, delay, tenant_id, product_id, object_id",
            "from ifttt_actuator",
            "where id = #{id,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="name", property="name", jdbcType= JdbcType.VARCHAR),
            @Result(column="label", property="label", jdbcType= JdbcType.VARCHAR),
            @Result(column="properties", property="properties", jdbcType= JdbcType.VARCHAR),
            @Result(column="device_id", property="deviceId", jdbcType= JdbcType.VARCHAR),
            @Result(column="rule_id", property="ruleId", jdbcType= JdbcType.BIGINT),
            @Result(column="position", property="position", jdbcType= JdbcType.VARCHAR),
            @Result(column="device_type", property="deviceType", jdbcType= JdbcType.VARCHAR),
            @Result(column="idx", property="idx", jdbcType= JdbcType.INTEGER),
            @Result(column="type", property="type", jdbcType= JdbcType.VARCHAR),
            @Result(column="delay", property="delay", jdbcType= JdbcType.INTEGER),
            @Result(column="tenant_id", property="tenantId", jdbcType= JdbcType.BIGINT),
            @Result(column="product_id", property="productId", jdbcType= JdbcType.BIGINT),
            @Result(column="object_id", property="objectId", jdbcType= JdbcType.VARCHAR)
    })
    Actuator selectByPrimaryKey(@Param("id") Long id, @Param("tenantId") Long tenantId);

    @UpdateProvider(type = ActuatorSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Actuator record);

    @Update({
            "update ifttt_actuator",
            "set name = #{name,jdbcType=VARCHAR},",
            "label = #{label,jdbcType=VARCHAR},",
            "properties = #{properties,jdbcType=VARCHAR},",
            "device_id = #{deviceId,jdbcType=VARCHAR},",
            "rule_id = #{ruleId,jdbcType=BIGINT},",
            "position = #{position,jdbcType=VARCHAR},",
            "device_type = #{deviceType,jdbcType=VARCHAR},",
            "idx = #{idx,jdbcType=INTEGER},",
            "type = #{type,jdbcType=VARCHAR},",
            "delay = #{delay,jdbcType=INTEGER},",
            "tenant_id = #{tenantId,jdbcType=BIGINT},",
            "product_id = #{productId,jdbcType=BIGINT},",
            "object_id = #{objectId,jdbcType=VARCHAR}",
            "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Actuator record);

    /////////////////////////////////////////以下为手动添加///////////////////////////////////////////////

    @Select({
            "select",
            "id, name, label, properties, device_id, rule_id, position, device_type, idx, ",
            "type, delay, tenant_id, product_id, object_id",
            "from ifttt_actuator",
            "where rule_id = #{ruleId,jdbcType=BIGINT} "
    })
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="name", property="name", jdbcType= JdbcType.VARCHAR),
            @Result(column="label", property="label", jdbcType= JdbcType.VARCHAR),
            @Result(column="properties", property="properties", jdbcType= JdbcType.VARCHAR),
            @Result(column="device_id", property="deviceId", jdbcType= JdbcType.VARCHAR),
            @Result(column="rule_id", property="ruleId", jdbcType= JdbcType.BIGINT),
            @Result(column="position", property="position", jdbcType= JdbcType.VARCHAR),
            @Result(column="device_type", property="deviceType", jdbcType= JdbcType.VARCHAR),
            @Result(column="idx", property="idx", jdbcType= JdbcType.INTEGER),
            @Result(column="type", property="type", jdbcType= JdbcType.VARCHAR),
            @Result(column="delay", property="delay", jdbcType= JdbcType.INTEGER),
            @Result(column="tenant_id", property="tenantId", jdbcType= JdbcType.BIGINT),
            @Result(column="product_id", property="productId", jdbcType= JdbcType.BIGINT),
            @Result(column="object_id", property="objectId", jdbcType= JdbcType.VARCHAR)
    })
    List<Actuator> selectByRuleId(@Param("ruleId") Long ruleId, @Param("tenantId") Long tenantId);

    @Delete({
            "delete from ifttt_actuator",
            "where rule_id = #{ruleId,jdbcType=BIGINT} and idx=#{idx,jdbcType=INTEGER} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    int delActuatorByRuleAndIdx(@Param("ruleId") Long ruleId, @Param("idx") Integer idx, @Param("tenantId") Long tenantId);

    @Delete({
            "delete from ifttt_actuator",
            "where rule_id = #{ruleId,jdbcType=BIGINT}"
    })
    int deleteByRuleId(@Param("ruleId") Long ruleId, @Param("tenantId") Long tenantId);

    @Select({
            "select",
            "id, name, label, properties, device_id, rule_id, position, device_type, idx, ",
            "type, delay, tenant_id, product_id, object_id",
            "from ifttt_actuator",
            "where device_id = #{deviceId,jdbcType=VARCHAR} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="name", property="name", jdbcType= JdbcType.VARCHAR),
            @Result(column="label", property="label", jdbcType= JdbcType.VARCHAR),
            @Result(column="properties", property="properties", jdbcType= JdbcType.VARCHAR),
            @Result(column="device_id", property="deviceId", jdbcType= JdbcType.VARCHAR),
            @Result(column="rule_id", property="ruleId", jdbcType= JdbcType.BIGINT),
            @Result(column="position", property="position", jdbcType= JdbcType.VARCHAR),
            @Result(column="device_type", property="deviceType", jdbcType= JdbcType.VARCHAR),
            @Result(column="idx", property="idx", jdbcType= JdbcType.INTEGER),
            @Result(column="type", property="type", jdbcType= JdbcType.VARCHAR),
            @Result(column="delay", property="delay", jdbcType= JdbcType.INTEGER),
            @Result(column="tenant_id", property="tenantId", jdbcType= JdbcType.BIGINT),
            @Result(column="product_id", property="productId", jdbcType= JdbcType.BIGINT),
            @Result(column="object_id", property="objectId", jdbcType= JdbcType.VARCHAR)
    })
    List<Actuator> selectActuatorsByDeviceId(@Param("deviceId") String deviceId, @Param("tenantId") Long tenantId);

    @Delete({
            "delete from ifttt_actuator",
            "where device_id = #{deviceId,jdbcType=VARCHAR} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    int deleteByDeviceId(@Param("deviceId") String deviceId, @Param("tenantId") Long tenantId);

    @Select({
            "select",
            "id, name, label, properties, device_id, rule_id, position, device_type, idx, ",
            "type, delay, tenant_id, product_id, object_id",
            "from ifttt_actuator",
            "where type = #{type,jdbcType=VARCHAR} and object_id = #{objectId,jdbcType=VARCHAR} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="name", property="name", jdbcType= JdbcType.VARCHAR),
            @Result(column="label", property="label", jdbcType= JdbcType.VARCHAR),
            @Result(column="properties", property="properties", jdbcType= JdbcType.VARCHAR),
            @Result(column="device_id", property="deviceId", jdbcType= JdbcType.VARCHAR),
            @Result(column="rule_id", property="ruleId", jdbcType= JdbcType.BIGINT),
            @Result(column="position", property="position", jdbcType= JdbcType.VARCHAR),
            @Result(column="device_type", property="deviceType", jdbcType= JdbcType.VARCHAR),
            @Result(column="idx", property="idx", jdbcType= JdbcType.INTEGER),
            @Result(column="type", property="type", jdbcType= JdbcType.VARCHAR),
            @Result(column="delay", property="delay", jdbcType= JdbcType.INTEGER),
            @Result(column="tenant_id", property="tenantId", jdbcType= JdbcType.BIGINT),
            @Result(column="product_id", property="productId", jdbcType= JdbcType.BIGINT),
            @Result(column="object_id", property="objectId", jdbcType= JdbcType.VARCHAR)
    })
    List<Actuator> selectActuatorsByObjectId(@Param("objectId") String objectId, @Param("type") String type, @Param("tenantId") Long tenantId);

    @Delete({
            "delete from ifttt_actuator",
            "where type = #{type,jdbcType=VARCHAR} and object_id = #{objectId,jdbcType=VARCHAR} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    int deleteByObjectId(@Param("objectId") String objectId, @Param("type") String type, @Param("tenantId") Long tenantId);
}
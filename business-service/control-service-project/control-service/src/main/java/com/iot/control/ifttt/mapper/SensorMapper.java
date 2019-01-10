package com.iot.control.ifttt.mapper;

import com.iot.control.ifttt.entity.Sensor;
import com.iot.control.ifttt.vo.SensorVo;
import com.iot.control.ifttt.vo.req.GetSensorReq;
import com.iot.control.ifttt.vo.res.SensorResp;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface SensorMapper {
    @Delete({
            "delete from ifttt_sensor",
            "where id = #{id,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(@Param("id") Long id, @Param("tenantId") Long tenantId);

    @Insert({
            "insert into ifttt_sensor (id, name, ",
            "label, properties, ",
            "device_id, rule_id, ",
            "position, device_type, ",
            "timing, idx, type, ",
            "delay, tenant_id, ",
            "product_id)",
            "values (#{id,jdbcType=BIGINT}, #{name,jdbcType=VARCHAR}, ",
            "#{label,jdbcType=VARCHAR}, #{properties,jdbcType=VARCHAR}, ",
            "#{deviceId,jdbcType=VARCHAR}, #{ruleId,jdbcType=BIGINT}, ",
            "#{position,jdbcType=VARCHAR}, #{deviceType,jdbcType=VARCHAR}, ",
            "#{timing,jdbcType=VARCHAR}, #{idx,jdbcType=INTEGER}, #{type,jdbcType=VARCHAR}, ",
            "#{delay,jdbcType=INTEGER}, #{tenantId,jdbcType=BIGINT}, ",
            "#{productId,jdbcType=BIGINT})"
    })
    int insert(Sensor record);

    @InsertProvider(type = SensorSqlProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertSelective(Sensor record);

    @Select({
            "select",
            "id, name, label, properties, device_id, rule_id, position, device_type, timing, ",
            "idx, type, delay, tenant_id, product_id",
            "from ifttt_sensor",
            "where id = #{id,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "label", property = "label", jdbcType = JdbcType.VARCHAR),
            @Result(column = "properties", property = "properties", jdbcType = JdbcType.VARCHAR),
            @Result(column = "device_id", property = "deviceId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "rule_id", property = "ruleId", jdbcType = JdbcType.BIGINT),
            @Result(column = "position", property = "position", jdbcType = JdbcType.VARCHAR),
            @Result(column = "device_type", property = "deviceType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "timing", property = "timing", jdbcType = JdbcType.VARCHAR),
            @Result(column = "idx", property = "idx", jdbcType = JdbcType.INTEGER),
            @Result(column = "type", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "delay", property = "delay", jdbcType = JdbcType.INTEGER),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "product_id", property = "productId", jdbcType = JdbcType.BIGINT)
    })
    Sensor selectByPrimaryKey(@Param("id") Long id, @Param("tenantId") Long tenantId);

    @UpdateProvider(type = SensorSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Sensor record);

    @Update({
            "update ifttt_sensor",
            "set name = #{name,jdbcType=VARCHAR},",
            "label = #{label,jdbcType=VARCHAR},",
            "properties = #{properties,jdbcType=VARCHAR},",
            "device_id = #{deviceId,jdbcType=VARCHAR},",
            "rule_id = #{ruleId,jdbcType=BIGINT},",
            "position = #{position,jdbcType=VARCHAR},",
            "device_type = #{deviceType,jdbcType=VARCHAR},",
            "timing = #{timing,jdbcType=VARCHAR},",
            "idx = #{idx,jdbcType=INTEGER},",
            "type = #{type,jdbcType=VARCHAR},",
            "delay = #{delay,jdbcType=INTEGER},",
            "tenant_id = #{tenantId,jdbcType=BIGINT},",
            "product_id = #{productId,jdbcType=BIGINT}",
            "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Sensor record);

    ////////////////////////////以下为手动生成////////////////////////////////////

    @Select({
            "select",
            "id, name, label, properties, device_id, rule_id, position, device_type, timing, ",
            "idx, type, delay, tenant_id, product_id",
            "from ifttt_sensor",
            "where rule_id = #{ruleId,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "label", property = "label", jdbcType = JdbcType.VARCHAR),
            @Result(column = "properties", property = "properties", jdbcType = JdbcType.VARCHAR),
            @Result(column = "device_id", property = "deviceId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "rule_id", property = "ruleId", jdbcType = JdbcType.BIGINT),
            @Result(column = "position", property = "position", jdbcType = JdbcType.VARCHAR),
            @Result(column = "device_type", property = "deviceType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "timing", property = "timing", jdbcType = JdbcType.VARCHAR),
            @Result(column = "idx", property = "idx", jdbcType = JdbcType.INTEGER),
            @Result(column = "type", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "delay", property = "delay", jdbcType = JdbcType.INTEGER),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "product_id", property = "productId", jdbcType = JdbcType.BIGINT)
    })
    List<Sensor> selectByRuleId(@Param("ruleId") Long ruleId, @Param("tenantId") Long tenantId);

    @Delete({
            "delete from ifttt_sensor",
            "where rule_id = #{ruleId,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    int deleteByRuleId(@Param("ruleId") Long ruleId, @Param("tenantId") Long tenantId);

    @Delete({
            "delete from ifttt_sensor",
            "where rule_id = #{ruleId,jdbcType=BIGINT} and idx = #{idx,jdbcType=INTEGER} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    int delSensorByRuleAndIdx(@Param("ruleId") Long ruleId, @Param("idx") Integer idx, @Param("tenantId") Long tenantId);

    @Update({
            "update ifttt_sensor",
            "set delay = #{delay,jdbcType=INTEGER}",
            "where rule_id = #{ruleId,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    int updateDelayByRuleId(@Param("ruleId") Long ruleId, @Param("delay") Integer delay, @Param("tenantId") Long tenantId);

    @SelectProvider(type = SensorSqlProvider.class, method = "getSensorByParams")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "label", property = "label", jdbcType = JdbcType.VARCHAR),
            @Result(column = "properties", property = "properties", jdbcType = JdbcType.VARCHAR),
            @Result(column = "device_id", property = "deviceId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "rule_id", property = "ruleId", jdbcType = JdbcType.BIGINT),
            @Result(column = "position", property = "position", jdbcType = JdbcType.VARCHAR),
            @Result(column = "device_type", property = "deviceType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "timing", property = "timing", jdbcType = JdbcType.VARCHAR),
            @Result(column = "idx", property = "idx", jdbcType = JdbcType.INTEGER),
            @Result(column = "type", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "delay", property = "delay", jdbcType = JdbcType.INTEGER),
            @Result(column = "product_id", property = "productId", jdbcType = JdbcType.BIGINT)
    })
    List<SensorResp> getSensorByParams(GetSensorReq req);

    @Select({
            "select",
            "id, name, label, properties, device_id, rule_id, position, device_type, timing, ",
            "idx, type, delay, tenant_id, product_id",
            "from ifttt_sensor",
            "where device_id = #{deviceId,jdbcType=VARCHAR} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "label", property = "label", jdbcType = JdbcType.VARCHAR),
            @Result(column = "properties", property = "properties", jdbcType = JdbcType.VARCHAR),
            @Result(column = "device_id", property = "deviceId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "rule_id", property = "ruleId", jdbcType = JdbcType.BIGINT),
            @Result(column = "position", property = "position", jdbcType = JdbcType.VARCHAR),
            @Result(column = "device_type", property = "deviceType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "timing", property = "timing", jdbcType = JdbcType.VARCHAR),
            @Result(column = "idx", property = "idx", jdbcType = JdbcType.INTEGER),
            @Result(column = "type", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "delay", property = "delay", jdbcType = JdbcType.INTEGER),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "product_id", property = "productId", jdbcType = JdbcType.BIGINT)
    })
    List<Sensor> findSensorsByDeviceId(@Param("deviceId") String deviceId, @Param("tenantId") Long tenantId);

    @Select({
            "select a.rule_id from ifttt_sensor a " +
            "LEFT JOIN ifttt_rule b on a.rule_id=b.id " +
            "where a.device_id=#{deviceId,jdbcType=VARCHAR} " +
             "and b.template_flag=0 and b.rule_type=0 "
    })
    List<Long> getRuleIdByDeviceId(@Param("deviceId") String deviceId);

    @Delete({
            "delete from ifttt_sensor",
            "where device_id = #{deviceId, jdbcType=VARCHAR} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    int deleteByDeviceId(@Param("deviceId") String deviceId, @Param("tenantId") Long tenantId);

    @Select({
            "select a.id, a.properties, a.rule_id as ruleId from ifttt_sensor a   ",
            "left join ifttt_rule b on a.rule_id=b.id 				  ",
            "where a.type= #{type} and b.`status`=#{status} and b.tenant_id=${tenantId}			  "
    })
    List<SensorVo> getTriggerProperties(@Param("type") String type, @Param("status") String status, @Param("tenantId") Long tenantId);

    @Select({
            "SELECT                                                    ",
            "a.id AS id,						   ",
            "a.NAME AS NAME,					   ",
            "a.label AS label,					   ",
            "a.properties AS properties,				   ",
            "a.STATUS AS STATUS,					   ",
            "a.device_id AS deviceId,				   ",
            "a.rule_id AS ruleId,					   ",
            "a.type AS type						   ",
            "FROM							   ",
            "ifttt_sensor a 					   ",
            "left join ifttt_rule b on a.rule_id=b.id 		   ",
            "WHERE							   ",
            "a.properties LIKE CONCAT(CONCAT('%', #{properties}), '%') ",
            "and b.`status`=#{status}			   ",
            "and b.tenant_id=${tenantId}				   "
    })
    List<SensorVo> getSensorVoByProperties(@Param("value") String value, @Param("status") String status, @Param("tenantId") Long tenantId);

    @Select({
            "SELECT                                                           ",
            "s.id AS id,							  ",
            "s.NAME AS NAME,						  ",
            "s.label AS label,						  ",
            "s.properties AS properties,					  ",
            "s.STATUS AS STATUS,						  ",
            "s.device_id AS deviceId,					  ",
            "s.rule_id AS ruleId,						  ",
            "s.type AS type							  ",
            "FROM lds_ifttt_sensor s 					  ",
            "left join lds_ifttt_rule r on s.rule_id=r.id			  ",
            "WHERE s.properties LIKE CONCAT(CONCAT('%', #{value}), '%')  ",
            "and s.type=#{type}						  ",
            "and r.space_id=#{spaceId}					  "
    })
    List<SensorVo> getSensorVoByPropType(@Param("value") String value, @Param("spaceId") Long spaceId, @Param("type") String type);

    @Select({" SELECT properties " +
            " FROM " +
            " ifttt_sensor " +
            " WHERE rule_id = #{ruleId} " +
            "AND (type = \"sunrise\" OR type = \"sunset\")"
    })
    List<String> findPropertiesByruleId(Long ruleId);

}
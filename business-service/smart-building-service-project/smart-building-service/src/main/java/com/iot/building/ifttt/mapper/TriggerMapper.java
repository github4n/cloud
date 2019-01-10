package com.iot.building.ifttt.mapper;

import com.iot.building.ifttt.entity.Trigger;
import com.iot.building.ifttt.vo.TriggerVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface TriggerMapper {
    @Delete({
            "delete from ifttt_trigger",
            "where id = #{id,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(@Param("id") Long id, @Param("tenantId") Long tenantId);

    @Insert({
            "insert into ifttt_trigger (id, line_id, ",
            "source_label, start, ",
            "destination_label, end, ",
            "invocation_policy, status_trigger, ",
            "rule_id, tenant_id)",
            "values (#{id,jdbcType=BIGINT}, #{lineId,jdbcType=VARCHAR}, ",
            "#{sourceLabel,jdbcType=VARCHAR}, #{start,jdbcType=BIGINT}, ",
            "#{destinationLabel,jdbcType=VARCHAR}, #{end,jdbcType=BIGINT}, ",
            "#{invocationPolicy,jdbcType=INTEGER}, #{statusTrigger,jdbcType=VARCHAR}, ",
            "#{ruleId,jdbcType=BIGINT}, #{tenantId,jdbcType=BIGINT})"
    })
    int insert(Trigger record);

    @InsertProvider(type = TriggerSqlProvider.class, method = "insertSelective")
    int insertSelective(Trigger record);

    @Select({
            "select",
            "id, line_id, source_label, start, destination_label, end, invocation_policy, ",
            "status_trigger, rule_id, tenant_id",
            "from ifttt_trigger",
            "where id = #{id,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "line_id", property = "lineId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "source_label", property = "sourceLabel", jdbcType = JdbcType.VARCHAR),
            @Result(column = "start", property = "start", jdbcType = JdbcType.BIGINT),
            @Result(column = "destination_label", property = "destinationLabel", jdbcType = JdbcType.VARCHAR),
            @Result(column = "end", property = "end", jdbcType = JdbcType.BIGINT),
            @Result(column = "invocation_policy", property = "invocationPolicy", jdbcType = JdbcType.INTEGER),
            @Result(column = "status_trigger", property = "statusTrigger", jdbcType = JdbcType.VARCHAR),
            @Result(column = "rule_id", property = "ruleId", jdbcType = JdbcType.BIGINT),
            @Result(column = "tenant_d", property = "tenantId", jdbcType = JdbcType.BIGINT)
    })
    Trigger selectByPrimaryKey(@Param("id") Long id, @Param("tenantId") Long tenantId);

    @UpdateProvider(type = TriggerSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Trigger record);

    @Update({
            "update ifttt_trigger",
            "set line_id = #{lineId,jdbcType=VARCHAR},",
            "source_label = #{sourceLabel,jdbcType=VARCHAR},",
            "start = #{start,jdbcType=BIGINT},",
            "destination_label = #{destinationLabel,jdbcType=VARCHAR},",
            "end = #{end,jdbcType=BIGINT},",
            "invocation_policy = #{invocationPolicy,jdbcType=INTEGER},",
            "status_trigger = #{statusTrigger,jdbcType=VARCHAR},",
            "rule_id = #{ruleId,jdbcType=BIGINT},",
            "tenant_id = #{tenantId,jdbcType=BIGINT}",
            "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Trigger record);

    ///////////////////////////////////////以下为手动添加//////////////////////////////////////////////

    @Select({
            "select",
            "id, line_id, source_label, start, destination_label, end, invocation_policy, ",
            "status_trigger, rule_id, tenant_id",
            "from ifttt_trigger",
            "where rule_id = #{ruleId,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "line_id", property = "lineId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "source_label", property = "sourceLabel", jdbcType = JdbcType.VARCHAR),
            @Result(column = "start", property = "start", jdbcType = JdbcType.BIGINT),
            @Result(column = "destination_label", property = "destinationLabel", jdbcType = JdbcType.VARCHAR),
            @Result(column = "end", property = "end", jdbcType = JdbcType.BIGINT),
            @Result(column = "invocation_policy", property = "invocationPolicy", jdbcType = JdbcType.INTEGER),
            @Result(column = "status_trigger", property = "statusTrigger", jdbcType = JdbcType.VARCHAR),
            @Result(column = "rule_id", property = "ruleId", jdbcType = JdbcType.BIGINT),
            @Result(column = "tenant_d", property = "tenantId", jdbcType = JdbcType.BIGINT)
    })
    List<TriggerVo> selectByRuleId(@Param("ruleId") Long ruleId, @Param("tenantId") Long tenantId);


    @Delete({
            "delete from ifttt_trigger",
            "where rule_id = #{ruleId,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    int deleteByRuleId(@Param("ruleId") Long ruleId, @Param("tenantId") Long tenantId);
}
package com.iot.building.ifttt.mapper;

import com.iot.building.ifttt.entity.Relation;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import java.util.List;

public interface RelationMapper {
    @Delete({
            "delete from ifttt_relation",
            "where id = #{id,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(@Param("id") Long id, @Param("tenantId") Long tenantId);

    @Insert({
            "insert into ifttt_relation (id, label, ",
            "type, parent_labels, ",
            "combinations, rule_id, ",
            "position, tenant_id)",
            "values (#{id,jdbcType=BIGINT}, #{label,jdbcType=VARCHAR}, ",
            "#{type,jdbcType=VARCHAR}, #{parentLabels,jdbcType=VARCHAR}, ",
            "#{combinations,jdbcType=VARCHAR}, #{ruleId,jdbcType=BIGINT}, ",
            "#{position,jdbcType=VARCHAR}, #{tenantId,jdbcType=BIGINT})"
    })
    int insert(Relation record);

    @InsertProvider(type = RelationSqlProvider.class, method = "insertSelective")
    int insertSelective(Relation record);

    @Select({
            "select",
            "id, label, type, parent_labels, combinations, rule_id, position, tenant_id",
            "from ifttt_relation",
            "where id = #{id,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "label", property = "label", jdbcType = JdbcType.VARCHAR),
            @Result(column = "type", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "parent_labels", property = "parentLabels", jdbcType = JdbcType.VARCHAR),
            @Result(column = "combinations", property = "combinations", jdbcType = JdbcType.VARCHAR),
            @Result(column = "rule_id", property = "ruleId", jdbcType = JdbcType.BIGINT),
            @Result(column = "position", property = "position", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT)
    })
    Relation selectByPrimaryKey(@Param("id") Long id, @Param("tenantId") Long tenantId);

    @UpdateProvider(type = RelationSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Relation record);

    @Update({
            "update ifttt_relation",
            "set label = #{label,jdbcType=VARCHAR},",
            "type = #{type,jdbcType=VARCHAR},",
            "parent_labels = #{parentLabels,jdbcType=VARCHAR},",
            "combinations = #{combinations,jdbcType=VARCHAR},",
            "rule_id = #{ruleId,jdbcType=BIGINT},",
            "position = #{position,jdbcType=VARCHAR},",
            "tenant_id = #{tenantId,jdbcType=BIGINT}",
            "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Relation record);

    ///////////////////////////////////////以下为手动添加///////////////////////////////////////////
    @Select({
            "select",
            "id, label, type, parent_labels, combinations, rule_id, position, tenant_id",
            "from ifttt_relation",
            "where rule_id = #{ruleId,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "label", property = "label", jdbcType = JdbcType.VARCHAR),
            @Result(column = "type", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "parent_labels", property = "parentLabels", jdbcType = JdbcType.VARCHAR),
            @Result(column = "combinations", property = "combinations", jdbcType = JdbcType.VARCHAR),
            @Result(column = "rule_id", property = "ruleId", jdbcType = JdbcType.BIGINT),
            @Result(column = "position", property = "position", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT)
    })
    List<Relation> selectByRuleId(@Param("ruleId") Long ruleId, @Param("tenantId") Long tenantId);

    @Delete({
            "delete from ifttt_relation",
            "where rule_id = #{ruleId,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT}"
    })
    int deleteByRuleId(@Param("ruleId") Long ruleId, @Param("tenantId") Long tenantId);
}
package com.iot.building.ifttt.mapper;


import com.iot.building.ifttt.entity.Relation;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class RelationSqlTobProvider {

    public String insertSelective(Relation record) {
        BEGIN();
        INSERT_INTO("tob_relation");

        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=BIGINT}");
        }

        if (record.getLabel() != null) {
            VALUES("label", "#{label,jdbcType=VARCHAR}");
        }

        if (record.getType() != null) {
            VALUES("type", "#{type,jdbcType=VARCHAR}");
        }

        if (record.getParentLabels() != null) {
            VALUES("parent_labels", "#{parentLabels,jdbcType=VARCHAR}");
        }

        if (record.getCombinations() != null) {
            VALUES("combinations", "#{combinations,jdbcType=VARCHAR}");
        }

        if (record.getRuleId() != null) {
            VALUES("rule_id", "#{ruleId,jdbcType=BIGINT}");
        }

        if (record.getPosition() != null) {
            VALUES("position", "#{position,jdbcType=VARCHAR}");
        }

        if (record.getTenantId() != null) {
            VALUES("tenant_id", "#{tenantId,jdbcType=BIGINT}");
        }

        return SQL();
    }

    public String updateByPrimaryKeySelective(Relation record) {
        BEGIN();
        UPDATE("ifttt_relation");

        if (record.getLabel() != null) {
            SET("label = #{label,jdbcType=VARCHAR}");
        }

        if (record.getType() != null) {
            SET("type = #{type,jdbcType=VARCHAR}");
        }

        if (record.getParentLabels() != null) {
            SET("parent_labels = #{parentLabels,jdbcType=VARCHAR}");
        }

        if (record.getCombinations() != null) {
            SET("combinations = #{combinations,jdbcType=VARCHAR}");
        }

        if (record.getRuleId() != null) {
            SET("rule_id = #{ruleId,jdbcType=BIGINT}");
        }

        if (record.getPosition() != null) {
            SET("position = #{position,jdbcType=VARCHAR}");
        }

        if (record.getTenantId() != null) {
            SET("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }

        WHERE("id = #{id,jdbcType=BIGINT}");

        return SQL();
    }
}
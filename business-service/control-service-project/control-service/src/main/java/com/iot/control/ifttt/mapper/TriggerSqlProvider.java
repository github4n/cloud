package com.iot.control.ifttt.mapper;

import com.iot.control.ifttt.entity.Trigger;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class TriggerSqlProvider {

    public String insertSelective(Trigger record) {
        BEGIN();
        INSERT_INTO("ifttt_trigger");

        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=BIGINT}");
        }

        if (record.getLineId() != null) {
            VALUES("line_id", "#{lineId,jdbcType=VARCHAR}");
        }

        if (record.getSourceLabel() != null) {
            VALUES("source_label", "#{sourceLabel,jdbcType=VARCHAR}");
        }

        if (record.getStart() != null) {
            VALUES("start", "#{start,jdbcType=BIGINT}");
        }

        if (record.getDestinationLabel() != null) {
            VALUES("destination_label", "#{destinationLabel,jdbcType=VARCHAR}");
        }

        if (record.getEnd() != null) {
            VALUES("end", "#{end,jdbcType=BIGINT}");
        }

        if (record.getInvocationPolicy() != null) {
            VALUES("invocation_policy", "#{invocationPolicy,jdbcType=INTEGER}");
        }

        if (record.getStatusTrigger() != null) {
            VALUES("status_trigger", "#{statusTrigger,jdbcType=VARCHAR}");
        }

        if (record.getRuleId() != null) {
            VALUES("rule_id", "#{ruleId,jdbcType=BIGINT}");
        }

        if (record.getTenantId() != null) {
            VALUES("tenant_id", "#{tenantId,jdbcType=BIGINT}");
        }

        return SQL();
    }

    public String updateByPrimaryKeySelective(Trigger record) {
        BEGIN();
        UPDATE("ifttt_trigger");

        if (record.getLineId() != null) {
            SET("line_id = #{lineId,jdbcType=VARCHAR}");
        }

        if (record.getSourceLabel() != null) {
            SET("source_label = #{sourceLabel,jdbcType=VARCHAR}");
        }

        if (record.getStart() != null) {
            SET("start = #{start,jdbcType=BIGINT}");
        }

        if (record.getDestinationLabel() != null) {
            SET("destination_label = #{destinationLabel,jdbcType=VARCHAR}");
        }

        if (record.getEnd() != null) {
            SET("end = #{end,jdbcType=BIGINT}");
        }

        if (record.getInvocationPolicy() != null) {
            SET("invocation_policy = #{invocationPolicy,jdbcType=INTEGER}");
        }

        if (record.getStatusTrigger() != null) {
            SET("status_trigger = #{statusTrigger,jdbcType=VARCHAR}");
        }

        if (record.getRuleId() != null) {
            SET("rule_id = #{ruleId,jdbcType=BIGINT}");
        }

        if (record.getTenantId() != null) {
            SET("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }

        WHERE("id = #{id,jdbcType=BIGINT}");

        return SQL();
    }
}
package com.iot.building.ifttt.mapper;


import com.iot.building.ifttt.entity.Trigger;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class TriggerSqlTobProvider {

    public String insertSelective(Trigger record) {
        BEGIN();
        INSERT_INTO("tob_trigger");

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
        if (record.getSensorPosition() != null) {
            VALUES("sensor_position", "#{sensorPosition,jdbcType=VARCHAR}");
        }
        if (record.getSensorType() != null) {
            VALUES("sensor_type", "#{sensorType,jdbcType=VARCHAR}");
        }
        if (record.getSensorProperties() != null) {
            VALUES("sensor_properties", "#{sensorProperties,jdbcType=VARCHAR}");
        }
        if (record.getSensorDeviceId() != null) {
            VALUES("sensor_device_id", "#{sensorDeviceId,jdbcType=VARCHAR}");
        }
        if (record.getActuctorPosition() != null) {
            VALUES("actuctor_position", "#{actuctorPosition,jdbcType=VARCHAR}");
        }
        if (record.getActuctorType() != null) {
            VALUES("actuctor_type", "#{actuctorType,jdbcType=VARCHAR}");
        }
        if (record.getActuctorProperties() != null) {
            VALUES("actuctor_properties", "#{actuctorProperties,jdbcType=VARCHAR}");
        }
        if (record.getActuctorDeviceId() != null) {
            VALUES("actuctor_device_id", "#{actuctorDeviceId,jdbcType=VARCHAR}");
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
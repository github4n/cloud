package com.iot.control.ifttt.mapper;

import com.iot.control.ifttt.entity.Actuator;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class ActuatorSqlProvider {

    public String insertSelective(Actuator record) {
        BEGIN();
        INSERT_INTO("ifttt_actuator");

        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=BIGINT}");
        }

        if (record.getName() != null) {
            VALUES("name", "#{name,jdbcType=VARCHAR}");
        }

        if (record.getLabel() != null) {
            VALUES("label", "#{label,jdbcType=VARCHAR}");
        }

        if (record.getProperties() != null) {
            VALUES("properties", "#{properties,jdbcType=VARCHAR}");
        }

        if (record.getDeviceId() != null) {
            VALUES("device_id", "#{deviceId,jdbcType=VARCHAR}");
        }

        if (record.getRuleId() != null) {
            VALUES("rule_id", "#{ruleId,jdbcType=BIGINT}");
        }

        if (record.getPosition() != null) {
            VALUES("position", "#{position,jdbcType=VARCHAR}");
        }

        if (record.getDeviceType() != null) {
            VALUES("device_type", "#{deviceType,jdbcType=VARCHAR}");
        }

        if (record.getIdx() != null) {
            VALUES("idx", "#{idx,jdbcType=INTEGER}");
        }

        if (record.getType() != null) {
            VALUES("type", "#{type,jdbcType=VARCHAR}");
        }

        if (record.getDelay() != null) {
            VALUES("delay", "#{delay,jdbcType=INTEGER}");
        }

        if (record.getTenantId() != null) {
            VALUES("tenant_id", "#{tenantId,jdbcType=BIGINT}");
        }

        if (record.getProductId() != null) {
            VALUES("product_id", "#{productId,jdbcType=BIGINT}");
        }

        if (record.getObjectId() != null) {
            VALUES("object_id", "#{objectId,jdbcType=VARCHAR}");
        }

        return SQL();
    }

    public String updateByPrimaryKeySelective(Actuator record) {
        BEGIN();
        UPDATE("ifttt_actuator");

        if (record.getName() != null) {
            SET("name = #{name,jdbcType=VARCHAR}");
        }

        if (record.getLabel() != null) {
            SET("label = #{label,jdbcType=VARCHAR}");
        }

        if (record.getProperties() != null) {
            SET("properties = #{properties,jdbcType=VARCHAR}");
        }

        if (record.getDeviceId() != null) {
            SET("device_id = #{deviceId,jdbcType=VARCHAR}");
        }

        if (record.getRuleId() != null) {
            SET("rule_id = #{ruleId,jdbcType=BIGINT}");
        }

        if (record.getPosition() != null) {
            SET("position = #{position,jdbcType=VARCHAR}");
        }

        if (record.getDeviceType() != null) {
            SET("device_type = #{deviceType,jdbcType=VARCHAR}");
        }

        if (record.getIdx() != null) {
            SET("idx = #{idx,jdbcType=INTEGER}");
        }

        if (record.getType() != null) {
            SET("type = #{type,jdbcType=VARCHAR}");
        }

        if (record.getDelay() != null) {
            SET("delay = #{delay,jdbcType=INTEGER}");
        }

        if (record.getTenantId() != null) {
            SET("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }

        if (record.getProductId() != null) {
            SET("product_id = #{productId,jdbcType=BIGINT}");
        }

        if (record.getObjectId() != null) {
            SET("object_id = #{objectId,jdbcType=VARCHAR}");
        }

        WHERE("id = #{id,jdbcType=BIGINT}");

        return SQL();
    }
}
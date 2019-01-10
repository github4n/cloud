package com.iot.building.ifttt.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.LEFT_OUTER_JOIN;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import com.iot.building.ifttt.entity.Sensor;
import com.iot.control.ifttt.vo.req.GetSensorReq;

public class SensorSqlProvider {

    public String insertSelective(Sensor record) {
        BEGIN();
        INSERT_INTO("ifttt_sensor");

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

        if (record.getTiming() != null) {
            VALUES("timing", "#{timing,jdbcType=VARCHAR}");
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

        return SQL();
    }

    public String updateByPrimaryKeySelective(Sensor record) {
        BEGIN();
        UPDATE("ifttt_sensor");

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

        if (record.getTiming() != null) {
            SET("timing = #{timing,jdbcType=VARCHAR}");
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

        WHERE("id = #{id,jdbcType=BIGINT}");

        return SQL();
    }


    public String getSensorByParams(GetSensorReq req) {
        BEGIN();
        SELECT("a.id, a.name, a.label, a.properties, a.device_id, a.rule_id, a.position, a.device_type, a.timing," +
                " a.idx, a.type, a.delay, a.tenant_id, a.product_id");
        FROM("ifttt_sensor a");
        LEFT_OUTER_JOIN("ifttt_rule b on a.rule_id=b.id");

        if (req.getProperties() != null) {
            WHERE("a.properties like concat(concat('%',#{properties,jdbcType=VARCHAR}),'%')");
        }

        if (req.getType() != null) {
            WHERE("a.type = #{type,jdbcType=VARCHAR}");
        }

        if (req.getStatus() != null) {
            WHERE("b.status = #{status,jdbcType=TINYINT}");
        }

        if (req.getSpaceId() != null) {
            WHERE("b.space_id = #{spaceId,jdbcType=BIGINT}");
        }

        return SQL();
    }
}
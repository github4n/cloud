package com.iot.user.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import com.iot.user.entity.UserLogin;

public class UserLoginSqlProvider {

    public String insertSelective(UserLogin record) {
        BEGIN();
        INSERT_INTO("user_login");

        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=BIGINT}");
        }

        if (record.getUserId() != null) {
            VALUES("user_id", "#{userId,jdbcType=BIGINT}");
        }

        if (record.getLastIp() != null) {
            VALUES("last_ip", "#{lastIp,jdbcType=VARCHAR}");
        }

        if (record.getLastLoginTime() != null) {
            VALUES("last_login_time", "#{lastLoginTime,jdbcType=TIMESTAMP}");
        }

        if (record.getPhoneId() != null) {
            VALUES("phone_id", "#{phoneId,jdbcType=VARCHAR}");
        }

        if (record.getOs() != null) {
            VALUES("os", "#{os,jdbcType=VARCHAR}");
        }

        if (record.getTenantId() != null) {
            VALUES("tenant_id", "#{tenantId,jdbcType=BIGINT}");
        }

        return SQL();
    }

    public String updateByPrimaryKeySelective(UserLogin record) {
        BEGIN();
        UPDATE("user_login");

        if (record.getUserId() != null) {
            SET("user_id = #{userId,jdbcType=BIGINT}");
        }

        if (record.getLastIp() != null) {
            SET("last_ip = #{lastIp,jdbcType=VARCHAR}");
        }

        if (record.getLastLoginTime() != null) {
            SET("last_login_time = #{lastLoginTime,jdbcType=TIMESTAMP}");
        }

        if (record.getPhoneId() != null) {
            SET("phone_id = #{phoneId,jdbcType=VARCHAR}");
        }

        if (record.getOs() != null) {
            SET("os = #{os,jdbcType=VARCHAR}");
        }

        if (record.getTenantId() != null) {
            SET("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }

        WHERE("id = #{id,jdbcType=BIGINT}");

        return SQL();
    }
}
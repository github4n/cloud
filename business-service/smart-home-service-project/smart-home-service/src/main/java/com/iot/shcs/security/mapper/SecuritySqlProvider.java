package com.iot.shcs.security.mapper;

import com.iot.shcs.security.domain.Security;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;



public class SecuritySqlProvider {

    public String insertSelective(Security record) {
        BEGIN();
        INSERT_INTO("security");
        
        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getSpaceId() != null) {
            VALUES("space_id", "#{spaceId,jdbcType=BIGINT}");
        }
        
        if (record.getArmMode() != null) {
            VALUES("arm_mode", "#{armMode,jdbcType=VARCHAR}");
        }
        
        if (record.getPassword() != null) {
            VALUES("password", "#{password,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getCreateBy() != null) {
            VALUES("create_by", "#{createBy,jdbcType=BIGINT}");
        }
        
        if (record.getUpdateBy() != null) {
            VALUES("update_by", "#{updateBy,jdbcType=BIGINT}");
        }
        
        if (record.getTenantId() != null) {
            VALUES("tenant_id", "#{tenantId,jdbcType=BIGINT}");
        }
        
        if (record.getOrgId() != null) {
            VALUES("org_id", "#{orgId,jdbcType=BIGINT}");
        }
        
        return SQL();
    }

    public String updateByPrimaryKeySelective(Security record) {
        BEGIN();
        UPDATE("security");
        
        if (record.getSpaceId() != null) {
            SET("space_id = #{spaceId,jdbcType=BIGINT}");
        }
        
        if (record.getArmMode() != null) {
            SET("arm_mode = #{armMode,jdbcType=VARCHAR}");
        }
        
        if (record.getPassword() != null) {
            SET("password = #{password,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getCreateBy() != null) {
            SET("create_by = #{createBy,jdbcType=BIGINT}");
        }
        
        if (record.getUpdateBy() != null) {
            SET("update_by = #{updateBy,jdbcType=BIGINT}");
        }
        
        if (record.getTenantId() != null) {
            SET("tenant_id = #{tenantId,jdbcType=BIGINT}");
        }
        
        if (record.getOrgId() != null) {
            SET("org_id = #{orgId,jdbcType=BIGINT}");
        }
        
        WHERE("id = #{id,jdbcType=BIGINT}");
        
        return SQL();
    }

    public String securityResetFactory(Security record) {
        BEGIN();
        UPDATE("security");

        if (record.getArmMode() != null) {
            SET("arm_mode = #{armMode,jdbcType=VARCHAR}");
        }

        if (record.getUpdateTime() != null) {
            SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUpdateBy() != null) {
            SET("update_by = #{updateBy,jdbcType=BIGINT}");
        }

        WHERE("space_id = #{spaceId,jdbcType=BIGINT}");

        return SQL();
    }
}
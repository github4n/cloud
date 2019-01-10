package com.iot.tenant.mapper.sql;

import com.iot.tenant.domain.Tenant;

import static org.apache.ibatis.jdbc.SqlBuilder.*;


public class TenantSqlProvider {

    public String insertSelective(Tenant record) {
        BEGIN();
        INSERT_INTO(Tenant.TABLE_NAME);
        
        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=VARCHAR}");
        }
        
        if (record.getName() != null) {
            VALUES("name", "#{name,jdbcType=VARCHAR}");
        }
        if (record.getCode() != null) {
            VALUES("code90", "#{name,jdbcType=VARCHAR}");
        }

        if (record.getCellphone() != null) {
            VALUES("cellphone", "#{cellphone,jdbcType=VARCHAR}");
        }
        
        if (record.getEmail() != null) {
            VALUES("email", "#{email,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            VALUES("created_time", "#{createdTime,jdbcType=TIMESTAMP}");
        }
        
        return SQL();
    }

    public String updateByPrimaryKeySelective(Tenant record) {
        BEGIN();
        UPDATE(Tenant.TABLE_NAME);
        
        if (record.getName() != null) {
            SET("name = #{name,jdbcType=VARCHAR}");
        }
        
        if (record.getCellphone() != null) {
            SET("cellphone = #{cellphone,jdbcType=VARCHAR}");
        }
        
        if (record.getEmail() != null) {
            SET("email = #{email,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            SET("create_time = #{createdTime,jdbcType=TIMESTAMP}");
        }
        
        WHERE("id = #{id,jdbcType=VARCHAR}");
        
        return SQL();
    }
}
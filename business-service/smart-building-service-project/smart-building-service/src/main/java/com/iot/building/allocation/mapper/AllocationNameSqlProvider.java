package com.iot.building.allocation.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import com.google.common.base.Strings;
import com.iot.building.allocation.entity.AllocationName;
import com.iot.building.allocation.vo.AllocationNameReq;

public class AllocationNameSqlProvider {

    public String insertSelective(AllocationName record) {
        BEGIN();
        INSERT_INTO("allocation_name");
        
        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getName() != null) {
            VALUES("name", "#{name,jdbcType=VARCHAR}");
        }
        
        if (record.getDescription() != null) {
            VALUES("description", "#{description,jdbcType=VARCHAR}");
        }
        
        return SQL();
    }

    public String updateByPrimaryKeySelective(AllocationName record) {
        BEGIN();
        UPDATE("allocation_name");
        
        if (record.getName() != null) {
            SET("name = #{name,jdbcType=VARCHAR}");
        }
        
        if (record.getDescription() != null) {
            SET("description = #{description,jdbcType=VARCHAR}");
        }
        
        WHERE("id = #{id,jdbcType=BIGINT}");
        
        return SQL();
    }

    public String queryList(AllocationNameReq req) {
        BEGIN();
        SELECT("id, name, description ");
        FROM("allocation_name");
        if (req.getId() != null) {
            WHERE(" id = = #{id,jdbcType=BIGINT}");
        }
        if (!Strings.isNullOrEmpty(req.getName())) {
            WHERE(" name like concat(concat('%',#{name,jdbcType=VARCHAR}),'%')");
        }
        return SQL();
    }
}
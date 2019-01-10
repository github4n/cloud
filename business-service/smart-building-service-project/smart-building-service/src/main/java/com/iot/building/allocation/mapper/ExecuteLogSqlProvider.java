package com.iot.building.allocation.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import com.iot.building.allocation.entity.ExecuteLog;

public class ExecuteLogSqlProvider {

    public String insertSelective(ExecuteLog record) {
        BEGIN();
        INSERT_INTO("execute_log");
        
        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getFunctionId() != null) {
            VALUES("function_id", "#{functionId,jdbcType=BIGINT}");
        }
        
        if (record.getExeResult() != null) {
            VALUES("exe_result", "#{exeResult,jdbcType=INTEGER}");
        }
        
        if (record.getExeContent() != null) {
            VALUES("exe_content", "#{exeContent,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        return SQL();
    }

    public String updateByPrimaryKeySelective(ExecuteLog record) {
        BEGIN();
        UPDATE("execute_log");
        
        if (record.getFunctionId() != null) {
            SET("function_id = #{functionId,jdbcType=BIGINT}");
        }
        
        if (record.getExeResult() != null) {
            SET("exe_result = #{exeResult,jdbcType=INTEGER}");
        }
        
        if (record.getExeContent() != null) {
            SET("exe_content = #{exeContent,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        
        WHERE("id = #{id,jdbcType=BIGINT}");
        
        return SQL();
    }
}
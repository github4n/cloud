package com.iot.building.allocation.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.ORDER_BY;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import com.iot.building.allocation.entity.Allocation;
import com.iot.building.allocation.vo.AllocationReq;

public class AllocationSqlProvider {

    public String insertSelective(Allocation record) {
        BEGIN();
        INSERT_INTO("allocation");
        
        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getAllocationId() != null) {
            VALUES("allocation_id", "#{allocationId,jdbcType=BIGINT}");
        }
        
        if (record.getExeStatus() != null) {
            VALUES("exe_status", "#{exeStatus,jdbcType=INTEGER}");
        }
        
        if (record.getExeResult() != null) {
            VALUES("exe_result", "#{exeResult,jdbcType=INTEGER}");
        }
        
        if (record.getIsLoop() != null) {
            VALUES("is_loop", "#{isLoop,jdbcType=INTEGER}");
        }
        
        if (record.getSelectWeek() != null) {
            VALUES("select_week", "#{selectWeek,jdbcType=VARCHAR}");
        }
        
        if (record.getSpaceIds() != null) {
            VALUES("space_ids", "#{spaceIds,jdbcType=VARCHAR}");
        }
        
        if (record.getSpaceName() != null) {
            VALUES("space_name", "#{spaceName,jdbcType=VARCHAR}");
        }
        
        if (record.getDeviceInterval() != null) {
            VALUES("device_interval", "#{deviceInterval,jdbcType=INTEGER}");
        }
        
        if (record.getParamInfo() != null) {
            VALUES("param_info", "#{paramInfo,jdbcType=VARCHAR}");
        }
        
        if (record.getExeTime() != null) {
            VALUES("exe_time", "#{exeTime,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getCreateBy() != null) {
            VALUES("create_by", "#{createBy,jdbcType=VARCHAR}");
        }
        
        if (record.getUpdateBy() != null) {
            VALUES("update_by", "#{updateBy,jdbcType=VARCHAR}");
        }
        
        if (record.getOrgId() != null) {
        	VALUES("org_id", "#{orgId}");
        }
        
        if (record.getLocationId() != null) {
        	VALUES("location_id", "#{locationId}");
        }
        
        
        return SQL();
    }

    public String updateByPrimaryKeySelective(Allocation record) {
        BEGIN();
        UPDATE("allocation");
        
        if (record.getAllocationId() != null) {
            SET("allocation_id = #{allocationId,jdbcType=BIGINT}");
        }
        
        if (record.getExeStatus() != null) {
            SET("exe_status = #{exeStatus,jdbcType=INTEGER}");
        }
        
        if (record.getExeResult() != null) {
            SET("exe_result = #{exeResult,jdbcType=INTEGER}");
        }
        
        if (record.getIsLoop() != null) {
            SET("is_loop = #{isLoop,jdbcType=INTEGER}");
        }
        
        if (record.getSelectWeek() != null) {
            SET("select_week = #{selectWeek,jdbcType=VARCHAR}");
        }
        
        if (record.getSpaceIds() != null) {
            SET("space_ids = #{spaceIds,jdbcType=VARCHAR}");
        }
        
        if (record.getSpaceName() != null) {
            SET("space_name = #{spaceName,jdbcType=VARCHAR}");
        }
        
        if (record.getDeviceInterval() != null) {
            SET("device_interval = #{deviceInterval,jdbcType=INTEGER}");
        }
        
        if (record.getParamInfo() != null) {
            SET("param_info = #{paramInfo,jdbcType=VARCHAR}");
        }
        
        if (record.getExeTime() != null) {
            SET("exe_time = #{exeTime,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getCreateBy() != null) {
            SET("create_by = #{createBy,jdbcType=VARCHAR}");
        }
        
        if (record.getUpdateBy() != null) {
            SET("update_by = #{updateBy,jdbcType=VARCHAR}");
        }
        
        WHERE("id = #{id,jdbcType=BIGINT}");
        
        return SQL();
    }

    public String queryList(AllocationReq req) {
        BEGIN();
//        SELECT("a.id, a.allocation_id, a.exe_status, a.exe_result, a.is_loop, a.select_week, a.space_ids," +
//                "a.space_name, a.device_interval, a.param_info, a.exe_time, a.create_time, a.update_time, a.create_by, a.update_by, " +
//                "(select n.name from allocation_name n where n.id = a.allocation_id) as allocation_name ");
        SELECT("a.id, a.allocation_id, (select n.name from allocation_name n where n.id = a.allocation_id) as allocation_name," +
                "a.exe_status, a.exe_result, a.is_loop, a.select_week, a.space_ids, a.space_name, a.device_interval," +
                "a.param_info, a.exe_time, a.create_time, a.update_time, a.create_by, a.update_by,tenant_id,location_id,concurrent,org_id");
        FROM("allocation a");
        if (req.getId() != null) {
            WHERE(" a.id = #{id,jdbcType=BIGINT}");
        }
        if (req.getAllocationId() != null) {
            WHERE(" a.allocation_id = #{allocationId,jdbcType=BIGINT}");
        }
        if (req.getTenantId() != null) {
        	WHERE(" a.tenant_id = #{tenantId,jdbcType=BIGINT}");
        }
        if (req.getLocationId() != null) {
        	WHERE(" a.location_id = #{locationId,jdbcType=BIGINT}");
        }
        if (req.getOrgId() != null) {
        	WHERE(" a.org_id = #{orgId,jdbcType=BIGINT}");
        }
        ORDER_BY("a.id desc");
        return SQL();
    }

}
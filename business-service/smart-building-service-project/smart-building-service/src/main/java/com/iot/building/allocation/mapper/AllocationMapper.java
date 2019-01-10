package com.iot.building.allocation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.type.JdbcType;

import com.iot.building.allocation.entity.Allocation;
import com.iot.building.allocation.vo.AllocationReq;
import com.iot.building.allocation.vo.AllocationResp;

public interface AllocationMapper {
    @Delete({
        "delete from allocation",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into allocation (id, allocation_id, ",
        "exe_status, exe_result, ",
        "is_loop, select_week, ",
        "space_ids, space_name, ",
        "device_interval, param_info, ",
        "exe_time, create_time, ",
        "update_time, create_by, ",
        "update_by,tenant_id,location_id,concurrent,org_id)",
        "values (#{id,jdbcType=BIGINT}, #{allocationId,jdbcType=BIGINT}, ",
        "#{exeStatus,jdbcType=INTEGER}, #{exeResult,jdbcType=INTEGER}, ",
        "#{isLoop,jdbcType=INTEGER}, #{selectWeek,jdbcType=VARCHAR}, ",
        "#{spaceIds,jdbcType=VARCHAR}, #{spaceName,jdbcType=VARCHAR}, ",
        "#{deviceInterval,jdbcType=INTEGER}, #{paramInfo,jdbcType=VARCHAR}, ",
        "#{exeTime,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP}, #{createBy,jdbcType=VARCHAR}, ",
        "#{updateBy,jdbcType=VARCHAR},#{tenantId,jdbcType=BIGINT},",
        "#{locationId,jdbcType=BIGINT},#{concurrent,jdbcType=INTEGER},#{orgId,jdbcType=INTEGER})"
    })
    @SelectKey(statement = "SELECT last_insert_id() as id", keyProperty = "id", before = false,
            resultType = Long.class, statementType = StatementType.STATEMENT)
    int insert(Allocation record);

    @InsertProvider(type=AllocationSqlProvider.class, method="insertSelective")
    int insertSelective(Allocation record);

    @Select({
        "select",
        "id, allocation_id, exe_status, exe_result, is_loop, select_week, space_ids, ",
        "space_name, device_interval, param_info, exe_time, create_time, update_time, ",
        "create_by, update_by,tenant_id,location_id,concurrent,org_id",
        "from allocation",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="allocation_id", property="allocationId", jdbcType=JdbcType.BIGINT),
        @Result(column="exe_status", property="exeStatus", jdbcType=JdbcType.INTEGER),
        @Result(column="exe_result", property="exeResult", jdbcType=JdbcType.INTEGER),
        @Result(column="is_loop", property="isLoop", jdbcType=JdbcType.INTEGER),
        @Result(column="select_week", property="selectWeek", jdbcType=JdbcType.VARCHAR),
        @Result(column="space_ids", property="spaceIds", jdbcType=JdbcType.VARCHAR),
        @Result(column="space_name", property="spaceName", jdbcType=JdbcType.VARCHAR),
        @Result(column="device_interval", property="deviceInterval", jdbcType=JdbcType.INTEGER),
        @Result(column="param_info", property="paramInfo", jdbcType=JdbcType.VARCHAR),
        @Result(column="exe_time", property="exeTime", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="create_by", property="createBy", jdbcType=JdbcType.VARCHAR),
        @Result(column="update_by", property="updateBy", jdbcType=JdbcType.VARCHAR),
        @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
        @Result(column="location_id", property="locationId", jdbcType=JdbcType.BIGINT),
        @Result(column="concurrent", property="concurrent", jdbcType=JdbcType.INTEGER),
        @Result(column="org_id", property="orgId", jdbcType=JdbcType.INTEGER)
    })
    Allocation selectByPrimaryKey(Long id);

    @UpdateProvider(type=AllocationSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Allocation record);

    @Update({
        "update allocation",
        "set allocation_id = #{allocationId,jdbcType=BIGINT},",
          "exe_status = #{exeStatus,jdbcType=INTEGER},",
          "exe_result = #{exeResult,jdbcType=INTEGER},",
          "is_loop = #{isLoop,jdbcType=INTEGER},",
          "select_week = #{selectWeek,jdbcType=VARCHAR},",
          "space_ids = #{spaceIds,jdbcType=VARCHAR},",
          "space_name = #{spaceName,jdbcType=VARCHAR},",
          "device_interval = #{deviceInterval,jdbcType=INTEGER},",
          "param_info = #{paramInfo,jdbcType=VARCHAR},",
          "exe_time = #{exeTime,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP},",
          "create_by = #{createBy,jdbcType=VARCHAR},",
          "update_by = #{updateBy,jdbcType=VARCHAR},",
          "concurrent = #{concurrent,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Allocation record);

    @SelectProvider(type = AllocationSqlProvider.class, method = "queryList")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="allocation_id", property="allocationId", jdbcType=JdbcType.BIGINT),
        @Result(column="allocation_name", property="allocationName", jdbcType=JdbcType.VARCHAR),
        @Result(column="exe_status", property="exeStatus", jdbcType=JdbcType.INTEGER),
        @Result(column="exe_result", property="exeResult", jdbcType=JdbcType.INTEGER),
        @Result(column="is_loop", property="isLoop", jdbcType=JdbcType.INTEGER),
        @Result(column="select_week", property="selectWeek", jdbcType=JdbcType.VARCHAR),
        @Result(column="space_ids", property="spaceIds", jdbcType=JdbcType.VARCHAR),
        @Result(column="space_name", property="spaceName", jdbcType=JdbcType.VARCHAR),
        @Result(column="device_interval", property="deviceInterval", jdbcType=JdbcType.INTEGER),
        @Result(column="param_info", property="paramInfo", jdbcType=JdbcType.VARCHAR),
        @Result(column="exe_time", property="exeTime", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_by", property="createBy", jdbcType=JdbcType.VARCHAR),
        @Result(column="update_by", property="updateBy", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
        @Result(column="location_id", property="locationId", jdbcType=JdbcType.BIGINT),
        @Result(column="concurrent", property="concurrent", jdbcType=JdbcType.INTEGER)
    })
    List<AllocationResp> getList(AllocationReq req);
}
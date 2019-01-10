package com.iot.building.allocation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import com.iot.building.allocation.entity.ExecuteLog;

public interface ExecuteLogMapper {
    @Delete({
        "delete from execute_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into execute_log (id, function_id, ",
        "exe_result, exe_content, ",
        "create_time,tenant_id,location_id,org_id)",
        "values (#{id,jdbcType=BIGINT}, #{functionId,jdbcType=BIGINT}, ",
        "#{exeResult,jdbcType=INTEGER}, #{exeContent,jdbcType=VARCHAR}, ",
        "#{createTime,jdbcType=TIMESTAMP},#{tenantId},#{locationId},#{orgId})"
    })
    int insert(ExecuteLog record);

    @InsertProvider(type=ExecuteLogSqlProvider.class, method="insertSelective")
    int insertSelective(ExecuteLog record);

    @Select({
        "select",
        "id, function_id, exe_result, exe_content, create_time",
        "from execute_log",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="function_id", property="functionId", jdbcType=JdbcType.BIGINT),
        @Result(column="exe_result", property="exeResult", jdbcType=JdbcType.INTEGER),
        @Result(column="exe_content", property="exeContent", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP)
    })
    ExecuteLog selectByPrimaryKey(Long id);

    @UpdateProvider(type=ExecuteLogSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ExecuteLog record);

    @Update({
        "update execute_log",
        "set function_id = #{functionId,jdbcType=BIGINT},",
          "exe_result = #{exeResult,jdbcType=INTEGER},",
          "exe_content = #{exeContent,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ExecuteLog record);

    @Select({
            "select",
            "id, function_id, exe_result, exe_content, create_time,tenant_id,location_id,org_id",
            "from execute_log",
            "where function_id = #{functionId,jdbcType=BIGINT} ",
            " and tenant_id = #{tenantId} and location_id=#{locationId} and org_id =#{orgId}",
            "order by id desc"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="function_id", property="functionId", jdbcType=JdbcType.BIGINT),
            @Result(column="exe_result", property="exeResult", jdbcType=JdbcType.INTEGER),
            @Result(column="exe_content", property="exeContent", jdbcType=JdbcType.VARCHAR),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
            @Result(column="location_id", property="locationId", jdbcType=JdbcType.BIGINT),
            @Result(column="org_id", property="orgId", jdbcType=JdbcType.BIGINT),
    })
    List<ExecuteLog> queryByFunctionId(@Param("tenantId") Long tenantId,@Param("locationId") Long locationId,
    		@Param("functionId") Long functionId);
}
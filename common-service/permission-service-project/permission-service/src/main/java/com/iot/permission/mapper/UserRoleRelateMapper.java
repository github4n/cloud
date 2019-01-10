package com.iot.permission.mapper;

import com.iot.permission.entity.UserRoleRelate;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface UserRoleRelateMapper {

    @Delete("<script>"+
            "delete from user_role_relate where id in"+
            "<foreach item='id' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}"+
            "</foreach>"+
            "</script>")
    int deleteUserRoleRelateById(@Param("ids") List<Long> ids);

    @Select("select id," +
            "user_id," +
            "role_id," +
            "create_by," +
            "create_time," +
            "update_by," +
            "update_time," +
            "is_deleted from user_role_relate where user_id = #{userId,jdbcType=BIGINT}")
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
            @Result(column="role_id", property="roleId", jdbcType=JdbcType.BIGINT),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="is_deleted", property="isDeleted", jdbcType=JdbcType.CHAR)
    })
    List<UserRoleRelate> getUserRoleRelateByUserId(@Param("userId") Long userId);
}

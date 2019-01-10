package com.iot.user.mapper;

import com.iot.user.entity.UserLogin;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

/**
 * 描述：登录信息操作类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/11 13:51
 */
public interface UserLoginMapper {

    @Delete({
            "delete from user_login",
            "where id = #{id,jdbcType=BIGINT}"
        })
    int deleteByPrimaryKey(Long id);

    @Insert({
            "insert into user_login (id, user_id, ",
            "last_ip, last_login_time, ",
            "phone_id, os, tenant_id)",
            "values (#{id,jdbcType=BIGINT}, #{userId,jdbcType=BIGINT}, ",
            "#{lastIp,jdbcType=VARCHAR}, #{lastLoginTime,jdbcType=TIMESTAMP}, ",
            "#{phoneId,jdbcType=VARCHAR}, #{os,jdbcType=VARCHAR}, #{tenantId,jdbcType=BIGINT})"
        })
    int insert(UserLogin record);

    @InsertProvider(type = UserLoginSqlProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertSelective(UserLogin record);

    @Select({
            "select",
            "id, user_id, last_ip, last_login_time, phone_id, os, tenant_id",
            "from user_login",
            "where id = #{id,jdbcType=BIGINT}"
        })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.BIGINT),
            @Result(column = "last_ip", property = "lastIp", jdbcType = JdbcType.VARCHAR),
            @Result(column = "last_login_time", property = "lastLoginTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "phone_id", property = "phoneId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "os", property = "os", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT)
        })
    UserLogin selectByPrimaryKey(Long id);

    @UpdateProvider(type = UserLoginSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(UserLogin record);

    @Update({
            "update user_login",
            "set user_id = #{userId,jdbcType=BIGINT},",
            "last_ip = #{lastIp,jdbcType=VARCHAR},",
            "last_login_time = #{lastLoginTime,jdbcType=TIMESTAMP},",
            "phone_id = #{phoneId,jdbcType=VARCHAR},",
            "os = #{os,jdbcType=VARCHAR},",
            "tenant_id = #{tenantId,jdbcType=BIGINT}",
            "where id = #{id,jdbcType=BIGINT}"
        })
    int updateByPrimaryKey(UserLogin record);

    /*****************************************以下为手动生成部分***************************************/

    @Select({
            "select",
            "id, user_id, last_ip, last_login_time, phone_id, os, tenant_id",
            "from user_login",
            "where user_id = #{userId,jdbcType=VARCHAR} and tenant_id = #{tenantId,jdbcType=BIGINT}",
        })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.BIGINT),
            @Result(column = "last_ip", property = "lastIp", jdbcType = JdbcType.VARCHAR),
            @Result(column = "last_login_time", property = "lastLoginTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "phone_id", property = "phoneId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "os", property = "os", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT)
        })
    UserLogin selectByUserId(@Param("userId") Long userId, @Param("tenantId") Long tenantId);
}

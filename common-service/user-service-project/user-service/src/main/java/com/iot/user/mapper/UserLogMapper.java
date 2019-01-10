package com.iot.user.mapper;

import com.iot.user.entity.UserLog;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface UserLogMapper {

    @Update(
            {"<script>",
                    "update user_log ",
                    "<set>",
                    "<if test=\"accept!=null\">accept=#{accept,jdbcType=INTEGER}, </if>",
                    "<if test=\"acceptTime!=null\">accept_time=#{acceptTime,jdbcType=TIMESTAMP}, </if>",
                    "<if test=\"cancel!=null\">cancel=#{cancel,jdbcType=INTEGER}, </if>",
                    "<if test=\"cancelTime!=null\">cancel_time=#{cancelTime,jdbcType=TIMESTAMP}, </if> ",
                    "</set>",
                    "where uuid=#{uuid,jdbcType=VARCHAR} AND tenant_id=#{tenantId,jdbcType=BIGINT}",
               "</script>"
            }
    )
    int updateUserLog(UserLog userLog);

    @Insert(
                    "insert into user_log "+
                    "(uuid,user_name,tenant_id,accept,accept_time,cancel,cancel_time) "+
                    "values "+
                    "(#{uuid,jdbcType=VARCHAR},#{userName,jdbcType=VARCHAR},#{tenantId,jdbcType=BIGINT},"+
                    "#{accept,jdbcType=INTEGER},#{acceptTime,jdbcType=TIMESTAMP}," +
                    "#{cancel,jdbcType=INTEGER},#{cancelTime,jdbcType=TIMESTAMP})"
    )
    int inserUserLog(UserLog userLog);

    @Delete(
            "delect from user_long " +
            "where uuid=#{uuid,jdbcType=VARCHAR} AND tenant_id=#{tenantId,jdbcType=BIGINT}"
    )
    int deleteUserLog(@Param("uuid") String uuid,@Param("tenantId") Long tenantId);


    @Select(
            "select uuid,user_name,tenant_id,accept,accept_time,cancel,cancel_time from user_log " +
            "where uuid=#{uuid,jdbcType=VARCHAR} AND tenant_id=#{tenantId,jdbcType=BIGINT}"
    )
    @Results({
            @Result(column = "uuid", property = "uuid", jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_name", property = "userName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT),
            @Result(column = "accept", property = "accept", jdbcType = JdbcType.INTEGER),
            @Result(column = "accept_time", property = "acceptTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "cancel", property = "cancel", jdbcType = JdbcType.INTEGER),
            @Result(column = "cancel_time", property = "cancelTime", jdbcType = JdbcType.TIMESTAMP)
    })
    UserLog selectByUuidAndTenatId(@Param("uuid") String uuid,@Param("tenantId") Long tenantId);
}

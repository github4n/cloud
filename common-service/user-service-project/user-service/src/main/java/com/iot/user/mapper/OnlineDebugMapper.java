package com.iot.user.mapper;

import com.iot.common.util.StringUtil;
import com.iot.user.entity.OnlineDebugEntity;
import org.apache.ibatis.annotations.*;

import java.util.Date;

public interface OnlineDebugMapper {

    @Select({"select state " +
            "from online_debug " +
            "where uuid = #{uuid,jdbcType=VARCHAR}"})
    OnlineDebugEntity checkOnlineDebug(@Param("uuid") String uuid);


    @Select({"select uuid " +
            "from iot_db_user.user " +
            "where user_name=#{userName,jdbcType=VARCHAR} and tenant_id=#{tenantId,jdbcType=TINYINT} and user_level=3"})
    String getUuidByUserName(@Param("userName") String userName,@Param("tenantId") int tenantId);


    @Update({"update online_debug " +
            "set state=#{state,jdbcType=TINYINT},update_time=#{updateTime,jdbcType=TIMESTAMP}" +
            "where uuid=#{uuid,jdbcType=VARCHAR} "})
    int updateState(@Param("uuid")String uuid,@Param("state") int state,@Param("updateTime") Date updateTime);


    @Insert({"insert into online_debug (uuid,state) values " +
            "(#{uuid,jdbcType=VARCHAR},#{state,jdbcType=TINYINT})"})
    int addOnlineDebug(@Param("uuid") String uuid, @Param("state") int state);
}

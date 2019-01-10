package com.iot.control.activity.mapper;

import com.iot.control.activity.domain.OnlineStatusRecord;
import com.iot.control.activity.mapper.sql.OnlineStatusRecordSqlProvider;
import com.iot.control.activity.vo.req.OnlineStatusRecordReq;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface OnlineStatusRecordMapper {
    @Insert({
            "insert into online_status_record (id, status, type, record_time, tenant_id)",
            "values (#{id,jdbcType=VARCHAR}, #{status,jdbcType=VARCHAR}, #{type,jdbcType=VARCHAR}, #{recordTime,jdbcType=TIMESTAMP}, #{tenantId,jdbcType=BIGINT})"
    })
    int insert(OnlineStatusRecordReq record);

    @InsertProvider(type = OnlineStatusRecordSqlProvider.class, method = "insertSelective")
    int insertSelective(OnlineStatusRecord record);

    @Update({
            "update online_status_record",
            "status = #{status,jdbcType=VARCHAR},",
            "type = #{type,jdbcType=VARCHAR},",
            "record_time = #{recordTime,jdbcType=TIMESTAMP}",
            "where id = #{id,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(OnlineStatusRecord record);

    @Select({
            "select",
            "id, status, type, record_time",
            "from online_status_record",
            "where id = #{id,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.VARCHAR),
            @Result(column = "status", property = "status", jdbcType = JdbcType.VARCHAR),
            @Result(column = "type", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "record_time", property = "recordTime", jdbcType = JdbcType.TIMESTAMP)
    })
    OnlineStatusRecord selectById(String id);
}
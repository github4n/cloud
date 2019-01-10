package com.iot.control.activity.mapper.sql;

import com.iot.control.activity.domain.OnlineStatusRecord;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class OnlineStatusRecordSqlProvider {

    public String insertSelective(OnlineStatusRecord record) {
        BEGIN();
        INSERT_INTO("online_status_record");

        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=VARCHAR}");
        }

        if (record.getStatus() != null) {
            VALUES("status", "#{status,jdbcType=CHAR}");
        }

        if (record.getType() != null) {
            VALUES("type", "#{type,jdbcType=VARCHAR}");
        }

        if (record.getRecordTime() != null) {
            VALUES("record_time", "#{recordTime,jdbcType=TIMESTAMP}");
        }

        return SQL();
    }
}

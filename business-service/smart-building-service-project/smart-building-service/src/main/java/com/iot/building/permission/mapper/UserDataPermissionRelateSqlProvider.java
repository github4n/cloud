package com.iot.building.permission.mapper;

import com.google.common.base.Strings;
import com.iot.building.permission.entity.UserDataPermissionRelate;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class UserDataPermissionRelateSqlProvider {

    public String insertSelective(UserDataPermissionRelate record) {
        BEGIN();
        INSERT_INTO("user_data_permission_relate");
        
        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getUserId() != null) {
            VALUES("user_id", "#{userId,jdbcType=BIGINT}");
        }
        
        if (record.getDataId() != null) {
            VALUES("data_id", "#{dataId,jdbcType=BIGINT}");
        }
        
        if (record.getDataType() != null) {
            VALUES("data_type", "#{dataType,jdbcType=INTEGER}");
        }

        if (!Strings.isNullOrEmpty(record.getDataName())) {
            VALUES("data_name", "#{dataName,jdbcType=VARCHAR}");
        }

        return SQL();
    }
}
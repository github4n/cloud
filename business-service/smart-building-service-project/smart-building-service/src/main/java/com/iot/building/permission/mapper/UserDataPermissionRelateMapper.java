package com.iot.building.permission.mapper;

import com.iot.building.permission.entity.UserDataPermissionRelate;
import com.iot.building.permission.vo.UserDataPermissionRelateDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserDataPermissionRelateMapper {
    @Insert({
        "insert into user_data_permission_relate (id, user_id, ",
        "data_id, data_type, data_name)",
        "values (#{id,jdbcType=BIGINT}, #{userId,jdbcType=BIGINT}, ",
        "#{dataId,jdbcType=BIGINT}, #{dataType,jdbcType=INTEGER}, #{dataName,jdbcType=VARCHAR})"
    })
    int insert(UserDataPermissionRelate record);

    @InsertProvider(type=UserDataPermissionRelateSqlProvider.class, method="insertSelective")
    int insertSelective(UserDataPermissionRelate record);

    @Delete({"delete from user_data_permission_relate ",
            "where user_id = #{userId,jdbcType=BIGINT} and data_type in (2,3,4)"})
    void deleteSpacePermissionByUserId(Long userId);

    @Select({"select id, user_id as userId, data_id as dataId, data_type as dataType, data_name as dataName ",
            "from user_data_permission_relate ",
            "where user_id = #{userId,jdbcType=BIGINT}"})
    List<UserDataPermissionRelateDto> getDataPermissionByUserId(Long userId);
}
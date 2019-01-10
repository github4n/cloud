package com.iot.mapper;

import com.iot.domain.UserSpaceUsed;

import java.util.Map;

public interface UserSpaceUsedMapper {
    int deleteByPrimaryKey(String userUsedId);

    int insert(UserSpaceUsed record);

    int saveUserUsed(Map<String, Object> map);

    int insertSelective(UserSpaceUsed record);

    UserSpaceUsed selectByPrimaryKey(String userUsedId);

    int updateByPrimaryKeySelective(UserSpaceUsed record);

    int updateByPrimaryKey(UserSpaceUsed record);

}
package com.iot.mapper;

import com.iot.domain.SpaceDate;
import com.iot.domain.SpaceTemplate;
import com.iot.domain.UserSpaceSubscribe;

import java.util.List;
import java.util.Map;

public interface UserSpaceSubscribeMapper {
    int deleteByPrimaryKey(String userSubscribeId);

    int insert(UserSpaceSubscribe record);

    int insertSelective(UserSpaceSubscribe record);

    UserSpaceSubscribe selectByPrimaryKey(String userSubscribeId);

    int updateByPrimaryKeySelective(UserSpaceSubscribe record);

    int updateByPrimaryKey(UserSpaceSubscribe record);

    int countBySpaceDateId(String spaceDateId);

    List<SpaceDate> findSpaceDateByUserId(String userId);

    int deleteUserSubscribe(Map<String, Object> map);

    List<String> findSpaceDateId(Map<String, Object> map);

    int saveUserSubscribe(Map<String, Object> map);

    List<SpaceTemplate> findBookableSpaceDateByTenantId(String TenantId);
}
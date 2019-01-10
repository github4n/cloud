package com.iot.mapper;

import com.iot.domain.Space;

import java.util.List;
import java.util.Map;

public interface SpaceMapper {
    int deleteByPrimaryKey(String id);

    int insert(Space record);

    int insertSelective(Space record);

    Space selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Space record);

    int updateByPrimaryKeyWithBLOBs(Space record);

    int updateByPrimaryKey(Space record);

    List<Space> findSpaceRelationshipBytenantId(String tenantId);

    List<Space> findSpaceByParentId(String parentId);

    int countUserUsedAmounts(Map<String, Object> map);

    int countUserSubscribeAmounts(Map<String, Object> map);
}
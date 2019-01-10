package com.iot.mapper;

import com.lds.iot.domain.SpaceTemplate;

public interface SpaceTemplateMapper {
    int deleteByPrimaryKey(String spaceTempleId);

    int insert(SpaceTemplate record);

    int insertSelective(SpaceTemplate record);

    SpaceTemplate selectByPrimaryKey(String spaceTempleId);

    int updateByPrimaryKeySelective(SpaceTemplate record);

    int updateByPrimaryKey(SpaceTemplate record);
}
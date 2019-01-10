package com.iot.mapper;

import com.iot.domain.SpaceDate;

import java.util.List;
import java.util.Map;

public interface SpaceDateMapper {
    int deleteByPrimaryKey(String spaceDateId);

    int insert(SpaceDate record);

    int insertSelective(SpaceDate record);

    SpaceDate selectByPrimaryKey(String spaceDateId);

    int updateByPrimaryKeySelective(SpaceDate record);

    int updateByPrimaryKey(SpaceDate record);

    List<SpaceDate> selectSpaceDateIdBySpaceId(Map<String, Object> map);

//    List<SpaceDate> selectSpaceDateIdBySpaceId(@Param("spaceId") String spaceId, @Param("settingDate") Date settingDate);

    List<SpaceDate> findSpaceDateByDate(Map<String, Object> map);

}
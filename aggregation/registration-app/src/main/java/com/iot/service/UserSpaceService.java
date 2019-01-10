package com.iot.service;

import com.iot.domain.SpaceDate;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface UserSpaceService {

    public Map<String, Object> findUserSubscribeByUserId(String userId);

    public int deleteUserSubscribe(Map<String, Object> map);

    public int saveUserSubscribe(Map<String, Object> map);

    public int saveUserUsed(Map<String, Object> map);

    public Map<String, Object> findBookableSpaceDateByTenantId(String TenantId) throws ParseException;

//	public List<SpaceDate> distributeSpaceBySubscribeTime(List<Map<String, Object>> dateMapList) throws ParseException;

    public List<SpaceDate> distributeSpaceBySubscribeTime(List<Map<String, Object>> DateMapList, List<SpaceDate> ResultspaceDateList) throws ParseException;

}

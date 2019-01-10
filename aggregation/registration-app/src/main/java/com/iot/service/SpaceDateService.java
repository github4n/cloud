package com.iot.service;

import com.iot.domain.Space;
import com.iot.domain.SpaceDate;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface SpaceDateService {

    public List<SpaceDate> findSpaceDateBySpaceIdAndDate(Map<String, Object> map) throws ParseException;

    public Map<String, Object> showSpaceDistribution(String tenantId);

    public List<Map<String, Object>> showPeopleDistribution(Map<String, Object> map);

    public int deleteSpaceByTenantId(String tenantId);

    public void uploadeSpace(List<Space> spaceList);
}

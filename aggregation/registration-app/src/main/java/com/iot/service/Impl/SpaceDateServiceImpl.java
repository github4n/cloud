package com.iot.service.Impl;

import com.iot.BusinessExceptionEnum;
import com.iot.common.exception.BusinessException;
import com.iot.domain.Space;
import com.iot.domain.SpaceDate;
import com.iot.mapper.SpaceDateMapper;
import com.iot.mapper.SpaceMapper;
import com.iot.mapper.UserSpaceSubscribeMapper;
import com.iot.service.SpaceDateService;
import com.iot.util.ToolUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SpaceDateServiceImpl implements SpaceDateService {

    @Autowired
    SpaceDateMapper spaceDateMapper;

    @Autowired
    UserSpaceSubscribeMapper userSpaceSubscribeMapper;

    @Autowired
    SpaceMapper spaceMapper;

    @Override
    public List<SpaceDate> findSpaceDateBySpaceIdAndDate(Map<String, Object> map) throws ParseException {
        List<SpaceDate> spaceDateList = new ArrayList<SpaceDate>();

        if (map.get("spaceId") != null && map.get("settingDate") != null) {
            List<SpaceDate> spaceDateAllList = new ArrayList<SpaceDate>();

            String settingDate = ToolUtils.stampToDate(Long.valueOf(map.get("settingDate").toString()));
            map.replace("settingDate", settingDate);
            spaceDateAllList = spaceDateMapper.selectSpaceDateIdBySpaceId(map);
            if (CollectionUtils.isNotEmpty(spaceDateAllList)) {
                for (SpaceDate spaceDate : spaceDateAllList) {
                    int count = 0;
                    count = userSpaceSubscribeMapper.countBySpaceDateId(spaceDate.getSpaceDateId());
                    if (count < spaceDate.getGalleryful()) {
                        spaceDateList.add(spaceDate);
                    }
                }
            }
        } else {
            throw new BusinessException(BusinessExceptionEnum.PARAMETER_ILLEGALITY);
        }
        return spaceDateList;

    }

    @Override
    public Map<String, Object> showSpaceDistribution(String tenantId) {
        Map<String, Object> spaceMap = new HashMap<>();
        if (!tenantId.isEmpty()) {
            //查找租户下的栋，层
            List<Space> floorList = new ArrayList<>();
            List<Space> buildList = new ArrayList<>();

            buildList = spaceMapper.findSpaceRelationshipBytenantId(tenantId);
            if (CollectionUtils.isNotEmpty(buildList)) {
                for (Space buildSpace : buildList) {
                    floorList = spaceMapper.findSpaceByParentId(buildSpace.getId());
                    if (!spaceMap.containsKey(buildSpace.getName())) {
                        spaceMap.put(buildSpace.getName(), floorList);
                    } else {
                        floorList = (List<Space>) spaceMap.get(buildSpace.getName());
                        floorList.addAll(floorList);
                        spaceMap.replace(buildSpace.getName(), floorList);
                    }

                }
            }
        }
        return spaceMap;
    }

    /**
     * 楼层的spaceId：spaceId tenantId
     */

    @Override
    public List<Map<String, Object>> showPeopleDistribution(Map<String, Object> map) {
        List<Map<String, Object>> spaceList = new ArrayList<>();
        if (map.get("spaceId") != null && map.get("tenantId") != null) {
            String parentId = (String) map.get("spaceId");
            List<Space> roomList = new ArrayList<>();
            roomList = spaceMapper.findSpaceByParentId(parentId);
            for (Space room : roomList) {
                Map<String, Object> spaceMap = new HashMap<>();
                String spaceId = room.getId();
                String spaceName = room.getName();
                map.replace("spaceId", spaceId);
                int usedAmount = spaceMapper.countUserUsedAmounts(map);
                int subscribeAmount = spaceMapper.countUserSubscribeAmounts(map);
                spaceMap.put("spaceId", spaceId);
                spaceMap.put("spaceName", spaceName);
                spaceMap.put("usedAmount", usedAmount);
                spaceMap.put("subscribeAmount", subscribeAmount);
                spaceList.add(spaceMap);
            }
        }

        return spaceList;
    }

    @Override
    public int deleteSpaceByTenantId(String tenantId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void uploadeSpace(List<Space> spaceList) {
        // TODO Auto-generated method stub

    }
}

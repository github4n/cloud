package com.iot.control.space.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.space.domain.Space;
import com.iot.control.space.domain.SpaceDevice;
import com.iot.control.space.mapper.SpaceDeviceMapper;
import com.iot.control.space.mapper.SpaceMapper;
import com.iot.control.space.util.RedisKeyUtil;
import com.iot.control.space.util.SpaceBaseCacheUtils;
import com.iot.control.space.util.SpaceCacheCoreUtils;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/07/13 11:36
 **/
public class SpaceBaseServiceUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(SpaceBaseCacheUtils.class);


    /**
     * @param resList
     * @param targetList
     * @Description: 检查部分缓存过期的 ---捞出未命中的spaceIds
     * @return: java.util.List<java.lang.Long>
     * @author: chq
     * @date: 2018/7/12 19:33
     **/
    public static List<Long> getNoHitCacheSpaces(List<Space> resList, List<Long> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return null;
        }
        if (CollectionUtils.isEmpty(resList)) {
            //结果内容为空都未命中 返回请求的集合 去db获取
            return targetList;
        }
        if (resList.contains(null)) {
            resList.remove(null);
        }
        if (resList.size() == targetList.size()) {
            LOGGER.info("hit-cache-all-------end");
            return null;
        }
        LOGGER.info("部分缓存失效----》》》");
        List<Long> noHitCacheList = Lists.newArrayList();
        Map<Long, Space> spaceMap = Maps.newHashMap();
        for (Space space : resList) {
            if (space == null) {
                continue;
            }
            spaceMap.put(space.getId(), space);
        }
        for (Long targetId : targetList) {
            Space compareSpace = spaceMap.get(targetId);
            if (compareSpace == null) {
                noHitCacheList.add(targetId);
            }
        }
        return noHitCacheList;
    }


    /**
     * @param spaceIds
     * @Description: 从数据库找出未命中space拼装数据，存缓存
     * @return: java.util.List<com.iot.control.space.domain.Space>
     * @author: chq
     * @date: 2018/7/12 18:02
     **/
    public static List<Space> findDBSpaceListBySpaceIds(Long tenantId, List<Long> spaceIds) {
        List<Space> spaceList = null;
        if (!CollectionUtils.isEmpty(spaceIds)) {
            SpaceMapper spaceMapper = ApplicationContextHelper.getBean(SpaceMapper.class);
            spaceList = spaceMapper.findSpaceListBySpaceIds(tenantId, spaceIds);
            SpaceCacheCoreUtils.saveOrDeleteCacheSpaces(spaceList, true);
        }
        return spaceList;
    }



    /**
     * @param spaceIds
     * @Description: 从数据库找出未命中spaceDevId拼装数据，存缓存
     * @return:
     * @author: chq
     * @date: 2018/7/12 18:02
     **/
    public static List<Long> findDBSpaceDevIdListBySpaceIds(Long tenantId, List<Long> spaceIds) {
        List<Long> spaceDevIdList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(spaceIds)) {
            SpaceDeviceMapper spaceDevMapper = ApplicationContextHelper.getBean(SpaceDeviceMapper.class);
            for (Long spaceId : spaceIds) {
                List<Long> spaceDevIds = spaceDevMapper.findSpaceDevIdBySpaceId(tenantId, spaceId);
                if (!CollectionUtils.isEmpty(spaceDevIds)) {
                    spaceDevIdList.addAll(spaceDevIds);
                    //将spaceDevIds存至缓存
                    SpaceBaseCacheUtils.saveCacheSpaceDevListToSpaceDev(spaceId, spaceDevIds);
                }
            }
        }
        return spaceDevIdList;
    }


}

package com.iot.control.space.service;

import com.alibaba.fastjson.JSON;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.space.domain.Space;
import com.iot.control.space.mapper.SpaceDeviceMapper;
import com.iot.control.space.util.RedisKeyUtil;
import com.iot.control.space.util.SpaceBaseCacheUtils;
import com.iot.control.space.util.SpaceCacheCoreUtils;
import com.iot.control.space.vo.RoomSpaceVo;
import com.iot.control.space.vo.SpaceDeviceVo;
import com.iot.redis.RedisCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @description:
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/07/13 14:15
 **/
public class SpaceCoreServiceUtils {

    private final static Logger logger = LoggerFactory.getLogger(SpaceCoreServiceUtils.class);

    //根据家庭获取设备列表
    public static List<SpaceDeviceVo> findSpaceDeviceVOByHomeIdAndUserId(RoomSpaceVo spaceVo) {
        Long homeId = spaceVo.getHomeId();
        Long tenantId = spaceVo.getTenantId();

        Set<Object> objectSet = SpaceBaseCacheUtils.getCacheDeviceListBySpaceId(tenantId, homeId);
        List<SpaceDeviceVo> spaceDeviceVOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(objectSet)) {
            SpaceDeviceMapper spaceDeviceMapper = ApplicationContextHelper.getBean(SpaceDeviceMapper.class);
            if (spaceDeviceMapper == null) {
                logger.error("***** findSpaceDeviceVOByHomeIdAndUserId() error! spaceDeviceMapper is null");
                return spaceDeviceVOList;
            }
            spaceDeviceVOList = spaceDeviceMapper.findSpaceDeviceVOByHomeSpaceId(homeId);
            Map<String, Set<ZSetOperations.TypedTuple<Object>>> map = new HashMap<>();
            spaceDeviceVOList.forEach(m->{
                Long score = m.getId() - new Long(m.getOrder()) * new Long("10000000000000");
                ZSetOperations.TypedTuple<Object> typedTuple = new DefaultTypedTuple<>(JSON.toJSONString(m), Double.valueOf(score));

                String spaceDeviceKey = RedisKeyUtil.getSpaceDevKey(tenantId,homeId);
                if (!map.containsKey(spaceDeviceKey)) {
                    map.put(spaceDeviceKey, new HashSet<>());
                }
                map.get(spaceDeviceKey).add(typedTuple);
//                if (m.getParentId() != null) {
//                    String homeDeviceKey = RedisKeyUtil.getSpaceDevKey(tenantId, m.getParentId());
//                    if (!map.containsKey(homeDeviceKey)) {
//                        map.put(homeDeviceKey, new HashSet<>());
//                    }
//                    map.get(homeDeviceKey).add(typedTuple);
//                }
            });

            for ( Map.Entry<String,Set<ZSetOperations.TypedTuple<Object>>> entry: map.entrySet()) {
                RedisCacheUtil.zSetAdd(entry.getKey(), entry.getValue());
            }
        } else {
            Iterator<Object> it = objectSet.iterator();
            while (it.hasNext()) {
                Object result = it.next();
                SpaceDeviceVo spaceDeviceVo = JSON.parseObject(result.toString(), SpaceDeviceVo.class);
                spaceDeviceVOList.add(spaceDeviceVo);
            }
        }
        return spaceDeviceVOList;

    }


    /**
     * 批量获取房间、家庭详情列表
     * 调用：getHomeList
     *
     * @param spaceIds
     * @return
     * @author chq
     * @date 2018/7/13 14:28
     */
    public static List<Space> getSpaceInfoListBySpaceIds(Long tenantId, List<Long> spaceIds) {
        if (CollectionUtils.isEmpty(spaceIds)) {
            return null;
        }
        // 缓存+db获取到 组装数据 查不到直接捞数据库返回
        List<Space> cacheList = SpaceCacheCoreUtils.getCacheSpaceInfoListBySpaceIds(tenantId, spaceIds);
        //检查部分缓存过期的 ---捞出未命中的spaceIds
        List<Long> noHitCache = SpaceBaseServiceUtils.getNoHitCacheSpaces(cacheList, spaceIds);


        List<Space> noHistSpaceList = null;
        if (!CollectionUtils.isEmpty(noHitCache)) {
            //从数据库找出未命中space拼装数据，存缓存
            noHistSpaceList = SpaceBaseServiceUtils.findDBSpaceListBySpaceIds(tenantId, noHitCache);

        }
        List<Space> targetSpaceList;
        if (!CollectionUtils.isEmpty(cacheList)) {
            if (!CollectionUtils.isEmpty(noHistSpaceList)) {
                cacheList.addAll(noHistSpaceList);
            }
            targetSpaceList = new ArrayList(new HashSet<>(cacheList));
        } else {
            targetSpaceList = new ArrayList(new HashSet<>(noHistSpaceList));
        }
        return targetSpaceList;
    }

}

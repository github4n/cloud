package com.iot.shcs.device.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author: lucky
 * @Descrpiton: 检查缓存是否命中和返回组装数据 工具类
 * @Date: 17:06 2018/9/25
 * @Modify by:
 */
@Slf4j
public class HitCacheUtils {


    /**
     * 获取缓存获取出来的ids 跟要搜索的targetIds 未命中缓存的id
     *
     * @param cacheIds
     * @param targetIds
     * @return
     * @author lucky
     * @date 2018/9/25 17:12
     */
    public static <T> List<T> getNoHitCacheIds(List<T> cacheIds, List<T> targetIds) {
        List<T> noHitCacheList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(targetIds)) {
            return noHitCacheList;
        }
        if (CollectionUtils.isEmpty(cacheIds)) {
            // 结果内容为空都未命中 返回请求的集合 去db获取
            return targetIds;
        }
        if (cacheIds.contains(null)) {
            cacheIds.remove(null);
        }

        if (cacheIds.size() == targetIds.size()) {
            log.debug("hit-cache-all-------end.targetIds:{}", targetIds);
            return noHitCacheList;
        }
        log.debug("a little cache dismiss...cacheIdsSize:{},targetIdsSize:{}", cacheIds, targetIds);

        Map<T, T> tempMap = Maps.newHashMap();
        for (T cacheId : cacheIds) {
            if (cacheId == null) {
                continue;
            }
            tempMap.put(cacheId, cacheId);
        }
        for (T targetId : targetIds) {
            T temp = tempMap.get(targetId);
            if (temp == null) {
                // 过滤出未命中的设备
                noHitCacheList.add(targetId);
            }
        }

        return noHitCacheList;
    }

    /**
     * 组装 缓存命中和db命中的数据
     *
     * @param cacheList 缓存list
     * @param dbList    db list
     * @return
     * @author lucky
     * @date 2018/6/27 18:37
     */
    public static <T> List<T> installList(List<T> cacheList, List<T> dbList) {
        List<T> returnInstallList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(cacheList)) {
            if (cacheList.contains(null)) {
                cacheList.remove(null);
            }
            returnInstallList = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(dbList)) {
                cacheList.addAll(dbList);
            }
            returnInstallList.addAll(cacheList);
        } else {
            if (!CollectionUtils.isEmpty(dbList)) {
                returnInstallList = Lists.newArrayList();
                returnInstallList.addAll(dbList);
            }
        }
        return returnInstallList;
    }
}

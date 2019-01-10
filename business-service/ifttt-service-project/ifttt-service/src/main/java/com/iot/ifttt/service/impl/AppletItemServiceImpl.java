package com.iot.ifttt.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.ifttt.entity.AppletItem;
import com.iot.ifttt.mapper.AppletItemMapper;
import com.iot.ifttt.service.IAppletItemService;
import com.iot.ifttt.util.RedisKeyUtil;
import com.iot.redis.RedisCacheUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 * 子规则表 服务实现类
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-28
 */
@Service
public class AppletItemServiceImpl extends ServiceImpl<AppletItemMapper, AppletItem> implements IAppletItemService {

    @Override
    public List<AppletItem> getByEventId(Long eventId, String type) {
        String key = RedisKeyUtil.getAppletItemMultiKey(eventId, type);
        String ids = RedisCacheUtil.valueGet(key);

        if (ids == null) {
            //查数据库
            ids = getIds(eventId, type);
            RedisCacheUtil.valueSet(key, ids, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_7);
        }

        //真实空
        if ("-1".equals(ids)) {
            return null;
        }

        return getlistCache(ids);
    }

    @Override
    public boolean deleteByEventId(Long eventId, String type) {
        String key = RedisKeyUtil.getAppletItemMultiKey(eventId, type);
        String ids = RedisCacheUtil.valueGet(key);
        if (!StringUtils.isEmpty(ids) && !"-1".equals(ids)) {
            //删除子项缓存
            List<Long> tempList = getIdList(ids);
            List<String> keys = Lists.newArrayList();
            tempList.forEach(id -> {
                keys.add(RedisKeyUtil.getAppletThatKey(id));
            });

            //批量删除子缓存
            RedisCacheUtil.delete(keys);
        }

        //删除关系缓存
        RedisCacheUtil.delete(key);

        //删除数据库
        Map<String, Object> params = Maps.newHashMap();
        params.put("event_id", eventId);
        params.put("type", type);
        deleteByMap(params);
        return true;
    }

    @Override
    public Long getAppletIdByItem(Long itemId) {
        //取缓存
        String key = RedisKeyUtil.getAppletItemKey(itemId);
        AppletItem data = RedisCacheUtil.valueObjGet(key, AppletItem.class);
        if (data == null) {
            data = selectById(itemId);
            if (data != null) {
                RedisCacheUtil.valueObjSet(key, data, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_7);
            }
        }

        Long appletId = null;
        if (data != null) {
            appletId = data.getAppletId();
        }

        return appletId;
    }

    @Override
    public boolean deleteByItemId(Long itemId) {
        Long appletId = getAppletIdByItem(itemId);
        //删除规则缓存
        String key = RedisKeyUtil.getAppletRuleKey(appletId);
        RedisCacheUtil.delete(key);

        //删除子项
        this.deleteById(itemId);
        //删除子项缓存
        RedisCacheUtil.delete(RedisKeyUtil.getAppletItemKey(itemId));
        return true;
    }

    //////////////////////////////////////////////////////////////////////////////////////

    private List<Long> getIdList(String thisIds) {
        Set<Long> set = new HashSet<>();
        String[] ids = thisIds.split(",");
        for (String vo : ids) {
            set.add(Long.parseLong(vo));
        }

        return new ArrayList<>(set);
    }

    /**
     * 从数据库获取关联主键
     *
     * @param eventId
     * @return
     */
    private String getIds(Long eventId, String type) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("event_id", eventId);
        params.put("type", type);
        List<AppletItem> list = selectByMap(params);
        StringBuffer ids = new StringBuffer();
        if (CollectionUtils.isNotEmpty(list)) {
            for (AppletItem vo : list) {
                ids.append(vo.getId());
                ids.append(",");
            }
        } else {
            ids.append("-1");
        }

        return ids.toString();
    }

    /**
     * 批量获取缓存列表
     *
     * @param ids
     * @return
     */
    private List<AppletItem> getlistCache(String ids) {
        List<Long> tempList = getIdList(ids);
        if (CollectionUtils.isEmpty(tempList)) {
            return null;
        }
        List<String> keys = Lists.newArrayList();
        tempList.forEach(id -> {
            keys.add(RedisKeyUtil.getAppletItemKey(id));
        });
        //批量取缓存
        List<AppletItem> cacheList = RedisCacheUtil.mget(keys, AppletItem.class);
        if (cacheList == null || cacheList.size() != tempList.size()) {
            // 未命中id
            List<Long> unHitIdList = tempList;
            if (cacheList == null) {
                cacheList = Lists.newArrayList();
            } else {
                List<Long> hitIdList = Lists.newArrayList();
                cacheList.forEach(cacheBean -> {
                    hitIdList.add(cacheBean.getId());
                });
                //取差集
                unHitIdList.removeAll(hitIdList);
            }
            //数据库取
            List<AppletItem> dataList = selectBatchIds(unHitIdList);
            //批量map
            Map<String, Object> multiSet = Maps.newHashMap();
            // 未命中 结果记录
            List<AppletItem> unHitList = Lists.newArrayList();

            if (CollectionUtils.isNotEmpty(dataList)) {
                dataList.forEach(data -> {
                    unHitList.add(data);
                    multiSet.put(RedisKeyUtil.getAppletItemKey(data.getId()), data);
                });

                RedisCacheUtil.multiSet(multiSet, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_7);
                cacheList.addAll(unHitList);
            }
        }

        return cacheList;
    }
}

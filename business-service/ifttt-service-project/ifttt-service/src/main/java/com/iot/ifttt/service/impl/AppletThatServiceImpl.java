package com.iot.ifttt.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.ifttt.entity.AppletThat;
import com.iot.ifttt.mapper.AppletThatMapper;
import com.iot.ifttt.service.IAppletThatService;
import com.iot.ifttt.util.RedisKeyUtil;
import com.iot.redis.RedisCacheUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 * that组表 服务实现类
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-28
 */
@Service
public class AppletThatServiceImpl extends ServiceImpl<AppletThatMapper, AppletThat> implements IAppletThatService {

    @Override
    public List<AppletThat> getByAppletIdFromDB(Long appletId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("applet_id", appletId);
        List<AppletThat> list = selectByMap(params);
        return list;
    }

    @Override
    public List<AppletThat> getByAppletId(Long appletId) {
        String key = RedisKeyUtil.getAppletThatMultiKey(appletId);
        String ids = RedisCacheUtil.valueGet(key);

        if (ids == null) {
            //查数据库
            ids = getIds(appletId);
            RedisCacheUtil.valueSet(key, ids, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_7);
        }

        //真实空
        if ("-1".equals(ids)) {
            return null;
        }

        return getlistCache(ids);
    }

    @Override
    public boolean deleteByAppletId(Long appletId) {
        String key = RedisKeyUtil.getAppletThatMultiKey(appletId);
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
        params.put("applet_id", appletId);
        deleteByMap(params);
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
     * @param appletId
     * @return
     */
    private String getIds(Long appletId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("applet_id", appletId);
        List<AppletThat> list = selectByMap(params);
        StringBuffer ids = new StringBuffer();
        if (CollectionUtils.isNotEmpty(list)) {
            for (AppletThat vo : list) {
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
    private List<AppletThat> getlistCache(String ids) {
        List<Long> tempList = getIdList(ids);
        if (CollectionUtils.isEmpty(tempList)) {
            return null;
        }
        List<String> keys = Lists.newArrayList();
        tempList.forEach(id -> {
            keys.add(RedisKeyUtil.getAppletThatKey(id));
        });
        //批量取缓存
        List<AppletThat> cacheList = RedisCacheUtil.mget(keys, AppletThat.class);
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
            List<AppletThat> dataList = selectBatchIds(unHitIdList);
            //批量map
            Map<String, Object> multiSet = Maps.newHashMap();
            // 未命中 结果记录
            List<AppletThat> unHitList = Lists.newArrayList();

            if (CollectionUtils.isNotEmpty(dataList)) {
                dataList.forEach(data -> {
                    unHitList.add(data);
                    multiSet.put(RedisKeyUtil.getAppletThatKey(data.getId()), data);
                });

                RedisCacheUtil.multiSet(multiSet, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_7);
                cacheList.addAll(unHitList);
            }
        }

        return cacheList;
    }
}

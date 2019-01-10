package com.iot.ifttt.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.ifttt.entity.AppletThis;
import com.iot.ifttt.mapper.AppletThisMapper;
import com.iot.ifttt.service.IAppletThisService;
import com.iot.ifttt.util.RedisKeyUtil;
import com.iot.redis.RedisCacheUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 * this组表 服务实现类
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-28
 */

/**
 * 缓存
 * <p>
 * getAppletThisMultiKey
 * getAppletThisKey
 * <p>
 * 获取是添加缓存
 * 删除时删除缓存
 */
@Service
public class AppletThisServiceImpl extends ServiceImpl<AppletThisMapper, AppletThis> implements IAppletThisService {

    @Override
    public List<AppletThis> getByAppletIdFromDB(Long appletId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("applet_id", appletId);
        List<AppletThis> thisList = selectByMap(params);
        return thisList;
    }

    @Override
    public List<AppletThis> getByAppletId(Long appletId) {
        String key = RedisKeyUtil.getAppletThisMultiKey(appletId);
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
        String key = RedisKeyUtil.getAppletThisMultiKey(appletId);
        String ids = RedisCacheUtil.valueGet(key);
        if (!StringUtils.isEmpty(ids) && !"-1".equals(ids)) {
            //删除子项缓存
            List<Long> tempList = getIdList(ids);
            List<String> keys = Lists.newArrayList();
            tempList.forEach(id -> {
                keys.add(RedisKeyUtil.getAppletThisKey(id));
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
        List<AppletThis> thisList = selectByMap(params);
        StringBuffer ids = new StringBuffer();
        if (CollectionUtils.isNotEmpty(thisList)) {
            for (AppletThis vo : thisList) {
                ids.append(vo.getId());
                ids.append(',');
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
    private List<AppletThis> getlistCache(String ids) {
        List<Long> tempList = getIdList(ids);
        if (CollectionUtils.isEmpty(tempList)) {
            return null;
        }
        List<String> keys = Lists.newArrayList();
        tempList.forEach(id -> {
            keys.add(RedisKeyUtil.getAppletThisKey(id));
        });
        //批量取缓存
        List<AppletThis> cacheList = RedisCacheUtil.mget(keys, AppletThis.class);
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
            List<AppletThis> dataList = selectBatchIds(unHitIdList);
            //批量map
            Map<String, Object> multiSet = Maps.newHashMap();
            // 未命中 结果记录
            List<AppletThis> unHitList = Lists.newArrayList();

            if (CollectionUtils.isNotEmpty(dataList)) {
                dataList.forEach(data -> {
                    unHitList.add(data);
                    multiSet.put(RedisKeyUtil.getAppletThisKey(data.getId()), data);
                });

                RedisCacheUtil.multiSet(multiSet, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_7);
                cacheList.addAll(unHitList);
            }
        }

        return cacheList;
    }
}

package com.iot.ifttt.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.ifttt.entity.AppletRelation;
import com.iot.ifttt.mapper.AppletRelationMapper;
import com.iot.ifttt.service.IAppletRelationService;
import com.iot.ifttt.util.RedisKeyUtil;
import com.iot.redis.RedisCacheUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 * 程序关联表 服务实现类
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-29
 */
@Service
public class AppletRelationServiceImpl extends ServiceImpl<AppletRelationMapper, AppletRelation> implements IAppletRelationService {

    @Override
    public List<Long> getAppletByKey(String type, String key) {
        return getAppletCache(type, key);
    }

    private List<Long> getAppletCache(String type, String triggerKey) {
        String key = RedisKeyUtil.getAppletRelationKey(type, triggerKey);
        String ids = RedisCacheUtil.valueGet(key);

        if (StringUtils.isEmpty(ids)) {
            //缓存不存在，从数据库中取
            List<Long> list = getListByDB(type, triggerKey);
            StringBuffer value = new StringBuffer();
            if (CollectionUtils.isNotEmpty(list)) {
                for (Long vo : list) {
                    value.append(vo);
                    value.append(',');
                }
            } else {
                value.append("-1");
            }
            ids = value.toString();
            //更新缓存，时效7天
            RedisCacheUtil.valueSet(key, ids, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_7);
        }


        Set<Long> set = new HashSet<>();
        if (!StringUtils.isEmpty(ids) && !"-1".equals(ids)) {
            //有数据
            String[] array = ids.split(",");
            for (String vo : array) {
                set.add(Long.parseLong(vo));
            }
        }

        return new ArrayList<>(set);
    }

    /**
     * 数据库获取
     *
     * @param type
     * @param key
     * @return
     */
    private List<Long> getListByDB(String type, String key) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("relation_key", key);
        params.put("type", type);
        List<AppletRelation> relations = selectByMap(params);
        List<Long> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(relations)) {
            for (AppletRelation vo : relations) {
                list.add(vo.getAppletId());
            }
        }
        return list;
    }
}

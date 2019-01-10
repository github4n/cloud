package com.iot.ifttt.channel.devstatus;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.iot.ifttt.channel.base.ILogic;
import com.iot.ifttt.common.IftttTypeEnum;
import com.iot.ifttt.entity.AppletItem;
import com.iot.ifttt.entity.AppletRelation;
import com.iot.ifttt.entity.AppletThis;
import com.iot.ifttt.service.IAppletRelationService;
import com.iot.ifttt.service.IAppletThisService;
import com.iot.ifttt.util.RedisKeyUtil;
import com.iot.redis.RedisCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 描述：设备状态操作逻辑
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/28 16:07
 */
@Service
public class DevStatusLogic implements ILogic {

    @Autowired
    private IAppletRelationService appletRelationService;
    @Autowired
    private IAppletThisService appletThisService;

    @Override
    public void add(AppletItem item) {
        //添加appletId-devId关联数据
        String json = item.getJson();
        Map<Object, Object> propMap = JSON.parseObject(json, Map.class);
        String devId = (String) propMap.get("devId");
        AppletRelation appletRelation = new AppletRelation();
        AppletThis appletThis = appletThisService.selectById(item.getEventId());
        appletRelation.setAppletId(appletThis.getAppletId());
        appletRelation.setType(IftttTypeEnum.DEV_STATUS.getType());
        appletRelation.setRelationKey(devId);
        appletRelationService.insert(appletRelation);
        //删除缓存
        String key = RedisKeyUtil.getAppletRelationKey(IftttTypeEnum.DEV_STATUS.getType(), devId);
        RedisCacheUtil.delete(key);
    }

    @Override
    public void delete(AppletItem item) {
        //删除appletId-devId关联数据
        String json = item.getJson();
        Map<Object, Object> propMap = JSON.parseObject(json, Map.class);
        String devId = (String) propMap.get("devId");
        String field = (String) propMap.get("field");
        Map<String, Object> params = Maps.newHashMap();
        params.put("relation_key", devId);
        params.put("applet_id", item.getAppletId());
        appletRelationService.deleteByMap(params);
        //删除缓存
        String key = RedisKeyUtil.getAppletRelationKey(IftttTypeEnum.DEV_STATUS.getType(), devId);
        RedisCacheUtil.delete(key);
        String devCheckKey = RedisKeyUtil.getAppletDevCheckKey(devId, item.getAppletId(), field);
        RedisCacheUtil.delete(devCheckKey);
    }
}

package com.iot.control.scene.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.util.StringUtil;
import com.iot.control.scene.domain.SceneDetail;
import com.iot.control.scene.mapper.SceneDetailMapper;
import com.iot.control.scene.service.SceneDetailService;
import com.iot.control.scene.util.RedisKeyUtil;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.rsp.SceneDetailResp;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/5/7 13:55
 * 修改人:
 * 修改时间：
 */

@Service
public class SceneDetailServiceImpl extends ServiceImpl<SceneDetailMapper,SceneDetail> implements SceneDetailService {

    private static final Logger logger = LoggerFactory.getLogger(SceneDetailServiceImpl.class);


    @Autowired
    private SceneDetailMapper sceneDetailMapper;

    @Override
    public List<SceneDetailResp> sceneDetailByParam(SceneDetailReq sceneDetailReq) {
        String sceneDetailListIdKey = RedisKeyUtil.getSceneDetailListIdKey(sceneDetailReq.getSceneId(),sceneDetailReq.getTenantId());

        List<SceneDetailResp> sceneDetailList = RedisCacheUtil.listGetAll(sceneDetailListIdKey, SceneDetailResp.class);
        if (CollectionUtils.isEmpty(sceneDetailList)){
            sceneDetailList = this.sceneDetailByParamDoing(sceneDetailReq);
            // 加入缓存
            RedisCacheUtil.listSet(sceneDetailListIdKey, sceneDetailList);
            RedisCacheUtil.expireKey(sceneDetailListIdKey, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT);
        }
        return sceneDetailList;
    }

    @Override
    public List<SceneDetailResp> sceneDetailByParamDoing(SceneDetailReq sceneDetailReq) {
        EntityWrapper wrapper = new EntityWrapper();
        if (sceneDetailReq.getId()!=null){
            wrapper.eq("id",sceneDetailReq.getId());
        }
        if (sceneDetailReq.getSceneId()!=null){
            wrapper.eq("scene_id",sceneDetailReq.getSceneId());
        }
        if (StringUtils.isNotEmpty(sceneDetailReq.getDeviceId())){
            wrapper.eq("device_id",sceneDetailReq.getDeviceId());
        }
        if (sceneDetailReq.getSpaceId()!=null){
            wrapper.eq("space_id",sceneDetailReq.getSpaceId());
        }
        if (sceneDetailReq.getDeviceTypeId()!=null){
            wrapper.eq("device_type_id",sceneDetailReq.getDeviceTypeId());
        }
        if (StringUtils.isNotEmpty(sceneDetailReq.getTargetValue())){
            wrapper.eq("target_value",sceneDetailReq.getTargetValue());
        }
        if (sceneDetailReq.getTenantId()!=null){
            wrapper.eq("tenant_id",sceneDetailReq.getTenantId());
        }
        if (sceneDetailReq.getSort()!=null){
            wrapper.eq("sort",sceneDetailReq.getSort());
        }
        if (sceneDetailReq.getLocationId()!=null){
            wrapper.eq("location_id",sceneDetailReq.getLocationId());
        }
        if (StringUtils.isNotEmpty(sceneDetailReq.getMethod())){
            wrapper.eq("method",sceneDetailReq.getMethod());
        }
        if (sceneDetailReq.getCreateBy()!=null){
            wrapper.eq("create_by",sceneDetailReq.getCreateBy());
        }
        if (sceneDetailReq.getUpdateBy()!=null){
            wrapper.eq("update_by",sceneDetailReq.getUpdateBy());
        }
        if (sceneDetailReq.getOrgId()!=null){
            wrapper.eq("org_id",sceneDetailReq.getOrgId());
        }
        List<SceneDetail> list = super.selectList(wrapper);
        List<SceneDetailResp> result = new ArrayList<>();
        list.forEach(m->{
            SceneDetailResp sceneDetailResp = new SceneDetailResp();
            sceneDetailResp.setId(m.getId());
            sceneDetailResp.setSceneId(m.getSceneId());
            sceneDetailResp.setDeviceId(m.getDeviceId());
            sceneDetailResp.setSpaceId(m.getSpaceId());
            sceneDetailResp.setDeviceTypeId(m.getDeviceTypeId());
            sceneDetailResp.setTargetValue(m.getTargetValue());
            sceneDetailResp.setCreateBy(m.getCreateBy());
            sceneDetailResp.setUpdateBy(m.getUpdateBy());
            sceneDetailResp.setCreateTime(m.getCreateTime());
            sceneDetailResp.setUpdateTime(m.getUpdateTime());
            sceneDetailResp.setTenantId(m.getTenantId());
            sceneDetailResp.setSort(m.getSort());
            sceneDetailResp.setLocationId(m.getLocationId());
            sceneDetailResp.setMethod(m.getMethod());
            sceneDetailResp.setOrgId(m.getOrgId());
            result.add(sceneDetailResp);
        });
        return result;
    }

    @Override
    public int updateSceneDetail(SceneDetailReq sceneDetailReq) {
        logger.debug("updateSceneDetail {}",JSONObject.toJSONString(sceneDetailReq));
        SceneDetail sceneDetail = new SceneDetail();
        sceneDetail.setId(sceneDetailReq.getId());
        sceneDetail.setSceneId(sceneDetailReq.getSceneId());
        sceneDetail.setDeviceId(sceneDetailReq.getDeviceId());
        sceneDetail.setSpaceId(sceneDetailReq.getSpaceId());
        sceneDetail.setDeviceTypeId(sceneDetailReq.getDeviceTypeId());
        sceneDetail.setTargetValue(sceneDetailReq.getTargetValue());
        sceneDetail.setUpdateBy(sceneDetailReq.getUpdateBy());
        sceneDetail.setUpdateTime(new Date());
        sceneDetail.setTenantId(sceneDetailReq.getTenantId());
        sceneDetail.setSort(sceneDetailReq.getSort());
        sceneDetail.setLocationId(sceneDetailReq.getLocationId());
        sceneDetail.setMethod(sceneDetailReq.getMethod());
        sceneDetail.setOrgId(sceneDetailReq.getOrgId());
        RedisCacheUtil.delete(RedisKeyUtil.getSceneDetailListIdKey(sceneDetail.getSceneId(),sceneDetail.getTenantId()));
        Boolean isOk = super.updateById(sceneDetail);
        return isOk?1:0;
    }

    @Override
    public void insert(SceneDetailReq sceneDetailReq){
        logger.debug("insert {}", JSONObject.toJSONString(sceneDetailReq));
        SceneDetail sceneDetail = new SceneDetail();
        sceneDetail.setId(sceneDetailReq.getId());
        sceneDetail.setSceneId(sceneDetailReq.getSceneId());
        sceneDetail.setDeviceId(sceneDetailReq.getDeviceId());
        sceneDetail.setSpaceId(sceneDetailReq.getSpaceId());
        sceneDetail.setDeviceTypeId(sceneDetailReq.getDeviceTypeId());
        sceneDetail.setTargetValue(sceneDetailReq.getTargetValue());
        sceneDetail.setCreateBy(sceneDetailReq.getCreateBy());
        sceneDetail.setUpdateBy(sceneDetailReq.getUpdateBy());
        sceneDetail.setCreateTime(new Date());
        sceneDetail.setUpdateTime(new Date());
        sceneDetail.setTenantId(sceneDetailReq.getTenantId());
        sceneDetail.setSort(sceneDetailReq.getSort());
        sceneDetail.setLocationId(sceneDetailReq.getLocationId());
        sceneDetail.setMethod(sceneDetailReq.getMethod());
        sceneDetail.setOrgId(sceneDetailReq.getOrgId());
        sceneDetailMapper.insertSceneDetail(sceneDetail);
        RedisCacheUtil.delete(RedisKeyUtil.getSceneDetailListIdKey(sceneDetail.getSceneId(),sceneDetail.getTenantId()));
        RedisCacheUtil.delete(RedisKeyUtil.getSceneDetailCountKey(sceneDetail.getSceneId(),sceneDetail.getTenantId()));
    }


    @Override
    public int deleteSceneDetail(SceneDetailReq sceneDetailReq) {
        List<SceneDetailResp> sceneDetailResps = this.sceneDetailByParamDoing(sceneDetailReq);
        List result = new ArrayList<>();
        Boolean isOk = false;
        if (sceneDetailResps.size()>0){
            sceneDetailResps.forEach(m->{
                // 移除缓存
                RedisCacheUtil.delete(RedisKeyUtil.getSceneDetailListIdKey(m.getSceneId(),m.getTenantId()));
                RedisCacheUtil.delete(RedisKeyUtil.getSceneDetailCountKey(m.getSceneId(),m.getTenantId()));
                result.add(m.getId());
            });
            isOk = super.deleteBatchIds(result);
        }
        return isOk?1:0;
    }

    @Override
    public int deleteSceneDetailByDeviceId(SceneDetailReq sceneDetailReq) {
        logger.debug("deleteSceneDetailByDeviceId {}", JSONObject.toJSONString(sceneDetailReq));
        List<SceneDetailResp> list = this.sceneDetailByParamDoing(sceneDetailReq);
        Boolean isOk = false;
        if (list.size()>0){
            List result = new ArrayList();
            List sceneListKey = new ArrayList();
            List sceneDetailCountListKey = new ArrayList();
            list.forEach(m->{
                sceneListKey.add(RedisKeyUtil.getSceneDetailListIdKey(m.getSceneId(),m.getTenantId()));
                sceneDetailCountListKey.add(RedisKeyUtil.getSceneDetailCountKey(m.getSceneId(),m.getTenantId()));
                result.add(m.getId());
            });
            RedisCacheUtil.delete(sceneListKey);
            RedisCacheUtil.delete(sceneDetailCountListKey);
            isOk = super.deleteBatchIds(result);
        }
        return isOk?1:0;
    }

    @Override
    public List<SceneDetailResp> sceneDetailList(SceneDetailReq sceneDetailReq){
        return sceneDetailMapper.sceneDetailList(sceneDetailReq);
    }


    @Override
    public int countChildBySceneId(Long sceneId) {
        return sceneDetailMapper.countChildBySceneId(sceneId);
    }

    @Override
    public Map<Long, Integer> countChildBySceneIds(List<Long> sceneIdList) {
        if (CollectionUtils.isEmpty(sceneIdList)) {
            return null;
        }

        Long tenantId = SaaSContextHolder.currentTenantId();

        // 需要返回的数据
        Map<Long, Integer> childCountMap = Maps.newHashMap();

        // 缓存的key值
        List<String> sceneDetailCountKeys = Lists.newArrayList();
        sceneIdList.forEach(sceneId -> {
            sceneDetailCountKeys.add(RedisKeyUtil.getSceneDetailCountKey(sceneId, tenantId));
        });

        // 从缓存读取
        List<Long> unHitIdList = Lists.newArrayList();
        List<String> cacheCountList = RedisCacheUtil.mget(sceneDetailCountKeys, false);
        for (int i = 0; i < cacheCountList.size(); i++) {
            Long sceneId = sceneIdList.get(i);
            String cacheCount = cacheCountList.get(i);

            if (StringUtil.isEmpty(cacheCount)) {
                unHitIdList.add(sceneId);
            } else {
                childCountMap.put(sceneId, Integer.parseInt(cacheCount));
            }
        }

        // 如果 缓存中个数 与 实际要查的个数 不一致, 改从数据库查
        if (unHitIdList.size() > 0) {
            // 从数据库 获取
            List<Map> resultList = sceneDetailMapper.countChildBySceneIds(unHitIdList);
            if (CollectionUtils.isNotEmpty(resultList)) {
                for (Map map : resultList) {
                    Long sceneId = (Long) map.get("sceneId");
                    Long childCount = (Long) map.get("childCount");
                    childCount = childCount == null ? 0 : childCount;
                    childCountMap.put(sceneId, childCount.intValue());
                }
            }

            // 存入缓存
            Map<String, Object> multiSet = Maps.newHashMap();
            unHitIdList.forEach(sceneId -> {
                Integer childCount = childCountMap.get(sceneId);
                childCount = childCount == null ? 0 : childCount;
                multiSet.put(RedisKeyUtil.getSceneDetailCountKey(sceneId, tenantId), childCount);
            });
            RedisCacheUtil.multiSet(multiSet, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT);
        }

        return childCountMap;
    }

}

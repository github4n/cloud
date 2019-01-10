package com.iot.control.share.web;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.iot.common.exception.BusinessException;
import com.iot.control.device.core.utils.CacheKeyUtils;
import com.iot.control.device.core.utils.HitCacheUtils;
import com.iot.control.share.api.ShareSpaceApi;
import com.iot.control.share.entity.ShareSpace;
import com.iot.control.share.exception.ShareSpaceExceptionEnum;
import com.iot.control.share.service.IShareSpaceService;
import com.iot.control.share.vo.req.AddShareSpaceReq;
import com.iot.control.share.vo.resp.ShareSpaceResp;
import com.iot.redis.RedisCacheUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 家分享表 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2019-01-02
 */
@RestController
public class ShareSpaceController implements ShareSpaceApi {

    public static final long CACHE_EXPIRE_TIME = CacheKeyUtils.EXPIRE_TIME_OUT;

    public static final String SHARE_SPACE_CACHE_DATA_KEY = "tenant:%s:share_space_data:id:%s";// share_space  id -> json data

    public static final String SHARE_SPACE_UUID_KEY = "tenant:%s:share_space_uuid:%s"; //存储对应的 share_space id

    public static final String SHARE_SPACE_TO_USER_KEY = "tenant:%s:share_space_to:%s";//存储对应的 share_space ids

    public static final String SHARE_SPACE_FROM_USER_KEY = "tenant:%s:share_space_from:%s";//存储对应的 share_space ids

    public static final String SHARE_SPACE_SPACE_KEY = "tenant:%s:share_space_sid:%s";//存储对应的 share_space ids

    @Autowired
    private IShareSpaceService shareSpaceService;

    @Override
    public ShareSpaceResp saveOrUpdate(@RequestBody AddShareSpaceReq params) {

        ShareSpace targetData = null;
        if (params.getId() == null) {
            targetData = new ShareSpace();
            BeanUtils.copyProperties(params, targetData);
            shareSpaceService.insert(targetData);
        } else {
            targetData = shareSpaceService.selectById(params.getId());
            if (targetData == null) {
                throw new BusinessException(ShareSpaceExceptionEnum.SHARE_SPACE_NOT_EXIST_ERROR);
            }
            if (params.getStatus() != null) {
                targetData.setStatus(params.getStatus());
            }
            if (!StringUtils.isEmpty(params.getShareUuid())) {
                RedisCacheUtil.delete(String.format(SHARE_SPACE_UUID_KEY, params.getTenantId(), params.getTenantId(), targetData.getShareUuid()));//删除原 uuid缓存
                targetData.setShareUuid(params.getShareUuid());
            }
            if (!StringUtils.isEmpty(params.getRemark())) {
                targetData.setRemark(params.getRemark());
            }
            shareSpaceService.updateById(targetData);
            RedisCacheUtil.delete(String.format(SHARE_SPACE_CACHE_DATA_KEY, targetData.getTenantId(), targetData.getId()));
        }
        RedisCacheUtil.delete(String.format(SHARE_SPACE_TO_USER_KEY, targetData.getTenantId(), targetData.getToUserId()));
        RedisCacheUtil.delete(String.format(SHARE_SPACE_FROM_USER_KEY, targetData.getTenantId(), targetData.getFromUserId()));
        RedisCacheUtil.delete(String.format(SHARE_SPACE_SPACE_KEY, targetData.getTenantId(), targetData.getSpaceId()));
        ShareSpaceResp resultData = new ShareSpaceResp();
        BeanUtils.copyProperties(targetData, resultData);
        return resultData;
    }

    @Override
    public ShareSpaceResp getByShareUUID(@RequestParam("tenantId") Long tenantId,
                                         @RequestParam("shareUUID") String shareUUID) {

        String cacheDataStr = RedisCacheUtil.valueGet(String.format(SHARE_SPACE_UUID_KEY, tenantId, shareUUID));
        if (!StringUtils.isEmpty(cacheDataStr)) {
            Long shareId = Long.parseLong(cacheDataStr);
            ShareSpaceResp resultData = this.getById(tenantId, shareId);
            return resultData;
        }
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("tenant_id", tenantId);
        wrapper.eq("share_uuid", shareUUID);
        ShareSpace targetData = shareSpaceService.selectOne(wrapper);
        if (targetData != null) {
            RedisCacheUtil.valueSet(String.format(SHARE_SPACE_UUID_KEY, tenantId, shareUUID), String.valueOf(targetData.getId()), CACHE_EXPIRE_TIME);
            ShareSpaceResp resultData = new ShareSpaceResp();
            BeanUtils.copyProperties(targetData, resultData);
            return resultData;
        }
        return null;
    }

    @Override
    public ShareSpaceResp getByToUserId(@RequestParam("tenantId") Long tenantId,
                                        @RequestParam("spaceId") Long spaceId, @RequestParam("toUserId") Long toUserId) {
        List<ShareSpaceResp> resultDataList = this.listByToUserId(tenantId, toUserId);
        if (CollectionUtils.isEmpty(resultDataList)) {
            return null;
        }
        for (ShareSpaceResp targetData : resultDataList) {
            if (targetData.getSpaceId().compareTo(spaceId) == 0) {
                ShareSpaceResp resultData = new ShareSpaceResp();
                BeanUtils.copyProperties(targetData, resultData);
                return resultData;
            }
        }
        return null;
    }

    @Override
    public int countByFromUserId(@RequestParam("tenantId") Long tenantId, @RequestParam("fromUserId") Long fromUserId) {
        List<ShareSpaceResp> resultDataList = this.listByFromUserId(tenantId, fromUserId);
        if (CollectionUtils.isEmpty(resultDataList)) {
            return 0;
        }
        return resultDataList.size();
    }

    @Override
    public List<ShareSpaceResp> listByFromUserId(@RequestParam("tenantId") Long tenantId, @RequestParam("fromUserId") Long fromUserId) {
        List<ShareSpaceResp> resultDataList = Lists.newArrayList();
        List<ShareSpace> targetDataList;
        List<Long> cacheIdsList = RedisCacheUtil.listGetAll(String.format(SHARE_SPACE_FROM_USER_KEY, tenantId, fromUserId), Long.class);
        if (CollectionUtils.isEmpty(cacheIdsList)) {
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.eq("tenant_id", tenantId);
            wrapper.eq("from_user_id", fromUserId);
            wrapper.eq("status", ShareSpace.INVITE_SUCCESS);
            targetDataList = shareSpaceService.selectList(wrapper);
            if (!CollectionUtils.isEmpty(targetDataList)) {
                List<Long> shareIds = Lists.newArrayList();
                targetDataList.forEach(targetData -> {
                    shareIds.add(targetData.getId());
                });
                //cache ids
                RedisCacheUtil.listSet(String.format(SHARE_SPACE_FROM_USER_KEY, tenantId, fromUserId), shareIds, CACHE_EXPIRE_TIME, true);
            }
        } else {
            targetDataList = this.listMultiByIds(tenantId, cacheIdsList);
        }
        if (!CollectionUtils.isEmpty(targetDataList)) {
            targetDataList.forEach(targetData -> {
                ShareSpaceResp resultData = new ShareSpaceResp();
                BeanUtils.copyProperties(targetData, resultData);
                resultDataList.add(resultData);
            });
        }
        return resultDataList;
    }

    @Override
    public List<ShareSpaceResp> listBySpaceId(@RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId) {

        List<ShareSpaceResp> resultDataList = Lists.newArrayList();
        List<ShareSpace> targetDataList;
        List<Long> cacheIdsList = RedisCacheUtil.listGetAll(String.format(SHARE_SPACE_SPACE_KEY, tenantId, spaceId), Long.class);
        if (CollectionUtils.isEmpty(cacheIdsList)) {
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.eq("tenant_id", tenantId);
            wrapper.eq("space_id", spaceId);
            targetDataList = shareSpaceService.selectList(wrapper);
            if (!CollectionUtils.isEmpty(targetDataList)) {
                List<Long> shareIds = Lists.newArrayList();
                targetDataList.forEach(targetData -> {
                    shareIds.add(targetData.getId());
                });
                //cache ids
                RedisCacheUtil.listSet(String.format(SHARE_SPACE_SPACE_KEY, tenantId, spaceId), shareIds, CACHE_EXPIRE_TIME, true);
            }
        } else {
            targetDataList = this.listMultiByIds(tenantId, cacheIdsList);
        }
        if (!CollectionUtils.isEmpty(targetDataList)) {
            targetDataList.forEach(targetData -> {
                ShareSpaceResp resultData = new ShareSpaceResp();
                BeanUtils.copyProperties(targetData, resultData);
                resultDataList.add(resultData);
            });
        }
        return resultDataList;
    }

    @Override
    public List<ShareSpaceResp> listByToUserId(@RequestParam("tenantId") Long tenantId, @RequestParam("toUserId") Long toUserId) {
        List<ShareSpaceResp> resultDataList = Lists.newArrayList();
        List<ShareSpace> targetDataList;
        List<Long> cacheIdsList = RedisCacheUtil.listGetAll(String.format(SHARE_SPACE_TO_USER_KEY, tenantId, toUserId), Long.class);
        if (CollectionUtils.isEmpty(cacheIdsList)) {
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.eq("tenant_id", tenantId);
            wrapper.eq("to_user_id", toUserId);
//            wrapper.eq("status", ShareSpace.INVITE_SUCCESS);
            targetDataList = shareSpaceService.selectList(wrapper);
            if (!CollectionUtils.isEmpty(targetDataList)) {
                List<Long> shareIds = Lists.newArrayList();
                targetDataList.forEach(targetData -> {
                    shareIds.add(targetData.getId());
                });
                //cache ids
                RedisCacheUtil.listSet(String.format(SHARE_SPACE_TO_USER_KEY, tenantId, toUserId), shareIds, CACHE_EXPIRE_TIME, true);
            }
        } else {
            targetDataList = this.listMultiByIds(tenantId, cacheIdsList);
        }
        if (!CollectionUtils.isEmpty(targetDataList)) {
            targetDataList.forEach(targetData -> {
                ShareSpaceResp resultData = new ShareSpaceResp();
                BeanUtils.copyProperties(targetData, resultData);
                resultDataList.add(resultData);
            });
        }
        return resultDataList;
    }

    @Override
    public ShareSpaceResp getById(@RequestParam("tenantId") Long tenantId, @RequestParam("shareId") Long shareId) {

        String cacheDataStr = RedisCacheUtil.valueGet(String.format(SHARE_SPACE_CACHE_DATA_KEY, tenantId, shareId));
        ShareSpace targetData;
        if (StringUtils.isEmpty(cacheDataStr)) {
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.eq("tenant_id", tenantId);
            wrapper.eq("id", shareId);
            targetData = shareSpaceService.selectOne(wrapper);
            if (targetData != null) {
                RedisCacheUtil.valueSet(String.format(SHARE_SPACE_CACHE_DATA_KEY, targetData.getTenantId(), targetData.getId()), JSON.toJSONString(targetData), CACHE_EXPIRE_TIME);
            }
        } else {
            targetData = JSON.parseObject(cacheDataStr, ShareSpace.class);
        }
        if (targetData != null) {
            ShareSpaceResp resultData = new ShareSpaceResp();
            BeanUtils.copyProperties(targetData, resultData);
            return resultData;
        }
        return null;
    }

    private List<ShareSpace> listMultiByIds(Long tenantId, List<Long> shareIds) {
        List<ShareSpace> resultDataList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(shareIds)) {
            return resultDataList;
        }
        List<String> redisKeys = Lists.newArrayList();

        shareIds.forEach(shareId -> {
            redisKeys.add(String.format(SHARE_SPACE_CACHE_DATA_KEY, tenantId, shareId));
        });
        //1.获取缓存数据
        List<ShareSpace> cacheDataList = RedisCacheUtil.mget(redisKeys, ShareSpace.class);
        List<Long> cacheIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(cacheDataList)) {
            cacheDataList.forEach(cacheData -> {
                cacheIds.add(cacheData.getId());
            });
            resultDataList.addAll(cacheDataList);
        }
        // 2.检查部分缓存过期的 ---捞出未命中的deviceIds
        List<Long> noHitIds = HitCacheUtils.getNoHitCacheIds(cacheIds, shareIds);
        // 3.db获取
        if (!CollectionUtils.isEmpty(noHitIds)) {
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.eq("tenant_id", tenantId);
            wrapper.in("id", noHitIds);
            List<ShareSpace> dbDataList = shareSpaceService.selectList(wrapper);
            // 4.cache
            if (!CollectionUtils.isEmpty(dbDataList)) {
                dbDataList.forEach(targetData -> {
                    RedisCacheUtil.valueSet(String.format(SHARE_SPACE_CACHE_DATA_KEY, targetData.getTenantId(), targetData.getId()), JSON.toJSONString(targetData), CACHE_EXPIRE_TIME);
                });
            }
            // 5.组转返回
            resultDataList.addAll(dbDataList);
        }

        return resultDataList;

    }

    @Override
    public void delById(@RequestParam("tenantId") Long tenantId, @RequestParam("shareId") Long shareId) {

        ShareSpaceResp targetData = this.getById(tenantId, shareId);
        if (targetData != null) {
            RedisCacheUtil.delete(String.format(SHARE_SPACE_CACHE_DATA_KEY, targetData.getTenantId(), targetData.getId()));
            RedisCacheUtil.delete(String.format(SHARE_SPACE_TO_USER_KEY, targetData.getTenantId(), targetData.getToUserId()));
            RedisCacheUtil.delete(String.format(SHARE_SPACE_FROM_USER_KEY, targetData.getTenantId(), targetData.getFromUserId()));
            RedisCacheUtil.delete(String.format(SHARE_SPACE_SPACE_KEY, targetData.getTenantId(), targetData.getSpaceId()));

            shareSpaceService.deleteById(shareId);
        }
    }

    @Override
    public void delBySpaceId(@RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId) {

        List<Long> cacheIds = RedisCacheUtil.listGetAll(String.format(SHARE_SPACE_SPACE_KEY, tenantId, spaceId), Long.class);
        if (!CollectionUtils.isEmpty(cacheIds)) {

            List<String> redisDataKeys = Lists.newArrayList();
            cacheIds.forEach(shareId -> {
                redisDataKeys.add(String.format(SHARE_SPACE_CACHE_DATA_KEY, tenantId, shareId));
            });
            RedisCacheUtil.delete(redisDataKeys);
            List<String> redisToUserKeys = Lists.newArrayList();
            List<String> redisFromUserKeys = Lists.newArrayList();
            List<ShareSpace> resultDataList = this.listMultiByIds(tenantId, cacheIds);
            resultDataList.forEach(targetData -> {
                redisToUserKeys.add(String.format(SHARE_SPACE_TO_USER_KEY, tenantId, targetData.getToUserId()));
                redisFromUserKeys.add(String.format(SHARE_SPACE_FROM_USER_KEY, tenantId, targetData.getFromUserId()));

            });
            RedisCacheUtil.delete(redisToUserKeys);
            RedisCacheUtil.delete(redisFromUserKeys);
            RedisCacheUtil.delete(String.format(SHARE_SPACE_SPACE_KEY, tenantId, spaceId));

            shareSpaceService.deleteBatchIds(cacheIds);
        } else {
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setSqlSelect("id");
            wrapper.eq("tenant_id", tenantId);
            wrapper.eq("space_id", spaceId);
            List<Long> targetDataList = shareSpaceService.selectObjs(wrapper);
            if (!CollectionUtils.isEmpty(targetDataList)) {
                shareSpaceService.deleteBatchIds(targetDataList);
            }
        }

    }
}


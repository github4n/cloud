package com.iot.control.favorite.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.iot.control.favorite.entity.FavoriteEntity;
import com.iot.control.favorite.mapper.FavoriteMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.control.favorite.service.IFavoriteService;
import com.iot.control.favorite.utils.BeanCopyUtils;
import com.iot.control.favorite.utils.RedisKeyUtil;
import com.iot.control.favorite.vo.FavoriteReq;
import com.iot.control.favorite.vo.FavoriteResp;
import com.iot.redis.RedisCacheUtil;
import com.iot.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 最喜爱设备及场景表 服务实现类
 * </p>
 *
 * @author lucky
 * @since 2019-01-02
 */
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, FavoriteEntity> implements IFavoriteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteServiceImpl.class);

    //通过用户id查找
    @Override
    public List<FavoriteResp> getFavoriteList(Long tenantId, Long userId,Long homeId) {

        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(userId, "userId.notnull");

        String favoriteKeybyUserId= RedisKeyUtil.getFavoriteListUser(userId,tenantId);
        List<FavoriteResp> respList=RedisCacheUtil.listGetAll(favoriteKeybyUserId,FavoriteResp.class);
        if(respList==null){
            EntityWrapper ew = new EntityWrapper();
            ew.eq("tenant_id", tenantId);
            ew.eq("user_id", userId);
            List<FavoriteEntity> mapResultList = super.selectList(ew);
            if(mapResultList!=null){
                BeanCopyUtils.copyToFavoriteRespList(respList,mapResultList);
                RedisCacheUtil.listSet(favoriteKeybyUserId,respList);
            }
        }
        LOGGER.info("getFavoriteList={}", JSON.toJSONString(respList));
        return respList;
    }

    //根据userId和tenantId删除
    @Override
    public Boolean delAllFavoriteByUserId(Long tenantId, Long userId) {

        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(userId, "userId.notnull");
        try {
            EntityWrapper ew = new EntityWrapper();
            ew.eq("tenant_id", tenantId);
            ew.eq("user_id", userId);
            String favoriteKeybyUserId= RedisKeyUtil.getFavoriteListUser(userId,tenantId);
            RedisCacheUtil.delete(favoriteKeybyUserId);
            return super.delete(ew);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("delAllFavoriteByUserId erro!"+e.toString());
        }
        return false;
    }

    @Override
    public Boolean delAllFavoriteBySpaceId(Long tenantId, Long spaceId) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(spaceId, "spaceId.notnull");
        try {
            EntityWrapper ew = new EntityWrapper();
            ew.eq("tenant_id", tenantId);
            ew.eq("space_id", spaceId);
            String favoriteKeybyUserId= RedisKeyUtil.getFavoriteListUser(spaceId,tenantId);
            RedisCacheUtil.delete(favoriteKeybyUserId);
            return super.delete(ew);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("delAllFavoriteBySpaceId erro!"+e.toString());
        }
        return false;
    }

    @Override
    public void insertFavorite(FavoriteReq favoriteReq) {

        if(favoriteReq==null){
            throw new IllegalArgumentException("favoriteEntity.notnull");
        }

        Long tenantId=favoriteReq.getTenantId();
        Long userId=favoriteReq.getUserId();
        Long spaceId=favoriteReq.getSpaceId();
        Integer typeId=favoriteReq.getTypeId();
        String devScene=favoriteReq.getDevScene();
        Long createBy=favoriteReq.getCreateBy();
        Long updateBy=favoriteReq.getUpdateBy();
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(userId, "userId.notnull");
        AssertUtils.notNull(spaceId, "homeId.notnull");
        AssertUtils.notNull(typeId, "typeId.notnull");
        AssertUtils.notNull(devScene, "devScene.notnull");
        AssertUtils.notNull(createBy, "createBy.notnull");
        AssertUtils.notNull(updateBy, "updateBy.notnull");

        try {


            EntityWrapper ew = new EntityWrapper();
            ew.eq("tenant_id", tenantId);
            ew.eq("user_id", userId);
            ew.eq("space_id", spaceId);
            ew.eq("dev_scene", devScene);
            ew.eq("type_id", typeId);

            FavoriteEntity tmp=(FavoriteEntity)super.selectObj(ew);
            LOGGER.info("insertFavoriteTmp={}", JSON.toJSONString(tmp));
            if(tmp==null){
                tmp=new FavoriteEntity();
                BeanCopyUtils.copyToFavoriteEntity(tmp,favoriteReq);
                String favoriteKeybyUserId= RedisKeyUtil.getFavoriteListUser(userId,tenantId);
                RedisCacheUtil.delete(favoriteKeybyUserId);
                Boolean ok=super.insert(tmp);
                LOGGER.info("insertFavorite insertok={}",ok);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("insertFavorite erro!"+e.toString());
        }
    }

    @Override
    public void delSingleFavorite(FavoriteReq favoriteReq) {
        Long tenantId=favoriteReq.getTenantId();
        Long userId=favoriteReq.getUserId();
        Long spaceId=favoriteReq.getSpaceId();
        Integer typeId=favoriteReq.getTypeId();
        String devScene=favoriteReq.getDevScene();

        AssertUtils.notNull(devScene, "devScene.notnull");
        AssertUtils.notNull(typeId, "typeId.notnull");
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(userId, "userId.notnull");
        try {
            EntityWrapper ew = new EntityWrapper();
            ew.eq("tenant_id", tenantId);
            ew.eq("user_id", userId);
            if(spaceId!=null){
                ew.eq("space_id", spaceId);
            }
            ew.eq("dev_Scene", devScene);
            ew.eq("type_id", typeId);

            String favoriteKeybyUserId= RedisKeyUtil.getFavoriteListUser(userId,tenantId);
            RedisCacheUtil.delete(favoriteKeybyUserId);
            super.delete(ew);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("delSingleFavorite erro!"+e.toString());
        }

    }


    @Override
    public FavoriteResp getFavoriteStatus(FavoriteReq favoriteReq) {
        Long tenantId=favoriteReq.getTenantId();
        Long userId=favoriteReq.getUserId();
        Long spaceId=favoriteReq.getSpaceId();
        Integer typeId=favoriteReq.getTypeId();
        String devScene=favoriteReq.getDevScene();

        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(userId, "userId.notnull");
        AssertUtils.notNull(spaceId, "homeId.notnull");
        AssertUtils.notNull(typeId, "typeId.notnull");
        AssertUtils.notNull(devScene, "devScene.notnull");

        EntityWrapper ew = new EntityWrapper();
        ew.eq("tenant_id", tenantId);
        ew.eq("user_id", userId);
        ew.eq("space_id", spaceId);
        ew.eq("dev_Scene", devScene);
        ew.eq("type_id", typeId);

        FavoriteEntity result=(FavoriteEntity)super.selectObj(ew);
        FavoriteResp favoriteResp=new FavoriteResp();
        BeanCopyUtils.copyToFavoriteResp(favoriteResp,result);
        LOGGER.info("insertFavoriteResult={}  favoriteResp={}", JSON.toJSONString(result),JSON.toJSONString(favoriteResp));
        return favoriteResp;
    }


}

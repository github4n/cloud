package com.iot.control.favorite.utils;

import com.iot.control.favorite.entity.FavoriteEntity;
import com.iot.control.favorite.vo.FavoriteReq;
import com.iot.control.favorite.vo.FavoriteResp;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class BeanCopyUtils {

    public static void copyToFavoriteResp(FavoriteResp target, FavoriteEntity source){
        if(target==null || source==null){
            return ;
        }
        target.setId(source.getId());
        target.setTenantId(source.getTenantId());
        target.setSpaceId(source.getSpaceId());
        target.setDevScene(source.getDevScene());
        target.setUserId(source.getUserId());
        target.setTypeId(source.getTypeId());
        target.setOrderId(source.getOrderId());
        target.setCreateBy(source.getCreateBy());
        target.setUpdateBy(source.getUpdateBy());
        target.setCreateTime(source.getCreateTime());
        target.setUpdatTime(source.getUpdatTime());
    }
    public static void copyToFavoriteEntity(FavoriteEntity target, FavoriteReq source){
        if(target==null || source==null){
            return ;
        }
        target.setId(source.getId());
        target.setTenantId(source.getTenantId());
        target.setSpaceId(source.getSpaceId());
        target.setDevScene(source.getDevScene());
        target.setUserId(source.getUserId());
        target.setTypeId(source.getTypeId());
        target.setOrderId(source.getOrderId());
        target.setCreateBy(source.getCreateBy());
        target.setUpdateBy(source.getUpdateBy());
        target.setCreateTime(source.getCreateTime());
        target.setUpdatTime(source.getUpdatTime());
    }


    public static void  copyToFavoriteRespList(List<FavoriteResp> target,List<FavoriteEntity> source){
        if(target==null){
            return;
        }
        if(CollectionUtils.isEmpty(source)){
            return;
        }
        for (FavoriteEntity tmp:source){
            FavoriteResp favoriteResp=new FavoriteResp();
            copyToFavoriteResp(favoriteResp,tmp);
            target.add(favoriteResp);
        }
    }
}

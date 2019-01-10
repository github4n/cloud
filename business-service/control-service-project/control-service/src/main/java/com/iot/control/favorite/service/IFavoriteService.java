package com.iot.control.favorite.service;


import com.baomidou.mybatisplus.service.IService;
import com.iot.control.favorite.entity.FavoriteEntity;
import com.iot.control.favorite.vo.FavoriteReq;
import com.iot.control.favorite.vo.FavoriteResp;

import java.util.List;

/**
 * <p>
 * 最喜爱设备及场景表 服务类
 * </p>
 *
 * @author lucky
 * @since 2019-01-02
 */
public interface IFavoriteService extends IService<FavoriteEntity> {

    List<FavoriteResp> getFavoriteList(Long tenantId, Long userId,Long homeId);

    Boolean delAllFavoriteByUserId(Long tenantId, Long userId);

    Boolean delAllFavoriteBySpaceId(Long tenantId, Long spaceId);

    void insertFavorite(FavoriteReq favoriteEntity);

    void delSingleFavorite(FavoriteReq favoriteEntity);

    FavoriteResp getFavoriteStatus(FavoriteReq favoriteEntity);


}

package com.iot.control.favorite.controller;


import com.iot.control.favorite.api.FavoriteApi;
import com.iot.control.favorite.service.IFavoriteService;
import com.iot.control.favorite.vo.FavoriteReq;
import com.iot.control.favorite.vo.FavoriteResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 最喜爱设备及场景表 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2019-01-02
 */
@RestController
public class FavoriteController implements FavoriteApi {

    @Autowired
    private IFavoriteService iFavoriteService;
    @Override
    public List<FavoriteResp> getFavoriteList(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId,@RequestParam(value = "homeId",required = false)Long homeId) {
        return iFavoriteService.getFavoriteList(tenantId,userId,homeId);
    }

    @Override
    public Boolean delAllFavoriteByUserId(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId){
        return iFavoriteService.delAllFavoriteByUserId(tenantId,userId);
    }

    @Override
    public Boolean delAllFavoriteBySpaceId(Long tenantId, Long spaceId) {
        return iFavoriteService.delAllFavoriteBySpaceId(tenantId,spaceId);
    }


    @Override
    public void insertFavorite(@RequestBody FavoriteReq favoriteReq) {
        iFavoriteService.insertFavorite(favoriteReq);
    }

    @Override
    public void delSingleFavorite(@RequestBody FavoriteReq favoriteReq){
        iFavoriteService.delSingleFavorite(favoriteReq);
    }

    @Override
    public FavoriteResp getFavoriteStatus(@RequestBody FavoriteReq favoriteReq) {
        return iFavoriteService.getFavoriteStatus(favoriteReq);
    }


}


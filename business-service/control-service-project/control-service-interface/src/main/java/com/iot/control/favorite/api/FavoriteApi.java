package com.iot.control.favorite.api;

import com.iot.control.favorite.vo.FavoriteReq;
import com.iot.control.favorite.vo.FavoriteResp;
import io.swagger.annotations.Api;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api("用户设备接口")
@FeignClient(value = "control-service")
@RequestMapping("/favoriteApi")
public interface FavoriteApi {

    @RequestMapping(value = "/getFavoriteList", method = RequestMethod.GET)
    List<FavoriteResp> getFavoriteList(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId,@RequestParam(value = "homeId",required = false) Long homeId);

    @RequestMapping(value = "/delAllFavoriteByUserId",method = RequestMethod.GET)
    Boolean delAllFavoriteByUserId(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId);

    @RequestMapping(value = "/delAllFavoriteBySpaceId",method = RequestMethod.GET)
    Boolean delAllFavoriteBySpaceId(@RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId);

    @RequestMapping(value = "/insertFavorite",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    void insertFavorite(@RequestBody FavoriteReq favoriteReq);

    @RequestMapping(value = "/delSingleFavorite",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void delSingleFavorite(@RequestBody FavoriteReq favoriteReq);

    @RequestMapping(value = "/getFavoriteStatus",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    FavoriteResp getFavoriteStatus(@RequestBody FavoriteReq favoriteReq);

}

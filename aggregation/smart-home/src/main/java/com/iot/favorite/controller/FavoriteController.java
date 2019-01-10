package com.iot.favorite.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.control.favorite.vo.FavoriteReq;
import com.iot.favorite.service.FavoriteService;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@Api(description = "视频云-设备接口")
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

   @Autowired
   private FavoriteService favoriteService;

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取最喜爱列表", notes = "获取最喜爱列表")
    @RequestMapping(value = "/getFavoriteList", method = RequestMethod.GET)
    public CommonResponse getFavoriteDevList(@RequestParam(value = "homeId",required = false) Long homeId){
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long userId = SaaSContextHolder.getCurrentUserId();
        Map<String, Object> devMap = favoriteService.getFavoriteList(tenantId,userId,homeId);
        return ResultMsg.SUCCESS.info(devMap);
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "添加最喜爱设备", notes = "添加最喜爱设备")
    @RequestMapping(value = "/addFavorite", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addFavoriteStatus(@RequestBody  FavoriteReq req){
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long userId = SaaSContextHolder.getCurrentUserId();
        req.setUserId(userId);
        req.setTenantId(tenantId);
        req.setCreateBy(userId);
        req.setUpdateBy(userId);
        req.setCreateTime(new Date());
        req.setUpdatTime(new Date());
        favoriteService.addFavorite(req);
        return ResultMsg.SUCCESS.info("setDevFavoriteStatus ok");
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取单一设备最喜爱状态", notes = "获取单一设备最喜爱状态")
    @RequestMapping(value = "/getFavoriteStatus", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getFavoriteStatus(@RequestBody  FavoriteReq req){
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long userId = SaaSContextHolder.getCurrentUserId();
        req.setTenantId(tenantId);
        req.setUserId(userId);
        Boolean status=favoriteService.getFavoriteStatus(req);
        return ResultMsg.SUCCESS.info(status);
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "取消最喜爱", notes = "获取单一设备最喜爱状态")
    @RequestMapping(value = "/cancelFavorite", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse cancelFavorite(@RequestBody  FavoriteReq req){
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long userId = SaaSContextHolder.getCurrentUserId();
        req.setTenantId(tenantId);
        req.setUserId(userId);
        favoriteService.cancelFavorite(req);
        return ResultMsg.SUCCESS.info("cancel favorite ok！");
    }




}

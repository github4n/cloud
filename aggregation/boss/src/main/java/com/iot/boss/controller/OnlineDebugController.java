package com.iot.boss.controller;


import com.iot.boss.service.onlineDebug.OnlineDebugService;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "admin接口",value ="admin接口")
@RequestMapping("/debug")
public class OnlineDebugController {

    @Autowired
    private OnlineDebugService onlineDebugService;

    @LoginRequired(Action.Normal)
    @ApiOperation("修改用户线上Debug权限")
    @RequestMapping(value = "/update",method = RequestMethod.GET)
    String updateOnlineDebug(@RequestParam("userName") String userName, @RequestParam("state") int state, @RequestParam("tenantId") int tenantId){
        return onlineDebugService.updateOnlineDebug(userName,state,tenantId);
    }
}

package com.iot.onlineDebug.controller;


import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.onlineDebug.service.OnlineDebugService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api(description = "在线Debug接口")
@RestController
@RequestMapping("/online-debug")
public class OnlineDebugController {
    @Autowired
    private OnlineDebugService onlineDebugService;

    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "检测Debug权限")
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public CommonResponse checkOnlineDebug(@RequestParam("uuid") String uuid) {
        return onlineDebugService.checkOnlineDebug(uuid);
    }

}

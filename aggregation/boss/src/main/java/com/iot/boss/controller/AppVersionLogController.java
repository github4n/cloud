package com.iot.boss.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.AppVersionLogApi;
import com.iot.tenant.vo.req.AppVersionLogReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "应用版本记录接口",value = "应用版本记录接口")
@RestController
@RequestMapping("/api/app/version/log")
public class AppVersionLogController {

    private Logger log = LoggerFactory.getLogger(AppVersionLogController.class);

    @Autowired
    AppVersionLogApi appVersionLogApi;


    @ApiOperation("分页获取版本信息")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public CommonResponse page(@RequestBody AppVersionLogReq appVersionLogReq){
        log.debug("分页获取版本信息", JSONObject.toJSON(appVersionLogReq));
        appVersionLogReq.setTenantId(SaaSContextHolder.currentTenantId());
        appVersionLogReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        PageInfo result = appVersionLogApi.page(appVersionLogReq);
        return CommonResponse.success(result);
    }


    @ApiOperation("删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public CommonResponse delete(@RequestBody List<Long> ids){
        log.debug("删除", JSONObject.toJSON(ids));
        appVersionLogApi.delete(ids);
        return CommonResponse.success();
    }


    @LoginRequired(Action.Normal)
    @ApiOperation(value = "保存或者更新")
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    public CommonResponse insertOrUpdate(@RequestBody AppVersionLogReq appVersionLogReq){
        appVersionLogReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        appVersionLogReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        appVersionLogReq.setTenantId(SaaSContextHolder.currentTenantId());
        Long result = appVersionLogApi.insertOrUpdate(appVersionLogReq);
        return CommonResponse.success(result);
    }

}

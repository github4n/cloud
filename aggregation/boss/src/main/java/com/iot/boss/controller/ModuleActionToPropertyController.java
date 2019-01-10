package com.iot.boss.controller;


import com.alibaba.fastjson.JSONObject;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.device.api.ModuleActionToPropertyApi;
import com.iot.device.api.ServiceModuleApi;
import com.iot.device.vo.req.ModuleActionToPropertyReq;
import com.iot.device.vo.req.ServiceModuleReq;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 模组表 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@RestController
@Api(description = "模组方法关联属性",value = "模组方法关联属性")
@RequestMapping("/api/module/action/property")
public class ModuleActionToPropertyController {

    private Logger log = LoggerFactory.getLogger(ModuleActionToPropertyController.class);

    @Autowired
    private ModuleActionToPropertyApi moduleActionToPropertyApi;

    @ApiOperation("增加")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public CommonResponse save(@RequestBody ModuleActionToPropertyReq moduleActionToPropertyReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(moduleActionToPropertyReq));
        moduleActionToPropertyReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        moduleActionToPropertyReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        moduleActionToPropertyReq.setTenantId(SaaSContextHolder.currentTenantId());
        moduleActionToPropertyApi.save(moduleActionToPropertyReq);
        return CommonResponse.success();
    }

    @ApiOperation("批量增加")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveMore", method = RequestMethod.POST)
    public CommonResponse saveMore(@RequestBody ModuleActionToPropertyReq moduleActionToPropertyReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(moduleActionToPropertyReq));
        moduleActionToPropertyReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        moduleActionToPropertyReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        moduleActionToPropertyReq.setTenantId(SaaSContextHolder.currentTenantId());
        moduleActionToPropertyApi.saveMore(moduleActionToPropertyReq);
        return CommonResponse.success();
    }


    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public CommonResponse delete(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids.toArray());
        moduleActionToPropertyApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "deleteByPost", method = RequestMethod.POST)
    public CommonResponse deleteByPost(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids.toArray());
        moduleActionToPropertyApi.delete(ids);
        return CommonResponse.success();
    }
}


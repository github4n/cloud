package com.iot.boss.controller;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.device.api.ServiceModuleStyleApi;
import com.iot.device.vo.req.ServiceModuleStyleReq;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(description = "模组样式",value = "模组样式")
@RequestMapping("/api/service/module/style")
public class ServiceModuleStyleController {

    private Logger log = LoggerFactory.getLogger(ServiceModuleStyleController.class);

    @Autowired
    private ServiceModuleStyleApi serviceModuleStyleApi;


    @ApiOperation("增加或者更新")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
    public CommonResponse saveOrUpdate(@RequestBody ServiceModuleStyleReq serviceModuleStyleReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(serviceModuleStyleReq));
        serviceModuleStyleReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        serviceModuleStyleReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        serviceModuleStyleReq.setTenantId(SaaSContextHolder.currentTenantId());
        CommonResponse<Long> result = new CommonResponse<>(ResultMsg.SUCCESS,serviceModuleStyleApi.saveOrUpdate(serviceModuleStyleReq));
        return result;
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public CommonResponse delete(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids);
        serviceModuleStyleApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "deleteByPost", method = RequestMethod.POST)
    public CommonResponse deleteByPost(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids);
        serviceModuleStyleApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据serviceModuleId获取列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonResponse list(@RequestBody ArrayList<Long> serviceModuleIds) {
        log.debug("根据serviceModuleId获取列表",serviceModuleIds);
        List list = serviceModuleStyleApi.list(serviceModuleIds);
        return CommonResponse.success(list);
    }

    @ApiOperation("根据styleTemplateId获取列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "listByStyleTemplateId", method = RequestMethod.GET)
    public CommonResponse listByStyleTemplateId(@RequestParam("styleTemplateId") Long styleTemplateId){
        log.debug("根据styleTemplateId获取列表",styleTemplateId);
        List list = serviceModuleStyleApi.listByStyleTemplateId(styleTemplateId);
        return CommonResponse.success(list);
    }



}

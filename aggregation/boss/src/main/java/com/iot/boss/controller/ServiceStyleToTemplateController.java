package com.iot.boss.controller;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.device.api.ServiceStyleToTemplateApi;
import com.iot.device.vo.req.ServiceStyleToTemplateReq;
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
@Api(description = "模组样式关联模板",value = "模组样式关联模板")
@RequestMapping("/api/service/style/template")
public class ServiceStyleToTemplateController {

    private Logger log = LoggerFactory.getLogger(ServiceStyleToTemplateController.class);

    @Autowired
    private ServiceStyleToTemplateApi serviceStyleToTemplateApi;


    @ApiOperation("增加或者更新")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
    public CommonResponse saveOrUpdate(@RequestBody ServiceStyleToTemplateReq serviceStyleToTemplateReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(serviceStyleToTemplateReq));
        serviceStyleToTemplateReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        serviceStyleToTemplateReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        serviceStyleToTemplateReq.setTenantId(SaaSContextHolder.currentTenantId());
        CommonResponse<Long> result = new CommonResponse<>(ResultMsg.SUCCESS,serviceStyleToTemplateApi.saveOrUpdate(serviceStyleToTemplateReq));
        return result;
    }

    @ApiOperation("批量增加")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveMore", method = RequestMethod.POST)
    public CommonResponse saveMore(@RequestBody ServiceStyleToTemplateReq serviceStyleToTemplateReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(serviceStyleToTemplateReq));
        serviceStyleToTemplateReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        serviceStyleToTemplateReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        serviceStyleToTemplateReq.setTenantId(SaaSContextHolder.currentTenantId());
        serviceStyleToTemplateApi.saveMore(serviceStyleToTemplateReq);
        return CommonResponse.success();
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public CommonResponse delete(@RequestBody ArrayList<Long> ids) {
        log.debug("删除",ids);
        serviceStyleToTemplateApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "deleteByPost", method = RequestMethod.POST)
    public CommonResponse deleteByPost(@RequestBody ArrayList<Long> ids) {
        log.debug("删除",ids);
        serviceStyleToTemplateApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据moduleStyleId获取列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonResponse list(@RequestBody ArrayList<Long> moduleStyleId) {
        log.debug("根据moduleStyleId获取列表",moduleStyleId);
        List list = serviceStyleToTemplateApi.list(moduleStyleId);
        return CommonResponse.success(list);
    }
}

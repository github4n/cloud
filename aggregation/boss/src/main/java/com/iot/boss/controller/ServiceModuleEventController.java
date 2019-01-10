package com.iot.boss.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.device.api.ServiceEventApi;
import com.iot.device.vo.req.ServiceModuleActionReq;
import com.iot.device.vo.req.ServiceModuleEventReq;
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
 * 模组-事件表 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@RestController
@Api(description = "模组事件",value = "模组事件")
@RequestMapping("/api/service/module/event")
public class ServiceModuleEventController {

    private Logger log = LoggerFactory.getLogger(ServiceModuleEventController.class);

    @Autowired
    private ServiceEventApi serviceEventApi;

    @ApiOperation("增加或者更新")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
    public CommonResponse saveOrUpdate(@RequestBody ServiceModuleEventReq serviceModuleEventReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(serviceModuleEventReq));
        serviceModuleEventReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        serviceModuleEventReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        serviceModuleEventReq.setTenantId(SaaSContextHolder.currentTenantId());
        CommonResponse<Long> result = new CommonResponse<>(ResultMsg.SUCCESS,serviceEventApi.saveOrUpdate(serviceModuleEventReq));
        return result;
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public CommonResponse delete(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids.toArray());
        serviceEventApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "deleteByPost", method = RequestMethod.POST)
    public CommonResponse deleteByPost(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids.toArray());
        serviceEventApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("分页获取列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonResponse list(@RequestBody ServiceModuleEventReq serviceModuleEventReq) {
        PageInfo result = serviceEventApi.list(serviceModuleEventReq);
        return CommonResponse.success(result);
    }

    @ApiOperation("根据serviceModuleId获取列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "listByServiceModuleId/{serviceModuleId}", method = RequestMethod.GET)
    public CommonResponse listByServiceModuleId(@PathVariable(value = "serviceModuleId",required = true) Long serviceModuleId) {
        log.debug("根据serviceModuleId获取列表",serviceModuleId);
        List list = serviceEventApi.listByServiceModuleId(serviceModuleId);
        return CommonResponse.success(list);
    }
}


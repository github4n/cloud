package com.iot.boss.controller;


import com.alibaba.fastjson.JSONObject;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.device.api.ServiceToActionApi;
import com.iot.device.api.ServiceToEventApi;
import com.iot.device.vo.req.ServiceToActionReq;
import com.iot.device.vo.req.ServiceToEventReq;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * <p>
 * 模组表 前端控制器
 * </p>
 *
 * @author zhangyue
 * @since 2018-06-27
 */
@RestController
@Api(description = "模组关联事件",value = "模组关联事件")
@RequestMapping("/api/service/to/event")
public class ServiceToEventController {

    private Logger log = LoggerFactory.getLogger(ServiceToEventController.class);

    @Autowired
    private ServiceToEventApi serviceToEventApi;

    @ApiOperation("增加")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public CommonResponse save(@RequestBody ServiceToEventReq serviceToEventReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(serviceToEventReq));
        serviceToEventReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        serviceToEventReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        serviceToEventReq.setTenantId(SaaSContextHolder.currentTenantId());
        serviceToEventApi.save(serviceToEventReq);
        return CommonResponse.success();
    }

    @ApiOperation("批量增加")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveMore", method = RequestMethod.POST)
    public CommonResponse saveMore(@RequestBody ServiceToEventReq serviceToEventReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(serviceToEventReq));
        serviceToEventReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        serviceToEventReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        serviceToEventReq.setTenantId(SaaSContextHolder.currentTenantId());
        serviceToEventApi.saveMore(serviceToEventReq);
        return CommonResponse.success();
    }


    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public CommonResponse delete(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids.toArray());
        serviceToEventApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "deleteByPost", method = RequestMethod.POST)
    public CommonResponse deleteByPost(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids.toArray());
        serviceToEventApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据id更新")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "update", method = RequestMethod.GET)
    public CommonResponse update(@RequestParam("id") Long id,@RequestParam("status") Integer status){
        log.debug("根据id更新",id,status);
        serviceToEventApi.update(id,status);
        return CommonResponse.success();
    }
}


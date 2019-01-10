package com.iot.boss.controller;


import com.alibaba.fastjson.JSONObject;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.device.api.ModuleActionToPropertyApi;
import com.iot.device.api.ServiceToActionApi;
import com.iot.device.vo.req.ModuleActionToPropertyReq;
import com.iot.device.vo.req.ServiceToActionReq;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
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
 * @author zhangyue
 * @since 2018-06-27
 */
@RestController
@Api(description = "模组关联方法",value = "模组关联方法")
@RequestMapping("/api/service/to/action")
public class ServiceToActionController {

    private Logger log = LoggerFactory.getLogger(ServiceToActionController.class);

    @Autowired
    private ServiceToActionApi serviceToActionApi;

    @ApiOperation("增加")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public CommonResponse save(@RequestBody ServiceToActionReq serviceToActionReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(serviceToActionReq));
        serviceToActionReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        serviceToActionReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        serviceToActionReq.setTenantId(SaaSContextHolder.currentTenantId());
        serviceToActionApi.save(serviceToActionReq);
        return CommonResponse.success();
    }

    @ApiOperation("批量增加")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveMore", method = RequestMethod.POST)
    public CommonResponse saveMore(@RequestBody ServiceToActionReq serviceToActionReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(serviceToActionReq));
        serviceToActionReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        serviceToActionReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        serviceToActionReq.setTenantId(SaaSContextHolder.currentTenantId());
        serviceToActionApi.saveMore(serviceToActionReq);
        return CommonResponse.success();
    }


    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public CommonResponse delete(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids.toArray());
        serviceToActionApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "deleteByPost", method = RequestMethod.POST)
    public CommonResponse deleteByPost(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids.toArray());
        serviceToActionApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据id更新")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "update", method = RequestMethod.GET)
    public CommonResponse update(@RequestParam("id") Long id, @RequestParam("status") Integer status){
        log.debug("根据id更新",id,status);
        serviceToActionApi.update(id,status);
        return CommonResponse.success();
    }


}


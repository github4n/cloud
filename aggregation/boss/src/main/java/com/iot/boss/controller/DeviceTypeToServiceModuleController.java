package com.iot.boss.controller;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.device.api.DeviceTypeToServiceModuleApi;
import com.iot.device.vo.req.DeviceTypeToServiceModuleReq;
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
@Api(description = "类型模组",value = "类型模组")
@RequestMapping("/api/device/type/service/module")
public class DeviceTypeToServiceModuleController {

    private Logger log = LoggerFactory.getLogger(DeviceTypeToServiceModuleController.class);

    @Autowired
    private DeviceTypeToServiceModuleApi deviceTypeToServiceModuleApi;

    @ApiOperation("增加或者更新")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
    public CommonResponse saveOrUpdate(@RequestBody DeviceTypeToServiceModuleReq deviceTypeToServiceModuleReq) {
        deviceTypeToServiceModuleReq.setTenantId(SaaSContextHolder.currentTenantId());
        deviceTypeToServiceModuleReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        deviceTypeToServiceModuleReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        log.debug("增加或者更新:{}", JSONObject.toJSON(deviceTypeToServiceModuleReq));
        CommonResponse<Long> result = new CommonResponse<>(ResultMsg.SUCCESS,deviceTypeToServiceModuleApi.saveOrUpdate(deviceTypeToServiceModuleReq));
        return result;
    }

    @ApiOperation("批量增加")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveMore", method = RequestMethod.POST)
    public CommonResponse saveMore(@RequestBody DeviceTypeToServiceModuleReq deviceTypeToServiceModuleReq) {
        deviceTypeToServiceModuleReq.setTenantId(SaaSContextHolder.currentTenantId());
        deviceTypeToServiceModuleReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        deviceTypeToServiceModuleReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        log.debug("批量增加:{}", JSONObject.toJSON(deviceTypeToServiceModuleReq));
        deviceTypeToServiceModuleApi.saveMore(deviceTypeToServiceModuleReq);
        return CommonResponse.success();
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public CommonResponse delete(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids);
        deviceTypeToServiceModuleApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "deleteByPost", method = RequestMethod.POST)
    public CommonResponse deleteByPost(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids);
        deviceTypeToServiceModuleApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据deviceTypeId获取列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "listByDeviceTypeId/{deviceTypeId}", method = RequestMethod.GET)
    public CommonResponse listByDeviceTypeId(@PathVariable(value = "deviceTypeId",required = true) Long deviceTypeId) {
        log.debug("根据deviceTypeId获取列表",deviceTypeId);
        Long tenantId = SaaSContextHolder.currentTenantId();
        List list = deviceTypeToServiceModuleApi.listByDeviceTypeId(deviceTypeId,tenantId);
        return CommonResponse.success(list);
    }

    @ApiOperation("根据serviceModuleId获取列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "listByServiceModuleId/{serviceModuleId}", method = RequestMethod.GET)
    public CommonResponse listByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId) {
        log.debug("根据serviceModuleId获取列表",serviceModuleId);
        Long tenantId = SaaSContextHolder.currentTenantId();
        List list = deviceTypeToServiceModuleApi.listByServiceModuleId(serviceModuleId,tenantId);
        return CommonResponse.success(list);
    }

    @ApiOperation("根据id更新")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "update", method = RequestMethod.GET)
    public CommonResponse update(@RequestParam("id") Long id, @RequestParam("status") Integer status){
        log.debug("根据id更新",id,status);
        deviceTypeToServiceModuleApi.update(id,status);
        return CommonResponse.success();
    }


}

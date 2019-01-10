package com.iot.boss.controller;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.device.api.DeviceTypeToStyleApi;
import com.iot.device.vo.req.DeviceTypeToStyleReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(description = "类型样式",value = "类型样式")
@RequestMapping("/api/device/type/style")
public class DeviceTypeToStyleController{

    private Logger log = LoggerFactory.getLogger(DeviceTypeToStyleController.class);

    @Autowired
    private DeviceTypeToStyleApi deviceTypeToStyleApi;

    @ApiOperation("增加或者更新")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
    public CommonResponse<Long> saveOrUpdate(@RequestBody DeviceTypeToStyleReq deviceTypeToStyleReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(deviceTypeToStyleReq));
        CommonResponse<Long> result = new CommonResponse<>(ResultMsg.SUCCESS,deviceTypeToStyleApi.saveOrUpdate(deviceTypeToStyleReq));
        return result;
    }

    @ApiOperation("批量增加")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveMore", method = RequestMethod.POST)
    public CommonResponse saveMore(@RequestBody DeviceTypeToStyleReq deviceTypeToStyleReq) {
        log.debug("批量增加:{}", JSONObject.toJSON(deviceTypeToStyleReq));
        deviceTypeToStyleApi.saveMore(deviceTypeToStyleReq);
        return CommonResponse.success();
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public CommonResponse delete(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids);
        deviceTypeToStyleApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "deleteByPost", method = RequestMethod.POST)
    public CommonResponse deleteByPost(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除",ids);
        deviceTypeToStyleApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据deviceTypeId获取列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "listDeviceTypeStyleByDeviceTypeId/{deviceTypeId}", method = RequestMethod.GET)
    public CommonResponse listDeviceTypeStyleByDeviceTypeId(@PathVariable(value = "deviceTypeId",required = true) Long deviceTypeId) {
        log.debug("根据deviceTypeId获取列表",deviceTypeId);
        List list = deviceTypeToStyleApi.listDeviceTypeStyleByDeviceTypeId(deviceTypeId);
        return CommonResponse.success(list);
    }
}

package com.iot.device.api;


import com.iot.device.api.fallback.DeviceTypeToServiceModuleApiFallbackFactory;
import com.iot.device.vo.req.DeviceTypeToServiceModuleReq;
import com.iot.device.vo.rsp.DeviceTypeToServiceModuleResp;
import com.iot.device.vo.rsp.servicemodule.PackageServiceModuleDetailResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Api("样式模板接口")
@FeignClient(value = "device-service" , fallbackFactory = DeviceTypeToServiceModuleApiFallbackFactory.class)
@RequestMapping("/device/type/service/module")
public interface DeviceTypeToServiceModuleApi {


    @ApiOperation("保存或更新")
    @RequestMapping(value = "/saveOrUpdate" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveOrUpdate(@RequestBody DeviceTypeToServiceModuleReq deviceTypeToServiceModuleReq);

    @ApiOperation("批量保存")
    @RequestMapping(value = "/saveMore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMore(@RequestBody DeviceTypeToServiceModuleReq deviceTypeToServiceModuleReq);

    @ApiOperation("删除")
    @RequestMapping(value = "/delete" , method = RequestMethod.DELETE,consumes = MediaType.APPLICATION_JSON_VALUE)
    void delete(@RequestBody ArrayList<Long> ids);

    @ApiOperation("根据deviceTypeId获取")
    @RequestMapping(value = "/listByDeviceTypeId" , method = RequestMethod.GET)
    List<DeviceTypeToServiceModuleResp> listByDeviceTypeId(@RequestParam("deviceTypeId") Long deviceTypeId,@RequestParam("tenantId") Long tenantId);

    @ApiOperation("根据serviceModuleId获取")
    @RequestMapping(value = "/listByServiceModuleId" , method = RequestMethod.GET)
    List<DeviceTypeToServiceModuleResp> listByServiceModuleId(@RequestParam("serviceModuleId") Long serviceModuleId,@RequestParam("tenantId") Long tenantId);

    @ApiOperation("根据id删除")
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public void update(@RequestParam("id") Long id, @RequestParam("status") Integer status);

    /**
      * @despriction：校验设备类型是否有iftttType属性、方法、事件
      * @author  yeshiyuan
      * @created 2018/11/22 14:00
      */
    @ApiOperation(value = "校验设备类型是否有iftttType属性、方法、事件", notes = "校验设备类型是否有iftttType属性、方法、事件")
    @RequestMapping(value = "/checkDeviceTypeHadIftttType", method = RequestMethod.GET)
    boolean checkDeviceTypeHadIftttType(@RequestParam("deviceTypeId") Long deviceTypeId);

    /**
      * @despriction：根据ifttt类型找到对应的模组信息
      * @author  yeshiyuan
      * @created 2018/11/22 15:53
      * @return
      */
    @ApiOperation(value = "根据ifttt类型找到对应的模组信息", notes = "根据ifttt类型找到对应的模组信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceTypeId", value = "设备类型id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "iftttType", value = "ifttt类型（if：支持if；then：支持then）", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/queryServiceModuleDetailByIfttt", method = RequestMethod.GET)
    PackageServiceModuleDetailResp queryServiceModuleDetailByIfttt(@RequestParam("deviceTypeId") Long deviceTypeId, @RequestParam("iftttType") String iftttType);
}

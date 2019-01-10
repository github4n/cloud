package com.iot.device.api;


import com.iot.device.api.fallback.DeviceTypeDataApiFallbackFactory;
import com.iot.device.api.fallback.DeviceTypeToStyleApiFallbackFactory;
import com.iot.device.vo.req.DeviceTypeToStyleReq;
import com.iot.device.vo.rsp.DeviceTypeToStyleResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "类型样式接口")
@FeignClient(value = "device-service", fallbackFactory = DeviceTypeToStyleApiFallbackFactory.class)
@RequestMapping("/device/type/style")
public interface DeviceTypeToStyleApi {

    @ApiOperation("保存或更新")
    @RequestMapping(value = "/saveOrUpdate" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveOrUpdate(@RequestBody DeviceTypeToStyleReq deviceTypeToStyleReq);

    @ApiOperation("批量保存")
    @RequestMapping(value = "/saveMore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMore(@RequestBody DeviceTypeToStyleReq deviceTypeToStyleReq);

    @ApiOperation("删除")
    @RequestMapping(value = "/delete" ,method = RequestMethod.DELETE,consumes = MediaType.APPLICATION_JSON_VALUE)
    void delete(@RequestBody ArrayList<Long> ids);


    @ApiOperation("根据deviceTypeId获取类型样式")
    @RequestMapping(value = "/listDeviceTypeStyleByDeviceTypeId" , method = RequestMethod.GET)
    List<DeviceTypeToStyleResp> listDeviceTypeStyleByDeviceTypeId(@RequestParam("deviceTypeId") Long deviceTypeId);

}

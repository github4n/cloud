package com.iot.device.api;

import com.github.pagehelper.PageInfo;
import com.iot.device.api.fallback.DataPointApiFallbackFactory;
import com.iot.device.vo.req.DataPointReq;
import com.iot.device.vo.rsp.DataPointResp;
import com.iot.device.vo.rsp.DeviceFunResp;
import com.iot.device.vo.rsp.voicebox.SmartDataPointResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = "设备功能点接口")
@FeignClient(value = "device-service", fallbackFactory = DataPointApiFallbackFactory.class)
@RequestMapping(value = "/dataPoint")
public interface DataPointApi {


    @ApiOperation("根据设备id获取功能列表")
    @RequestMapping(value = "/findDataPointListByDeviceId", method = RequestMethod.GET)
    List<DeviceFunResp> findDataPointListByDeviceId(@RequestParam(value = "deviceId") String deviceId);

    @ApiOperation("根据产品id获取功能列表")
    @RequestMapping(value = "/findDataPointListByProductId", method = RequestMethod.GET)
    List<DeviceFunResp> findDataPointListByProductId(@RequestParam(value = "productId") Long productId);

    @ApiOperation("根据产品ids获取功能列表")
    @RequestMapping(value = "/findDataPointListByProductIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<Long, List<DeviceFunResp>> findDataPointListByProductIds(@RequestBody List<Long> productIds);

    @ApiOperation("新增功能点")
    @RequestMapping(value = "/addDataPoint", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean addDataPoint(@RequestBody DataPointReq req);
    
    @ApiOperation("修改功能点")
    @RequestMapping(value = "/updateDataPoint", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean updateDataPoint(@RequestBody DataPointReq req);
    
    @ApiOperation("删除功能点")
    @RequestMapping(value = "/deleteByIds", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean deleteByIds(@RequestBody ArrayList<Long> ids);

    @ApiOperation("根据设备类型id获取功能列表")
    @RequestMapping(value = "/findDataPointListByDeviceTypeId", method = RequestMethod.GET)
    List<DeviceFunResp> findDataPointListByDeviceTypeId(@RequestParam("deviceTypeId")Long deviceTypeId);

    @ApiOperation("查询所有非自定义功能点")
    @RequestMapping(value = "/findExceptCustom", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    PageInfo findExceptCustom(@RequestBody DataPointReq req);
    
    @ApiOperation("根据datapointId查询自定义功能点")
    @RequestMapping(value = "/findExceptCustomById/{dataPointId}", method = RequestMethod.GET)
    DataPointResp findExceptCustomById(@PathVariable("dataPointId") Long dataPointId);
    
    @ApiOperation("查询smart code")
    @RequestMapping(value = "/getSmartCode/{smart}", method = RequestMethod.GET)
    String getSmartCode(@PathVariable("smart") Integer smart, @RequestParam("propertyCode") String propertyCode);
    
    @ApiOperation("查询smart")
    @RequestMapping(value = "/getSmart/{dataPointId}", method = RequestMethod.GET)
    List<SmartDataPointResp> getSmartByDataPointId(@PathVariable("dataPointId") Long dataPointId);
    
    @ApiOperation("删除自定义功能点")
    @RequestMapping(value = "/deleteByIdsAndProduct/{productId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean deleteByIdsAndProduct(@PathVariable("productId") Long productId, @RequestBody ArrayList<Long> ids);
}

package com.iot.device.api;

import com.iot.device.api.fallback.DeviceTypeToGoodsApiFallbackFactory;
import com.iot.device.vo.rsp.DeviceTypeGoodsResp;
import com.iot.device.vo.rsp.ListGoodsSubDictResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@Api("设备类型增值服务接口")
@FeignClient(value = "device-service", fallbackFactory = DeviceTypeToGoodsApiFallbackFactory.class)
@RequestMapping("/device/type/goods")
public interface DeviceTypeToGoodsApi {
    @ApiOperation("根据deviceTypeId获取服务code")
    @RequestMapping(value = "/getDeviceTypeGoodsCodeByDeviceTypeId", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<String> getDeviceTypeGoodsCodeByDeviceTypeId(@RequestParam(value = "deviceTypeId") Long deviceTypeId);

    @ApiOperation("获取服务子项，根据服务分组")
    @RequestMapping(value = "/getAllGoodsSubDictMap", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<String, List<ListGoodsSubDictResp>> getAllGoodsSubDictMap();

    @ApiOperation("根据deviceTypeId获取服务子项")
    @RequestMapping(value = "/getGoodsSubDictMapByDeviceTypeId", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<String, List<ListGoodsSubDictResp>> getGoodsSubDictMapByDeviceTypeId(@RequestParam(value = "deviceTypeId") Long deviceTypeId);

    @ApiOperation("根据deviceTypeId获取已配置的服务和子项")
    @RequestMapping(value = "/getConfigGoodsByDeviceTypeId", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<DeviceTypeGoodsResp> getConfigGoodsByDeviceTypeId(@RequestParam(value = "deviceTypeId") Long deviceTypeId);
}

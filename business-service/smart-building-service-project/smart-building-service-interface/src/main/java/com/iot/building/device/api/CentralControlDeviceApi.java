package com.iot.building.device.api;

import com.iot.building.device.vo.DeviceParamReq;
import com.iot.building.device.vo.DeviceParamResp;
import com.iot.building.device.vo.DevicePropertyVo;
import com.iot.device.vo.req.CommDeviceInfoReq;
import com.iot.device.vo.rsp.ProductResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api(tags = "中控设备接口")
@FeignClient(value = "building-control-service")
@RequestMapping("/device-central")
public interface CentralControlDeviceApi {

    @ApiOperation(value = "根据设备ids查询列表信息")
    @RequestMapping(value = "/findDeviceListByDeviceIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    DeviceParamResp findDeviceListByDeviceIds(@RequestBody DeviceParamReq paramReq);

    @ApiOperation(value = "根据productIds查询ProductResp")
    @RequestMapping(value = "/listProducts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ProductResp> listProducts(@RequestBody List<Long> productIds);

    @ApiOperation(value = "获取网关下的所有子设备")
    @RequestMapping(value = "/getDeviceList", method = RequestMethod.GET)
    void getDeviceList(@RequestParam("deviceId") String deviceId);
    
    @ApiOperation(value = "搜索子设备")
    @RequestMapping(value = "/searchStar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void searchStar(@RequestBody CommDeviceInfoReq commDeviceInfoReq);
    
    @ApiOperation(value = "停止搜索子设备")
    @RequestMapping(value = "/searchStop", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void searchStop(@RequestBody CommDeviceInfoReq commDeviceInfoReq);
    
    @ApiOperation(value = "编辑网关信息")
    @RequestMapping(value = "/editGatewayInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void editGatewayInfo(@RequestBody CommDeviceInfoReq commDeviceInfoReq);
    
    @ApiOperation(value = "编辑子设备信息")
    @RequestMapping(value = "/editDeviceInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void editDeviceInfo(@RequestBody CommDeviceInfoReq commDeviceInfoReq);
    
    @ApiOperation(value = "空开设备信息回调处理")
    @RequestMapping(value = "/airSwitchBack", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void airSwitchBack(@RequestBody DevicePropertyVo vo);
}

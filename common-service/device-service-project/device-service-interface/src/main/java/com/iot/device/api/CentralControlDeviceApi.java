package com.iot.device.api;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.common.helper.Page;
import com.iot.device.api.fallback.CentralControlDeviceApiFallbackFactory;
import com.iot.device.vo.req.CommDeviceInfoReq;
import com.iot.device.vo.req.DeviceBusinessTypeIDSwitchReq;
import com.iot.device.vo.req.DevicePageReq;
import com.iot.device.vo.req.device.PageDeviceInfoReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.IftttDeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:17 2018/4/17
 * @Modify by:
 */
@Api(tags = "中控设备接口")
@FeignClient(value = "device-service", fallbackFactory = CentralControlDeviceApiFallbackFactory.class)
@RequestMapping("/device-central")
public interface CentralControlDeviceApi {

    @ApiOperation(value = "分页查找直连设备-中控")
    @RequestMapping(value = "/findDirectDevicePageToCenter", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<DeviceResp> findDirectDevicePageToCenter(@RequestBody DevicePageReq pageReq);

    @ApiOperation(value = "分页查找非直连设备")
    @RequestMapping(value = "/findUnDirectDevicePage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<DeviceResp> findUnDirectDevicePage(@RequestBody DevicePageReq pageReq);

    @ApiOperation(value = "根据分类查找直连设备列表")
    @RequestMapping(value = "/findDirectDeviceListByVenderCode", method = RequestMethod.GET)
    List<GetDeviceInfoRespVo> findDirectDeviceListByVenderCode(@RequestParam("tenantId") Long tenantId, @RequestParam("locationId") Long locationId, 
    		@RequestParam("venderFlag") String venderFlag,@RequestParam("isDirectDevice") Integer isDirectDevice);

    @ApiOperation(value = "根据id删除设备")
    @RequestMapping(value = "/deleteDeviceByDeviceId", method = RequestMethod.DELETE)
    List<String> deleteDeviceByDeviceId(@RequestParam("deviceId") String deviceId);

    @ApiOperation(value = "查询所有直连设备记录")
    @RequestMapping(value = "/findAllUnDirectDeviceList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<DeviceResp> findAllUnDirectDeviceList(@RequestBody DevicePageReq pageReq);

    @ApiOperation(value = "根据parent设备id查询非直连设备列表")
    @RequestMapping(value = "/findUnDirectDeviceListByParentDeviceId", method = RequestMethod.GET)
    List<DeviceResp> findUnDirectDeviceListByParentDeviceId(@RequestParam("parentDeviceId") String parentDeviceId);

    @ApiOperation(value = "查询所有IFTTT设备记录")
    @RequestMapping(value = "/findIftttDeviceList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<IftttDeviceResp> findIftttDeviceList(@RequestBody CommDeviceInfoReq req);

    @ApiOperation(value = "根据设备ids业务类型开关状态查询统计信息")
    @RequestMapping(value = "/getCountByDeviceIdsAndBusinessTypesAndSwitch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Integer getCountByDeviceIdsAndBusinessTypesAndSwitch(@RequestBody DeviceBusinessTypeIDSwitchReq req);


    @ApiOperation(value = "根据设备ids、功能类型、业务类型、开关状态查询设备信息")
    @RequestMapping(value = "/findDeviceByCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<DeviceResp> findDeviceByCondition(@RequestBody DeviceBusinessTypeIDSwitchReq req);

    @ApiOperation(value = "分页查找空调设备")
    @RequestMapping(value = "/queryAirCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<DeviceResp> queryAirCondition(DevicePageReq pageReq);
    
    @ApiOperation("获取所有设备列表")
    @RequestMapping(value = "/selectAllDeviceToCenter", method = RequestMethod.POST)
    List<GetDeviceInfoRespVo> selectAllDeviceToCenter(@RequestBody DevicePageReq pageReq);
    
    @ApiOperation(value = "根据分类查找直连设备")
    @RequestMapping(value = "/findDirectDeviceByDeviceCatgory", method = RequestMethod.GET)
    List<GetDeviceInfoRespVo> findDirectDeviceByDeviceCatgory(@RequestParam("venderCode") String venderCode,
    		@RequestParam("tenantId") Long tenantId,@RequestParam("locationId") Long locationId);

    @ApiOperation(value = "根据ip查询设备记录")
    @RequestMapping(value = "/getDeviceByDeviceIp", method = RequestMethod.GET)
    GetDeviceInfoRespVo getDeviceByDeviceIp(@RequestParam("orgId") Long orgId, @RequestParam("tenantId") Long tenantId, @RequestParam("deviceIp") String deviceIp);

    @ApiOperation("查询传感器报表")
    @RequestMapping(value = "/queryDataReport", method = RequestMethod.GET)
    Map<String, Object> findDataReport(@RequestParam("spaceId") Long spaceId, @RequestParam("deviceId") String deviceId,
                                       @RequestParam("dateType") String dateType, @RequestParam(name="deviceType",required = false) String deviceType);

    @ApiOperation(value = "分页查找直连设备和所属子设备-中控")
    @RequestMapping(value = "/getGatewayAndSubDeviceList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<DeviceResp> getGatewayAndSubDeviceList(@RequestBody DevicePageReq pageReq);
    
    @ApiOperation(value = "查找存在的产品列表-中控")
    @RequestMapping(value = "/getExistProductList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Long> getExistProductList(@RequestBody PageDeviceInfoReq params);
    
    @ApiOperation(value = "根据父ID获取子设备列表")
    @RequestMapping(value = "/getDeviceListByParentId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<GetDeviceInfoRespVo> getDeviceListByParentId(@RequestBody CommDeviceInfoReq commDeviceInfoReq);
}

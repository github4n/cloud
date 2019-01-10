package com.iot.device.api;

import com.iot.common.helper.Page;
import com.iot.device.api.fallback.DeviceTypeApiFallbackFactory;
import com.iot.device.vo.req.DeviceType2PointsReq;
import com.iot.device.vo.req.DeviceTypeReq;
import com.iot.device.vo.rsp.DeviceTypeListResp;
import com.iot.device.vo.rsp.DeviceTypeResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Api(tags = "设备类型接口")
@FeignClient(value = "device-service", fallbackFactory = DeviceTypeApiFallbackFactory.class)
@RequestMapping(value = "/devType")
public interface DeviceTypeApi {

    @ApiOperation(value = "查找设备类型")
    @RequestMapping(value = "/getDeviceTypeById", method = RequestMethod.GET)
    DeviceTypeResp getDeviceTypeById(@RequestParam(value = "deviceTypeId") Long deviceTypeId);

    @ApiOperation(value = "根据分类Id查找设备类型")
    @RequestMapping(value = "/getDeviceTypeByCataLogId", method = RequestMethod.GET)
    List<DeviceTypeResp> getDeviceTypeByCataLogId(@RequestParam(value = "catalogId") Long catalogId);

    @ApiOperation(value = "查找设备类型")
    @RequestMapping(value = "/getDeviceTypeByCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<DeviceTypeResp> getDeviceTypeByCondition(@RequestBody DeviceTypeReq req);

    @ApiOperation(value = "查询设备业务类型列表")
    @RequestMapping(value = "/findDeviceTypeList", method = RequestMethod.GET)
    List<DeviceTypeResp> findDeviceTypeList();

    @ApiOperation(value = "删除设备类型")
    @RequestMapping(value = "/deleteByDeviceTypeId", method = RequestMethod.DELETE)
    boolean deleteByDeviceTypeId(@RequestParam(value = "deviceTypeId") Long deviceTypeId);

    @ApiOperation(value = "新增设备分类")
    @RequestMapping(value = "/addDeviceType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean addDeviceType(@RequestBody DeviceTypeReq req);

    @ApiOperation(value = "修改设备分类")
    @RequestMapping(value = "/updateDeviceType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean updateDeviceType(@RequestBody DeviceTypeReq req);

    @ApiOperation(value = "批量删除设备分类")
    @RequestMapping(value = "/deleteByDeviceTypeIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean deleteByDeviceTypeIds(@RequestBody ArrayList<Long> ids);

    @ApiOperation(value = "设备类型增加功能点")
    @RequestMapping(value = "/addDataPoint", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean addDataPoint(@RequestBody DeviceType2PointsReq req);

    @ApiOperation(value = "smart设备类型查询")
    @RequestMapping(value = "/getSmartCode", method = RequestMethod.GET)
    String getSmartCode(@RequestParam("keyEnum")Integer keyEnum, @RequestParam("deviceTypeId")Long deviceTypeId);

    @ApiOperation(value = "根据catalogId获取类型列表")
    @RequestMapping(value = "/findDeviceTypeListByCatalogId", method = RequestMethod.GET)
    List<DeviceTypeListResp> findDeviceTypeListByCatalogId(@RequestParam(value = "catalogId") Long catalogId);

    @ApiOperation(value = "根据获取类型列表[new]")
    @RequestMapping(value = "/findAllDeviceTypeList", method = RequestMethod.GET)
    List<DeviceTypeListResp> findAllDeviceTypeList();

    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    void delete(@RequestParam("id") Long id,@RequestParam("tenantId") Long tenantId);

    @ApiOperation(value = "批量查询设备类型信息", notes = "批量查询设备类型信息")
    @RequestMapping(value = "/getByIds", method = RequestMethod.GET)
    List<DeviceTypeResp> getByIds(@RequestParam("ids") List<Long> ids);

    @ApiOperation(value = "根据ifttt类型过滤", notes = "根据ifttt类型过滤")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "设备类型id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "iftttType", value = "ifttt类型（if：支持if/ifThen；then：支持then/ifthen）", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getByIdsAndIfffType", method = RequestMethod.GET)
    List<DeviceTypeResp> getByIdsAndIfffType(@RequestParam("ids") List<Long> ids, @RequestParam("iftttType") String iftttType);

    @ApiOperation(value = "批量查询设备类型名称", notes = "批量查询设备类型名称")
    @RequestMapping(value = "/getDeviceTypeNameByIds", method = RequestMethod.GET)
    List<String> getDeviceTypeNameByIds(@RequestParam("ids") List<Long> ids);

    /**
     *@description 批量查询设备类型名称
     *@author wucheng
     *@params [deviceTypeIds]
     *@create 2018/12/12 10:58
     *@return java.util.List<com.iot.device.vo.rsp.DeviceTypeResp>
     */
    @ApiOperation(value = "批量查询设备类型名称", notes = "批量查询设备类型名称")
    @RequestMapping(value = "/getDeviceTypeIdAndNameByIds", method = RequestMethod.GET)
    List<DeviceTypeResp> getDeviceTypeIdAndNameByIds(@RequestParam("ids") List<Long> deviceTypeIds);

}

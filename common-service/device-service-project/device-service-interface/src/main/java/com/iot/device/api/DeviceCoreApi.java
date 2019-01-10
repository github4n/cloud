package com.iot.device.api;

import com.iot.common.helper.Page;
import com.iot.device.api.fallback.DeviceCoreApiFallbackFactory;
import com.iot.device.vo.req.DevTypePageReq;
import com.iot.device.vo.req.device.ListCommDeviceReq;
import com.iot.device.vo.req.device.ListDeviceByParamsReq;
import com.iot.device.vo.req.device.ListDeviceInfoReq;
import com.iot.device.vo.req.device.PageDeviceInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceConditionReq;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeByDeviceRespVo;
import com.iot.device.vo.rsp.device.GetProductByDeviceRespVo;
import com.iot.device.vo.rsp.device.ListDeviceByParamsRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.PageDeviceInfoRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Api(tags = "设备core接口")
@FeignClient(value = "device-service", fallbackFactory = DeviceCoreApiFallbackFactory.class)
@RequestMapping("/deviceCore")
public interface DeviceCoreApi {


  @ApiOperation("设备列表")
  @RequestMapping(
    value = "/listDevices",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  List<ListDeviceInfoRespVo> listDevices(@RequestBody @Validated ListDeviceInfoReq params);

  @ApiOperation("设备详情")
  @RequestMapping(value = "/get", method = RequestMethod.GET)
  GetDeviceInfoRespVo get(@RequestParam(value = "deviceId", required = true) String deviceId);

  @ApiOperation("保存或修改设备信息")
  @RequestMapping(
    value = "/saveOrUpdate",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  GetDeviceInfoRespVo saveOrUpdate(@RequestBody UpdateDeviceInfoReq params);

  @ApiOperation("批量条件更新设备信息")
  @RequestMapping(
          value = "/updateByCondition",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  void updateByCondition(@RequestBody UpdateDeviceConditionReq params);

  @ApiOperation("批量保存或修改设备信息")
  @RequestMapping(
    value = "/saveOrUpdateBatch",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  void saveOrUpdateBatch(@RequestBody List<UpdateDeviceInfoReq> paramsList);

  @ApiOperation("获取子设备列表")
  @RequestMapping(value = "/listDevicesByParentId", method = RequestMethod.GET)
  List<ListDeviceInfoRespVo> listDevicesByParentId(
      @RequestParam(value = "parentDeviceId", required = true) String parentDeviceId);

  @ApiOperation("删除设备信息")
  @RequestMapping(
    value = "/deleteByDeviceId",
    method = RequestMethod.GET
  )
  List<String> deleteByDeviceId(@RequestParam(value = "deviceId", required = true) String deviceId);

  @ApiOperation("批量删除设备信息")
  @RequestMapping(
    value = "/deleteBatchByDeviceIds",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  void deleteBatchByDeviceIds(@RequestBody List<String> deviceIds);

  @ApiOperation("获取设备对应产品信息")
  @RequestMapping(value = "/getProductByDeviceId", method = RequestMethod.GET)
  GetProductByDeviceRespVo getProductByDeviceId(
      @RequestParam(value = "deviceId", required = true) String deviceId);

  @ApiOperation("获取设备对应类型信息")
  @RequestMapping(value = "/getDeviceTypeByDeviceId", method = RequestMethod.GET)
  GetDeviceTypeByDeviceRespVo getDeviceTypeByDeviceId(
      @RequestParam(value = "deviceId", required = true) String deviceId);

  @ApiOperation("分页条件查询设备信息")
  @RequestMapping(
    value = "/pageDeviceInfoByParams",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  Page<PageDeviceInfoRespVo> pageDeviceInfoByParams(@RequestBody PageDeviceInfoReq params);

  // 根据deviceTyp获取设备列表
  @ApiOperation("分页条件deviceType设备列表")
  @RequestMapping(
    value = "/pageDeviceByDeviceTypeList",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  Page<DeviceResp> pageDeviceByDeviceTypeList(@RequestBody DevTypePageReq pageReq);

  @ApiOperation("根据条件获取设备列表")
  @RequestMapping(
    value = "/listDeviceByParams",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  List<ListDeviceByParamsRespVo> listDeviceByParams(@RequestBody ListDeviceByParamsReq params);

  @ApiOperation("聚合获取设备集合明细【看需求误乱用】")
  @RequestMapping(
          value = "/findDevRelationListByDeviceIds",
          method = RequestMethod.POST,
          consumes = MediaType.APPLICATION_JSON_VALUE
  )
  List<DeviceResp> findDevRelationListByDeviceIds(@RequestBody ListCommDeviceReq params);

}

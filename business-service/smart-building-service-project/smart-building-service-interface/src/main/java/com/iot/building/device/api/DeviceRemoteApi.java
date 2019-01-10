package com.iot.building.device.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.building.device.api.fallback.DeviceRemoteApiFallbackFactory;
import com.iot.building.device.vo.DeviceRemoteControlReq;
import com.iot.building.device.vo.DeviceRemoteControlResp;
import com.iot.building.device.vo.DeviceRemoteTemplatePageReq;
import com.iot.building.device.vo.DeviceRemoteTemplateReq;
import com.iot.building.device.vo.DeviceRemoteTemplateResp;
import com.iot.building.device.vo.DeviceRemoteTemplateSimpleResp;
import com.iot.building.device.vo.DeviceRemoteTypeResp;
import com.iot.common.helper.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 */
@Api(tags = "遥控器相关接口")
@FeignClient(value = "building-control-service", fallbackFactory = DeviceRemoteApiFallbackFactory.class)
@RequestMapping(value = "/deviceRemoteService")
public interface DeviceRemoteApi {

    @ApiOperation("新增遥控器模板")
    @RequestMapping(value = "/addDeviceRemoteTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void addDeviceRemoteTemplate(@RequestBody DeviceRemoteTemplateReq deviceRemoteTemplateReq);

    @ApiOperation("更新遥控器模板")
    @RequestMapping(value = "/updateDeviceRemoteTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateDeviceRemoteTemplate(@RequestBody DeviceRemoteTemplateReq deviceRemoteTemplateReq);

    @ApiOperation("删除遥控器模板")
    @RequestMapping(value = "/deleteDeviceRemoteTemplate", method = RequestMethod.POST)
    void deleteDeviceRemoteTemplate(@RequestParam("tenantId") Long tenantId, @RequestParam(value = "id")Long id, @RequestParam(value = "userId")Long userId);

    @ApiOperation("根据ID获取遥控器模板详情")
    @RequestMapping(value = "/getDeviceRemoteTemplateById", method = RequestMethod.GET)
    DeviceRemoteTemplateResp getDeviceRemoteTemplateById(@RequestParam("tenantId") Long tenantId, @RequestParam(value = "id")Long id);


    @ApiOperation("根据BusinessTypeId获取遥控器模板详情")
    @RequestMapping(value = "/getDeviceRemoteTemplateByBusinessTypeId", method = RequestMethod.GET)
    DeviceRemoteTemplateResp getDeviceRemoteTemplateByBusinessTypeId(@RequestParam("tenantId") Long tenantId, @RequestParam(value = "id")Long id);

    @ApiOperation("分页获取遥控器模板")
    @RequestMapping(value = "/pageDeviceRemoteTemplatePage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<DeviceRemoteTemplateSimpleResp> pageDeviceRemoteTemplatePage(@RequestBody DeviceRemoteTemplatePageReq pageReq);

    @ApiOperation("获取遥控器类型")
    @RequestMapping(value = "/listDeviceRemoteType", method = RequestMethod.GET)
    List<DeviceRemoteTypeResp> listDeviceRemoteType(@RequestParam(value = "tenantId", required = false) Long tenantId);


    @ApiOperation("获取全部遥控器模板的businessTypeId")
    @RequestMapping(value = "/listDeviceRemoteBusinessType", method = RequestMethod.GET)
    List<Long> listDeviceRemoteBusinessType(@RequestParam(value = "tenantId", required = false) Long tenantId);

    @ApiOperation("下发具体遥控器")
    @RequestMapping(value = "/addDeviceRemoteControl", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void addDeviceRemoteControl(@RequestBody List<DeviceRemoteControlReq> deviceRemoteControlReqs);

    @ApiOperation("删除之前下发的遥控器配置")
    @RequestMapping(value = "/deleteDeviceRemoteControlIfExsit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteDeviceRemoteControlIfExsit(@RequestParam("tenantId") Long tenantId, @RequestParam(value = "deviceId")String deviceId, @RequestParam(value = "businessTypeId")Long businessTypeId);

    @ApiOperation("根据businessTypeId获取遥控器的键")
    @RequestMapping(value = "/listDeviceRemoteControlByBusinessTypeId", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<DeviceRemoteControlResp> listDeviceRemoteControlByBusinessTypeId(@RequestParam("tenantId") Long tenantId, @RequestParam("businessTypeId") Long businessTypeId);
    
    @ApiOperation(value = "根据type查询设备类型")
    @RequestMapping(value = "/findRemoteControlByDeviceType", method = RequestMethod.GET)
    List<DeviceRemoteControlResp> findRemoteControlByDeviceType(@RequestParam("tenantId") Long tenantId, @RequestParam("deviceTypeId") Long deviceTypeId);

}

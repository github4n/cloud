package com.iot.device.api;

import com.iot.common.helper.Page;
import com.iot.device.api.fallback.DeviceTypeCoreApiFallbackFactory;
import com.iot.device.vo.req.device.ListDeviceTypeReq;
import com.iot.device.vo.req.device.PageDeviceTypeByParamsReq;
import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceTypeRespVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "device-service", fallbackFactory = DeviceTypeCoreApiFallbackFactory.class)
@RequestMapping("/deviceTypeCore")
public interface DeviceTypeCoreApi {

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    GetDeviceTypeInfoRespVo get(@RequestParam("deviceTypeId") Long deviceTypeId);

    @RequestMapping(value = "/listDeviceType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ListDeviceTypeRespVo> listDeviceType(@RequestBody @Validated ListDeviceTypeReq params);

    @RequestMapping(value = "/pageDeviceType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<ListDeviceTypeRespVo> pageDeviceType(@RequestBody PageDeviceTypeByParamsReq params);

}

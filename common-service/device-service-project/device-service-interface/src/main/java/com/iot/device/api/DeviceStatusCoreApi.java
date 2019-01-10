package com.iot.device.api;

import com.iot.device.api.fallback.DeviceStatusCoreApiFallbackFactory;
import com.iot.device.vo.req.device.ListDeviceStateReq;
import com.iot.device.vo.req.device.UpdateDeviceStatusReq;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceStatusRespVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(value = "device-service", fallbackFactory = DeviceStatusCoreApiFallbackFactory.class)
@RequestMapping("/deviceStatusCore")
public interface DeviceStatusCoreApi {

    @RequestMapping(value = "/listDeviceStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ListDeviceStatusRespVo> listDeviceStatus(@RequestBody @Validated ListDeviceStateReq params);

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    GetDeviceStatusInfoRespVo get(@RequestParam(value = "tenantId", required = true) Long tenantId
            , @RequestParam(value = "deviceId", required = true) String deviceId);

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveOrUpdate(@RequestBody @Validated UpdateDeviceStatusReq params);

    @RequestMapping(value = "/saveOrUpdateBatch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveOrUpdateBatch(@RequestBody @Validated List<UpdateDeviceStatusReq> paramsList);
}

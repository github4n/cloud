package com.iot.device.api;

import com.iot.device.api.fallback.DeviceExtendsCoreApiFallbackFactory;
import com.iot.device.vo.req.device.ListDeviceExtendReq;
import com.iot.device.vo.req.device.UpdateDeviceExtendReq;
import com.iot.device.vo.rsp.device.GetDeviceExtendInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceExtendRespVo;
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

@Api(tags = "云端设备扩展接口")
@FeignClient(value = "device-service", fallbackFactory = DeviceExtendsCoreApiFallbackFactory.class)
@RequestMapping("/deviceExtendsCore")
public interface DeviceExtendsCoreApi {

    @ApiOperation("获取扩展集合列表")
    @RequestMapping(value = "/listDeviceExtends", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ListDeviceExtendRespVo> listDeviceExtends(@RequestBody @Validated ListDeviceExtendReq params);

    @ApiOperation("获取扩展信息")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    GetDeviceExtendInfoRespVo get(@RequestParam(value = "tenantId", required = true) Long tenantId
            , @RequestParam(value = "deviceId", required = true) String deviceId);

    @ApiOperation("保存或修改")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveOrUpdate(@RequestBody @Validated UpdateDeviceExtendReq params);

    @ApiOperation("批量保存或修改")
    @RequestMapping(value = "/saveOrUpdateBatch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveOrUpdateBatch(@RequestBody @Validated List<UpdateDeviceExtendReq> paramsList);

}

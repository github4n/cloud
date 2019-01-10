package com.iot.device.api;

import com.iot.device.api.fallback.DeviceTypeDataApiFallbackFactory;
import com.iot.device.vo.req.DeviceType2PointsReq;
import com.iot.device.vo.rsp.DeviceType2PointsRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Api(tags = "设备类型功能接口")
@FeignClient(value = "device-service", fallbackFactory = DeviceTypeDataApiFallbackFactory.class)
@RequestMapping("/devicetypedataPoint")
public interface DeviceTypeDataPointApi {

	@ApiOperation("设备类型映射功能点")
    @RequestMapping(value = "/typeMap2Point", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	boolean typeMap2Point(@RequestBody DeviceType2PointsReq req);
	
	@ApiOperation("根据设备类型Id获取功能点")
	@RequestMapping(value = "/getPointsByDeviceTypeId", method = RequestMethod.GET)
	DeviceType2PointsRes getPointsByDeviceTypeId(@RequestParam(value = "deviceTypeId") Long deviceTypeId);
}
